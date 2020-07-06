

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
public class Diagram extends Application {
    static String path=System.getProperty("user.dir")+"\\input.txt"; //default
    @Override
    public void start(Stage stage) throws Exception {
        Simulator.simulateFile();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 1300, 800));
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
