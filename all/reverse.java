import java.util.Scanner;

public class reverse
{
   public static void main(String[] args)
   {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter a number: ");
      int num = scanner.nextInt();
      int rev = 0;

      for (int i = 0; i < 31; i++)
      {
         int x = num & 1; //check if last bit is 1
         num = num >> 1; //right shift the number
         rev = rev | x;
         rev = rev << 1; //left shift the result
      }
      System.out.println("Reversed number: " + rev);
   }
}
