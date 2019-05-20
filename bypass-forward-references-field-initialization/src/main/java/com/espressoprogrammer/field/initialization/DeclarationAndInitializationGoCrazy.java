package com.espressoprogrammer.field.initialization;

public class DeclarationAndInitializationGoCrazy {

    private int value = this.value + 1;

    public static void main(String... args) {
        System.out.println(new DeclarationAndInitializationGoCrazy().value);
    }

}
