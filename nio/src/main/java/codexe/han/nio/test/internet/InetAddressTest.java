package codexe.han.nio.test.internet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {
    public static void main(String[] args) {
        //创建InetAddress
        try {
            /**
             * 建立与本地DNS服务器的连接，来查找名字和数字地址（如果本地有缓存，就不需要在建立连接）
             * 如果找不到地址，就跑出UnknownHostException
             * IP地址相同的连个InetAddress相同
             * ''
             */
            InetAddress address = InetAddress.getByName("www.codexe.net");
            System.out.println(address);

            InetAddress addressByIp = InetAddress.getByName("185.199.110.153");//不会查询DNS
            System.out.println(addressByIp.getHostName());//调用gethostname会查询DNS，没有主机名的话，会返回ip地址
            addressByIp.getCanonicalHostName();//即使有缓存，也会查询DNS服务器
            System.out.println("网络可达性："+addressByIp.isReachable(3000));

            //多个地址的情况
            InetAddress[] addresses = InetAddress.getAllByName("www.oreilly.com");
            for(InetAddress addressTmp : addresses){
                System.out.println(addressTmp);
            }

            InetAddress me = InetAddress.getLocalHost();
            System.out.println("localhost "+me);

            /**
             * getByAddress 不能保证主机一定存在，只有当输入参数的时候才会跑出unknowexception
             */
            byte[] addressIp = {23,15,24,78};
            InetAddress lessWrong = InetAddress.getByAddress(addressIp);
            System.out.println("less wrong "+lessWrong);
        }catch(UnknownHostException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
