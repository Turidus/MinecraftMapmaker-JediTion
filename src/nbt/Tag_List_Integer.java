package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Tag_List_Integer extends Tag {
    private final String name;
    private final byte payloadID;
    private final int payloadSize;
    private List<Integer> value;

    public Tag_List_Integer(String name, List<Integer> value) {
        super((byte)9);
        this.name = name;
        this.payloadID = 3;
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

    private ByteArrayOutputStream listToBytes(List<Integer> list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(Integer i : list){
            intToBytes(i).writeTo(byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }
}
