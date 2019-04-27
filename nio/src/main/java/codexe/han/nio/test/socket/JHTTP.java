package codexe.han.nio.test.socket;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JHTTP {

    private static final int NUM_THREADS = 50;

    private static final String INDEX_FIFLE = "index.html";

    private final File rootDirectory;

    private final int port;

    public JHTTP(File rootDirectory, int port) throws IOException {
        if(!rootDirectory.isDirectory()){
            throw new IOException(rootDirectory+" does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
        this.port = port;
    }

    public void start() throws IOException{
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try(ServerSocket server = new ServerSocket(port)){
            log.info("Accepting connections on port {}",server.getLocalPort());
            log.info("Document Root {}", rootDirectory);

            while(true){
                try{
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(rootDirectory,INDEX_FIFLE,request);
                    pool.submit(r);
                }catch(IOException ex){
                    log.error("Error accepting connection", ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        File docroot = new File("/Users/zhenchao/Documents/IdeaProjects/java-related/nio/src/main/resources");

        //设置要监听的端口
        int port = 12000;

        try{
            JHTTP webserver = new JHTTP(docroot, port);
            webserver.start();
        } catch (IOException e) {
            log.error("Server could not start",e);
        }
    }
}

@Slf4j
class RequestProcessor implements Runnable{

    private File rootDirectory;
    private String indexFileName = "index.html";
    private Socket connection;

    public RequestProcessor(File rootDirectory, String indexFileName, Socket connection){
        if(rootDirectory.isFile()){//root directory 必须是根目录
            throw new IllegalArgumentException("rootDirectory must be a directory, not a file");
        }
        try{
            rootDirectory = rootDirectory.getCanonicalFile();//将文件路径解析为与操作系统相关的唯一的规范形式的字符串，通常会移除多余的名称（.和..）
        }catch(IOException ex){
            log.error("Get canonical file error",ex);
        }

        this.rootDirectory = rootDirectory;
        if(indexFileName!=null){
            this.indexFileName = indexFileName;
        }
        this.connection = connection;
    }

    @Override
    public void run() {
        String root = rootDirectory.getPath();
        try{
            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()),"US-ASCII");

            StringBuilder requestLine = new StringBuilder();
            while(true){
                int c = in.read();
                if(c!='\r' || c=='\n'){
                    break;
                }
                requestLine.append((char)c);
            }

            String get = requestLine.toString();

            if(!StringUtils.isEmpty(get)) {
                log.info("get request is {}", get);
            }

            log.info("remote socket address{} {}",connection.getRemoteSocketAddress(),get);

            String[] tokens = get.split("\\s+");//按空格划分
            String method = tokens[0];
            String version = "";
            if(method.equals("GET")){
                String fileName = tokens[1];
                if(fileName.endsWith("/")) fileName += indexFileName;//如果请求的是路径
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                if(tokens.length > 2){
                    version = tokens[2];
                }
                File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));

                if(theFile.canRead()&&theFile.getCanonicalPath().startsWith(root)){
                    //不要让客户端超出文档根目录之外
                    byte[] theData = Files.readAllBytes(theFile.toPath());//读取要发送的文件数据
                    if(version.startsWith("HTTP/")){
                        sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
                    }

                    //发送文件，可能是一个图像或其他二进制数据
                    //所以要使用底层输出流
                    //而不是writer
                    raw.write(theData);
                    raw.flush();
                }
                else{//没有找到文件
                    String body = new StringBuilder("<HTML>\r\n")
                            .append("<HEAD><TITLE>File NOT FOUND</TITLE>")
                            .append("</HEAD>\r\n")
                            .append("<BODY>")
                            .append("<H1>HTTP Error 404: File Not Found</H1>\r\n")
                            .append("</BODY></HTML>\r\n").toString();
                    if(version.startsWith("HTTP/")){
                        sendHeader(out, "HTTP/1.0 404 File Not Found","text/html; charset=utf-8",body.length());
                    }
                    out.write(body);
                    out.flush();
                }
            }
            else{//Get 以外的其他方法
                String body = new StringBuilder("<HTML>\r\n")
                        .append("<HEAD><TITLE>Not Implemented</TITLE>\r\n")
                        .append("</HEAD>\r\n")
                        .append("<BODY>")
                        .append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
                        .append("</BODY></HTML>\r\n").toString();
                if(version.startsWith("HTTP/")){
                    sendHeader(out, "HTTP/1.0 501 Not Implemented","text/html; charset=utf-8",body.length());
                }
                out.write(body);
                out.flush();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error talking to {}",connection.getRemoteSocketAddress(),e);
        }finally {
            try {
                connection.close();
            } catch (IOException e) {
            }
        }
    }

    private void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException {
        out.write(responseCode+"\r\n");
        Date now = new Date();
        out.write("Date: "+now+"\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: "+length+"\r\n");
        out.write("Content-type: "+contentType+"\r\n");
        out.flush();
    }
}

