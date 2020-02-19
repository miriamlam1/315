import java.util.Scanner;

public class exponent
{
   public static void main(String[] args)
   {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter a number: ");
      int num = scanner.nextInt();
      System.out.println("Enter an exponent: ");
      int exp = scanner.nextInt();
      int result = num;
      int partial = num;
      int x = 1;
      if (exp == 0)
      {
         System.out.println(1);
         return;
      }
      while (x < exp)
      {
         for (int i = 1; i < num; i++)
         {
            result += partial;
         }
         partial = result;
         x++;
      }
      System.out.println(result);
   }
}

