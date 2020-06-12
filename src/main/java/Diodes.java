public abstract class Diodes extends Element {
    public Diodes(String name, Nodes node1,Nodes node2){
        super(name, node1, node2);
    }

}

class Diode1 extends Diodes {
    public Diode1(String name, Nodes node1, Nodes node2) {
        super(name, node1, node2);
    }

    @Override
    public double getI(Nodes thisNode) {
        double i;
        if (node[1].v>node[0].v)
            i=0;
        else {
            double hold=0;
            for (Element element:node[0].elements){
                if (!element.equals(this)){
                    hold+=element.getI(element.otherNode(node[0]));
                }
            }
            i=hold;
        }
        if (thisNode.equals(this.node[0]))
            return i;
        else
            return -i;
    }

}

class Diode2 extends Diodes {
    public Diode2(String name, Nodes node1, Nodes node2) {
        super(name, node1, node2);
    }

    @Override
    public double getI(Nodes thisNode) {
        double i=(Math.exp(40*(node[0].v-node[1].v))-1)*Math.pow(10,-12);
        if (thisNode.equals(this.node[0]))
            return i;
        else
            return -i;
    }

}