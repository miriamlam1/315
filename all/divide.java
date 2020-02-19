import java.util.Scanner;


public class divide
{
   public static void main(String []args)
   {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter upper: ");
      int up = scanner.nextInt();
      System.out.println("Enter lower: ");
      int low = scanner.nextInt();
      System.out.println("Enter divisor: ");
      int div = scanner.nextInt();
      int temp = 1;
      int num = 0;
      while(temp<div)
      {
         temp <<= 1;
         low >>= 1;
         low = low & 2147483647;
         num = up & 1;
         num = (int)num;
         num <<= 31;
         low = low + num;
         up >>= 1;
         up = up & 2147483647;
      }
      System.out.printf("%d, %d", up, low);
   }
}