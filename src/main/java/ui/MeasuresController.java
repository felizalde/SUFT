package ui;

import core.PRData;
import core.QueryEval;
import core.SUFTHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import searcher.Response;
import javafx.scene.control.Tooltip;

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
    private Label resFMeasureLabel;
    @FXML
    private Label resnDCG;

    private XYChart.Series<Number, Number> series;


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
        evals = new ArrayList<QueryEval>();
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
        Ch.getData().remove(series);
        QueryEval eval = (QueryEval) querysChoiceBox.getValue();
        if (eval == null){
            System.out.println("Error: no query selected.");
            return;
        }
        String query = eval.toString();
        Response response = SUFTHelper.getInstance().search(query, POSITIONS);
        drawChart(eval.datacharts(response),query);
        this.resFMeasureLabel.setText(String.format(dFMT,  eval.fmeasure(response)));
        System.out.println("Precision: " + eval.precision(response));
        System.out.println("Recall: " + eval.recall(response));
        this.resnDCG.setText(String.format(dFMT, eval.calculateNDCG(response)));
    }

    private File showFileChooser(String title){
        FileChooser fileChooser =
                new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showOpenDialog(null);

    }

    private void drawChart(List<PRData> data, String name){

        series = new XYChart.Series<Number, Number>();
        series.setName(name);
        Iterator<PRData> it = data.iterator();
        while(it.hasNext()) {
            PRData d = it.next();
            series.getData().add(new XYChart.Data<Number, Number>(d.getRecall(), d.getPrecision()));
        }

        Ch.getData().add(series);
        int p = 1;
        for (XYChart.Data<Number, Number> d : series.getData()) {
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
