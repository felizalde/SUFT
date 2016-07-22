package ui;


import core.SUFTHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    private static int SEARCH = 1;
    private static int CONFIG = 2;
    private static int MEASURES = 3;

    private int currentView;

    @FXML
    Button measuresButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currentView = SEARCH;

    }

    public void showSearch(){
        if(isSelected(SEARCH)){return;}
        Main.showSearchScene();
    }

    public void showConfig(){
        if (isSelected(CONFIG)) {
            return;
        }
        Main.showConfigScene();
    }

    public void showMeasure() {
        if (isSelected(MEASURES)) {
            return;
        }
        Main.showMeasureScene();
    }

    private boolean isSelected(int selected){
        if (this.currentView == selected){
            return true;
        }
        this.currentView = selected;
        return false;
    }



}
