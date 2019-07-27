package codexe.han.elasticsearch.doc;

public class Cluster {

    /**
     1.集群发现，容错细节
     1.discovery
     什么时候会执行？当集群启动，或者master node failed的时候
     集群中的每台机器通过seed hosts provider互相通信，并交换master-eligible的地址信息。这个过程分为两个阶段：1.根绝seed hosts provider列表，进行probes（探索），并且连接，搞清楚这些节点的状态 what it is connected  2.与远程节点互相交换master-eligible列表，remote node与master-eligible响应。当前节点会probes新的节点，request their peers …

     如果当前节点不是一个master-eligible节点，他会一直持续discovery到发现一个 elected master node。如果一直没有发现elected master node,会在1s（discovery.find_peers_interval）后重试。

     如果当前节点是一个master-eligible节点，他会一直持续这个过程直到发现一个elected master node。或这个发现足够多数量的master-eligible node，可以进行election（默认的voting configuration是所有的master-eligible node投票数大于一半）。如果两者没有做到，在1s以后重试

     2.quorum-based decision making
     选举一个master node和改变集群状态是所有的master-eligible node必须要一起做的事情
     保证集群工作正常，要must not stop half or more of the nodes in the voting configuration at the same time

     Master election
     Es会在启动或者是当前master失败的情况下，进行选举。通常情况下，第一个选举的node会成为master node。所以 当多个node同时选举的时候，会导致选举失败，所以node会被schedule任意的时间点进行选举，Nodes will retry elections until a master is elected, backing off on failure, so that eventually an election will succeed (with arbitrarily high probability)

     3.voting configuration
     选举一个新的master或者commit a new cluster state. 决定生效的条件是必须有一半以上voting configuration node 的respond
     通常情况下，voting configuration一般设置为一个奇数。假设是一个偶数，假设4，说明必须有三个node respond才能进行选举或者更新cluster state。那么假设网络问题，恰好将master-eligiblenode 分成了两个 两个，这样的话，找不到三个master-eligible node
     做投票，那么集群就彻底失效了。如果是一个奇数，网络发生问题，分成两部分，我们永远可以保证，一部分是由一半以上的master eligible node存在的。

     4.bootstraping cluster
     最开始启动一个集群需要一个set of master eligible nodes. 这个只有在最开始启动的时候，才需要。如果是启动之前加入过的节点，这些信息会被保存在data folder中。如果是新节点要加入一个正在运行的节点，他会从master节点上拉取信息。这也就是为什么非master-eligible node discovery需要找到一个master节点才能加入集群。、
     注意 当我们只是要临时重启一个节点的时候，需要设置shard allocation为false，避免默认一分钟以后，重新迁移primary shard
     cluster.routing.allocation.enable
     Enable or disable allocation for specific kinds of shards:
     * all - (default) Allows shard allocation for all kinds of shards.
     * primaries - Allows shard allocation only for primary shards.
     * new_primaries - Allows shard allocation only for primary shards for new indices.
     * none - No shard allocations of any kind are allowed for any indices.

     5.adding or removing nodes
     As nodes are added or removed Elasticsearch maintains an optimal level of fault tolerance by automatically updating the cluster’s voting configuration, which is the set of master-eligible nodes whose responses are counted when making decisions such as electing a new master or committing a new cluster state.

     6.Publishing cluster state
     Cluster state包含哪些内容
     1.set of node in cluster
     2.all cluster level setting
     3.information about indices and the setting and mappings of them
     4.the location of all shard in the cluster

     多个coordinating node client 负载均衡，用TransportClient  https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html round robin负载均衡算法

     */
}
