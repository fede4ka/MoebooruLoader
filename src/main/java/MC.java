import API.YanAPI;

public class MC {
    public static void main(String[] args) {
        Loader.StartLoading(Loader ->
                Loader.api(new YanAPI())
                        .tags("tag")
                        .number(50));
    }
}