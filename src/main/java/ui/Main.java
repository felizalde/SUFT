package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final String MAIN_FXML = "/mainview.fxml";
    public static final String SEARCH_FXML = "/search.fxml";
    public static final String CONFIG_FXML = "/config.fxml";
    public static final String MEASURE_FXML = "/measures.fxml";

    private static Main mInstance;

    private Stage primaryStage;
    private static BorderPane mainLayout;
    private static BorderPane searchPane;
    private static BorderPane configPane;
    private static BorderPane measurePane;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        this.primaryStage.setTitle("SUFT");
        this.searchPane = FXMLLoader.load(getClass().getResource(Main.SEARCH_FXML));
        this.configPane = FXMLLoader.load(getClass().getResource(Main.CONFIG_FXML));
        this.measurePane = FXMLLoader.load(getClass().getResource(Main.MEASURE_FXML));

        showMainView();
        showSearchScene();

        mInstance = this;

    }

    public void showMainView() throws IOException {
        this.mainLayout = FXMLLoader.load(getClass().getResource(Main.MAIN_FXML));
        Scene scene = new Scene(this.mainLayout);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }

    public static void showSearchScene(){
        mainLayout.setCenter(searchPane);
    }

    public static void showConfigScene(){
        mainLayout.setCenter(configPane);
    }

    public static void showMeasureScene(){
        mainLayout.setCenter(measurePane);
    }

    public static Application getInstance(){
        return mInstance;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
