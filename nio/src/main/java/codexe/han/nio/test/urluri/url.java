package codexe.han.nio.test.urluri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class url {
    public static void main(String[] args) {
        /**
         * url是最常见的uri（uniform resource identifier  ）
         * url比通用uri的区别是，会为资源提供一个特定的网络位置。客户端可以用它来获取资源
         * java.net.URI 只标识资源，java.net.URL既可以标识又可以获取
         * URL类主要用于下载内容，不要存储在列表或者map中，因为调用equals方法会阻塞，访问dns服务。存储使用URI
         */
        try {
            URL u = new URL("http://www.lolcats.com");
            InputStream in = u.openStream();
            //添加BufferedInputStream, Reader阅读器
            int c;
            while((c=in.read())!=-1) System.out.write(c);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
