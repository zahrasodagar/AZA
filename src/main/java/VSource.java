public class VSource extends Source {
    public VSource(String name,Nodes node1, Nodes node2, double offset,double amplitude,double frequency,double phase){
        super(name,node1,node2,offset,amplitude,frequency,phase);
    }

    public double getV(Nodes node){
        if (node.equals(this.node[0]))
            return value();
        else
            return -value();
    }

    @Override
    public double getI(Nodes thisNode) {
        double hold=0;
        for (Element element:node[0].elements){
            if (!element.equals(this)){
                hold+=element.getI(element.otherNode(node[0]));
            }
        }
        if (thisNode.equals(this.node[0]))
            return hold;
        else
            return -hold;
    }
}

class HSource extends VSource {
    Element element;
    double a;

    public HSource(String name,Nodes node1,Nodes node2,Element element, double a){
        super(name,node1,node2,0,0,0,0);
        this.element=element;
        this.a=a;
    }

    @Override
    public double getV(Nodes node) {
        if (node.equals(this.node[0]))
            return a * element.is.get(element.is.size()-1);
        else
            return -a * element.is.get(element.is.size()-1);
    }

}

class ESource extends VSource {
    Nodes node1,node2;
    double a;
    public ESource(String name,Nodes node1,Nodes node2,Nodes node21,Nodes node22, double a){
        super(name,node1,node2,0,0,0,0);
        this.node1=node21;
        this.node2=node22;
        this.a=a;
    }

    @Override
    public double getV(Nodes node) {
        if (node.equals(this.node[0]))
            return a * (node1.vs.get(node1.vs.size()-1)-node2.vs.get(node2.vs.size()-1));
        else
            return -a * (node1.vs.get(node1.vs.size()-1)-node2.vs.get(node2.vs.size()-1));
    }

}

