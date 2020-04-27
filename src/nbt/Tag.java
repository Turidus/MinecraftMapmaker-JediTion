/**
 *
 * Extremely bare bones implementation of Minecrafts NBT format.
 * The only function it can fulfill is support the generation of schematic files and map.dat files.
 *
 *
 * Implemented are Tag_End, Tag_Byte, Tag_Short, Tag_Int, Tag_ByteArray, Tag_List_Integer (incomplete), Tag_String, Tag_Compound
 *
 *
 * Made by Turidus https://github.com/Turidus/Minecraft-MapMaker
 * Copyright (c) 2018 Turidus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package nbt;


import com.sun.istack.internal.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class Tag {

    protected final byte tagID;

    public Tag(@NotNull byte tagID){
        this.tagID = tagID;
    }

    public abstract ByteArrayOutputStream toBytes() throws IOException;

    protected ByteArrayOutputStream stringToBytes(@NotNull String input) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        shortToBytes((short)input.length()).writeTo(byteArrayOutputStream);

        byte[] bytes = input.getBytes();

        byteArrayOutputStream.write(bytes,0,bytes.length);
        return byteArrayOutputStream;
    }

    protected ByteArrayOutputStream shortToBytes(@NotNull short input){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write((byte)(input >> 8));
        byteArrayOutputStream.write((byte)input);

        return byteArrayOutputStream;
    }

    protected ByteArrayOutputStream intToBytes(@NotNull int input){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write((byte)(input >> 24));
        byteArrayOutputStream.write((byte)(input >> 16));
        byteArrayOutputStream.write((byte)(input >> 8));
        byteArrayOutputStream.write((byte)input);

        return byteArrayOutputStream;
    }
}
