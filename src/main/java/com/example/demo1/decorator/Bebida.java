package com.example.demo1.decorator;

public class Bebida implements BebidaInterface {
    @Override
    public String servir() {
        return "...Sirviendo un boba";
    }

    @Override
    public int total() {
        return 50;
    }
}
