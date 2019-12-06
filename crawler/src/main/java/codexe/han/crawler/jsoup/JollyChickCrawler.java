package codexe.han.crawler.jsoup;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class JollyChickCrawler {
    /**
     <meta property="og:price:amount" content="9.99" />
     <meta property="og:price:currency" content="USD" />
     <meta property="og:availability" content="in stock" />

     <del id="discount-price" class="price-del"><b class="jolly-price">$14.99</b></del>
     */
    public static void main(String[] args) {
        checkUpdate();
    }
    public static void checkUpdate(){
        try {
            long startTime = System.currentTimeMillis();
         //   String url = "https://www.jollychic.com/p/womens-blazer-fashion-checkered-notched-collar-coat-g0xdx0xrv2-g-oku-d-gt-eoo-73.html?SPM=DL.X.X";//out stock
            String url = "https://www.jollychic.com/p/womens-a-line-skirt-solid-color-button-fashion-mini-skirt-g0xdx0x-k9c-n-v-s-z-qm-g-moo-73.html?SPM=CAT.WOMEN.C2.C34";//in stock
            Document doc = Jsoup
                    .connect(url)
                 //   .proxy("127.0.0.1", 8080) // sets a HTTP proxy
                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2") //
                    .header("Content-Language", "en-US") //
                    .get();
            Element metaPrice = doc.select("meta[property=og:price:amount]").first();
            System.out.println("current price is "+metaPrice.attr("content"));
            Element metaCurrency = doc.select("meta[property=og:price:currency]").first();
            System.out.println("currency is "+metaCurrency.attr("content"));
            Element metaStock = doc.select("meta[property=og:availability]").first();
            System.out.println("stock is "+metaStock.attr("content"));//out stock, in stock

            //
            Element discountPriceEle = doc.select("b.jolly-price").first();
            System.out.println("original price is "+ discountPriceEle.text().replace("$",""));
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
