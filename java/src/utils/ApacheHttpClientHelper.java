package utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by donmunro on 2017-03-15.
 */
public class ApacheHttpClientHelper {

    // ... a work in progress ...
    public static void post(String url)
    {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            client.execute(httpPost);
            CloseableHttpResponse response = client.execute(httpPost);
        }
        catch (Exception exc)
        {
            exc = exc;
        }
    }
}
