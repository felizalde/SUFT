package core;


public class IndexEvent {

    public static int TOTAL = 1;
    public static int NEW = 2;

    public String path;
    public int total;
    public int type;

    public IndexEvent(String path){
        this.path = path;
        this.type = NEW;
    }

    public IndexEvent(int total){
        this.total = total;
        this.type = TOTAL;
    }

}
