package com.example.bombtext;

import cn.bmob.v3.BmobObject;

/**
 * Created by 李志军 on 2017/3/6.
 */

public class Person extends BmobObject{
    private String name;
    private String address;
    private int age;


    public Person(){
    }

    public Person(String name,String address,int age){
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
