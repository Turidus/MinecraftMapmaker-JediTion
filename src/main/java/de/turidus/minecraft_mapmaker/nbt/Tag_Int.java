package de.turidus.minecraft_mapmaker.nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_Int extends Tag {
    private final String name;
    private final int value;

    public Tag_Int(String name, int value) {
        super((byte) 3);
        this.name = name;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);

        stringToBytes(name).writeTo(byteArrayOutputStream);
        intToBytes(value).writeTo(byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        return intToBytes(value);
    }


}
