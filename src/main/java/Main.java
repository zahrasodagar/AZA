import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static ArrayList<Object> everything=new ArrayList<>();
    static double t,dt=-1,dV=-1,dI=-1;
    public static void main(String[] args){
        InputManager manager=InputManager.getInstance();
        Scanner scanner=new Scanner(System.in);
        String line=""; //scanner.nextLine()
        double time=0;
        int nLine=1;
        Pattern pattern=Pattern.compile("\\.tran\\s+(\\d+\\.?\\d*)([pnumkMGx]?)$");
        Matcher matcher=pattern.matcher(line);
        while (true){
            line=scanner.nextLine();
            ++nLine;
            matcher=pattern.matcher(line);
            if (matcher.find()) {
                setTime(matcher.group(1),matcher.group(2));
                break;
            }
            if (line.charAt(0)!='*'){
                manager.setInput(line);
                if (!manager.checkInputFormat()){
                    System.out.println("Invalid Input ( line    "+nLine+" )");
                    //return;    it's  temporary comment
                }
            }
        }
        if (dV==-1||dI==-1||dt==-1){
            // TODO: 20/06/11 print error ...
        }
        for (double i =0 ; i<t ; i+=dt){
            calculateVoltageAtT();
            for (Object o: Main.everything){
                if (o instanceof Nodes){
                    System.out.println(((Nodes) o).v);
                }
            }
        }


    }
    public static void setTime(String time,String p){
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
        t=Double.parseDouble(time)*Math.pow(10,power);
    }

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
                        System.out.println("V : "+((Nodes) o).v);
                        ((Nodes) o).v += Main.dV;
                        System.out.println("V : "+((Nodes) o).v);
                        Itotal2 = ((Nodes) o).getTotalI((Nodes) o);
                        ((Nodes) o).v -= Main.dV;
                        System.out.println("I1 : "+Itotal1);
                        System.out.println("I2 : "+Itotal2);
                        ((Nodes) o).v = ((Nodes) o).finalV + (Math.abs(Itotal1 - Itotal2)) * Main.dV / Main.dI;
                        ((Nodes) o).visited = true;
                    }
                }
            }
            ////-------------         visiting all nodes
            ////-------------     total input currents of all nodes is less than 0.01 so exits from the while
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

