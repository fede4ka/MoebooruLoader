import PicturePojo.PictureYan;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
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
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class ProxyLoader{
    private String apiurl = "https://yande.re/post.json?tags=";
    private int number = 50;
    private String tags = "";
    private  Proxy proxy = new Proxy(Proxy.Type.HTTP,
            new InetSocketAddress("proxy-nossl.antizapret.prostovpn.org", 29976));

    private ProxyLoader() {}
    ProxyLoader apiurl(final String apiurl) {
        this.apiurl = apiurl;
        return this;
    }
    ProxyLoader tags(final String tags) {
        this.tags = tags;
        return this;
    }

    ProxyLoader number(final Integer number) {
        this.number = number;
        return this;
    }

    ProxyLoader proxy(final Proxy proxy) {
        this.number = number;
        return this;
    }

    static void StartLoading(final Consumer<ProxyLoader> block) {
        final ProxyLoader loader = new ProxyLoader();
        block.accept(loader);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ExecutorService service = Executors.newFixedThreadPool(3);

        try {
            URL url = new URL(loader.apiurl + (loader.tags) + ";limit=" + (loader.number * 3));
            PictureYan[] pics = mapper.readValue(url, PictureYan[].class);
            Predicate<PictureYan> byRating = picture -> picture.getRating() != 'e';
            pics = Arrays.stream(pics).filter(byRating).limit(loader.number).toArray(PictureYan[]::new);
            new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/pics").mkdirs();
            for (PictureYan p : pics) {
                Runnable DlTask = () -> {
                    try {
                    URL picurl = new URL(p.getJpeg_url());
                    HttpURLConnection conn = (HttpURLConnection) picurl.openConnection(loader.proxy);
                    InputStream stream = conn.getInputStream();
                    Path copied = Paths.get("pics/" + PictureYan.namebuilder(p) + ".jpeg");
                    Files.copy(stream, copied, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                service.submit(DlTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}