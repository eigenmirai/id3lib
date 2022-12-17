package io.github.mirai42.id3lib;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TagUtil {
    public static String readTag(FileChannel channel, int len) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(len);
        channel.read(buf);
        String tag = new String(buf.array());
        int firstNullByte;
        if ((firstNullByte = tag.indexOf(0)) != -1) {
            return tag.substring(0, firstNullByte); // remove trailing null bytes
        }
        return tag;
    }

    public static void writeTag(FileChannel channel, String value, int maxLen) throws IOException {
        int len = value.length();
        String val = value;
        if (len > maxLen) {
            val = value.substring(0, maxLen);
            len = val.length();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(val.getBytes());
        channel.write(byteBuffer);
        channel.write(ByteBuffer.allocate(maxLen - len)); // fill in remaining space
    }
}
