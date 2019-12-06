package codexe.han.crawler.jsoup;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;

public class YesStyleCrawler {
    /**
     <div class="sellingPrice" ng-bind-html="productData.product.sellPrice" ng-if="productData.isSellable || productData.isAdventCalendar">SG$&nbsp;97.96</div>
     <div class="unitPrice" ng-if="(productData.product.discount &amp;&amp; productData.isSellable)  || productData.isAdventCalendar">
     <span class="listPrice" ng-bind-html="productData.product.listPrice" ng-if="productData.product.discount || productData.isAdventCalendar" ng-cloak="">SG$&nbsp;99.96</span>
     <span ng-show="productData.product.discount" class="discount" ng-cloak="" ng-bind-html="productData.product.discount">2% OFF</span>

     */
    public static void main(String[] args) {
        checkPrice();
    }
    public static void checkPrice(){
        try {
            long startTime = System.currentTimeMillis();
            String url = "https://www.yesstyle.com/en/ariadne-double-breasted-houndstooth-coat/info.html/pid.1071167506";
            Document doc = Jsoup
                    .connect(url)
                    .proxy("127.0.0.1", 8080) // sets a HTTP proxy
                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2") //
                    .header("Content-Language", "en-US") //
                    .get();
            Element sellingPriceEle = doc.select("div.sellingPrice").first();
            System.out.println("current price is "+sellingPriceEle.text());
            Element listPriceEle = doc.select("span.listPrice").first();
            System.out.println("original price is "+listPriceEle.text());
            long endTime = System.currentTimeMillis();
            System.out.println("total time is "+(endTime-startTime));

        }catch(HttpStatusException e){//404 product offline
            System.out.println("product is offline");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
