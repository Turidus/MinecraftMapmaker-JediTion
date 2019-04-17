package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag_String extends Tag {
    private String name;
    private String value;

    public Tag_String(String name, String value) {
        super((byte)8);
        this.name = name;
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2);

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);
        stringToBytes(value).writeTo(byteArrayOutputStream);

        return byteArrayOutputStream;
    }
}
