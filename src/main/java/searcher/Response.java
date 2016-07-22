package searcher;


import core.UThread;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private String query;
    private List<UThread> threads;

    private int itemsCount;
    private long timeSearching;

    public Response(String query){
        this.query = query;
        this.threads = new ArrayList<UThread>();
        this.itemsCount = 0;
        this.timeSearching = 0;

    }

    public void setItems(List<UThread> threads){
        this.threads = threads;
    }

    public List<UThread> getItems(){
        return this.threads;
    }

    public int getItemsCount(){
        return this.threads.size();
    }

    public long getTimeSearching(){
        return this.timeSearching;
    }

    public void setTimeSearching(long timeSearching){
        this.timeSearching = timeSearching;
    }



}
