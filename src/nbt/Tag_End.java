package nbt;

import java.io.ByteArrayOutputStream;

public class Tag_End extends Tag {
    public Tag_End() {
        super((byte)0);
    }

    @Override
    public ByteArrayOutputStream toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2);

        byteArrayOutputStream.write(tagID);

        return byteArrayOutputStream;
    }
}
