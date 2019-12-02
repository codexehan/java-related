package codexe.han.kafkadatapipeline.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class 爬虫 {

    public static void main(String[] args) {
        String url = "https://www.zara.com/sg/en/top-with-puff-sleeves-p07563263.html?v1=23481776&v2=1281626";
        String body = getBody(url);
        System.out.println(body);
    }

    public static String getBody(String url) {
        // 实例一个http客户端
        DefaultHttpClient httpClient = new DefaultHttpClient();
        // 实例一个httpGet请求，url放进去
        HttpGet httpGet = new HttpGet(url);
        try {
            // 用客户端执行get请求，并得到一个response回应，这里的执行时间根据客户端请求服务器的时间来决定
            // 如连接不成功会有IO异常
            HttpResponse response = httpClient.execute(httpGet);
            // 获取response里面的内容等。
            HttpEntity entity = response.getEntity();
            // 将entity元素通过Entity工具类转化为字符串形式，此时即为url页面html的字符串,这里的UTF_8为页面的实际编码
            String body = EntityUtils.toString(entity, HTTP.UTF_8);
            return body;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpClient.close();
        }
        return null;
    }

}
