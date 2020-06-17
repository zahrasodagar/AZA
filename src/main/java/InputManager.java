import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager {
    private static InputManager manager;
    String input = "";

    private InputManager() {

    }

    public static InputManager getInstance() {
        if (manager == null)
            manager = new InputManager();
        return manager;
    }

    public void setInput(String input){
        this.input=input;
    }

    public boolean checkInputFormat(){
        return dSomething()||checkResistor()||checkCapacitor()||checkInductor()||checkDiode()||checkVSource()||checkISource()||checkGSource()||checkFSource()||checkESource()||checkHSource();
    }

    public boolean checkResistor(){
        Pattern pattern=Pattern.compile("^(([R|r])\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)$");
        Matcher matcher=pattern.matcher(input);

        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(3),n2=matcher.group(4),r=matcher.group(5);
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            HashMap <Integer,ArrayList<double[]>> resistance=getPolynomial(r);

            if (resistance==null)
                return false;

            Resistor resistor=new Resistor(name,node1,node2,resistance);
            addElement(resistor,node1,node2);

            return true;
        }
        return false;
    }

    public boolean checkCapacitor(){
        Pattern pattern=Pattern.compile("^([C|c]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)$");
        Matcher matcher=pattern.matcher(input);

        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3),c=matcher.group(4);
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            HashMap <Integer,ArrayList<double[]>> capacity=getPolynomial(c);

            if (capacity==null)
                return false;

            Capacitor capacitor=new Capacitor(name,node1,node2,capacity);
            addElement(capacitor,node1,node2);

            return true;
        }

        return false;
    }

    public boolean checkInductor(){
        Pattern pattern=Pattern.compile("^([L|l]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)$");
        Matcher matcher=pattern.matcher(input);

        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3),l=matcher.group(4);
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            HashMap <Integer,ArrayList<double[]>> inductance=getPolynomial(l);
            if (inductance==null)
                return false;

            Inductor inductor=new Inductor(name,node1,node2,inductance);
            addElement(inductor,node1,node2);
            return true;
        }
        return false;
    }

    public boolean checkDiode(){
        Pattern pattern=Pattern.compile("^([D|d]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d)$");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3),type=matcher.group(4);
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            Diodes diode=null;
            switch (type) {
                case "1":
                    diode=new Diode1(name,node1,node2);
                    break;
                case "2":
                    diode=new Diode2(name,node1,node2);
                    break;
                default:
                    return false;
            }
            addElement(diode,node1,node2);
            return true;
        }
        return false;
    }

    public boolean checkVSource(){
        Pattern pattern=Pattern.compile("^([V|v]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3);
            double v= Double.parseDouble(matcher.group(4)),a= Double.parseDouble(matcher.group(5)),f= Double.parseDouble(matcher.group(6)),ph= Double.parseDouble(matcher.group(7));
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            VSource vSource=new VSource(name,node1,node2,v,a,f,ph);
            addElement(vSource,node1,node2);
            return true;
        }
        return false;
    }

    public boolean checkISource(){
        Pattern pattern=Pattern.compile("^([I|i]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3);
            double i= Double.parseDouble(matcher.group(4)),a= Double.parseDouble(matcher.group(5)),f= Double.parseDouble(matcher.group(6)),ph= Double.parseDouble(matcher.group(7));
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            ISource iSource=new ISource(name,node1,node2,i,a,f,ph);
            addElement(iSource,node1,node2);
            return true;
        }
        return false;
    }

    public boolean checkGSource(){
        Pattern pattern=Pattern.compile("^([G|g]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n11=matcher.group(2),n12=matcher.group(3);
            String n21= matcher.group(4),n22= matcher.group(5);
            double a= Double.parseDouble(matcher.group(6));
            Nodes node11=getNode(n11);
            Nodes node12=getNode(n12);
            Nodes node21=getNode(n21);
            Nodes node22=getNode(n22);

            GSource gSource=new GSource(name,node11,node12,node21,node22,a);
            addElement(gSource,node11,node12);
            return true;
        }
        return false;
    }

    public boolean checkFSource(){
        Pattern pattern=Pattern.compile("^([F|f]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3),e=matcher.group(4);
            double a= Double.parseDouble(matcher.group(5));
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            Element element=getElement(e);

            FSource fSource=new FSource(name,node1,node2,element,a);
            addElement(fSource,node1,node2);
            return true;
        }
        return false;
    }

    public boolean checkESource(){
        Pattern pattern=Pattern.compile("^([G|g]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n11=matcher.group(2),n12=matcher.group(3);
            String n21= matcher.group(4),n22= matcher.group(5);
            double a= Double.parseDouble(matcher.group(6));
            Nodes node11=getNode(n11);
            Nodes node12=getNode(n12);
            Nodes node21=getNode(n21);
            Nodes node22=getNode(n22);

            ESource eSource=new ESource(name,node11,node12,node21,node22,a);
            addElement(eSource,node11,node12);
            return true;
        }
        return false;
    }

    public boolean checkHSource(){
        Pattern pattern=Pattern.compile("^([H|h]\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String name=matcher.group(1),n1=matcher.group(2),n2=matcher.group(3),e=matcher.group(4);
            double a= Double.parseDouble(matcher.group(5));
            Nodes node1=getNode(n1);
            Nodes node2=getNode(n2);
            Element element=getElement(e);

            HSource hSource=new HSource(name,node1,node2,element,a);
            addElement(hSource,node1,node2);
            return true;
        }
        return false;
    }

    public boolean dSomething(){
        Pattern pattern=Pattern.compile("^d([IiVvTt])\\s+(\\d+\\.?\\d*)([p|n|u|m|k|M|G|x]?)$");
        Matcher matcher=pattern.matcher(input);
        if (matcher.find()){
            String sth=matcher.group(1).toLowerCase(),p=matcher.group(3);
            double a=Double.parseDouble(matcher.group(2));
            int power=0;
            if (p.equals("p"))
                power=-12;
            if (p.equals("n"))
                power=-9;
            if (p.equals("u"))
                power=-6;
            if (p.equals("m"))
                power=-3;
            if (p.equals("k"))
                power=3;
            if (p.equals("M")||p.equals("x"))
                power=6;
            if (p.equals("G"))
                power=9;
            double hold=a*Math.pow(10,power);
            switch (sth) {
                case "t":
                    Main.dt=hold;
                    break;
                case "i":
                    Main.dI=hold;
                    break;
                case "v":
                    Main.dV=hold;
                    break;
                default:
                    //oh shit
                    break;
            }
            return true;
            // TODO: 20/06/11 if (dSTH<0) ?!
        }
        return false;
    }

    public HashMap <Integer,ArrayList<double[]>> getPolynomial(String input){
        Pattern pattern=Pattern.compile("([+-]?\\d*t?[^-+]+)");
        Matcher matcher=pattern.matcher(input);

        if (matcher.find()){
            HashMap <Integer,ArrayList<double[]>> dcp=new HashMap<Integer, ArrayList<double[]>>();

            while (matcher.find()){
                String s=matcher.group();
                Pattern pattern2=Pattern.compile("^([+|-]?\\d*[\\.]?\\d*)([pnumkMGx]?)t?\\^?(\\d*)");
                Matcher matcher2=pattern2.matcher(s);
                if (matcher2.find()){
                    int deg=0,power=0;
                    double coeff;
                    String c=matcher2.group(1),p=matcher2.group(2),d=matcher2.group(3);
                    if (c.equals("")||c.equals("+"))
                        coeff=1;
                    else if (c.equals("-"))
                        coeff=-1;
                    else
                        coeff= Double.parseDouble(c);

                    //p|n|u|m|k|M|G
                    if (p.equals("p"))
                        power=-12;
                    if (p.equals("n"))
                        power=-9;
                    if (p.equals("u"))
                        power=-6;
                    if (p.equals("m"))
                        power=-3;
                    if (p.equals("k"))
                        power=3;
                    if (p.equals("M")||p.equals("x"))
                        power=6;
                    if (p.equals("G"))
                        power=9;

                    if (s.contains("t")){
                        deg=1;
                        if (s.contains("^")) {
                            if (d.equals(""))
                                return null;
                            deg= Integer.parseInt(d);
                        }
                    }

                    double[] cp={coeff,power};
                    if (dcp.containsKey(deg)){
                        dcp.get(deg).add(cp);
                    }
                    else {
                        ArrayList<double[]> cpList=new ArrayList<>();
                        cpList.add(cp);
                        dcp.put(deg,cpList);
                    }
                }
                else
                    return null;
            }
            return dcp;
        }
        return null;
    }

    public void addElement(Element element, Nodes node1, Nodes node2){
        Main.everything.add(element);
        node1.elements.add(element);
        node2.elements.add(element);
    }

    public Nodes getNode(String name){
        Nodes node=null;
        for (Object o: Main.everything){
            if (o instanceof Nodes){
                if (((Nodes) o).name.equals(name))
                    node= (Nodes) o;
            }
        }
        if (node==null){
            if (name.equals("0")||name.equals("gnd"))
                node=Ground.getInstance();
            else
                node=new Node(name);
            Main.everything.add(node);
        }
        return node;
    }

    public Element getElement(String name){
        Element element=null;
        for (Object o: Main.everything){
            if (o instanceof Element){
                if (((Element) o).name.equals(name))
                    element= (Element) o;
            }
        }
        return element;
    }

}

