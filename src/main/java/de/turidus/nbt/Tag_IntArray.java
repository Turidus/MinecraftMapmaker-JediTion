package de.turidus.nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * For Tag
 */
public class Tag_IntArray extends Tag {
    private final String name;
    private final int payloadSize;
    private final int[] value;

    public Tag_IntArray(String name, int[] value) {
        super((byte)11);
        this.name = name;
        this.payloadSize = value.length;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(this.name).writeTo(byteArrayOutputStream);
        intToBytes(this.payloadSize).writeTo(byteArrayOutputStream);
        arrayToBytes(this.value).writeTo(byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        intToBytes(this.payloadSize).writeTo(byteArrayOutputStream);
        arrayToBytes(this.value).writeTo(byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    private ByteArrayOutputStream arrayToBytes(int[] array) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(Integer i : array){
            intToBytes(i).writeTo(byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }
}
