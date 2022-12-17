package io.github.mirai42.id3lib.v1;

import io.github.mirai42.id3lib.TagUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

@Getter
@Setter
@AllArgsConstructor
public class ID3v1Tags {
    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;

    private int genre; // 1 byte song id

    /**
     * This method returns an instance with all blank tags. This is useful
     * for when you need to create tags for a file that doesn't have any.
     * @return Instance with blank tags
     */
    public static ID3v1Tags blank() {
        return new ID3v1Tags("", "", "", "", "", 255);
    }

    /**
     * Reads ID3 tags from a file. If the file doesn't have any tags, or there
     * was an error parsing them, an instance with blank tag values will be returned.
     * @param file The audio file to parse
     * @return Tags read from the file, or blank tags if there was an error.
     */
    public static ID3v1Tags read(File file) {
        try (FileChannel channel = FileChannel.open(file.toPath())) {
            channel.position(channel.size() - 128); // go to last 128 bytes
            String prefix = TagUtil.readTag(channel, 3);
            if (!"TAG".equals(prefix)) {
                channel.close();
                return blank();
            }
            String title = TagUtil.readTag(channel, 30);
            String artist = TagUtil.readTag(channel, 30);
            String album = TagUtil.readTag(channel, 30);
            String year = TagUtil.readTag(channel, 4);
            String comment = TagUtil.readTag(channel, 30);
            int genre = TagUtil.readTag(channel, 1).charAt(0);
            return new ID3v1Tags(title, artist, album, year, comment, genre);
        } catch (IOException e) {
            e.printStackTrace();
            return blank();
        }
    }

    /**
     * Write an instance of {@link ID3v1Tags} to a file.
     * @param file File to write to
     * @param tags Tags to be written
     */
    public static void write(File file, ID3v1Tags tags) {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw"); // open for writing
             FileChannel channel = f.getChannel()) {
            String prefix = TagUtil.readTag(channel, 3);
            if (!"TAG".equals(prefix)) {
                channel.position(channel.size());
                TagUtil.writeTag(channel, "TAG", 3); // add prefix if missing
            }
            TagUtil.writeTag(channel, tags.title, 30);
            TagUtil.writeTag(channel, tags.artist, 30);
            TagUtil.writeTag(channel, tags.album, 30);
            TagUtil.writeTag(channel, tags.year, 4);
            TagUtil.writeTag(channel, tags.comment, 30);
            TagUtil.writeTag(channel, String.valueOf(tags.genre), 1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
