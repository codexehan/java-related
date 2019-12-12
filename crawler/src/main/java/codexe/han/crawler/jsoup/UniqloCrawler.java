package codexe.han.crawler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class UniqloCrawler {
    /**
     description: https://www.uniqlo.com/sg/store/women-j-w-anderson-flannel-checked-long-sleeve-t-shirtunic-4220690002.html
     color price: https://uqsg-media.s3.amazonaws.com/json/422069.json
     size: https://d15udtvdbbfasl.cloudfront.net/sizechart/re01/422069_size.html
     */

    public static void main(String[] args) {
        try {
            String url  = "https://www.uniqlo.com/sg/store/women-j-w-anderson-flannel-checked-long-sleeve-t-shirtunic-4220690002.html";
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2") //
                    .header("Content-Language", "en-US") //
                    .get();
            System.out.println(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
