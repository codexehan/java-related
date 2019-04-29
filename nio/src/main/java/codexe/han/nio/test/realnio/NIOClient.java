package codexe.han.nio.test.realnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 客户端实现非阻塞读写
 */
public class NIOClient {

    public static int DEFAULT_PORT = 19;

    public static void main(String[] args) {
        int port = 19;

        String ip = "rama.poly.edu";

        try{
            SocketAddress address = new InetSocketAddress(ip, port);
            SocketChannel channel = SocketChannel.open(address);

            ByteBuffer buffer = ByteBuffer.allocate(74);
            WritableByteChannel out = Channels.newChannel(System.out);

            while(channel.read(buffer)!=-1){//channel -> buffer
                buffer.flip();//mark to start of buffer
                out.write(buffer);//buffer -> system.out channel
                buffer.clear();//clear buffer
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class NIOChargenClient{
    {
        int port = 19;

        String ip = "rama.poly.edu";

        try{
            SocketAddress address = new InetSocketAddress(ip, port);
            SocketChannel channel = SocketChannel.open(address);

            channel.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(74);
            WritableByteChannel out = Channels.newChannel(System.out);

            while(true){//channel -> buffer
                int n = channel.read(buffer);//nio 模式 读不到数据会直接返回0
                if(n>0) {
                    buffer.flip();//mark to start of buffer
                    out.write(buffer);//buffer -> system.out channel
                    buffer.clear();//clear buffer
                }else if(n == -1){
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
