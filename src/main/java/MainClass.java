import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;

public class MainClass {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            URL url = new URL("https://yande.re/post.json?tags=azur_lane;limit=100");
            Picture[] pics = mapper.readValue(url, Picture[].class);
            Predicate<Picture> byRating = picture -> picture.getRating() != 'e';
            pics = Arrays.stream(pics).filter(byRating).limit(50).toArray(Picture[]::new);
            new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/pics").mkdirs();
            for (Picture p : pics) {
                System.out.println(Picture.namebuilder(p));
                System.out.println(p.getRating());
                URL picurl = new URL(p.getJpeg_url());
                try (
                        ReadableByteChannel rbc = Channels.newChannel(picurl.openStream());
                        FileOutputStream fos = new FileOutputStream("pics/" + Picture.namebuilder(p) + ".jpeg")
                ) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
