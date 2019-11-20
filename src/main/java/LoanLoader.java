import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;

class LoanLoader {
        private int number;
        private String tags;
        private LoanLoader() {};

        LoanLoader tags(final String tags) { this.tags = tags;return this; };
        LoanLoader number(final Integer number){ this.number = number; return this; };

        static  void StartLoading(final Consumer<LoanLoader> block) {
        final LoanLoader loader = new LoanLoader();
        block.accept(loader);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ExecutorService service = Executors.newFixedThreadPool(3);

        try {
            URL url = new URL("https://yande.re/post.json?tags="+loader.tags+";limit="+loader.number);
            Picture[] pics = mapper.readValue(url, Picture[].class);
            Predicate<Picture> byRating = picture -> picture.getRating() != 'e';
            pics = Arrays.stream(pics).filter(byRating).limit(50).toArray(Picture[]::new);
            new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/pics").mkdirs();
            for (Picture p : pics) {
                Runnable DlTask = () -> Picture.downloadpicture(p);
                service.submit(DlTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}