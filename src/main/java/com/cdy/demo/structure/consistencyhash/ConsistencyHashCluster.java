package com.cdy.demo.structure.consistencyhash;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static com.cdy.demo.structure.consistencyhash.Node.hash;
import static com.cdy.demo.structure.consistencyhash.NormalHashCluster.DATA_COUNT;
import static com.cdy.demo.structure.consistencyhash.NormalHashCluster.PRE_KEY;


/**
 * 一致性hash  带虚拟节点
 * Created by 陈东一
 * 2018/11/4 0004 10:35
 */
public class ConsistencyHashCluster extends Cluster{
    
    private SortedMap<Long, String> virNodes = new TreeMap<>();
    
    private static final int VIR_NODE_COUNT = 512;
    
    private static final String SPLIT = "#";
    
    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
        IntStream.range(0, VIR_NODE_COUNT)
                .forEach(index -> {
                    long hash = hash(node.getIp() + SPLIT + index);
                    virNodes.put(hash, node.getIp());
                });
    }
    
    @Override
    public void removeNode(Node node) {
        nodes.removeIf(o -> node.getIp().equals(o.getIp()));
        IntStream.range(0, VIR_NODE_COUNT)
                .forEach(index -> {
                    long hash = hash(node.getIp() + SPLIT + index);
                    virNodes.remove(hash);
                });
    }
    
    @Override
    public Node get(String key) {
        long hash = hash(key);
        SortedMap<Long, String> subMap =
                hash >= virNodes.lastKey() ? virNodes.tailMap(0L) : virNodes.tailMap(hash);
        if (subMap.isEmpty()) {
            return null;
        }
//        return subMap.get(subMap.firstKey());
        String ip = subMap.get(subMap.firstKey());
        for (Node node : nodes) {
            if (node.getIp().equals(ip)) {
                return node;
            }
        }
        return null;
    }
    
    
    public static void main(String[] args) {
        Cluster cluster = new ConsistencyHashCluster();
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
        cluster.removeNode(new Node("c4.yywang.info", "192.168.0.4"));
        long hitCount = IntStream.range(0, DATA_COUNT)
                .filter(index -> cluster.get(PRE_KEY + index).get(PRE_KEY + index) != null)
                .count();
        System.out.println("缓存命中率：" + hitCount * 1f / DATA_COUNT);
    }
}
