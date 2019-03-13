package com.cdy.demo.structure.consistencyhash;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static com.cdy.demo.structure.consistencyhash.Node.hash;
import static com.cdy.demo.structure.consistencyhash.NormalHashCluster.DATA_COUNT;
import static com.cdy.demo.structure.consistencyhash.NormalHashCluster.PRE_KEY;


/**
 * 一致性hash 没有许你几点
 * Created by 陈东一
 * 2018/11/4 0004 10:35
 */
public class ConsistencyHashClusterNoVirtual extends Cluster {
    
    private SortedMap<Long, Node> virNodes = new TreeMap<Long, Node>();
    
    
    private static final String SPLIT = "#";
    
    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
        long hash = hash(node.getIp());
        virNodes.put(hash, node);
    }
    
    @Override
    public void removeNode(Node node) {
        nodes.removeIf(o -> node.getIp().equals(o.getIp()));
        long hash = hash(node.getIp());
        virNodes.remove(hash);
    }
    
    @Override
    public Node get(String key) {
        long hash = hash(key);
        SortedMap<Long, Node> subMap =
                hash >= virNodes.lastKey() ? virNodes.tailMap(0L) : virNodes.tailMap(hash);
        if (subMap.isEmpty()) {
            return null;
        }
        return subMap.get(subMap.firstKey());
    }
    
    
    public static void main(String[] args) {
        Cluster cluster = new ConsistencyHashClusterNoVirtual();
        cluster.addNode(new Node("c1.yywang.info", "192.168.0.1"));
        cluster.addNode(new Node("c2.yywang.info", "192.168.0.2"));
        cluster.addNode(new Node("c3.yywang.info", "192.168.0.3"));
        cluster.addNode(new Node("c4.yywang.info", "192.168.0.4"));
        
        IntStream.range(0, DATA_COUNT)
                .forEach(index -> {
                    Node node = cluster.get(PRE_KEY + index);
                    node.put(PRE_KEY + index, "Test Data");
                });
        cluster.nodes.forEach(node -> {
            System.out.println("IP:" + node.getIp() + ",数据量:" + node.getData().size());
        });
        
        long hitCount = IntStream.range(0, DATA_COUNT)
                .filter(index -> cluster.get(PRE_KEY + index).get(PRE_KEY + index) != null)
                .count();
        System.out.println("缓存命中率：" + hitCount * 1f / DATA_COUNT);
    }
}
