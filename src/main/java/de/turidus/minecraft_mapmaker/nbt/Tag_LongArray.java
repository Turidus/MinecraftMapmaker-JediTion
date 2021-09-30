package de.turidus.minecraft_mapmaker.nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_LongArray extends Tag {
    private final String name;
    private final long[] value;

    public Tag_LongArray(String name, long[] value) {
        super((byte)12);
        this.name = name;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);
        intToBytes(value.length).writeTo(byteArrayOutputStream);
        for (long item : value){
            longToBytes(item).writeTo(byteArrayOutputStream);
        }

        return  byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        intToBytes(value.length).writeTo(byteArrayOutputStream);
        for (long item : value){
            longToBytes(item).writeTo(byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }
}
