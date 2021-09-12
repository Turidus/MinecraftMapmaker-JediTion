package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Tag_ByteArray extends Tag {
    private final String name;
    private final byte[] value;

    public Tag_ByteArray(String name, byte[] value) {
        super((byte)7);
        this.name = name;
        this.value = value;
    }

    public Tag_ByteArray(String name, List<Byte> value) {
        super((byte)7);
        this.name = name;
        this.value = new byte[value.size()];
        for (int i = 0; i < value.size(); i++) {
             this.value[i] = value.get(i);
        }
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);
        intToBytes(value.length).writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(value,0, value.length);

        return  byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        intToBytes(value.length).writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(value,0, value.length);

        return byteArrayOutputStream;
    }
}
