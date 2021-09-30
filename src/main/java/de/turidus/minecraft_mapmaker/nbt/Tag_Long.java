package de.turidus.minecraft_mapmaker.nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_Long extends Tag{


    private final String name;
    private final long value;

    public Tag_Long(String name, long value) {
        super((byte)4);
        this.name = name;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);
        longToBytes(value).writeTo(byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        return longToBytes(value);
    }
}
