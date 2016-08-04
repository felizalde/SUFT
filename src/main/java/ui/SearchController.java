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
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class SearchController implements Initializable{

    @FXML
    private Label label;
    @FXML
    private TextField inputSearch;
    @FXML
    private ListView<UThread> listview = new ListView<UThread>();

    private ObservableList<UThread> result;

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
        result = FXCollections.observableArrayList();
        listview.setItems(result);
        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1){
                    String path = listview.getSelectionModel().getSelectedItem().toString();
                    if (path != null){
                      Main.getInstance().getHostServices().showDocument(path);
                    }
                  }
                }
              });
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
        result.clear();
        Response res = SUFTHelper.getInstance().search(query);
        if (res!=null){
            Iterator<UThread> it = res.getItems().iterator();
            while (it.hasNext()){
                UThread thread = it.next();
                result.add(thread);
            }
        }
    }



}
