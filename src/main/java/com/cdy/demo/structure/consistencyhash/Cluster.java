package com.cdy.demo.structure.consistencyhash;

import java.util.ArrayList;
import java.util.List;

/**
 * 集群
 * Created by 陈东一
 * 2018/11/4 0004 10:02
 */
public abstract class Cluster {
    
    protected List<Node> nodes;
    
    public Cluster() {
        this.nodes = new ArrayList<>();
    }
    
    public abstract void addNode(Node node);
    
    public abstract void removeNode(Node node);
    
    public abstract Node get(String key);

}
