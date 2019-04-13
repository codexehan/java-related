package codexe.han.nio.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkInterfaceTest {
    public static void main(String[] args) {
        try {
            /**
             * 物理硬件和虚拟地址
             * 找到unix接口上的主以太网接口
             */
            NetworkInterface ni = NetworkInterface.getByName("eth0");//null 表示没有该接口
            System.out.println("接口是 "+ni);

            InetAddress local = InetAddress.getByName("127.0.0.1");
            NetworkInterface ni2 = NetworkInterface.getByInetAddress(local);
            System.out.println("与该ip绑定的ip地址是 "+ni2);
            System.out.println("与该ip绑定的ip地址是 "+ni2.getDisplayName());
            System.out.println("与该ip绑定的ip地址是 "+ni2.getName());
            System.out.println("与该ip绑定的ip地址是 "+ni2.getHardwareAddress());

            /**
             获得本地主机的所有网络接口
             */
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()){
                NetworkInterface ni3 = interfaces.nextElement();
                System.out.println("本地接口 "+ni3);
                Enumeration addresses = ni3.getInetAddresses();
                while(addresses.hasMoreElements()){
                    System.out.println("与之绑定的ip "+addresses.nextElement());
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
