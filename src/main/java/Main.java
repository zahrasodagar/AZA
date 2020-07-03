

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
    public static int counter=0;
    static ArrayList<Object> everything=new ArrayList<>();
    static double time,t,dt=-1,dV=-1,dI=-1;
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
        for (double i =0 ; i<time ; i+=dt){
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
            printAll(i);
            t+=dt;
        }



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
                            while ((Math.abs(Itotal4)>Math.abs(Itotal1))&&Math.abs(Itotal4)>0.01&&Math.abs(temp)<Math.pow(10,-13)){
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
                    if(I3>0.01||I3<-0.01){
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
    public static void printAll(double time1){
        if(counter==0){
            try{
                FileWriter fileout=new FileWriter("output.txt",false);
                fileout.write("Time:"+time1+" s\n------------\n");
                for(Object object:everything){
                    if(object instanceof Node){
                        String nodeoutput=String.format("Node %s: voltage=%f V\n",((Node) object).name,((Node) object).v);
                        fileout.write(nodeoutput);
                    }

                }
                fileout.write("------------\n");
                for (Object object:everything){
                    if (object instanceof ISource){
                        String currentsourceoutput=String.format("ISource %s: voltage=%f V, current=%f A\n",((ISource) object).name,((ISource) object).node[0].v-((ISource) object).node[1].v,((ISource) object).getI(((ISource) object).node[1]));
                        fileout.write(currentsourceoutput);
                    }
                    if (object instanceof VSource){
                        String currentsourceoutput=String.format("VSource %s: voltage=%f V, current=%f A\n",((VSource) object).name,((VSource) object).node[0].v-((VSource) object).node[1].v,((VSource) object).getI(((VSource) object).node[1]));
                        fileout.write(currentsourceoutput);
                    }
                    if(object instanceof Resistor){
                        String  resistoroutput=String.format("Resistor %s: voltage=%f V, current=%f A\n",((Resistor) object).name,((Resistor) object).node[0].v-((Resistor) object).node[1].v,((Resistor) object).getI(((Resistor) object).node[1]));
                        fileout.write(resistoroutput);
                    }
                    if(object instanceof Capacitor){
                        String capacitoroutput=String.format("Capacitor %s: voltage=%f V, current=%f A\n",((Capacitor) object).name,((Capacitor) object).node[0].v-((Capacitor) object).node[1].v,((Capacitor) object).getI(((Capacitor) object).node[1]));
                        fileout.write(capacitoroutput);
                    }
                    if (object instanceof Inductor ){
                        String inductoroutput=String.format("Inductor %s: voltage=%f V, current=%f A\n",((Inductor) object).name,((Inductor) object).node[0].v-((Inductor) object).node[1].v,((Inductor) object).getI(((Inductor) object).node[1]));
                        fileout.write(inductoroutput);
                    }
                    if (object instanceof Diodes){
                        String diodeoutput=String.format("Diode %s: voltage=%f V, current=%f A\n",((Diodes) object).name,((Diodes) object).node[0].v-((Diodes) object).node[1].v,((Diodes) object).getI(((Diodes) object).node[1]));
                        fileout.write(diodeoutput);
                    }

                }
                fileout.write("------------\n");
                fileout.close();
                counter++;
            }
            catch (IOException e){

            }
        }
        else {
            try{
                FileWriter fileout=new FileWriter("output.txt",true);
                fileout.write("Time:"+time1+" s\n------------\n");
                for(Object object:everything){
                    if(object instanceof Node){
                        String nodeoutput=String.format("Node %s: voltage=%f V\n",((Node) object).name,((Node) object).v);
                        fileout.write(nodeoutput);
                    }

                }
                fileout.write("------------\n");
                for (Object object:everything){
                    if (object instanceof ISource){
                        String currentsourceoutput=String.format("ISource %s: voltage=%f V, current=%f A\n",((ISource) object).name,((ISource) object).node[0].v-((ISource) object).node[1].v,((ISource) object).getI(((ISource) object).node[1]));
                        fileout.write(currentsourceoutput);
                    }
                    if(object instanceof VSource){
                        String voltagesourceoutput=String.format("Vsource %s: voltage=%f V, current=%f A\n",((VSource) object).name,((VSource) object).node[0].v-((VSource) object).node[1].v,((VSource) object).getI(((VSource) object).node[1]));
                        fileout.write(voltagesourceoutput);
                    }
                    if(object instanceof Resistor){
                        String  resistoroutput=String.format("Resistor %s: voltage=%f V, current=%f A\n",((Resistor) object).name,((Resistor) object).node[0].v-((Resistor) object).node[1].v,((Resistor) object).getI(((Resistor) object).node[1]));
                        fileout.write(resistoroutput);
                    }
                    if(object instanceof Capacitor){
                        String capacitoroutput=String.format("Capacitor %s: voltage=%f V, current=%f A\n",((Capacitor) object).name,((Capacitor) object).node[0].v-((Capacitor) object).node[1].v,((Capacitor) object).getI(((Capacitor) object).node[1]));
                        fileout.write(capacitoroutput);
                    }
                    if (object instanceof Inductor ){
                        String inductoroutput=String.format("Inductor %s: voltage=%f V, current=%f A\n",((Inductor) object).name,((Inductor) object).node[0].v-((Inductor) object).node[1].v,((Inductor) object).getI(((Inductor) object).node[1]));
                        fileout.write(inductoroutput);
                    }
                    if (object instanceof Diodes){
                        String diodeoutput=String.format("Diode %s: voltage=%f V, current=%f A\n",((Diodes) object).name,((Diodes) object).node[0].v-((Diodes) object).node[1].v,((Diodes) object).getI(((Diodes) object).node[1]));
                        fileout.write(diodeoutput);
                    }
                }
                fileout.write("------------\n");
                fileout.close();
            }
            catch (IOException e){

            }
        }
    }
}
