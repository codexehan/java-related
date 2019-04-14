package codexe.han.nio.test.internet;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SpamCheck {
    public static final String BLACKHOLE = "sbl.spamhaus.org";

    public static void main(String[] args) {
        String[] argss = {"207.34.56.23"};
        for (String arg: argss){
            if(isSpammer(arg)){
                System.out.println(arg + " is a known spammer.");
            }
            else{
                System.out.println(arg + " appears legitimate.");
            }
        }
    }
    public static boolean isSpammer(String arg){
        try {
            InetAddress address = InetAddress.getByName(arg);
            byte[] quad = address.getAddress();
            String query = BLACKHOLE;
            for(byte octet : quad){
                int unsignedByte = octet<0?octet+256:octet;
                query = unsignedByte +"."+query;//reverse ip
            }
            System.out.println("query is "+query);
            InetAddress.getByName(query);
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
}
