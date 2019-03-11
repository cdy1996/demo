package com.cdy.demo.structure.consistencyhash;

import java.util.stream.IntStream;

import static com.cdy.demo.structure.consistencyhash.Node.hash;


/**
 * 普通hash 的集群
 * Created by 陈东一
 * 2018/11/4 0004 10:02
 */
public class NormalHashCluster extends Cluster {
    
    
    public static int DATA_COUNT = 10;
    
    public static String PRE_KEY = "pre";
    
    
    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }
    
    @Override
    public void removeNode(Node node) {
        this.nodes.removeIf(o -> o.getIp().equals(node.getIp()) ||
                o.getDomain().equals(node.getDomain()));
    }
    
    @Override
    public Node get(String key) {
        long hash = hash(key);
        long index =  hash % nodes.size();
        return nodes.get((int)index);
    }
    
    
    public static void main(String[] args) {
        Cluster cluster = new NormalHashCluster();
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
//        cluster.addNode(new Node("c5.yywang.info", "192.168.0.5"));
        cluster.removeNode(new Node("c3.yywang.info", "192.168.0.3"));
        long hitCount = IntStream.range(0, DATA_COUNT)
                .filter(index -> {
                    return cluster.get(PRE_KEY + index).get(PRE_KEY + index) != null;
                })
                .count();
        System.out.println("缓存命中率：" + hitCount * 1f / DATA_COUNT);
    }
}
