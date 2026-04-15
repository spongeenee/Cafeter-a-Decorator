package com.example.demo1.decorator;

public class DecoratorPop extends BebidaDecorator {
    public DecoratorPop(BebidaInterface b) {
        super(b);
    }

    @Override
    public String servir() {
        return super.servir() + "burbujas pop";
    }

    @Override
    public int total() {
        return super.total() + 5;
    }
}
