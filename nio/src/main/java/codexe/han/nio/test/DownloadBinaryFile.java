package codexe.han.nio.test;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * URLConnection download binary file
 * 下载二进制文件 图片
 */
public class DownloadBinaryFile {
    public static void main(String[] args) {
        args = new String[]{"https://timedotcom.files.wordpress.com/2019/04/taylor-swift-time-100-2019-082.jpg?quality=85&zoom=2"};
        for(int i=0;i<args.length;i++){
            try{
                URL root = new URL(args[i]);
                saveBinaryFile(root);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveBinaryFile(URL u) throws IOException {
        URLConnection uc = u.openConnection();
        String contentType = uc.getContentType();
        System.out.println("content type is "+contentType);
        int contentLength = uc.getContentLength();
        System.out.println("content length is "+contentLength);
        if(contentType.startsWith("text/") || contentLength == -1){
            throw new IOException("This is not a binary file");
        }
        try(InputStream raw = uc.getInputStream()){
            InputStream in = new BufferedInputStream(raw);
            byte[]data = new byte[contentLength];
            int offset = 0;
            while(offset < contentLength){
                int byteRead = in.read(data, offset, data.length-offset);//return how many byte read
                if(byteRead == -1) break;//read the end
                offset += byteRead;
            }
            if(offset != contentLength){
                throw new IOException("only read "+offset+" bytes; Expected "+contentLength+" bytes");
            }

            System.out.println("binary content is ");
            System.out.println(data);

            String fileName = u.getFile();//getPath+getQuery
            System.out.println("file name is "+fileName);
      //      fileName = fileName.substring(fileName.lastIndexOf('/')+1);
            fileName = "download.jpg";

            try(FileOutputStream fout = new FileOutputStream(fileName)){
                fout.write(data);//默认根路径是root project
                fout.flush();
            }
        }
    }
}
