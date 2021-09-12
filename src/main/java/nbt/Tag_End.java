package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        return toBytes();
    }
}
