package codexe.han.nio.test.realnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 使用复制缓冲区
 */
public class NoblockingSingleFileHTTPServer {

    private ByteBuffer contentBuffer;

    private int port = 80;

    public NoblockingSingleFileHTTPServer(ByteBuffer data, String encoding, String MIMEType, int port){
        this.port = port;

        String header = "HTTP/1.0 200 PK\r\n"
                +"Server: NonblockingSingleFileHTTPServer\r\n"
                +"Content-length: "+data.limit()+"\r\n"
                +"Contnet-type: "+MIMEType+"\r\n\r\n";

        byte[] headerData = header.getBytes(Charset.forName("US-ASCII"));

        ByteBuffer buffer = ByteBuffer.allocate(data.limit()+headerData.length);
        buffer.put(headerData);
        buffer.put(data);
        buffer.flip();
        this.contentBuffer = buffer;
    }
    public void run() throws IOException{
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        Selector selector = Selector.open();
        InetSocketAddress localPort = new InetSocketAddress(port);
        serverSocket.bind(localPort);
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while(keys.hasNext()){
                SelectionKey key = keys.next();
                keys.remove();
                try{
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel channel = server.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                    else if(key.isWritable()){//通道写回客户端
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if(buffer.hasRemaining()){
                            channel.write(buffer);
                        }
                        else{
                            //写完毕
                            channel.close();
                        }
                    }
                    else if(key.isReadable()){//客户端读请求进来
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        channel.read(buffer);
                        //将通道切换为写模式
                        key.interestOps(SelectionKey.OP_WRITE);
                        key.attach(contentBuffer.duplicate());
                    }
                }catch(IOException ex){
                    key.cancel();
                    try{
                        key.channel().close();
                    }
                    catch(IOException exc){}
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}
