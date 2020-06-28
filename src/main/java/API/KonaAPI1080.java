package API;

import PicturePojo.IPicture;
import PicturePojo.PictureKona;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class KonaAPI1080 implements API {
    static Proxy proxy = new Proxy(Proxy.Type.HTTP,
            new InetSocketAddress("proxy-nossl.antizapret.prostovpn.org",
                    29976));
    public KonaAPI1080() {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
    }
    public String getapiurl () { return "https://konachan.net/post.json?tags=width%3A1920..+height%3A1080..";}

    public static String namebuilder(PictureKona pic) {
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
        return "konachan.com" + " " + pic.getId() + " " + tags.replaceAll("[/|.|\\|?]", "_");
    }

    public PictureKona[] getlastpics (String url) throws IOException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(proxy);
        InputStream picsJson = connection.getInputStream();
        return mapper.readValue(picsJson, PictureKona[].class);
    }

    public void downloadTask  (IPicture p) {
        try {
            URL picurl = new URL(p.getJpeg_url());
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection(proxy);
            InputStream stream = conn.getInputStream();
            Path copied = Paths.get("pics/" + KonaAPI1080.namebuilder((PictureKona) p) + ".jpeg");
            Files.copy(stream, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
