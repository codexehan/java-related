package codexe.han.nio.test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * post 请求
 */
public class StreamToServer {

    private URL url;

    private QueryString query = new QueryString();

    public StreamToServer(URL url){
        if(!url.getProtocol().toLowerCase().startsWith("http")){
           throw new IllegalArgumentException("Posting only works for http URLs");
        }
        this.url = url;
    }

    public void add(String name, String value){
        query.add(name, value);
    }

    public URL getURL(){
        return this.url;
    }

    public InputStream post() throws IOException {
        //打开连接 准备post
        URLConnection uc = this.url.openConnection();
        uc.setDoOutput(true);//get->post
        try(OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(),"UTF-8")){
            out.write(query.toString());
            out.write("\r\n");
            out.flush();
        }

        return uc.getInputStream();
    }

    public static void main(String[] args) {
        URL url;
        if(args.length>0){
            try{
                url = new URL(args[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return;
            }
        }
        else{
            try{
                url = new URL("http://www.cafeaulait.org/books/jnp4/postquery.phtml");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return;
            }
        }

        StreamToServer poster = new StreamToServer(url);
        poster.add("name", "Elliotte Rusty Harold");
        poster.add("email","elharo@ibiblio.org");

        try(InputStream in = poster.post()){
            Reader r = new InputStreamReader(in);
            int c;
            while((c=r.read())!=-1){
                System.out.print((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class QueryString{
    private StringBuilder query = new StringBuilder();

    public synchronized void add(String name, String value){
        query.append('&');
        encode(name,value);

    }
    private synchronized void encode(String name, String value){
        try {
            query.append(URLEncoder.encode(name,"UTF-8"));
            query.append('=');
            query.append(URLEncoder.encode(value,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public synchronized String getQuery(){
        return query.toString();
    }

    @Override
    public String toString(){
        return getQuery();
    }
}