package com.espressoprogrammer.competition.primitives.heap;

public class ComposeFields {

    private ByteField byteField;

    private ShortField shortField;

    private IntField intField;

    private LongField longField;

    public ComposeFields setByteField(ByteField byteField) {
        this.byteField = byteField;
        return this;
    }

    public ComposeFields setShortField(ShortField shortField) {
        this.shortField = shortField;
        return this;
    }

    public ComposeFields setIntField(IntField intField) {
        this.intField = intField;
        return this;
    }

    public ComposeFields setLongField(LongField longField) {
        this.longField = longField;
        return this;
    }
}
