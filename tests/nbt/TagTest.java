package nbt;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagTest {

    @Test
    void tag_end() {
        Tag_End tag_end = new Tag_End();
        ByteArrayOutputStream byteArrayOutputStream = tag_end.toBytes();

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(0);
        assertEquals(byteArrayOutputStream.toString(),expected.toString());
    }

    @Test
    void tag_byte() throws IOException {
        Tag_Byte tag_byte = new Tag_Byte("test", (byte)10);
        ByteArrayOutputStream byteArrayOutputStream = tag_byte.toBytes();

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(1);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());
        expected.write(10);

        assertEquals(byteArrayOutputStream.toString(),expected.toString());
    }

    @Test
    void tag_short() throws IOException{
        Tag_Short tag_short = new Tag_Short("test", (short)10);
        ByteArrayOutputStream byteArrayOutputStream = tag_short.toBytes();

        /*for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }*/

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(2);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());
        expected.write(0);
        expected.write(10);

        assertEquals(byteArrayOutputStream.toString(),expected.toString());
    }

    @Test
    void tag_int() throws IOException{
        Tag_Int tag_int = new Tag_Int("test", 10);
        ByteArrayOutputStream byteArrayOutputStream = tag_int.toBytes();

        /*for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }*/

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(3);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());
        expected.write(0);
        expected.write(0);
        expected.write(0);
        expected.write(10);

        assertEquals(byteArrayOutputStream.toString(),expected.toString());
    }

    @Test
    void tag_byteArray() throws IOException{
        byte[] bytes = {0,1,2};
        List<Byte> bytes2 = new ArrayList<>();
        bytes2.add((byte)0);
        bytes2.add((byte)1);
        bytes2.add((byte)2);

        Tag_ByteArray tag_byteArray = new Tag_ByteArray("test", bytes);
        ByteArrayOutputStream byteArrayOutputStream = tag_byteArray.toBytes();

        Tag_ByteArray tag_byteArray2 = new Tag_ByteArray("test", bytes2);
        ByteArrayOutputStream byteArrayOutputStream2 = tag_byteArray2.toBytes();

        /*for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }
        System.out.println("");
        for(byte item : byteArrayOutputStream2.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }*/

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(7);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());
        expected.write(0);
        expected.write(1);
        expected.write(2);

        assertEquals(expected.toString(),byteArrayOutputStream.toString());

        assertEquals(byteArrayOutputStream.toByteArray().length, byteArrayOutputStream2.toByteArray().length);
        assertEquals(byteArrayOutputStream.toString(),byteArrayOutputStream2.toString());
    }

    @Test
    void tag_string() throws IOException{

        Tag_String tag_string = new Tag_String("test", "value");
        ByteArrayOutputStream byteArrayOutputStream = tag_string.toBytes();

        /*for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }*/

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(8);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());
        expected.write(0);
        expected.write(5);
        expected.write("value".getBytes());

        assertEquals(expected.toString(),byteArrayOutputStream.toString());
    }

    @Test
    void tag_list() throws IOException{
        List<Tag_Int> tagIntList = new ArrayList<>();
        tagIntList.add(new Tag_Int("a", 1));
        tagIntList.add(new Tag_Int("a", 2));

        Tag_List tag_list = new Tag_List("test", tagIntList);
        ByteArrayOutputStream byteArrayOutputStream = tag_list.toBytes();

        /*for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }*/

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(9);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());
        expected.write(3);
        expected.write(0);
        expected.write(0);
        expected.write(0);
        expected.write(2);
        expected.write(0);
        expected.write(0);
        expected.write(0);
        expected.write(1);
        expected.write(0);
        expected.write(0);
        expected.write(0);
        expected.write(2);

        assertEquals(expected.toString(),byteArrayOutputStream.toString());
    }



    @Test
    void tag_compound() throws IOException{
        List<Tag> listOfTags = new ArrayList<>();
        listOfTags.add(new Tag_Byte("1",(byte)1));
        listOfTags.add(new Tag_Byte("2",(byte)2));
        listOfTags.add(new Tag_Byte("3",(byte)3));


        Tag_Compound tag_list = new Tag_Compound("test",listOfTags);
        ByteArrayOutputStream byteArrayOutputStream = tag_list.toBytes();

        /*for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.print(String.format("%8s%n", Integer.toBinaryString(item & 0xFF)).replace(" ", "0"));
        }*/

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(10);
        expected.write(0);
        expected.write(4);
        expected.write("test".getBytes());

        expected.write(1);  //first tag
        expected.write(0);
        expected.write(1);
        expected.write("1".getBytes());
        expected.write(1);

        expected.write(1);  //second tag
        expected.write(0);
        expected.write(1);
        expected.write("2".getBytes());
        expected.write(2);

        expected.write(1);  //third tag
        expected.write(0);
        expected.write(1);
        expected.write("3".getBytes());
        expected.write(3);
        expected.write(0);

        assertEquals(expected.toString(),byteArrayOutputStream.toString());
    }

    @Test
    public void tag_longArray() throws IOException {
        long[] longArray = {1L, 2L, 3L};

        Tag_LongArray tag = new Tag_LongArray("test", longArray);
        ByteArrayOutputStream byteArrayOutputStream = tag.toBytes();

        for(byte item : byteArrayOutputStream.toByteArray()){
            System.out.printf("%8s%n", Integer.toBinaryString(item & 0xFF));
        }
    }

    @Test
    public void nbtExplorerTest() throws IOException {

        File file = new File("testResults/test.nbt");
        file.getParentFile().mkdirs();
        file.createNewFile();

        byte[] bytes = {(byte)1,(byte)2};
        int[] ints = {1,2};
        long[] longs = {1,2};
        Tag_String stringTag = new Tag_String("a", "b");

        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag_Byte("Byte", (byte)1));
        tagList.add(new Tag_Short("Short", (short) 1));
        tagList.add(new Tag_Int("Int", 1));
        tagList.add(new Tag_Long("Long", 1));
        tagList.add(new Tag_String("Long", "1"));
        tagList.add(new Tag_ByteArray("Bytes", bytes));
        tagList.add(new Tag_IntArray("Ints", ints));
        tagList.add(new Tag_LongArray("Ints", longs));

        List<Tag> tagList2 = new ArrayList<>();
        tagList2.add(new Tag_Compound("Test1", tagList));
        tagList2.add(new Tag_Compound("Test2", tagList));

        List<Tag_Compound> tagList3 = new ArrayList<>();
        tagList3.add(new Tag_Compound("aC", Collections.singletonList(stringTag)));
        tagList3.add(new Tag_Compound("aC", Collections.singletonList(stringTag)));
        tagList3.add(new Tag_Compound("aC", Collections.singletonList(stringTag)));

        tagList2.add(new Tag_List("List", tagList3));
        Tag_Compound tagC = new Tag_Compound("Test", tagList2);

        ByteArrayOutputStream byteArrayOutputStream = tagC.toBytes();

        try (OutputStream out = new GZIPOutputStream(new FileOutputStream(file))) {
            byteArrayOutputStream.writeTo(out);
        }
    }

}