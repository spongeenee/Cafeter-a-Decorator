package com.example.demo1.decorator;

public class BebidaDecorator implements BebidaInterface {
    private BebidaInterface decorated;

    public BebidaDecorator(BebidaInterface b) {
        decorated = b;
    }

    @Override
    public String servir() {
        return decorated.servir().contains("con") ? decorated.servir() + " + "
                : decorated.servir() + " con ";
    }

    @Override
    public int total() {
        return decorated.total();
    }
}
