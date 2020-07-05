

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;


public class Main {
    static ArrayList<Object> everything=new ArrayList<>();
    static double time,t,dt=-1,dV=-1,dI=-1,i=0;
    public static void main(String[] args){
        InputManager manager=InputManager.getInstance();
        /*String current = null;
        try {
            current = new File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Current dir:"+current);
        String currentDir = System.getProperty("user.dir");
        //System.out.println("Current dir using System:" +currentDir);*/
        Scanner scanner=null;
        String path=System.getProperty("user.dir")+"\\input.txt"; //default
        //System.out.println("Working Directory: " + System.getProperty("user.dir"));
        /*scanner= new Scanner(System.in);
        System.out.println("Enter your file directory path");
        path=scanner.nextLine();*/
        File inputFile=new File(path);

        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line=scanner.nextLine(); //first line khonde nashe
        int nLine=1; //1
        Pattern pattern=Pattern.compile("\\.tran\\s+(\\d+\\.?\\d*)([pnumkMGx]?)$");
        Matcher matcher;
        boolean tran=false;
        while (scanner.hasNextLine()&&!tran){
            line=scanner.nextLine();
            ++nLine;
            matcher=pattern.matcher(line);
            if (matcher.find()) {
                setTime(matcher.group(1),matcher.group(2));
                tran=true;
                break;
            }
            if (line.charAt(0)!='*'){
                manager.setInput(line);
                if (!manager.checkInputFormat()) {
                    System.out.println("Error -1 ( line "+nLine+" )");
                    System.exit(0);
                }
            }
        }
        if (dV==-1||dI==-1||dt==-1||!tran){
            System.out.println("Error -1");
            System.exit(0);
        }
        //Nodes.updateNeighbourNodes();
        t=0;
        for (i =0 ; i<time ; i+=dt){
            calculateVoltageAtT();
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    System.out.println(((Nodes) o).name+" : "+((Nodes) o).v);
                }
                /*if (o instanceof Inductor){
                    System.out.println(((Inductor) o).name+" : "+((Inductor) o).getI(((Inductor) o).node[0]));
                }*/
            }
            System.out.println("T : "+i);
            System.out.println("--------------------------");

