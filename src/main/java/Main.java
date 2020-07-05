import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.awt.Color.*;
import static javafx.scene.paint.Color.gray;


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
        newMenu.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                //System.out.println(selectedDirectory.getPath());
                String newFilePath = selectedDirectory.getPath();

                Stage window= new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("New Project");
                window.setMinWidth(250);
                VBox layout1= new VBox();
                layout1.setPadding(new Insets(10,50,10,50));


                Label label=new Label("Enter your project's name");
                Button create=new Button("create");
                Button cancel=new Button("cancel");
                TextField textField=new TextField();
                textField.setPromptText("project's name");
                create.setOnAction(event1 -> {
                    if (!textField.getText().isEmpty()){
                        String newPath=newFilePath+"\\"+textField.getText()+".txt";
                        File newFile= new File(newPath);
                        try {
                            newFile.createNewFile();
                            path=newPath;
                            System.out.println(path);
                            window.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                cancel.setOnAction(event1 -> window.close());
                layout1.setAlignment(Pos.CENTER);
                HBox buttons= new HBox(create,cancel);
                buttons.setAlignment(Pos.CENTER);
                layout1.setSpacing(9);
                buttons.setSpacing(14);
                layout1.getChildren().addAll(label,textField,buttons);

                Scene scene=new Scene(layout1);
                window.setScene(scene);
                window.show();
            }
        });

        addMenuItems(fileMenu,newMenu,openMenu,openRecentMenu,saveMenu,reloadFileMenu,exitMenu);
        addMenus(menuBar,fileMenu,editMenu,helpMenu);
        layout.getChildren().addAll(menuBar);

        /////////// add toolbar here

        HBox mainHBox= new HBox();
        mainHBox.setBackground(new Background(new BackgroundFill(gray(0.865), CornerRadii.EMPTY, Insets.EMPTY)));
        mainHBox.setPadding(new Insets(5,5,5,5));
        layout.getChildren().add(mainHBox);

        TextArea inputTextArea = new TextArea();
        updateTextArea(inputTextArea);
        inputTextArea.setMinHeight(560);
        reloadFileMenu.setOnAction(event -> {
            try {
                updateTextArea(inputTextArea);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        VBox circuitAndData= new VBox();
        circuitAndData.setBackground(new Background(new BackgroundFill(gray(0.5), CornerRadii.EMPTY, Insets.EMPTY)));
        mainHBox.getChildren().addAll(inputTextArea,circuitAndData);

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

    public static void updateTextArea(TextArea textArea) throws FileNotFoundException {
        String text="";
        File file = new File(path);
        Scanner scanner=new Scanner(file);
        while (scanner.hasNextLine()){
            text+=scanner.nextLine()+"\n";
        }
        textArea.setText(text);

    }
}
