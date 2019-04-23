package codexe.han.nio.test.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * new Socket(String ip, int port)
 * new Socket(InetAddress address, int port)
 * 创建socket 并连接
 *
 * 其余构造函数不会连接
 */
public class SocketReadDaytime {
    public static void main(String[] args) {
        try(Socket socket = new Socket("time.nist.gov",13)){//telnet time.nist.gov 13
            socket.setSoTimeout(15000);//防止服务器接受了请求，没有回应
            InputStream in = socket.getInputStream();
            StringBuilder time = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in,"ASCII");
            int c;
            while((c=reader.read())!=-1){
                time.append((char)c);
            }
            System.out.println(time);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
