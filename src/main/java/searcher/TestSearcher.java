package searcher;


import core.SUFTHelper;
import core.UThread;
import java.io.IOException;
import java.util.Iterator;


public class TestSearcher {

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Colo\\Desktop\\AyRI\\INDEX-UBUNTU";
        SUFTHelper helper = SUFTHelper.getInstance();
        helper.loadIndex(path);
        Response res = helper.search("firefox no sound with flash");
        Iterator<UThread> it = res.getItems().iterator();
        while (it.hasNext()){
            UThread thread = it.next();
            System.out.println("Thread: "+ thread.getID());
        }

    }
}
