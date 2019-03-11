package com.cdy.demo.gof23Design.visitor;

public class VisitorTest {

    public static void main(String[] args){
        Visitor visitor = new MyVisitor2();
        visitor.visit(new VisiSubject() {
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String getSubject() {
                return "MyVisiSubject2";
            }
        });

        VisiSubject sub = new MyVisiSubject2();
        sub.accept(visitor);

        sub.accept((a)->{
            System.out.println("visitor MyVisitor2:"+a.getSubject());
        });
    }
}


interface Visitor{
    public void visit(VisiSubject sub);
}
interface VisiSubject{
    public void accept(Visitor visitor);
    public String getSubject();
}
class MyVisitor implements Visitor{
    public void visit(VisiSubject sub){
        System.out.println("visitor MyVisitor:"+sub.getSubject()+"");
    }
}
class MyVisitor2 implements Visitor{
    public void visit(VisiSubject sub){
        System.out.println("visitor MyVisitor2:"+sub.getSubject()+"");
    }
}
class MyVisiSubject implements VisiSubject{
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    public String getSubject() {
        return "MyVisiSubject";
    }
}
class MyVisiSubject2 implements VisiSubject{
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    public String getSubject() {
        return "MyVisiSubject2";
    }
}
