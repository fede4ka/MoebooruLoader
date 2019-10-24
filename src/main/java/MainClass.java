import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

public class MainClass {

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL( "https://yande.re/post.json?tags=azur_lane;limit=50");
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
           Picture[] pics = mapper.readValue(url, Picture[].class);
            new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/pics").mkdirs();
            for (Picture p : pics) {
                System.out.println(Picture.namebuilder(p));

                URL picurl = new URL(p.getJpeg_url());
                try (
                        ReadableByteChannel rbc = Channels.newChannel(picurl.openStream());
                        FileOutputStream fos = new FileOutputStream("pics/"+ Picture.namebuilder(p)+".jpeg");
                     )
                {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
