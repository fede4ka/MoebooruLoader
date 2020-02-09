package API;

import PicturePojo.IPicture;
import PicturePojo.PictureYan;

import java.io.IOException;
import java.net.URL;

public interface API {
String getapiurl();
IPicture[] getlastpics(URL url) throws IOException;
void downloadTask  (IPicture p);

}
