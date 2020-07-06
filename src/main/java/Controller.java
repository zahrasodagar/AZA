import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
public class Controller {
    @FXML LineChart<Number,Number> lineChart1;
    @FXML LineChart<Number,Number> lineChart2;
    @FXML LineChart<Number,Number> lineChart3;
    public void btn(ActionEvent actionEvent){
        lineChart1.getData().clear();
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
        for (double i=0;i<Simulator.time/Simulator.dt;i++){
            series.getData().add(new XYChart.Data<Number, Number>(i*Simulator.dt,((Resistor)Simulator.everything.get(3)).vs.get(((int)i))));
        }
        lineChart1.getData().add(series);
        series.setName("V:"+((Resistor)Simulator.everything.get(3)).name);
    }
    public void btn1(ActionEvent actionEvent){
        lineChart2.getData().clear();
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
        for (double i=0;i<Simulator.time/Simulator.dt;i++){
            series.getData().add(new XYChart.Data<Number, Number>(i*Simulator.dt,((Resistor)Simulator.everything.get(3)).is.get(((int)i))));
        }
        lineChart2.getData().add(series);
        series.setName("I:"+((Resistor)Simulator.everything.get(3)).name);
    }
    public void btn2(ActionEvent actionEvent){
        lineChart3.getData().clear();
        XYChart.Series<Number,Number> series=new XYChart.Series<Number, Number>();
        for (double i=0;i<Simulator.time/Simulator.dt;i++){
            series.getData().add(new XYChart.Data<Number, Number>(i*Simulator.dt,((Resistor)Simulator.everything.get(3)).vs.get(((int)i))*((Resistor)Simulator.everything.get(3)).is.get(((int)i))));
        }
        lineChart3.getData().add(series);
        series.setName("P:"+((Resistor)Simulator.everything.get(3)).name);
    }
}
