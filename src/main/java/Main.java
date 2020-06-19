import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
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
        for (double i =0 ; i<time ; i+=dt){
            calculateVoltageAtT();
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    System.out.println(((Nodes) o).name+" : "+((Nodes) o).v);
                }
                if (o instanceof Inductor){
                    System.out.println(((Inductor) o).name+" : "+((Inductor) o).getI(((Inductor) o).node[0]));
                }
            }
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
        int temp=0;
        double Itotal1=0,Itotal2=0,Itotal4=0;
        while (true){
            ////-------------         reset all nodes
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    Nodes.resetNodes();
                }
                if(o instanceof VSource){
                    if(((VSource) o).node[1].name.equals("0")){
                        ((VSource) o).node[0].v = ((VSource) o).node[1].v+((VSource) o).value();
                    }
                    else {
                        ((VSource) o).node[1].v = ((VSource) o).node[0].v+((VSource) o).value();
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
                        ((Nodes) o).v = ((Nodes) o).v + (Math.abs(Itotal1) - Math.abs(Itotal2))/Main.dI * Main.dV;
                        ((Nodes) o).visited = true;
                    }
                }
            }
            ////-------------         visiting all nodes
            ////-------------     total input currents of all nodes is less than 0.01 so exits from the while
            temp =0;
            //System.out.println("temp : "+temp);
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    double I3=((Nodes) o).getTotalI((Nodes) o);
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
}
