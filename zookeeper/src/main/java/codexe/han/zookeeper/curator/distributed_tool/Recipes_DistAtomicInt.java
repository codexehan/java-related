package codexe.han.zookeeper.curator.distributed_tool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

public class Recipes_DistAtomicInt {
    static String disatomicint_path = "/curator_recipes_distatomicint_path";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(client,disatomicint_path,new RetryNTimes(3,1000));

        AtomicValue<Integer> rc = distributedAtomicInteger.add(8);

        System.out.println("Result: "+rc.succeeded());
    }
}
