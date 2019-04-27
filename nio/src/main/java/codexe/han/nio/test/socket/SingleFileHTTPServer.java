package codexe.han.nio.test.socket;

import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SingleFileHTTPServer {

    private final byte[] content;

    private final byte[] header;

    private final int port;

    private final String encoding;

    public SingleFileHTTPServer(String data, String encoding, String mimeType, int port) throws UnsupportedEncodingException {
        this(data.getBytes(encoding), encoding, mimeType, port);
    }

    public SingleFileHTTPServer(byte[] data, String encoding, String mimeType, int port){
        this.content = data;
        this.port = port;
        this.encoding = encoding;
        String header = "HTTP1.0 200 OK\r\n"
                +"Server: OneFile 2.0\r\n"
                +"Content-length: "+this.content.length+"\r\n"
                +"Content-type: "+mimeType+"; charset="+encoding+"\r\n\r\n";
        this.header = header.getBytes(Charset.forName("US-ASCII"));
    }

    public void start(){
        ExecutorService pool = Executors.newFixedThreadPool(100);
        try(ServerSocket server = new ServerSocket(this.port)) {
            log.info("Accepting connections on port {}",server.getLocalPort());
            log.info("Data to be sent: {}", new String(this.content, encoding));

            while(true){
                try{
                    Socket connection = server.accept();
                    pool.submit(new HTTPHandler(connection));
                }catch(IOException ex){
                    log.error("Exception accepting connection",ex);
                }catch(RuntimeException ex){
                    log.error("Unexcepted error",ex);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HTTPHandler implements Callable{

        private final Socket connection;

        HTTPHandler(Socket connection){
            this.connection = connection;
        }

        @Override
        public Void call() throws Exception {
            try{
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                InputStream in = new BufferedInputStream(connection.getInputStream());
                //只读取第一行
                StringBuilder request = new StringBuilder(80);
                while(true){
                    int c = in.read();
                    if(c=='\r'||c=='\n'||c==-1) break;
                    request.append((char)c);
                }
                //如果是http/1.0或以后的版本，则发送一个MIME首部
                if(request.toString().indexOf("HTTP/")!= -1){
                    out.write(header);
                }
                out.write(content);
                out.flush();
            }catch(IOException ex){
                log.error("Error writing to client",ex);
            }finally{
                connection.close();
            }
            return null;
        }
    }

    //启动该server
    public static void main(String[] args) {
        int port = 12000;
        String encoding = "UTF-8";
        try{
            String args0 = "/Users/zhenchao/Documents/IdeaProjects/java-related/nio/HELP.md";
            Path path = Paths.get(args0);
            byte[]data = Files.readAllBytes(path);
            String contentType = URLConnection.getFileNameMap().getContentTypeFor(args0);
            SingleFileHTTPServer server = new SingleFileHTTPServer(data, encoding, contentType, port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
