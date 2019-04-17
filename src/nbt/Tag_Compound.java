package nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Tag_Compound extends Tag {

    private String name;
    private List<Tag> listOfTags;

    public Tag_Compound(String name, List<Tag> listOfTags) {
        super((byte)10);
        this.name = name;
        this.listOfTags = listOfTags;
    }

    @Override
    public ByteArrayOutputStream toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(tagID);
        stringToBytes(name).writeTo(byteArrayOutputStream);

        for (Tag tag : listOfTags){
            tag.toBytes().writeTo(byteArrayOutputStream);
        }
        Tag_End tag_end = new Tag_End();
        tag_end.toBytes().writeTo(byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }
}
