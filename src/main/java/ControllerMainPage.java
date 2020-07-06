import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ControllerMainPage implements Initializable {
    Stage window=Main.window;
    @FXML public TextArea codeArea;

    public void newProject(ActionEvent actionEvent) {
        Stage stage = Main.window;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            //System.out.println(selectedDirectory.getPath());
            String newFilePath = selectedDirectory.getPath();

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("New Project");
            window.setMinWidth(250);
            VBox layout1 = new VBox();
            layout1.setPadding(new Insets(10, 50, 10, 50));


            javafx.scene.control.Label label = new javafx.scene.control.Label("Enter your project's name");
            javafx.scene.control.Button create = new javafx.scene.control.Button("Create");
            javafx.scene.control.Button cancel = new javafx.scene.control.Button("Cancel");
            javafx.scene.control.TextField textField = new TextField();
            textField.setPromptText("project's name");
            create.setOnAction(event1 -> {
                if (!textField.getText().isEmpty()) {
                    String newPath = newFilePath + "\\" + textField.getText() + ".txt";
                    File newFile = new File(newPath);
                    try {
                        newFile.createNewFile();
                        Main.path = newPath;
                        //System.out.println(path);
                        updateTextArea();
                        window.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            cancel.setOnAction(event1 -> window.close());
            layout1.setAlignment(Pos.CENTER);
            HBox buttons = new HBox(create, cancel);
            buttons.setAlignment(Pos.CENTER);
            layout1.setSpacing(9);
            buttons.setSpacing(14);
            layout1.getChildren().addAll(label, textField, buttons);

            Scene scene = new Scene(layout1);
            window.setScene(scene);
            window.show();
        }
    }

    public void openProject(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            Main.path=file.getPath();
            //System.out.println(path);
            updateTextArea();
        }
    }

    public void saveProject(ActionEvent actionEvent){
        try {
            saveFile(codeArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadProject(ActionEvent actionEvent){
        updateTextArea();
    }

    public void exitAZA(ActionEvent actionEvent){
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Project");
        window.setMinWidth(250);
        VBox layout1= new VBox();
        layout1.setMinSize(300, 100);
        layout1.setPadding(new Insets(10,50,10,50));


        javafx.scene.control.Label label=new javafx.scene.control.Label("Are you sure you want to exit AZA?");
        javafx.scene.control.Button yes=new javafx.scene.control.Button("Yes");
        javafx.scene.control.Button cancel=new Button("No");

        //yes.(e -> yes.setStyle(String.valueOf(color(1, 0.1,0.1 ))));

        yes.setOnAction(event1 -> {
            // TODO: 20/07/05 Ask to save before exit
            System.exit(0);
        });
        cancel.setOnAction(event1 -> window.close());
        layout1.setAlignment(Pos.CENTER);
        HBox buttons= new HBox(yes,cancel);
        buttons.setAlignment(Pos.CENTER);
        layout1.setSpacing(9);
        buttons.setSpacing(14);
        layout1.getChildren().addAll(label,buttons);

        Scene scene=new Scene(layout1);
        window.setScene(scene);
        window.show();
    }

    public void addElement(ActionEvent actionEvent){
        addElementDialogue();
    }







    public void updateTextArea()  {
        StringBuilder text= new StringBuilder();
        File file = new File(Main.path);
        try {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNextLine()){
                text.append(scanner.nextLine()).append("\n");
            }
            codeArea.setText(text.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(String text) throws IOException {
        File dataFile=new File(Main.path);
        FileWriter fw=new FileWriter(dataFile);
        BufferedWriter writer = new BufferedWriter(fw);
        String[] lines=text.split("\n");
        for (String line:lines){
            writer.write(line);
            writer.newLine();
        }
        writer.close();
        fw.close();
    }

    public static void addElementDialogue(){
        Element element=null;

        Stage window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Element");
        window.setMinWidth(250);
        VBox layout1= new VBox();
        layout1.setPadding(new Insets(10,50,10,50));


        javafx.scene.control.Label label=new javafx.scene.control.Label("Select the type of element");
        ComboBox comboBox=new ComboBox();
        comboBox.getItems().addAll("Resistor","Capacitor","Inductor","Diode 1","Diode 2",
                "VSource DC","VSource AC", "ISource DC","ISource AC",
                "ESource", "HSource", "GSource", "FSource");
        Button next=new Button("Next");
        Button cancel=new Button("Cancel");

        layout1.setAlignment(Pos.CENTER);
        HBox buttons= new HBox(next,cancel);
        buttons.setAlignment(Pos.CENTER);
        layout1.setSpacing(9);
        buttons.setSpacing(14);
        layout1.getChildren().addAll(label,comboBox,buttons);

        Scene scene1=new Scene(layout1);

        next.setOnAction(event1 -> {
            if (comboBox.getValue()!=null){
                Scene scene2=new Scene(getLayout((String) comboBox.getValue(),window));
                window.setScene(scene2);
            }
        });
        cancel.setOnAction(event1 -> window.close());

        window.setScene(scene1);
        window.show();


        addElement(element);
    }

    public static VBox getLayout(String type, Stage window){
        window.setTitle("New "+type);
        String line="";
        VBox layout= new VBox();
        layout.setPadding(new Insets(10,50,10,50));

        javafx.scene.control.Label label=new javafx.scene.control.Label("Enter the parameters");
        Button add=new Button("Add");
        Button cancel=new Button("Cancel");
        GridPane grid=new GridPane();
        layout.setAlignment(Pos.CENTER);
        HBox buttons= new HBox(add,cancel);
        buttons.setAlignment(Pos.CENTER);
        layout.setSpacing(9);
        buttons.setSpacing(14);
        layout.getChildren().addAll(label,grid);
        add.setOnAction(event -> {
            InputManager manager=InputManager.getInstance();
            manager.input=line;
            if (manager.checkInputFormat()) // TODO: 20/07/06 DIALOGUE!!!
                window.close();
        });
        cancel.setOnAction(event1 -> window.close());

        javafx.scene.control.Label n=new javafx.scene.control.Label("Node 1");
        TextField name=new TextField();

        javafx.scene.control.Label n1=new javafx.scene.control.Label("Node 1");
        TextField node1=new TextField();

        javafx.scene.control.Label n2=new javafx.scene.control.Label("Node 2");
        TextField node2=new TextField();

        javafx.scene.control.Label val=new Label();
        TextField value=new TextField();

        grid.setHgap(10);
        grid.setVgap(7);

        grid.add(n,0,0);
        grid.add(name,1,0);
        grid.add(n1,0,1);
        grid.add(node1,1,1);
        grid.add(n2,0,2);
        grid.add(node2,1,2);






        if (type.equals("Resistor")){
            val.setText("Resistance");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("Capacitor")){
            val.setText("Capacity");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("Inductor")){
            val.setText("Inductance");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("Diode 1")){

        }
        if (type.equals("Diode 2")){

        }

        if (type.equals("VSource DC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);

        }
        if (type.equals("VSource AC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);

        }
        if (type.equals("ISource DC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("ISource AC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);

        }

        if (type.equals("ESource")){

        }
        if (type.equals("HSource")){

        }

        if (type.equals("GSource")){

        }
        if (type.equals("FSource")){

        }
        layout.getChildren().addAll(buttons);


        return layout;
    }

    public static void addElement(Element element){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTextArea();
    }
}