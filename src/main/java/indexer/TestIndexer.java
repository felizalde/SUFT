package indexer;

import java.io.File;
import java.io.IOException;



import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.analysis.*;

public class TestIndexer {

	public static void main(String[] args) throws IOException {
		String pathSource = "C:\\Users\\Colo\\Desktop\\AyRI\\Forum_Data\\All";
		String pathDestination = "C:\\Users\\Colo\\Desktop\\AyRI\\INDEX-UBUNTU";
		Directory dir = FSDirectory.open(new File(pathDestination).toPath());
		Analyzer analyzer = new EnglishAnalyzer();
		Indexer indexer = new Indexer(dir, analyzer);
		indexer.createIndex(pathSource);
	}

}
