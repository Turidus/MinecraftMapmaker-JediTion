package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_List extends Tag {
    String name;
    byte value = 0;

    public Tag_List(String name) {
        super((byte)9);
        this.name = name;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(10);
        intToBytes(0).writeTo(byteArrayOutputStream);

        return byteArrayOutputStream;
    }
}
