package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_ByteArray extends Tag {
    String name;
    byte[] value;

    public Tag_ByteArray(String name, byte[] value) {
        super((byte)7);
        this.name = name;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(value,0, value.length);

        return  byteArrayOutputStream;
    }
}
