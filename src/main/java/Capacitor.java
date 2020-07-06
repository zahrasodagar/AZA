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
            hold+=coeff*Math.pow(Simulator.i,deg);
            //System.out.println("hold : "+hold+"\ntime : "+Simulator.t);
        }
        if (hold<=0) {
            //System.out.println("Resistance : "+hold);
            Main.ErrorBox("ERROR",name+" value is negative at "+Simulator.i+" second" );
            System.out.println("Negative Capacity");
            System.exit(0);
        }
        return hold;
    }

    public void setDC(){
        if(Simulator.i==0){
            dC=0;
            return;
        }
        double hold=getC();
        Simulator.i-=Simulator.dt;
        hold-=getC();
        Simulator.i+=Simulator.dt;
        hold=hold/Simulator.dt;
        dC=hold;
    }

    @Override
    public double getI(Nodes thisNode) {
        double c=getC(),dV=((node[0].v-node[1].v)-(node[0].finalV-node[1].finalV))/Simulator.dt;
        //System.out.print(node[0].v+"-");
        //System.out.print(node[1].v+"-");
        //System.out.print(node[0].finalV+"-");
        //System.out.println(node[1].finalV+"-");
        setDC();
        double i=c*dV+(node[0].v-node[1].v)*dC;
        //System.out.println("Ic : "+i);
        if (thisNode.equals(this.node[0]))
            return -i;
        else
            return i;
    }
}