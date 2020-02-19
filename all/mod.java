import java.util.Scanner;

public class mod
{
   public static void main(String[] args)
   {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter a number: ");
      int num = scanner.nextInt();
      System.out.println("Enter a divisor: ");
      int div = scanner.nextInt();
      div--;
      int result = num & div;
      System.out.println("Mod = " + result);
   }
}