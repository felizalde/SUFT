package searcher;


import core.UThread;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Searcher {
    private Directory directory;
    private Analyzer analyzer;
    private  int hits = 50;

    public Searcher(Directory directory, Analyzer analyzer){
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public Response search(String query){
        return search(query, null);
    }

    public Response search(String query, ScoreDoc after){

        Response res = new Response(query);

        try {
            IndexReader reader = DirectoryReader.open(this.directory);

            Query q = new MultiFieldQueryParser(UThread.getSearchFields(),
                    this.analyzer, UThread.getBoostingValues()).parse(query);


            IndexSearcher searcher = new IndexSearcher(reader);

            TopDocs topDocs = searcher.searchAfter(after, q, hits);

            res.setItems(this.map(topDocs, searcher));

            reader.close();

            return res;

        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }

        return null;
    }


    private List<UThread> map(TopDocs topDocs, IndexSearcher searcher){
        ScoreDoc[] hits = topDocs.scoreDocs;
        ArrayList<UThread> threads = new ArrayList<UThread>();
        for(ScoreDoc s : hits) {
            int docId = s.doc;
            Document d = null;
            try {
                d = searcher.doc(docId);
            } catch (IOException ex) {
               System.out.println("Error al acceder al resultado de una búsqueda.");
            }
            threads.add(new UThread(d));
        }

        return threads;
    }

    public void setHits(int hits){
        this.hits = hits;
    }
}
