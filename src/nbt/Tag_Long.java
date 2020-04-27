package nbt;

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
        byteArrayOutputStream.write((byte)(value >> 56));
        byteArrayOutputStream.write((byte)(value >> 48));
        byteArrayOutputStream.write((byte)(value >> 40));
        byteArrayOutputStream.write((byte)(value >> 32));
        byteArrayOutputStream.write((byte)(value >> 24));
        byteArrayOutputStream.write((byte)(value >> 16));
        byteArrayOutputStream.write((byte)(value >> 8));
        byteArrayOutputStream.write((byte)value);

        return byteArrayOutputStream;
    }
}
