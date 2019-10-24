
public class Picture {
    public int id;
    public String tags;
    public String jpeg_url;

    public Picture() {
        super();
    }
    public Picture(int id, String tags, String jpeg_url) {
        this.id = id;
        this.tags = tags;
        this.jpeg_url = jpeg_url;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setJpeg_url(String jpeg_url) {
        this.jpeg_url = jpeg_url;
    }
    public int getId() {
        return id;
    }
    public String getTags() {
        return tags;
    }
    public String getJpeg_url() {
        return jpeg_url;
    }
    public static String namebuilder (Picture pic) {

        String tags = pic.getTags();
        String [] arr;
        if (tags.length()>150)  {
            arr = tags.split("\\s+");
            String tagsshort="";
            for(int i=0; i<5 ; i++){
                tagsshort = tagsshort + " " + arr[i] ;
            }
            tags = tagsshort;}
        return "yande.re" +" " +pic.getId() +" " + tags.replaceAll("[/|.|\\|]+", "_") ;
    }
}
