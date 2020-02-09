package PicturePojo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class PictureYan implements IPicture {
    public int id;
    public String tags;
    public String jpeg_url;
    public char rating;

    public PictureYan(int id, String tags, String jpeg_url, char rating) {
        this.id = id;
        this.tags = tags;
        this.jpeg_url = jpeg_url;
        this.rating = rating;
    }

    public PictureYan() {
        super();
    }

    public static String namebuilder(PictureYan pic) {

        String tags = pic.getTags();
        String[] arr;
        if (tags.length() > 150) {
            arr = tags.split("\\s+");
            StringBuilder tagsshort = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                tagsshort.append(" ").append(arr[i]);
            }
            tags = tagsshort.toString();
        }
        return "yande.re" + " " + pic.getId() + " " + tags.replaceAll("[/|.|\\|?]", "_");
    }

    public static void downloadpicture(PictureYan p) {
        try {
            URL picurl = new URL(p.getJpeg_url());
            try (
                    ReadableByteChannel rbc = Channels.newChannel(picurl.openStream());
                    FileOutputStream fos = new FileOutputStream("pics/" + PictureYan.namebuilder(p) + ".jpeg")
            ) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getJpeg_url() {
        return jpeg_url;
    }

    public void setJpeg_url(String jpeg_url) {
        this.jpeg_url = jpeg_url;
    }

    public char getRating() {
        return rating;
    }

    public void setRating(char rating) {
        this.rating = rating;
    }
}
