import API.DanbooruAPI;
import API.KonaAPI;
import API.KonaAPI1080;
import API.YanAPI;

public class MC {
    public static void main(String[] args) {
        LoaderKona1080.StartLoading(Loader ->
                Loader.api(new KonaAPI1080())
                        .number(150));
    }
}