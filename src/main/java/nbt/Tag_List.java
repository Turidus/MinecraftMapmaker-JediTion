package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Tag_List extends Tag {

    private final String name;
    private final byte payloadID;
    private final int payloadSize;
    private final List<? extends Tag> value;

    public Tag_List(String name, List<? extends Tag> value) {
        super((byte)9);
        this.name = name;
        if(value.isEmpty()) this.payloadID = 0;
        else this.payloadID = value.get(0).getTagID();
        this.payloadSize = value.size();
        this.value = value;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(this.name).writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(this.payloadID);
        intToBytes(this.payloadSize).writeTo(byteArrayOutputStream);
        listToBytes(this.value).writeTo(byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream payloadToBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        intToBytes(this.payloadSize).writeTo(byteArrayOutputStream);
        listToBytes(this.value).writeTo(byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    private ByteArrayOutputStream listToBytes(List<? extends Tag> list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(Tag i : list){
            i.payloadToBytes().writeTo(byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }
}
