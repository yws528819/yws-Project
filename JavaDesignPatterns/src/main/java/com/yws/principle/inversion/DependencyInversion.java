package com.yws.principle.inversion;

public class DependencyInversion {
    public static void main(String[] args) {
        Person person = new Person();
        person.receive(new Email());
    }


}



class Person {
    public void receive(Email email) {
        email.getInfo();
    }
}


class Email {
    public void getInfo() {
        System.out.println("电子邮件信息：hello world!");
    }
}
