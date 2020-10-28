package com.yws2;

public class Foo1 {

    Person p;

    public Foo1() {
        this.p = new Person(11);
    }

    public Person getP() {
        return p;
    }

    public void setP(Person p) {
        this.p = p;
    }

    void method1() {
        System.out.println(p);
    }
}


class Foo2 extends Foo1{
    public Foo2() {
//        Person p = this.getP();
//        p.setName("yws");
//        this.p = p;
        this.p = new Person(12, "yws");
    }

    public static void main(String[] args) {
        Foo2 f = new Foo2();
        f.method1();
    }
}
