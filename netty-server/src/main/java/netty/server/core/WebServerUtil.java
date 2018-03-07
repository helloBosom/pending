package netty.server.core;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.*;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.*;
import io.netty.util.*;
import io.netty.util.internal.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import javax.activation.*;

/**
 * Web服务工具类
 */
class WebServerUtil {

	private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	private static final int HTTP_CACHE_SECONDS = 60;

	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	private static final SimpleDateFormat FMT = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.CHINA);

	public static String sanitizeUri(String uri) {
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}

		if (uri.isEmpty() || uri.charAt(0) != '/')
			return null;

		uri = uri.replace('/', File.separatorChar);

		if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.charAt(0) == '.'
				|| uri.charAt(uri.length() - 1) == '.' || INSECURE_URI.matcher(uri).matches())
			return null;

		return SystemPropertyUtil.get("user.dir") + File.separator + uri;
	}

	/**
	 * 重定向
	 */
	public static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
		response.headers().set(HttpHeaderNames.LOCATION, newUri);

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 转向错误页
	 */
	public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void sendNotModified(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);

		Calendar time = new GregorianCalendar();
		response.headers().set(HttpHeaderNames.DATE, FMT.format(time.getTime()));

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
		Calendar time = new GregorianCalendar();
		time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);

		response.headers().set(HttpHeaderNames.DATE, FMT.format(time.getTime()))
				.set(HttpHeaderNames.EXPIRES, FMT.format(time.getTime()))
				.set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS)
				.set(HttpHeaderNames.LAST_MODIFIED, FMT.format(new Date(fileToCache.lastModified())));
	}

	public static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	}

	/**
	 * 读取配置文件
	 * 
	 * @param source 配置文件
	 * @param key
	 * @return value
	 */
	public static String getProperties(String source, String key) {
		try {
			Properties prop = new Properties();
			prop.load(WebServerUtil.class.getClassLoader().getResourceAsStream(source));
			return prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String listToString(List<String> list) {
		if (list == null || list.size() == 0)
			return null;

		if (list.size() == 1)
			return list.get(0);

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0)
				result.append(",");
			result.append(list.get(i));
		}
		return result.toString();
	}

	/**
	 * 读取文件
	 */
	public static File readFile(ChannelHandlerContext ctx, HttpRequest request, String fileName) throws Exception {
		HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
		HttpContent chunk = (HttpContent) request;
		HttpPostMultipartRequestDecoder decoder;
		File file = null;

		try {
			decoder = new HttpPostMultipartRequestDecoder(factory, request);
			decoder.offer(chunk);
		} catch (ErrorDataDecoderException e) {
			e.printStackTrace();
			return null;
		}

		InterfaceHttpData data = decoder.getBodyHttpData(fileName);
		if (data != null)
			file = writeHttpData(data);

		return file;
	}

	private static File writeHttpData(InterfaceHttpData data) {
		if (data.getHttpDataType() != HttpDataType.FileUpload)
			return null;

		FileUpload fileUpload = (FileUpload) data;
		if (!fileUpload.isCompleted())
			return null;

		OutputStream os = null;
		try {
			ByteBuf buf = fileUpload.getByteBuf();

			byte[] bytes = new byte[buf.readableBytes()];
			if (bytes.length == 0)
				return null;

			StringBuilder path = new StringBuilder(SystemPropertyUtil.get("user.dir"));
			path.append(File.separator).append(UUID.randomUUID());
			
			File file = new File(path.toString());
			os = new FileOutputStream(file);

			buf.readBytes(bytes);
			os.write(bytes);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}
			data.release();
		}
	}

	public static void write(final File file, final ChannelHandlerContext ctx, final FullHttpRequest request) throws Exception {
		// 用于文件下载
		final HttpResponse httpResponse = new DefaultHttpResponse(HTTP_1_1, OK);

		final RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException ignore) {
			WebServerUtil.sendError(ctx, NOT_FOUND);
			return;
		}

		final long fileLength = raf.length();

		HttpUtil.setContentLength(httpResponse, fileLength);
		WebServerUtil.setContentTypeHeader(httpResponse, file);
		WebServerUtil.setDateAndCacheHeaders(httpResponse, file);

		if (HttpUtil.isKeepAlive(request))
			httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

		ctx.write(httpResponse);
		ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());

		ChannelFuture lastContentFuture = ctx
				.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
				.addListener(ChannelFutureListener.CLOSE);

		if (!HttpUtil.isKeepAlive(request))
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);

		raf.close();
	}
}