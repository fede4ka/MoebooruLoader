import API.API;
import PicturePojo.IPicture;
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

public class Loader {
    private API api ;
    private int number = 50;
    private String tags = "";

    private Loader() {}
    Loader api(final API api) {
        this.api = api;
        return this;
    }
    Loader tags(final String tags) {
        this.tags = tags;
        return this;
    }

    Loader number(final Integer number) {
        this.number = number;
        return this;
    }

    static void StartLoading(final Consumer<Loader> block) {
        final Loader loader = new Loader();
        block.accept(loader);

        ExecutorService service = Executors.newFixedThreadPool(3);

        try {
            URL url = new URL( loader.api.getapiurl() + (loader.tags) + ";limit=" + (loader.number * 3));
            IPicture[] pics = loader.api.getlastpics(url);
            Predicate<IPicture> byRating = picture -> picture.getRating() != 'e';
            IPicture[] filteredpics = Arrays.stream(pics).filter(byRating).limit(loader.number).toArray(IPicture[]::new);
            new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/pics").mkdirs();
            for (IPicture picture : filteredpics) {
                Runnable DlTask = () -> { loader.api.downloadTask(picture) ;
                };
                service.submit(DlTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}
