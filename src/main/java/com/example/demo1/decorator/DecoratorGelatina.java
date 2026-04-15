package com.example.demo1.decorator;

public class DecoratorGelatina extends BebidaDecorator{
    public DecoratorGelatina(BebidaInterface b) {
        super(b);
    }

    @Override
    public String servir() {
        return super.servir() + "gelatina";
    }

    @Override
    public int total() {
        return super.total() + 5;
    }
}
