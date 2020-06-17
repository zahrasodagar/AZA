import java.util.ArrayList;
import java.util.HashMap;

public abstract class Nodes {
    //ArrayList<String> names=new ArrayList<String>();
    static ArrayList<Nodes> nodes=new ArrayList<>();
    String name;
    double v,finalV=0;
    boolean visited=false;
    static boolean checkGraph=false;
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
                if (element instanceof Diode1) if (element.getI(element.node[0])==0&&checkGraph)
                    continue;
                Nodes hold=element.otherNode(node);
                if (node.neighbours.containsKey(hold)){
                    int n=node.neighbours.get(hold);
                    node.neighbours.replace(hold,n+1);
                }
                else
                    node.neighbours.put(hold,1);
            }
        }
        if (!checkGraph){
            checkGraph=true;
            for (Nodes node: nodes){
                if (node.neighbours.size()==1){
                    Nodes n=((ArrayList<Nodes>) node.neighbours.keySet()).get(0);
                    if (node.neighbours.get(n)==1){
                        System.out.println("Error -5");
                        System.exit(0);
                    }
                }
            }
            checkGND();
        }

        // TODO: 20/06/12 case 1->1: print error ...
        // TODO: 20/06/12 diode
    }

    public static void checkGND(){
        HashMap<Nodes,Boolean> connection=new HashMap<>();
        Nodes gnd=null;
        for (Nodes node: nodes){
            if (node instanceof Node)
                connection.put(node,false);
            if (node instanceof Ground) {
                gnd=node;
                connection.put(gnd,true);
            }
        }
        if (gnd==null){ // TODO: 20/06/13 Error???
            System.out.println("Error -4");
            System.exit(0);
        }
        else {
            //directly connected to gnd
            connection=setConnection(connection,gnd);
            //indirect connections TODO: 20/06/13 why???
            for (Element element:Element.elements){
                if (!connection.get(element.node[0])){
                    if (isConnected(element.node[0],connection)){
                        connection.replace(element.node[0],true);
                        connection=setConnection(connection,element.node[0]);
                    }
                }
            }
        }


        for (Nodes node:nodes){
            if (!connection.get(node)){
                System.out.println("Error -5");
                System.exit(0);
            }
        }

    }

    public static HashMap<Nodes,Boolean> setConnection(HashMap<Nodes,Boolean> connection,Nodes origin){
        for (Nodes node: origin.neighbours.keySet()){
            if (!connection.get(node)){
                connection.replace(node,true);
                connection=setConnection(connection,node);
            }
        }
        return connection;
    }

    public static boolean isConnected(Nodes origin,HashMap<Nodes,Boolean> connection){
        if (connection.get(origin))
            return true;
        boolean isConnected=false;
        ArrayList<Nodes> graph=new ArrayList<>();
        graph=getGraph(graph,origin);
        ArrayList<Element> elements=new ArrayList<>();
        for (Nodes node:graph){
            for (Element element:node.elements){
                if (!elements.contains(element))
                    elements.add(element);
            }
        }
        for (Element element: elements){
            if (element instanceof FSource){
                if (!elements.contains(((FSource) element).element))
                    isConnected=isConnected||isConnected(((FSource) element).element.node[0],connection);
            }
            if (element instanceof GSource){
                if (!graph.contains(((GSource) element).node1))
                    isConnected=isConnected||isConnected(((GSource) element).node1,connection);
            }
            if (element instanceof ESource){
                if (!graph.contains(((ESource) element).node1))
                    isConnected=isConnected||isConnected(((ESource) element).node1,connection);
            }
            if (element instanceof HSource){
                if (!elements.contains(((HSource) element).element))
                    isConnected=isConnected||isConnected(((HSource) element).element.node[0],connection);
            }
        } // TODO: 20/06/13 A to B, B to A+ GND -__-, an arraylist of graphs!?
        return isConnected;
    }

    public static HashMap<Nodes,Boolean> getGraph(Nodes node){
        HashMap<Nodes,Boolean> graph=new HashMap<>();
        ArrayList<Nodes> ns=new ArrayList<>();
        ns=getGraph(ns,node);
        for (Nodes n:ns){
            graph.put(n,false);
        }
        return graph;
    }

    public static ArrayList<Nodes> getGraph(ArrayList<Nodes> nodes, Nodes origin){
        for (Nodes node:origin.neighbours.keySet()){
            if (!nodes.contains(node)){
                nodes.add(node);
                nodes=getGraph(nodes,node);
            }
        }
        return nodes;
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