            t+=dt;
        }
        printAll();



    }
    public static void setTime(String t,String p){
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
        time=Double.parseDouble(t)*Math.pow(10,power);
    }

    ////---------------   calculate voltage of nodes in t=T
    public static void calculateVoltageAtT(){
        double temp=0;
        double Itotal1=0,Itotal2=0,Itotal4=0;
        while (true){
            ////-------------         reset all nodes
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    Nodes.resetNodes();
                }
                if(o instanceof Diode1){
                    if(((Diode1) o).node[1].v>((Diode1) o).node[0].v){
                        if(((Diode1) o).node[1].name.equals("0")){
                            ((Diode1) o).node[0].v = ((Diode1) o).node[1].v;
                        }
                        else {
                            ((Diode1) o).node[1].v = ((Diode1) o).node[0].v;
                        }
                    }
                }
                if(o instanceof VSource){
                    if(((VSource) o).node[1].name.equals("0")){
                        ((VSource) o).node[0].v = ((VSource) o).node[1].v+((VSource) o).getV(((VSource) o).node[0]);
                    }
                    else {
                        int temporary=0;
                        for (Element element:((VSource) o).node[0].elements){
                            if(element instanceof ISource){
                                temporary++;
                            }
                        }
                        if(temporary==0){
                            ((VSource) o).node[1].v = ((VSource) o).node[0].v-((VSource) o).getV(((VSource) o).node[0]);
                        }
                        else {
                            ((VSource) o).node[0].v = ((VSource) o).node[1].v+((VSource) o).getV(((VSource) o).node[0]);
                        }
                    }
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
                        // TODO: 7/2/2020 Diode 2 has some problems:should be solved later
                        Itotal1 = ((Nodes) o).getTotalI((Nodes) o);
                        //System.out.println("-----------------------------");
                        //System.out.println("V : "+((Nodes) o).v);
                        ((Nodes) o).v += Main.dV;
                        Itotal2 = ((Nodes) o).getTotalI((Nodes) o);
                        ((Nodes) o).v -= Main.dV;
                        //((Nodes) o).v -=2*Main.dV;
                        //Itotal4 = ((Nodes) o).getTotalI((Nodes) o);
                        //((Nodes) o).v += Main.dV;
                        //System.out.println("Node : "+((Nodes) o).name);
                        //System.out.println("I1 : "+Itotal1);
                        //System.out.println("I2 : "+Itotal2);
                        //System.out.println("I4 : "+Itotal4);
                        temp=((Nodes) o).v;
                        ((Nodes) o).v = ((Nodes) o).v + (Math.abs(Itotal1) - Math.abs(Itotal2))/Main.dI * Main.dV;
                        temp=((Nodes) o).v-temp;
                        int temporary=0;
                        for (Element element:((Nodes) o).elements){
                            if(element instanceof Capacitor){
                                temporary++;
                            }
                        }
                        if (temporary>0){
                            Itotal4 = ((Nodes) o).getTotalI((Nodes) o);
                            //System.out.println("I4 : "+Itotal4);
                            while ((Math.abs(Itotal4)>Math.abs(Itotal1))&&Math.abs(Itotal4)>Main.dI&&Math.abs(temp)<Math.pow(10,-13)){
                                //System.out.println("-----------------------------");
                                //System.out.println("V : "+((Nodes) o).v);
                                //System.out.println("Node : "+((Nodes) o).name);
                                //System.out.println("Temp : "+temp);
                                //System.out.println("I4 : "+Itotal4);
                                temp/=2;
                                ((Nodes) o).v-=temp;
                                Itotal4 = ((Nodes) o).getTotalI((Nodes) o);
                            }
                        }
                        ((Nodes) o).visited = true;
                    }
                }
            }
            ////-------------         visiting all nodes
            ////--------------------------------------
            for (Object o: Main.everything){
                if(o instanceof Diode1){
                    if(((Diode1) o).node[1].v>((Diode1) o).node[0].v){
                        if(((Diode1) o).node[1].name.equals("0")){
                            ((Diode1) o).node[0].v = ((Diode1) o).node[1].v;
                        }
                        else {
                            ((Diode1) o).node[1].v = ((Diode1) o).node[0].v;
                        }
                    }
                }
                if(o instanceof VSource){
                    if(((VSource) o).node[1].name.equals("0")){
                        ((VSource) o).node[0].v = ((VSource) o).node[1].v+((VSource) o).getV(((VSource) o).node[0]);
                    }
                    else {
                        int temporary=0;
                        for (Element element:((VSource) o).node[0].elements){
                            if(element instanceof ISource){
                                temporary++;
                            }
                        }
                        if(temporary==0){
                            ((VSource) o).node[1].v = ((VSource) o).node[0].v-((VSource) o).getV(((VSource) o).node[0]);
                        }
                        else {
                            ((VSource) o).node[0].v = ((VSource) o).node[1].v+((VSource) o).getV(((VSource) o).node[0]);
                        }
                    }
                }
            }
            ////--------------------------------------
            ////-------------     total input currents of all nodes is less than 0.01 so exits from the while
            temp =0;
            //System.out.println("temp : "+temp);
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    double I3=((Nodes) o).getTotalI((Nodes) o);
                    //System.out.println("-----------------------------");
                    //System.out.println("V : "+((Nodes) o).v);
                    //System.out.println("Node : "+((Nodes) o).name);
                    //System.out.println("I3 : " +I3);
                    if(I3>Main.dI||I3<-Main.dI){
                        temp++;
                    }
                }
            }
            //System.out.println("temp : "+temp);
            if(temp==0){
                Element.update();
                Nodes.updateNodes();
                /*for (Object o: Main.everything){
                    if (o instanceof Element){
                        Element.update();
                    }
                }
                for (Object o: Main.everything){
                    if (o instanceof Nodes){
                        Nodes.updateNodes();
                    }
                }*/
                break;
            }
            ////-------------     total input currents of all nodes is less than 0.01 so exit from the while
        }
    }
