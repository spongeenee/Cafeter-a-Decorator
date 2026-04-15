package com.example.demo1.decorator;

public class DecoratorYogurt extends BebidaDecorator {
    public DecoratorYogurt(BebidaInterface b) {
        super(b);
    }

    @Override
    public String servir() {
        return super.servir() + "Yogurt";
    }

    @Override
    public int total() {
        return super.total() + 10;
    }
}
