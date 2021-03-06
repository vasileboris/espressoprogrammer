package com.espressoprogrammer.initialization.clazz;

public class RecursiveInitialization {

    private static final RecursiveInitialization TROUBLE = new RecursiveInitialization();

    private static final Boolean DONE;
    static {
        System.out.println("setting up DONE");
        DONE = Boolean.TRUE;
        System.out.println("DONE: " + DONE);
    }

    private boolean done;
    {
        System.out.println("setting up done");
        done = DONE;
        System.out.println("done: " + done);
    }

    public boolean isDone() {
        return done;
    }

    public static void main(String... args) {
        System.out.println("isDone(): "  + TROUBLE.isDone());
    }

}
