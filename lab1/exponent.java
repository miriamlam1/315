import java.util.Scanner;
public class exponent{
   public static void main(String[] args){
      int exp, num;
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter number: ");
      num = sc.nextInt();
      int res = num;
      System.out.println("Enter exponent: ");
      exp = sc.nextInt();
      sc.close();

      res=helperFunc(exp,num,res);

      System.out.println("Result is " +res);
   }   

   private static int helperFunc(int exp, int num, int sum){
      if (exp == 0)
         return 1;
      else if (exp==1)
         return sum;
      else{
         int temp = sum;
         sum = 0;
         for (int i=0;i<num;i++){
            sum+= temp;            
         }     
         exp--;
         return (helperFunc(exp,num,sum));
      }
   }
}
