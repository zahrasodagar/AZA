import java.util.ArrayList;
import java.util.HashMap;

public class Capacitor extends Element {
    HashMap<Integer, ArrayList<double[]>> c;
    double dC=0;

    // TODO: 20/06/12 dc
    public Capacitor(String name, Nodes node1,Nodes node2, HashMap<Integer, ArrayList<double[]>> c){
        super(name, node1, node2);
        this.c=c;
    }

    public double getC(){
        double hold=0;
        for (int deg:c.keySet()){
            double coeff=0;
            for (double[] cp:c.get(deg)){
                coeff+=cp[0]*Math.pow(10,cp[1]);
            }
            hold+=coeff*Math.pow(Main.t,deg);
        }
        // TODO: 20/06/07  if (hold<0) ?!
        return hold;
    }

    public void setDC(){
        double hold=getC();
        Main.t-=Main.dt;
        hold-=getC();
        Main.t+=Main.dt;
        hold=hold/Main.dt;
        dC=hold;
    }

    @Override
    public double getI(Nodes thisNode) {
        double c=getC(),dV=((node[0].v-node[1].v)-(node[0].finalV-node[1].finalV))/Main.dt;
        //System.out.print(node[0].v+"-");
        //System.out.print(node[1].v+"-");
        //System.out.print(node[0].finalV+"-");
        //System.out.println(node[1].finalV+"-");
        double i=c*dV+(node[0].v-node[1].v)*dC;
        //System.out.println("Ic : "+i);
        if (thisNode.equals(this.node[0]))
            return -i;
        else
            return i;
    }
}