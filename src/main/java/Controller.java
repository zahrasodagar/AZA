import com.sun.javafx.iio.ios.IosDescriptor;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
public class Controller implements Initializable {
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
    public void btn() throws IOException {
        lineChart1.getData().clear();
        xAxis.setAutoRanging(false);
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
//        for (double i=0;i<Brain.time/Brain.dt;i++){
//            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor)Brain.everything.get(5)).vs.get(((int)i))));
//        }
        addVoltage(ControllerMainPage.voltagename,series);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(Brain.time);
        xAxis.setTickUnit(Brain.time/10);
        lineChart1.getData().add(series);

    }


    public void btn1(){
        lineChart2.getData().clear();
        xAxis1.setAutoRanging(false);
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
//        for (double i=0;i<Brain.time/Brain.dt;i++){
//            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor)Brain.everything.get(5)).is.get(((int)i))));
//        }
        addCurrent(ControllerMainPage.currentname,series);
        xAxis1.setLowerBound(0);
        xAxis1.setUpperBound(Brain.time);
        xAxis1.setTickUnit(Brain.time/10);
        lineChart2.getData().add(series);
    }

    public void btn2(){
        lineChart3.getData().clear();
        xAxis2.setAutoRanging(false);
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
//        for (double i=0;i<Brain.time/Brain.dt;i++){
//            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor)Brain.everything.get(5)).vs.get(((int)i))*((Resistor)Brain.everything.get(5)).is.get(((int)i))));
//        }
        addPower(ControllerMainPage.powername,series);
        xAxis2.setLowerBound(0);
        xAxis2.setUpperBound(Brain.time);
        xAxis2.setTickUnit(Brain.time/10);
        lineChart3.getData().add(series);
    }
    public void addVoltage(String voltageorder,XYChart.Series<Number,Number> series){
        String[] token = voltageorder.split("[: ]+");
        if(token[0].equals("Node")){
            for(Object object:Brain.everything){
                if(object instanceof Nodes){
                    if(((Nodes) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Nodes) object).vs.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Resistor")){
            for(Object object:Brain.everything){
                if(object instanceof Resistor){
                    if(((Resistor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor) object).vs.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Capacitor")){
            for(Object object:Brain.everything){
                if(object instanceof Capacitor){
                    if(((Capacitor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Capacitor) object).vs.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("ISource")){
            for(Object object:Brain.everything){
                if(object instanceof ISource){
                    if(((ISource) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((ISource) object).vs.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("VSource")){
            for(Object object:Brain.everything){
                if(object instanceof VSource){
                    if(((VSource) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((VSource) object).vs.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Inductor")){
            for(Object object:Brain.everything){
                if(object instanceof Inductor){
                    if(((Inductor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Inductor) object).vs.get(((int)i))));
                        }
                    }
                }
            }
        }
        series.setName("    V:"+token[1]);
    }
    public void addCurrent(String currentorder,XYChart.Series<Number,Number> series){
        String[] token = currentorder.split("[: ]+");

        if(token[0].equals("Resistor")){
            for(Object object:Brain.everything){
                if(object instanceof Resistor){
                    if(((Resistor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor) object).is.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Capacitor")){
            for(Object object:Brain.everything){
                if(object instanceof Capacitor){
                    if(((Capacitor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Capacitor) object).is.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("ISource")){
            for(Object object:Brain.everything){
                if(object instanceof ISource){
                    if(((ISource) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((ISource) object).is.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("VSource")){
            for(Object object:Brain.everything){
                if(object instanceof VSource){
                    if(((VSource) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((VSource) object).is.get(((int)i))));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Inductor")){
            for(Object object:Brain.everything){
                if(object instanceof Inductor){
                    if(((Inductor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Inductor) object).is.get(((int)i))));
                        }
                    }
                }
            }
        }
        series.setName("    I:"+token[1]);
    }
    public void addPower(String powereorder,XYChart.Series<Number,Number> series){
        String[] token = powereorder.split("[: ]+");
        if(token[0].equals("Resistor")){
            for(Object object:Brain.everything){
                if(object instanceof Resistor){
                    if(((Resistor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Resistor) object).vs.get(((int)i))*((Resistor) object).is.get((int)i)));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Capacitor")){
            for(Object object:Brain.everything){
                if(object instanceof Capacitor){
                    if(((Capacitor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Capacitor) object).vs.get(((int)i))*((Capacitor) object).is.get((int)i)));
                        }
                    }
                }
            }
        }
        if(token[0].equals("ISource")){
            for(Object object:Brain.everything){
                if(object instanceof ISource){
                    if(((ISource) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((ISource) object).vs.get(((int)i))*((ISource) object).is.get((int)i)));
                        }
                    }
                }
            }
        }
        if(token[0].equals("VSource")){
            for(Object object:Brain.everything){
                if(object instanceof VSource){
                    if(((VSource) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((VSource) object).vs.get(((int)i))*((VSource) object).is.get((int)i)));
                        }
                    }
                }
            }
        }
        if(token[0].equals("Inductor")){
            for(Object object:Brain.everything){
                if(object instanceof Inductor){
                    if(((Inductor) object).name.equals(token[1])){
                        for (double i=0;i<Brain.time/Brain.dt;i++){
                            series.getData().add(new XYChart.Data<Number, Number>(i*Brain.dt,((Inductor) object).vs.get(((int)i))*((Inductor) object).is.get((int)i)));
                        }
                    }
                }
            }
        }
        series.setName("    P:"+token[1]);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            btn();
            btn1();
            btn2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
