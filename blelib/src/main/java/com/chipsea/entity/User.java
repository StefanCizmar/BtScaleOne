package com.chipsea.entity;

/**
 * @Desc:用户信息
 * @ClassName: User
 * @PackageName:com.chipsea.entity
 * @Create On 2019/3/29 0029
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */

public class User {
    private int id; // id 0x7F is for tourists
    private static byte sex; // Gender 1: male; 2: female
    private static int age; // Age
    private static float height; // Height
    private float weight; // Body weight
    private static float adc; // Impedance value

    public User() {
    }

    public User(int id, byte sex, int age, float height, float weight, float adc) {
        this.id = id;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.adc = adc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public static int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public static float getAdc() {
        return adc;
    }

    public void setAdc(float adc) {
        this.adc = adc;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", sex=" + sex +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", adc=" + adc +
                '}';
    }
}
