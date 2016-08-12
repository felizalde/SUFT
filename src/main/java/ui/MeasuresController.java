package ui;

import core.PRData;
import core.QueryEval;
import core.SUFTHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import searcher.Response;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class MeasuresController implements Initializable {

    private ArrayList<QueryEval> evals;
    private File file;
    private static final int POSITIONS = 10;
    private static final String dFMT = "%1.4f";

    @FXML
    private TextField pathRelev;
    @FXML
    private ChoiceBox querysChoiceBox;
    @FXML
    private LineChart<Number, Number> Ch ;
    @FXML
    private Label resnDCGsp;
    @FXML
    private Label resnDCGcp;
    @FXML
    private Spinner spinner;

    private XYChart.Series<Number, Number> sorted;
    private XYChart.Series<Number, Number> unsorted;


    public void initialize(URL location, ResourceBundle resources) {
    }

    public void loadQuerysEvalFile(){
        file = showFileChooser("Cargar Juicios de Relevancia");
        if (file!=null){pathRelev.setText(file.getAbsolutePath());}


    }

    public void generateQueryEvals(){

        if (file == null){
            System.out.println("No file selected.");
            return;
        }
        evals = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("<Query>")){
                    String[] second = line.split(">");
                    String[] query = second[1].split("<");
                    evals.add(new QueryEval(query[0]));
                 }
                else{
                	String[ ] dat = line.split(" ");
	                try{
	                	evals.get(evals.size()-1).put(dat[0], Integer.parseInt(dat[1]));
	                }
	                catch (RuntimeException e){
	                	System.out.println(e.getMessage());
	                }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        querysChoiceBox.setItems(FXCollections.observableArrayList(evals.toArray()));
    }

    public void showMeasures(){
    	int positions = POSITIONS;
    	try{
    		positions = (Integer)spinner.getValue();
    	}catch (Exception e){
    		e.printStackTrace();
    	}


        Ch.getData().remove(sorted);
        Ch.getData().remove(unsorted);
        QueryEval eval = (QueryEval) querysChoiceBox.getValue();
        if (eval == null){
            System.out.println("Error: no query selected.");
            return;
        }
        String query = eval.toString();
        Response rsorted = SUFTHelper.getInstance().search(query, positions);
        Response runsorted = SUFTHelper.getInstance().getLastResponse();
        drawSorted(eval.datacharts(rsorted));
        drawUnsorted(eval.datacharts(runsorted));
        this.resnDCGsp.setText(String.format(dFMT,  eval.calculateNDCG(runsorted)));
        this.resnDCGcp.setText(String.format(dFMT, eval.calculateNDCG(rsorted)));
    }

    private File showFileChooser(String title){
        FileChooser fileChooser =
                new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showOpenDialog(null);

    }


    //Should refactor here.
    private void drawSorted(List<PRData> data){

        sorted = new XYChart.Series<Number, Number>();
        sorted.setName("sorted");
        Iterator<PRData> it = data.iterator();

        while(it.hasNext()){
            PRData prdata = it.next();
            XYChart.Data<Number, Number> d = new XYChart.Data<Number, Number>(prdata.getRecall(), prdata.getPrecision());
            sorted.getData().add(d);
        }

        Ch.getData().add(sorted);
        int p = 1;
        for (XYChart.Data<Number, Number> d : sorted.getData()) {
            Tooltip.install(d.getNode(), new Tooltip(
            		"Position: " + p
            		+ "\nPrecision: " + String.format(dFMT,d.getYValue())
            		+ "\nRecall: " + String.format(dFMT,d.getXValue())));
            d.getNode().setOnMouseEntered(event -> d.getNode().setStyle("-fx-background-color: ORANGE;"));
            d.getNode().setOnMouseExited(event -> d.getNode().setStyle(""));
            p++;
        }

    }

    //Should refactor here.
    private void drawUnsorted(List<PRData> data){

        unsorted = new XYChart.Series<Number, Number>();
        unsorted.setName("unsorted");
        Iterator<PRData> it = data.iterator();

        while(it.hasNext()){
            PRData prdata = it.next();
            XYChart.Data<Number, Number> d = new XYChart.Data<Number, Number>(prdata.getRecall(), prdata.getPrecision());
            unsorted.getData().add(d);
        }

        Ch.getData().add(unsorted);
        int p = 1;
        for (XYChart.Data<Number, Number> d : unsorted.getData()) {
            Tooltip.install(d.getNode(), new Tooltip(
            		"Position: " + p
            		+ "\nPrecision: " + String.format(dFMT,d.getYValue())
            		+ "\nRecall: " + String.format(dFMT,d.getXValue())));
            d.getNode().setOnMouseEntered(event -> d.getNode().setStyle("-fx-background-color: ORANGE;"));
            d.getNode().setOnMouseExited(event -> d.getNode().setStyle(""));
            p++;
        }

    }

}
