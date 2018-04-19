package util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpConnectionUtil {
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    public static String doPostByJson(String url, String json) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = CloseableHttpClientUtil.getInstance();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.addHeader("Content-Type", APPLICATION_JSON);
        StringEntity se = new StringEntity(json, "UTF-8");         //这里必须处理编码UTF-8,不然就会出现中文乱码
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader("Content-Type", APPLICATION_JSON));
        httpPost.setEntity(se);
        try {
            HttpResponse httpResp = httpClient.execute(httpPost);
            int statusCode = httpResp.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                result = EntityUtils.toString(httpResp.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null == result ? "" : result;
    }
}