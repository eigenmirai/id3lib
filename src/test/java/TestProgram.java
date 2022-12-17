import io.github.mirai42.id3lib.ID3GenreIds;
import io.github.mirai42.id3lib.v1.ID3v1Tags;

import java.io.File;

public class TestProgram {
    public static void main(String[] args) {
        File file = new File("Kingslayer.mp3");
        ID3v1Tags tags = ID3v1Tags.blank();
        tags.setTitle("Kingslayer");
        tags.setArtist("Bring Me The Horizon");
        tags.setAlbum("POST HUMAN: SURVIVAL HORROR");
        tags.setYear("2020");
        tags.setGenre(9);
        ID3v1Tags.write(file, tags);

        // it works :DD
    }
}
