
public class Solve {

    ////---------------   calculate voltage of nodes in t=T
    public static void calculateVoltageAtT(){
        int temp=0;
        double Itotal1=0,Itotal2=0;
        while (true){
            ////-------------         reset all nodes
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    Nodes.resetNodes();
                }
            }
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    if(((Nodes) o).name.equals("0")||((Nodes) o).name.equals("gnd")){
                        ((Nodes) o).visited=true;
                    }
                }
            }
            ////-------------         reset all nodes
            ////-------------         visiting all nodes
            for (Object o: Main.everything) {
                if (o instanceof Nodes) {
                    if (!((Nodes) o).visited) {
                        Itotal1 = ((Nodes) o).getTotalI((Nodes) o);
                        ((Nodes) o).v += Main.dV;
                        Itotal2 = ((Nodes) o).getTotalI((Nodes) o);
                        ((Nodes) o).v -= Main.dV;
                        ((Nodes) o).v = ((Nodes) o).finalV + (Math.abs(Itotal1 - Itotal2)) * Main.dV / Main.dI;
                        ((Nodes) o).visited = true;
                    }
                }
            }
            ////-------------         visiting all nodes
            ////-------------     total input currents of all nodes is less than 0.01 so exit from the while
            temp =0;
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    if(((Nodes) o).getTotalI((Nodes) o)>0.01||((Nodes) o).getTotalI((Nodes) o)<-0.01){
                        temp++;
                    }
                }
            }
            if(temp==0){
                for (Object o: Main.everything){
                    if (o instanceof Element){
                        Element.update();
                    }
                }
                for (Object o: Main.everything){
                    if (o instanceof Nodes){
                        Nodes.updateNodes();
                    }
                }
                break;
            }
            ////-------------     total input currents of all nodes is less than 0.01 so exit from the while
        }
    }
    ////---------------   calculate voltage of nodes in t=T
}
