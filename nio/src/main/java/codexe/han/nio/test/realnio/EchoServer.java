package codexe.han.nio.test.realnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {

    public static int DEFAULT_PORT = 17001;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        ServerSocketChannel serverChannel;
        Selector selector;

        try{
            serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while(true){
            try{
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try{
                    if(key.isAcceptable()){//server socket channel
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connection from "+client);
                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ);
                        ByteBuffer buffer = ByteBuffer.allocate(2);
                        clientKey.attach(buffer);
                    }
                    if(key.isReadable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        System.out.println("read from client");
                        client.read(output);
                        System.out.println(output.toString());
                    }
                    if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        System.out.println("write to client");
                        output.flip();//limit = position position = 0
                        client.write(output);
                        output.compact();//为了利用一个缓冲区，进行交替的读写。下一次读入缓冲区的时候，会从当前position读入，读出会因为flip从头开始读出
                    }
                } catch (IOException e) {
                    key.cancel();
                    try{
                        key.channel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }
}
class EchoClient{
    public static void main(String[] args) {
        int port= 17001;
        try{
            SocketAddress address = new InetSocketAddress("localhost", port);
            SocketChannel channel = SocketChannel.open(address);

            channel.socket().getOutputStream().write("Hello Echo".getBytes("UTF-8"));


            ByteBuffer buffer = ByteBuffer.allocate(100);
            WritableByteChannel out = Channels.newChannel(System.out);

            while(channel.read(buffer)!=-1){
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
