import API.API;
import API.YanAPI;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class MC {
    public static void main(String[] args) {
        Loader.StartLoading(Loader ->
                Loader.api(new YanAPI())
                        .tags("arknights")
                        .number(50));
    }
}