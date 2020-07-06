import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerMainPage implements Initializable {
    Stage window=Main.window;
    @FXML public TextArea codeArea;
    @FXML public TextField dv,di,dt,time;
    @FXML public  Label percentage;
    @FXML public ProgressBar bar;

    private static ControllerMainPage controllerMainPage;

    private ControllerMainPage() {

    }

    public static ControllerMainPage getInstance() {
        if (controllerMainPage == null)
            controllerMainPage = new ControllerMainPage();
        return controllerMainPage;
    }






    public void newProject() {
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

    public void openProject(){
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

    public void saveProject(){
        try {
            saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadProject(){
        updateTextArea();
    }

    public void exitAZA(){
        Stage window = new Stage();
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

    public void draw(){
        saveProject();
        Simulator.simulateFile();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Diagrams");
        Controller.scene=new Scene(root1, 800, 500);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(Controller.scene);
        stage.show();
    }

    public void run(){
        saveProject();
        Simulator.simulateFile();
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
                String hold=scanner.nextLine();
                text.append(hold).append("\n");
                Pattern pattern=Pattern.compile("^d([IiVvTt])\\s+((\\d+\\.?\\d*)([pnumkMGx]?))$");
                Matcher matcher=pattern.matcher(hold);
                if (matcher.find()){
                    String sth=matcher.group(1).toLowerCase();
                    switch (sth) {
                        case "t":
                            dt.setText(matcher.group(2));
                            break;
                        case "i":
                            di.setText(matcher.group(2));
                            break;
                        case "v":
                            dv.setText(matcher.group(2));
                            break;
                        default:
                            //oh shit
                            break;
                    }

                }

                pattern=Pattern.compile("\\.tran\\s+((\\d+\\.?\\d*)([pnumkMGx]?))$");
                matcher=pattern.matcher(hold);
                if (matcher.find()){
                    time.setText(matcher.group(1));
                }

            }
            codeArea.setText(text.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() throws IOException {
        String text=codeArea.getText();
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

    public void addElementDialogue(){
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

    public VBox getLayout(String type, Stage window){
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

    public void addElement(Element element){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTextArea();
        percentage.setVisible(false);
        bar.setVisible(false);
    }
}