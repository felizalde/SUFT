package ui;


import core.IndexEvent;
import core.IndexListener;
import core.SUFTHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;



import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfigController implements Initializable{

    @FXML
    private TextField pathLoad;
    @FXML
    private TextField pathSource;
    @FXML
    private TextField pathDestination;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label labelMsg;
    @FXML
    private Label loadLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void getPathIndex(){
        String path = showFileChooser("Cargar indice");
        this.pathLoad.setText(path);

    }

    public void getPathDataSet(){
        String path = showFileChooser("Cargar el dataset.");
        this.pathSource.setText(path);
    }

    public void getPathOutputIndex(){
        String path = showFileChooser("Directorio para guardar el indice.");
        this.pathDestination.setText(path);

    }

    public void loadIndex(){
        String path = pathLoad.getText();
        if(path==null){
            this.loadLabel.setText("Debe ingresar un path.");
            return;
        }
        if(!path.isEmpty()) {
            if (SUFTHelper.getInstance().loadIndex(path)){
                this.loadLabel.setText("El indice se cargo con exito.");
            }else{
                this.loadLabel.setText("El indice no se pudo cargar.");
            }
        }else{
            this.loadLabel.setText("Debe ingresar un path.");
        }
    }

    public void createIndex(){
        if ((pathSource.getText() == null)||(pathDestination.getText()==null)){
            return;
        }
        if ((pathSource.getText().isEmpty())||(pathDestination.getText().isEmpty())){
            System.out.println("Invalid paths");
            return;
        }
        Task worker = createWorker();
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(worker.progressProperty());
        worker.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                labelMsg.setText(newValue);
            }
        });

        Thread w = new Thread(worker);
        w.start();

    }

    private String showFileChooser(String title){
        DirectoryChooser directoryChooser =
                new DirectoryChooser();
        directoryChooser.setTitle(title);
        final File selectedDirectory =
                directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            return selectedDirectory.getAbsolutePath();
        }
        return null;
    }

    private Task createWorker(){
        return new Task() {

            double total = 0;
            double count = 0;

            @Override
            protected Object call() throws Exception {
                SUFTHelper.getInstance().addListenerOnLoad(new IndexListener() {
                    @Override
                    public void changed(IndexEvent e) {

                        if (e.type == IndexEvent.TOTAL){
                            total = e.total;

                        }else {
                            count += 1;
                            updateMessage(e.path);
                        }

                        updateProgress(count * 100 / total, 100);
                    }
                });

                if (!SUFTHelper.getInstance().createIndex(pathSource.getText(), pathDestination.getText())){
                   return false;
                }

                return true;
            }
        };
    }

}
