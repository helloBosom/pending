package util;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class CloseableHttpClientUtil {
    public static CloseableHttpClient getInstance() {
        return new CloseableHttpClient() {
            @Override
            protected CloseableHttpResponse doExecute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
                return null;
            }

            public void close() throws IOException {

            }

            public HttpParams getParams() {
                return null;
            }

            public ClientConnectionManager getConnectionManager() {
                return null;
            }
        };
    }
}
