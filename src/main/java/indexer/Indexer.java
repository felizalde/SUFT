package indexer;

import core.IndexEvent;
import core.IndexListener;
import core.UThread;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Indexer {

	private Directory directory;
	private Analyzer analyzer;
	private SUFTReader reader;
	private IndexWriter writer;

	private ArrayList<IndexListener> listeners = new ArrayList<IndexListener>();
	
	public Indexer(Directory directory, Analyzer analyzer) throws IOException {
		this.directory = directory;
		this.analyzer = analyzer;
		this.writer = new IndexWriter(this.directory, new IndexWriterConfig(this.analyzer));
	}
	
	public boolean createIndex(String dir){
		UThread uthread;
        int ok = 0;
        int errors = 0;
		try {
			this.notifyListener(new IndexEvent("Buscando archivos XML.."));
			this.reader = new XMLReader(dir);
			this.notifyListener(new IndexEvent(this.reader.getCount()));
			while (((uthread = this.reader.read())!= null)||(this.reader.hasMore())){
					if (uthread != null ) {
                        ok++;
						this.notifyListener(new IndexEvent("Index\u00f3: " + uthread.getPath()));
						this.writer.addDocument(uthread.toDocument());

					}else{
						this.notifyListener(new IndexEvent("Not parsing.."));
                        errors++;
                    }

			}
			this.writer.close();
			this.notifyListener(new IndexEvent("Archivos indexados correctamente: " + ok));
			return true;
        } catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public void addListenersOnLoad(ArrayList<IndexListener> listeners){
		this.listeners.addAll(listeners);
	}

	private void notifyListener(IndexEvent e){
		Iterator<IndexListener> it = this.listeners.iterator();
		while (it.hasNext()){
			it.next().changed(e);
		}
	}



}
