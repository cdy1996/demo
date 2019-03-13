package com.cdy.demo.gof23Design.chain.chain1;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 陈东一
 * 2018/3/3 21:36
 */
public class ChainDemo {
    
    public interface Executor {
        public void execture(ExectureService exectureService);
    }

    public interface ExectureService {
        public void exectue();
    }

    public static class DefaultExectorCahin implements ExectureService {
        private final List<Executor> list = new LinkedList<>();
        
        private int position = 0;
        
        public void addExector(Executor executor) {
            list.add(executor);
        }
        

        @Override
        public void exectue() {
            int pos = position;
            Executor executor = list.get(pos);
            System.out.println("执行第" + pos + "个");
            position++;
            executor.execture(this);
        }
    }
    
    public static void main(String[] args) {
        DefaultExectorCahin defaultExectorCahin = new DefaultExectorCahin();
        defaultExectorCahin.addExector(c -> {
            System.out.println("111");
            c.exectue();
        });
        defaultExectorCahin.addExector(c -> {
            System.out.println("222");
//            c.exectue();
        });

        defaultExectorCahin.exectue();

        
    }
    
    
}
