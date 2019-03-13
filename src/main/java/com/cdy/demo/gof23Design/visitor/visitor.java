package com.cdy.demo.gof23Design.visitor;

import org.junit.Test;

/**
 * Created by 陈东一
 * 2018/9/2 16:07
 */
public class visitor {
    
    
    
    /* 访问者模式 */
    interface Action {
        void getManConclusion(Man man);
        
        void getWomanConclusion(Woman woman);
    }
    
    interface Person {
        
        void accept(Action action);
    }
    
    class Man implements Person {
        
        @Override
        public void accept(Action action) {
            action.getManConclusion(this);
        }
    }
    
    class Woman implements Person {
        @Override
        public void accept(Action action) {
            action.getWomanConclusion(this);
        }
    }
    
    class Success implements Action {
        
        @Override
        public void getManConclusion(Man man) {
            System.out.println(man.getClass().getSimpleName() + " " + this.getClass().getSimpleName());
        }
        
        @Override
        public void getWomanConclusion(Woman woman) {
            System.out.println(woman.getClass().getSimpleName() + " " + this.getClass().getSimpleName());
        }
    }
    
    class Fail implements Action {
        @Override
        public void getManConclusion(Man man) {
            System.out.println(man.getClass().getSimpleName() + " " + this.getClass().getSimpleName());
        }
        
        @Override
        public void getWomanConclusion(Woman woman) {
            System.out.println(woman.getClass().getSimpleName() + " " + this.getClass().getSimpleName());
        }
    }
    
    
    @Test
    public void test() {
//        System.out.println(new Man(new Success()));
        Man man = new Man();
        man.accept(new Success());
        
    }
    /* 访问者模式 */
    
    
}
