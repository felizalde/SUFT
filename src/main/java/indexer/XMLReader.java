package indexer;

import core.UThread;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class XMLReader implements SUFTReader{

	private boolean more = false;
	private ArrayList<File> files;
	private Iterator<File> it ;

	
	public XMLReader(String path){
		this.files = new ArrayList<File>();

		findAllFiles(path);

		this.it = this.files.iterator();

	}
	
	@Override
	public UThread read() {
		this.more = true;
		if (this.it.hasNext()){
            File input = this.it.next();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {

				builder = factory.newDocumentBuilder();
				InputStream inputStream= new FileInputStream(input.getAbsolutePath());
				Reader reader = new InputStreamReader(inputStream,"UTF-8");
				InputSource is = new InputSource(reader);
				is.setEncoding("UTF-8");
				Document document = builder.parse(is);
				document.getDocumentElement().normalize();
                UThread _uthread = new UThread();
				
				//Setting the ID, Tittle and initial content of each Thread.
				_uthread.setID(document.getElementsByTagName("ThreadID").item(0).getTextContent());
				_uthread.setTitle(document.getElementsByTagName("Title").item(0).getTextContent());
				_uthread.setInitContent(document.getElementsByTagName("icontent").item(0).getTextContent());
				_uthread.setPath(input.getPath());
				
				//Getting all post of Thread
				NodeList nList = document.getElementsByTagName("Post");
				for(int i=0; i < nList.getLength(); i++){
					Node nNode = nList.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE){
						Element eElement = (Element)nNode;
						//Append contents of all posts..
						_uthread.addContent(eElement.getElementsByTagName("rcontent").item(0).getTextContent());
						}
				}

				return _uthread;
			   
			} catch (ParserConfigurationException e) {
                System.out.println("Error in file: " + input.getAbsolutePath());
                System.out.println(e.getMessage());
			} catch (SAXException e) {
                System.out.println("Error in file: " + input.getAbsolutePath());
                System.out.println(e.getMessage());
			} catch (IOException e) {
                System.out.println("Error in file: " + input.getAbsolutePath());
                System.out.println(e.getMessage());
			} catch (NullPointerException e){
                System.out.println("Error in file: " + input.getAbsolutePath());
                System.out.println("El archivo no tiene todos los tags necesarios");
            }
		}else {
            this.more = false;
			return null;
		}
		
		return null;
	}


	public boolean hasMore(){
		return this.more;
	}
	
	private void findAllFiles(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {//Add the FileFilter.
				this.files.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				findAllFiles(listOfFiles[i].getPath());
			}
		}

	}

    public int getCount(){
        return this.files.size();
    }


	
	

}
