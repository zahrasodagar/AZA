import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;


public class Main extends Application {
    static String path=System.getProperty("user.dir")+"\\input.txt"; //default
    Stage window;
    Scene mainScene;

    public static void main(String[] args){
        launch(args);
        //Simulator.simulateFile();
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        VBox layout = new VBox();
        mainScene = new Scene(layout,800,600);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        MenuItem newMenu = new MenuItem("New");
        MenuItem openMenu = new MenuItem("Open");
        Menu openRecentMenu = new Menu("Open Recent");
        MenuItem saveMenu = new MenuItem("Save");
        MenuItem reloadFileMenu = new MenuItem("Reload File");
        MenuItem exitMenu = new MenuItem("Exit");

        openMenu.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showOpenDialog(window);
            if (file != null) {
                path=file.getPath();
                System.out.println(path);
            }
        });

        addMenuItems(fileMenu,newMenu,openMenu,openRecentMenu,saveMenu,reloadFileMenu,exitMenu);
        addMenus(menuBar,fileMenu,editMenu,helpMenu);
        layout.getChildren().addAll(menuBar);



        window.setScene(mainScene);
        window.setTitle("input.txt Simulation");
        window.show();
    }

    public static void addMenuItems(Menu menu, MenuItem ... items){
        for (MenuItem item: items){
            menu.getItems().add(item);
        }
    }

    public static void addMenus(MenuBar menuBar, Menu ... menus){
        for (Menu menu: menus){
            menuBar.getMenus().add(menu);
        }
    }
}
