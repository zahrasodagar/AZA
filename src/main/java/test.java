public class test {
    public static void main(String[] args){
        double r1=1,r2=2,r3=3,dt=0.01,t=2;
        double[] e = new double[3];
        double[][] A =new double[3][3];
        double[] a = {0,0,0};
        int n=3;
        String  node1I1="0", node2I1 = "1" ,
                node1r1 = "1" , node2r1 = "3",
                node1r2 = "1" , node2r2 = "2",
                node1r3 = "3" , node2r3 = "0",
                node1I2 = "2" , node2I2 = "3";

        Solve solve = new Solve();
        for (double i=0;i<=t;i+=dt){
            a = new double[] {0, 0, 0};
            A = new double[][] {{0,0,0},{0,0,0},{0,0,0}};
            for (int j=0 ; j<n ;j++){
                //  'j+1' is the number of the node that we are investing its equation
                // there should be a 'for()' to check all current generators but now as we know there are 2 generators so just we check them
                if(Integer.toString(j+1).equals(node1I1)){
                    a[j]-=10*Math.sin(Math.PI*i);
                }
                if(Integer.toString(j+1).equals(node2I1)){
                    a[j]+=10*Math.sin(Math.PI*i);
                }
                if(Integer.toString(j+1).equals(node1I2)){
                    a[j]-=5;
                }
                if(Integer.toString(j+1).equals(node2I2)){
                    a[j]+=5;
                }
                // now we gonna find out entries of A(Matrix)
                for (int l=0 ; l<n ; l++){
                    if(Integer.toString(j+1).equals(node1r1)||Integer.toString(j+1).equals(node2r1)){
                        if(l==j){
                            A[j][j]+=1/r1;
                        }
                    }
                    if(Integer.toString(j+1).equals(node1r2)||Integer.toString(j+1).equals(node2r2)){
                        if(l==j){
                            A[j][j]+=1/r2;
                        }
                    }
                    if(Integer.toString(j+1).equals(node1r3)||Integer.toString(j+1).equals(node2r3)){
                        if(l==j){
                            A[j][j]+=1/r3;
                        }
                    }
                    if((Integer.toString(j+1).equals(node1r1)&&Integer.toString(l+1).equals(node2r1))||(Integer.toString(j+1).equals(node2r1)&&Integer.toString(l+1).equals(node1r1))){
                        A[j][l]-=1/r1;
                    }
                    if((Integer.toString(j+1).equals(node1r2)&&Integer.toString(l+1).equals(node2r2))||(Integer.toString(j+1).equals(node2r2)&&Integer.toString(l+1).equals(node1r2))){
                        A[j][l]-=1/r2;
                    }
                    if((Integer.toString(j+1).equals(node1r3)&&Integer.toString(l+1).equals(node2r3))||(Integer.toString(j+1).equals(node2r3)&&Integer.toString(l+1).equals(node1r3))){
                        A[j][l]-=1/r1;
                    }
                }
            }
            e=solve.GaussJordanElimination(A,a,n);
            System.out.println(e[0]+"  "+e[1]+"  "+e[2]);
        }
    }
}


class Solve{
    double[] GaussJordanElimination(double[][] A,double[] a,double n){
        double b=0,c=0,d=0,temp;
        for (int i=0;i<n;i++){
            /////////////////////////////          if A[i][i]==0 we should change it with a non zero row
            if(A[i][i]==0){
                b=0;
                for (int l=i+1;l<n;l++){
                    if(A[l][i]!=0){
                        b=1;
                        temp=a[i];
                        a[i]=a[l];
                        a[l]=temp;
                        for (int p=i;p<n;p++){
                            temp=A[i][p];
                            A[i][p]=A[l][p];
                            A[l][p]=temp;
                        }
                        break;
                    }
                }
                if(b==0){
                    return null;
                }
            }
            /////////////////////////////          if A[i][i]==0 we should change it with a non zero row
            /////////////////////////////          making A[i][i]==1
            temp=A[i][i];
            a[i]/=temp;
            for (int k=i;k<n;k++){
                A[i][k]/=temp;
            }
            /////////////////////////////          making A[i][i]==1
            /////////////////////////////          make all entries of a column i th zero except A[i][i] which have to be one
            for (int j=0;j<n;j++){
                if (j==i){
                    continue;
                }
                else {
                    temp=A[j][i];
                    a[j]-=a[i]*temp;
                    for (int k=i;k<n;k++){
                        A[j][k]-=A[i][k]*temp;
                    }
                }
            }
            /////////////////////////////          make all entries of a column zero except A[i][i] which have to be one
        }
        return a;
    }
}