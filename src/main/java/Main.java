import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.time.LocalDate;
import java.awt.*;



public class Main extends Application {
    static String path=System.getProperty("user.dir")+"\\input.txt"; //default
    Stage window;
    Scene mainScene;

    public static void main(String[] args){
        launch(args);
        Simulator.simulateFile();
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        MenuItem newMenu = new MenuItem("New");
        MenuItem openMenu = new MenuItem("Open");
        MenuItem openRecentMenu = new MenuItem("Open Recent");
        MenuItem saveMenu = new MenuItem("Save");
        MenuItem reloadFileMenu = new MenuItem("Reload File");
        MenuItem exitMenu = new MenuItem("Exit");

        addMenuItems(fileMenu,newMenu,openMenu,openRecentMenu,saveMenu,reloadFileMenu,exitMenu);
        addMenus(menuBar,fileMenu,editMenu,helpMenu);
        VBox layout = new VBox(menuBar);
        layout.getChildren().addAll(menuBar);
        mainScene = new Scene(layout,800,600);


        window.setScene(mainScene);
        window.setTitle("input.txt Simulation");
        window.show();
    }

    public static void addMenuItems(Menu menu, MenuItem ... items){
        for (MenuItem item: items){
            menu.add(item);
        }
    }

    public static void addMenus(MenuBar menuBar, Menu ... menus){
        for (Menu menu: menus){
            menuBar.add(menu);
        }
    }
}
