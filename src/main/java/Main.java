import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static ArrayList<Object> everything=new ArrayList<>();
    static double t=0,dt=-1,dV=-1,dI=-1,time;

    public static void main(String[] args){
        InputManager manager=InputManager.getInstance();
        Scanner scanner=new Scanner(System.in);
        String line=scanner.nextLine();
        int nLine=1;
        Pattern pattern=Pattern.compile("\\.tran\\s+(\\d+\\.?\\d*)([pnumkMGx]?)$");
        Matcher matcher;
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
                if (!manager.checkInputFormat()) {
                    System.out.println("Error -1 ( line "+nLine+" )");
                    System.exit(0);
                }
            }
        }
        if (dV==-1||dI==-1||dt==-1){
            System.out.println("Error -1");
            System.exit(0);
        }
        Nodes.updateNeighbourNodes();

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
}

