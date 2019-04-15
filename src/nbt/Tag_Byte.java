package nbt;

import com.sun.istack.internal.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Tag_Byte extends Tag {
    private String name;
    private byte value;

    public Tag_Byte(@NotNull String name, @NotNull byte value) {
        super((byte)1);
        this.name = name;
        this.value = value;

    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2);

        byteArrayOutputStream.write(tagID);

        stringToBytes(name).writeTo(byteArrayOutputStream);

        byteArrayOutputStream.write(value);

        return byteArrayOutputStream;
    }
}
