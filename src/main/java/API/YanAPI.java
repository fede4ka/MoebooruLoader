package API;

import PicturePojo.IPicture;
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

public class YanAPI implements API {
    static  Proxy proxy = new Proxy(Proxy.Type.HTTP,
            new InetSocketAddress("proxy-nossl.antizapret.prostovpn.org",
                    29976));
    public YanAPI() { }
    public String getapiurl () {
        return "https://yande.re/post.json?tags=";}

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

public PictureYan[] getlastpics (URL url) throws IOException {
    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper.readValue(url, PictureYan[].class);
}

public void downloadTask  (IPicture p) {
    try {
        URL picurl = new URL(p.getJpeg_url());
        HttpURLConnection conn = (HttpURLConnection) picurl.openConnection(proxy);
        InputStream stream = conn.getInputStream();
        Path copied = Paths.get("pics/" + YanAPI.namebuilder((PictureYan) p) + ".jpeg");
        Files.copy(stream, copied, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
