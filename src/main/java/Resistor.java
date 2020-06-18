import java.util.ArrayList;
import java.util.HashMap;

public class Resistor extends Element {
    HashMap <Integer, ArrayList<double[]>> r;

    public Resistor(String name, Nodes node1,Nodes node2, HashMap <Integer,ArrayList<double[]>> r){
        super(name, node1, node2);
        this.r=r;
    }

    public double getR(){
        double hold=0;

        for (int deg:r.keySet()){
            double coeff=0;
            for (double[] cp:r.get(deg)){
                coeff+=cp[0]*Math.pow(10,cp[1]);
            }
            hold+=coeff*Math.pow(Main.time,deg);
        }
        if (hold<0) {
            System.out.println("Negative Resistance");
            System.exit(0);
        }
        return hold;
    }

    @Override
    public double getI(Nodes thisNode) {
        double r=getR();
        double i=(node[0].v-node[1].v)/r;
        if (thisNode.equals(this.node[0]))
            return -i;
        else
            return i;
    }

}