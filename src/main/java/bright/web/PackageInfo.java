package bright.web;

public class PackageInfo{
    private String pname;
    private String description;
    private String url;

    public PackageInfo(String pname, String description, String url){
        this.pname = pname;
        this.description = description;
        this.url = url;
    }

    public String getPname() {
        return pname;
    }
    public String getDescription() {
        return description;
    }
    public String getUrl() {
        return url;
    }
}