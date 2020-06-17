import java.util.ArrayList;
import java.util.HashMap;

public abstract class Nodes {
    //ArrayList<String> names=new ArrayList<String>();
    static ArrayList<Nodes> nodes=new ArrayList<>();
    String name;
    double v,finalV=0;
    boolean visited=false;
    ArrayList<Element> elements= new ArrayList<>();
    HashMap<Nodes,Integer> neighbours=new HashMap<>();
    ArrayList<Double> vs=new ArrayList<>();

    public static void updateNodes(){
        for (Nodes node: nodes){
            if (node instanceof Node) {
                node.finalV=node.v;
                node.vs.add(node.finalV);
            }
        }
    }

    public double getTotalI(Nodes node){
        double I=0;
        for (Element element:node.elements){
            System.out.println(element.name);
            I+=element.getI(node);
        }
        return I;
    }

    public static void resetNodes(){
        for (Nodes node: nodes){
            node.visited=false;
        }
    }

    public static void updateNeighbourNodes(){
        for (Nodes node: nodes){
            for (Element element: node.elements){
                Nodes hold=element.otherNode(node);
                if (node.neighbours.containsKey(hold)){
                    int n=node.neighbours.get(hold);
                    node.neighbours.replace(hold,n+1);
                }
                else
                    node.neighbours.put(hold,1);
            }
        }
        // TODO: 20/06/12 case 1->1: print error ...
        // TODO: 20/06/12 diode
    }

}

class Node extends Nodes {

    public Node (String name){
        this.name=name;
        Nodes.nodes.add(this);
    }

}

class Ground extends Nodes {
    private static Ground ground;
    private Ground() {
        this.name="0";
        v=0;
    }

    public static Ground getInstance() {
        if (ground == null)
            ground = new Ground();
        Nodes.nodes.add(ground);
        return ground;
    }

}