////---------------   calculate voltage of nodes in t=T
    public static void printAll(){
            try{
                FileWriter fileout=new FileWriter("output.txt",false);
                int counter=0;
                for(Object object:everything){
                    if (object instanceof Node)
                        counter++;
                }
                for(int j=1;j<=counter;j++){
                    for (Object object1:everything) {
                        if (object1 instanceof Node&&Integer.parseInt(((Node) object1).name)==j) {

                            String nodeoutput = String.format("Node %s: ", ((Node) object1).name);
                            fileout.write(nodeoutput);
                            for (int i = 0; i < ((Node) object1).vs.size(); i++) {
                                fileout.write(((Node) object1).vs.get(i) + "\t");
                            }
                            fileout.write("\n");
                        }
                    }
                }
                fileout.write("------------\n");
                for (Object object:everything){
                    if (object instanceof ISource){
                        String currentsourceoutput=String.format("ISource %s: ",((ISource) object).name);
                        fileout.write(currentsourceoutput);
                        for(int i=0;i<((ISource) object).vs.size();i++){
                            fileout.write("("+((ISource) object).vs.get(i)+" "+((ISource) object).is.get(i)+" "+((ISource) object).vs.get(i)*((ISource) object).is.get(i)+")\t");
                        }
                        fileout.write("\n");
                    }
                    if (object instanceof VSource){
                        String currentsourceoutput=String.format("VSource %s: ",((VSource) object).name);
                        fileout.write(currentsourceoutput);
                        for(int i=0;i<((VSource) object).vs.size();i++){
                            fileout.write("("+((VSource) object).vs.get(i)+" "+((VSource) object).is.get(i)+" "+((VSource) object).vs.get(i)*((VSource) object).is.get(i)+")\t");
                        }
                        fileout.write("\n");
                    }
                    if(object instanceof Resistor){
                        String  resistoroutput=String.format("Resistor %s: ",((Resistor) object).name);
                        fileout.write(resistoroutput);
                        for(int i=0;i<((Resistor) object).vs.size();i++){
                            fileout.write("("+((Resistor) object).vs.get(i)+" "+((Resistor) object).is.get(i)+" "+((Resistor) object).vs.get(i)*((Resistor) object).is.get(i)+")\t");
                        }
                        fileout.write("\n");
                    }
                    if(object instanceof Capacitor){
                        String capacitoroutput=String.format("Capacitor %s: ",((Capacitor) object).name);
                        fileout.write(capacitoroutput);
                        for(int i=0;i<((Capacitor) object).vs.size();i++){
                            fileout.write("("+((Capacitor) object).vs.get(i)+" "+((Capacitor) object).is.get(i)+" "+((Capacitor) object).vs.get(i)*((Capacitor) object).is.get(i)+")\t");
                        }
                        fileout.write("\n");
                    }
                    if (object instanceof Inductor ){
                        String inductoroutput=String.format("Inductor %s: ",((Inductor) object).name);
                        fileout.write(inductoroutput);
                        for(int i=0;i<((Inductor) object).vs.size();i++){
                            fileout.write("("+((Inductor) object).vs.get(i)+" "+((Inductor) object).is.get(i)+" "+((Inductor) object).vs.get(i)*((Inductor) object).is.get(i)+")\t");
                        }
                        fileout.write("\n");
                    }
                    if (object instanceof Diodes){
                        String diodeoutput=String.format("Diode %s: ",((Diodes) object).name);
                        fileout.write(diodeoutput);
                        for(int i=0;i<((Diodes) object).vs.size();i++){
                            fileout.write("("+((Diodes) object).vs.get(i)+" "+((Diodes) object).is.get(i)+" "+((Diodes) object).vs.get(i)*((Diodes) object).is.get(i)+")\t");
                        }
                        fileout.write("\n");
                    }

                }
                fileout.write("------------\n");
                fileout.close();
            }
            catch (IOException e){

            }
        }

    }
