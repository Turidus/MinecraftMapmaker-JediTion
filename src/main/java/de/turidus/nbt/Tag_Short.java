package de.turidus.nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_Short extends Tag {
    private final String name;
    private final short value;

    public Tag_Short(String name, short value) {
        super((byte) 2);
        this.name = name;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);

        stringToBytes(name).writeTo(byteArrayOutputStream);
        shortToBytes(value).writeTo(byteArrayOutputStream);


        return byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        return shortToBytes(value);
    }
}
