package core;


import indexer.Indexer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import searcher.Response;
import searcher.Searcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SUFTHelper {

    private static final double ALPHA = 0.15;
    private Response lastResponse;

    private Indexer indexer;
    private Searcher searcher;
    private Analyzer analyzer;
    private Directory directory;
    private PageRankSUFT ranking;

    private static SUFTHelper helper = null;
    private ArrayList<IndexListener> listeners = new ArrayList();


    private SUFTHelper(){
    }

    public static SUFTHelper getInstance(){
        if (helper == null){
            helper = new SUFTHelper();
        }
        return helper;
    }

    public void addListenerOnLoad(IndexListener listener){
        this.listeners.add(listener);
    }


    /**
     * Create an index with xml files and save this index.
     * @param source path where the xml files are.
     * @param destination path to save the index.
     * @return if it was successful.
     */
    public boolean createIndex(String source, String destination) {
        try {
            this.directory = FSDirectory.open(new File(destination).toPath());
            this.analyzer = new EnglishAnalyzer();
            this.indexer = new Indexer(this.directory, this.analyzer);
            this.indexer.addListenersOnLoad(this.listeners);
            return indexer.createIndex(source);
        } catch (IOException e){
            System.out.println("Error al crear el index. ["+e.getMessage()+"]");
            return false;
        }

    }


    /**
     * Load an index and prepare the searcher.
     * @param source path where the index is.
     * @return if it was successful.
     */
    public boolean loadIndex(String source){
        try {
            this.directory = FSDirectory.open(new File(source).toPath());
            this.analyzer = new EnglishAnalyzer();
            this.searcher = new Searcher(this.directory, this.analyzer);
            return true;
        } catch (IOException e){
            System.out.println("Error al cargar index. [ "+e.getMessage()+"]");
            return false;
        }
    }

    public boolean isInit(){
        return (this.searcher!=null);
    }

    public Response search(String query){
        this.lastResponse = this.searcher.search(query);
        if (this.ranking != null){
          return this.ranking.sort(this.lastResponse);
        }
        return this.lastResponse;
    }

    public Response search(String query, int hits){
        this.searcher.setHits(hits);
        return search(query);
    }


    public Indexer getIndexer(){
        return this.indexer;
    }

    public void generatePageRank(String path) throws IOException{
        this.ranking = new PageRankSUFT();
        this.ranking.createGraph(path);
        this.ranking.evaluate(ALPHA);
    }

}
