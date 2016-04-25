package com.espressoprogrammer.field.initialization;

public class DeclarationAndInitializationReallyGoCrazy {

    private int value1 = this.value2 + 1;
    private int value2 = this.value1 + 1;

    public static void main(String... args) {
        System.out.println(new DeclarationAndInitializationReallyGoCrazy().value1);
        System.out.println(new DeclarationAndInitializationReallyGoCrazy().value2);
    }

}
