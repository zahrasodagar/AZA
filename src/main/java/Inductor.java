import java.util.ArrayList;
import java.util.HashMap;

public class Inductor extends Element {
    HashMap<Integer, ArrayList<double[]>> l;
    double dL;
    public Inductor(String name, Nodes node1,Nodes node2, HashMap<Integer, ArrayList<double[]>> l){
        super(name, node1, node2);
        this.l=l;
    }

    public double getL(){
        double hold=0;
        for (int deg:l.keySet()){
            double coeff=0;
            for (double[] cp:l.get(deg)){
                coeff+=cp[0]*Math.pow(10,cp[1]);
            }
            hold+=coeff*Math.pow(Main.t,deg);
        }
        // TODO: 20/06/07  if (hold<0) ?!
        return hold;
    }

    public void setDL(){
        double hold=getL();
        Main.t-=Main.dt;
        hold-=getL();
        Main.t+=Main.dt;
        hold=hold/Main.dt;
        dL=hold;
    }

    @Override
    public double getI(Nodes thisNode) {
        double l=getL(),v=node[0].v-node[1].v;
        double i=(v-l*dI)/dL;
        if (thisNode.equals(this.node[0]))
            return i;
        else
            return -i;
    }
}