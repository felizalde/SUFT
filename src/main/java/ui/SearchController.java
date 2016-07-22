package ui;


import core.SUFTHelper;
import core.UThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import searcher.Response;

import java.io.UTFDataFormatException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable{

    @FXML
    private Label label;
    @FXML
    private TextField inputSearch;
    @FXML
    private ListView<UThread> listview = new ListView<UThread>();

    private ObservableList<UThread> sample;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listview.setCellFactory(new Callback<ListView<UThread>, ListCell<UThread>>() {

            @Override
            public ListCell<UThread> call(ListView<UThread> param) {
                ListCell<UThread> cell = new ListCell<UThread>() {

                    @Override
                    protected void updateItem(UThread item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText("Title: " + item.getTitle() +"\n" + "Path: "+item.getPath());
                        } else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
        sample = FXCollections.observableArrayList();
        listview.setItems(sample);

    }


    public void search(){
        String query = inputSearch.getText();
        if (query.isEmpty()){
            System.out.println("Query is empty.");
            return;
        }
        if (!SUFTHelper.getInstance().isInit()){
            this.label.setText("No se ha cargado ning\u00fan indice.");
            return;
        }
        this.label.setText("Resultados");
        sample.clear();
        Response res = SUFTHelper.getInstance().search(query);
        if (res!=null){
            Iterator<UThread> it = res.getItems().iterator();
            while (it.hasNext()){
                UThread thread = it.next();
                sample.add(thread);
            }
        }
    }



}
