package API;

import PicturePojo.IPicture;
import PicturePojo.PictureDanbooru;
import PicturePojo.PictureYan;
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

public class DanbooruAPI implements API {

    static  Proxy proxy = new Proxy(Proxy.Type.HTTP,
            new InetSocketAddress("proxy-nossl.antizapret.prostovpn.org",
                    29976));

    public DanbooruAPI() { }
    public String getapiurl () {
        return "https://danbooru.donmai.us/posts.json?tags=";}

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
        return "danbooru" + " " + pic.getId() + " " + tags.replaceAll("[/|.|\\|?]", "_");
    }

    public PictureDanbooru[] getlastpics (URL url) throws IOException {
        //System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        ObjectMapper mapper = new ObjectMapper();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0 ");
        //connection.connect();
        InputStream picsJson = connection.getInputStream();
       return mapper.readValue(picsJson, PictureDanbooru[].class);
    }

    public void downloadTask  (IPicture p) {
        try {
            URL picurl = new URL(p.getJpeg_url());
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection(proxy);
            InputStream stream = conn.getInputStream();
            Path copied = Paths.get("pics/" + DanbooruAPI.namebuilder((PictureDanbooru) p) + ((PictureDanbooru) p).getFile_ext());
            Files.copy(stream, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
