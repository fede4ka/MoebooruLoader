import API.API;
import PicturePojo.IPicture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LoaderKona1080 {
    private API api ;
    private int number = 50;

    private LoaderKona1080() {}
    LoaderKona1080 api(final API api) {
        this.api = api;
        return this;
    }

    LoaderKona1080 number(final Integer number) {
        this.number = number;
        return this;
    }

    static void StartLoading(final Consumer<LoaderKona1080> block) {
        final LoaderKona1080 loader = new LoaderKona1080();
        block.accept(loader);
        ExecutorService service = Executors.newFixedThreadPool(3);
        try {
            String url =  loader.api.getapiurl() + ";limit=" + (loader.number);
            IPicture[] pics = loader.api.getlastpics(url);
            IPicture[] filteredpics = Arrays.stream(pics).limit(loader.number).toArray(IPicture[]::new);
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
