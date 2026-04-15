package com.example.demo1.decorator;

public class DecoratorExtra extends BebidaDecorator {
    public DecoratorExtra(BebidaInterface b) {
        super(b);
    }

    @Override
    public String servir() {
        return super.servir() + "tapiocas extra";
    }

    @Override
    public int total() {
        return super.total() + 10;
    }
}
