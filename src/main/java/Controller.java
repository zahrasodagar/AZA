import com.sun.javafx.iio.ios.IosDescriptor;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
public class Controller {
    public static Scene scene;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private NumberAxis xAxis1;
    @FXML private NumberAxis yAxis1;
    @FXML private NumberAxis xAxis2;
    @FXML private NumberAxis yAxis2;
    @FXML LineChart<Number,Number> lineChart1;
    @FXML LineChart<Number,Number> lineChart2;
    @FXML LineChart<Number,Number> lineChart3;
    public void btn(ActionEvent actionEvent) throws IOException {
        lineChart1.getData().clear();
        xAxis.setAutoRanging(false);
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
        for (double i=0;i<Brain.time/Brain.dt;i++){
            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor)Brain.everything.get(5)).vs.get(((int)i))));
        }
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(Brain.time);
        xAxis.setTickUnit(Brain.time/10);
        lineChart1.getData().add(series);
        series.setName("V:"+((Resistor)Brain.everything.get(5)).name);
    }

    public void btn1(ActionEvent actionEvent){
        lineChart2.getData().clear();
        xAxis1.setAutoRanging(false);
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
        for (double i=0;i<Brain.time/Brain.dt;i++){
            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor)Brain.everything.get(5)).is.get(((int)i))));
        }
        xAxis1.setLowerBound(0);
        xAxis1.setUpperBound(Brain.time);
        xAxis1.setTickUnit(Brain.time/10);
        lineChart2.getData().add(series);
        series.setName("I:"+((Resistor)Brain.everything.get(5)).name);
    }
    public void btn2(ActionEvent actionEvent){
        lineChart3.getData().clear();
        xAxis2.setAutoRanging(false);
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
        for (double i=0;i<Brain.time/Brain.dt;i++){
            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor)Brain.everything.get(5)).vs.get(((int)i))*((Resistor)Brain.everything.get(5)).is.get(((int)i))));
        }
        xAxis2.setLowerBound(0);
        xAxis2.setUpperBound(Brain.time);
        xAxis2.setTickUnit(Brain.time/10);
        lineChart3.getData().add(series);
        series.setName("P:"+((Resistor)Brain.everything.get(5)).name);
    }


    public void saveAs(ActionEvent actionEvent) {
        Stage stage=new Stage();


            FileChooser fileChooser = new FileChooser();
        WritableImage image = lineChart1.snapshot(new SnapshotParameters(),new WritableImage(1100,800));
        fileChooser.setInitialDirectory(new File("C:\\Users\\228al\\Desktop"));
        File file = fileChooser.showSaveDialog(stage);
        try{

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

        }
        catch (IOException e){

        }



    }
}
