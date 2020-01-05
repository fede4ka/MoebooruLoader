//Main Class Proxy

import java.net.InetSocketAddress;
import java.net.Proxy;
public class MCP {
    public static void main(String[] args) {
        ProxyLoader.StartLoading(Loader ->
                Loader.apiurl("https://yande.re/post.json?tags=")
                       .tags("azur_lane")
                        .number(3)
                        .proxy(new Proxy(Proxy.Type.HTTP,
                                         new InetSocketAddress("proxy-nossl.antizapret.prostovpn.org",
                                                                29976))));
    }
}