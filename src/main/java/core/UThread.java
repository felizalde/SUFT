package core;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;

import java.util.HashMap;
import java.util.Map;

public class UThread {
	private String _id;
	private String title;
	private String initContent;
	private String content = null;
	private String path;



	public UThread(){

	}

	public UThread(Document doc){
		this._id = doc.get("ID");
		this.title = doc.get("Title");
		this.initContent = doc.get("icontent");
		this.content = doc.get("rcontent");
		this.path = doc.get("path");
	}

	public String getID() {
		return _id;
	}
	public void setID(String _id) {
		this._id = _id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getInitContent() {
		return this.initContent;
	}
	public void setInitContent(String content) {
		this.initContent = content;
	}
	
	public void addContent(String content){
		this.content += content;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString(){
		return this.path; 
		
	}
	
	public Document toDocument(){
		Document doc = new Document();		
		doc.add(new TextField("ID", this.getID(), Field.Store.YES));
		doc.add(new TextField("Title",this.getTitle(), Field.Store.YES));
		doc.add(new TextField("icontent", this.getInitContent(), Field.Store.NO));
		if (this.content!=null){
			doc.add(new TextField("rcontent", this.content, Field.Store.NO));
		}
		doc.add(new StoredField("path", this.getPath()));
		return doc;		
	}

	public static String[] getSearchFields(){
		return new String[]{"ID","Title","icontent","rcontent","path"};
	}

	public static Map<String, Float> getBoostingValues(){
		Map<String,Float> boosts = new HashMap<String, Float>();
		boosts.put("ID", 1.0F );
		boosts.put("Title", 1.7F );
		boosts.put("icontent", 1.4F );
		boosts.put("rcontent", 1.0F );


		return boosts;
	}

}
