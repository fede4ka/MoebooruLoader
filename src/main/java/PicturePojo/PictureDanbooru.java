package PicturePojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class PictureDanbooru implements IPicture {
    public int id;
    public String tags;
    public String jpeg_url;
    public String file_ext;
    public char rating;

    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    public PictureDanbooru(int id, String tags, String jpeg_url, char rating) {
        this.id = id;
        this.tags = tags;
        this.jpeg_url = jpeg_url;
        this.rating = rating;
    }

    public PictureDanbooru() {
        super();
    }

    public static String namebuilder(PictureDanbooru pic) {

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
        //return "danbooru" + " " + pic.getId() + " " + tags.replaceAll("[/|.|\\|?]", "_");
        return "danbooru" + " " + pic.getId() + " " + tags.replaceAll("[/\\\\\\-+.^:,?]", "");
    }

    public static void downloadpicture(PictureDanbooru p) {
        try {
            URL picurl = new URL(p.getJpeg_url());
            try (
                    ReadableByteChannel rbc = Channels.newChannel(picurl.openStream());
                    FileOutputStream fos = new FileOutputStream("pics/" + PictureDanbooru.namebuilder(p) + ".jpeg")
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

    @JsonProperty("tag_string")
    public String getTags() {
        return tags;
    }
    @JsonProperty("tag_string")
    public void setTags(String tags) {
        this.tags = tags;
    }


    @JsonProperty("file_url")
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
