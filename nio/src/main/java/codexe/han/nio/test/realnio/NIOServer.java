package codexe.han.nio.test.realnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * Selector 是SelectableChannel的多路复用对象  ->  一个线程处理多个连接，传统做法是一个连接对应一个线程
 * Selector.open调用系统默认的选择器提供程序来创建新的选择器
 * 还可以通过openSelector自动以选择器提供程序来创建选择器。
 *
 *
 * flip vs rewind
 * position 设置为0， flip改变limit为当前读写位置，rewind不会改变limit值，一般和capacity一样
 */
public class NIOServer {
    public static int DEFAULT_PORT =19;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        byte[] rotation = new byte[95*2];
        for(byte i=' ';i<='~';i++){
            rotation[i-' '] = i;
            rotation[i+95-' '] = i;
        }

        ServerSocketChannel serverChannel;
        Selector selector;
        try{
            serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while(true){
            try{
                selector.select();//阻塞
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
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connnection from "+client);
                        client.configureBlocking(false);
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
                        ByteBuffer buffer = ByteBuffer.allocate(74);
                        buffer.put(rotation, 0, 72);//copy 0-72 bytes from rotation to buffer
                        buffer.put((byte) '\r');
                        buffer.put((byte) '\n');
                        buffer.flip();
                        key2.attach(buffer);//可以用attachment读取
                    }
                    else if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if(!buffer.hasRemaining()){
                            //用下一行重新填充缓冲区
                            //position = 0 limit = capacity
                            buffer.rewind();
                            //得到上一次的首字符
                            int first = buffer.get();
                            //准备改变缓冲区中的数据
                            buffer.rewind();
                            //寻找rotation中新的首字符位置
                            int position = first - ' ' +1;
                            //将数据从rotation复制到缓冲区
                            buffer.put(rotation, position, 72);
                            buffer.put((byte) '\r');
                            buffer.put((byte) '\n');
                            buffer.flip();
                        }
                        client.write(buffer);
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
