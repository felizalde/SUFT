package ui;


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
import javafx.util.Pair;
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


    @FXML
    private TextField pathRelev;
    @FXML
    private ChoiceBox querysChoiceBox;
    @FXML
    private LineChart<Number, Number> Ch ;
    @FXML
    private Label resFMeasureLabel;

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
        Response response = SUFTHelper.getInstance().search(query, eval.getQueryHits());
        drawChart(eval.datacharts(response),query);
        double fmeasure = eval.fmeasure(response);
        this.resFMeasureLabel.setText(fmeasure+"");

    }

    private File showFileChooser(String title){
        FileChooser fileChooser =
                new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showOpenDialog(null);

    }

    private void drawChart(List<Pair<Number, Number>> data, String name){

        series = new XYChart.Series<Number, Number>();
        series.setName(name);
        Iterator<Pair<Number, Number>> it = data.iterator();
        while(it.hasNext()){
            Pair<Number, Number> pair = it.next();
            series.getData().add(new XYChart.Data<Number, Number>(pair.getKey(), pair.getValue()));
        }

        Ch.getData().add(series);



    }

}
