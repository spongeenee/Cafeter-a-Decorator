package com.example.demo1.decorator;

public class DecoratorFruta extends BebidaDecorator {
    public DecoratorFruta(BebidaInterface b) {
        super(b);
    }

    @Override
    public String servir() {
        return super.servir() + "frutas";
    }

    @Override
    public int total() {
        return super.total() + 5;
    }
}
