// Name: Riti Mittal and Miriam Lam
// Section: 315-05
// Description: This program is a 2 pass assembler. It takes in MIPS assembly files
// and outputs the corresponding machine code.

import java.util.*;
import java.io.*;
import java.lang.String;

public class lab2
{
   private static Map<String, String> registers = null;
   private static Map<String, String> instructions = null;
   private static Map<String, String> instructions2 = null;
   private static Map<String, String> label_addresses = null;

   public static void main(String []args)
   {
      //File file = new File(args[0]);
      File file = new File("test1.asm");
      mapping();
      pass(file);
      System.out.println("label prompt2 is"+label_addresses.get("prompt2:"));
   }

   public static void pass(File file){
      
      try
      {
         Scanner scan = new Scanner(file);
         int i = 0;
         while(scan.hasNextLine())
         {
            String line  = scan.nextLine();
            remove_comments(line, i);
            i++;
         }
         scan.close();
      }

      catch (FileNotFoundException e)
      {
         System.out.println(e);
         System.exit(-1);
      }
   }

   public static int remove_comments(String line, int i){
      String[] words = line.split("\\s+");
      String[] op = new String[6];
      int j = 0; //counter for words
      for (String word : words) {
         if (word.contains("#")){
            return i;
         }
         if (word.contains(":")){
            System.out.println("key: "+ word + " value: "+ i);
            label_addresses.put(word, Integer.toBinaryString(i));
         }
         else{
            op[j]=word;
            j++;
         }
         i++;
      }
      return i;
   }

   public static void mapping()
   {
      registers = new HashMap<String, String>();
      instructions = new HashMap<String, String>();
      instructions2 = new HashMap<String, String>();
      label_addresses = new HashMap<String, String>();

      //map values for all registers
      registers.put("$0", "00000");
      registers.put("$zero", "00000");
      //registers.put("$at", "00001");
      registers.put("$v0", "00010");
      registers.put("$v1", "00011");
      registers.put("$a0", "00100");
      registers.put("$a1", "00101");
      registers.put("$a2", "00110");
      registers.put("$a3", "00111");
      registers.put("$t0", "01000");
      registers.put("$t1", "01001");
      registers.put("$t2", "01010");
      registers.put("$t3", "01011");
      registers.put("$t4", "01100");
      registers.put("$t5", "01101");
      registers.put("$t6", "01110");
      registers.put("$t7", "01111");
      registers.put("$s0", "10000");
      registers.put("$s1", "10001");
      registers.put("$s2", "10010");
      registers.put("$s3", "10011");
      registers.put("$s4", "10100");
      registers.put("$s5", "10101");
      registers.put("$s6", "10110");
      registers.put("$s7", "10111");
      registers.put("$t8", "11000");
      registers.put("$t9", "11001");
      //registers.put("$gp", "11100");
      registers.put("$sp", "11101");
      //registers.put("$fp", "11110");
      registers.put("$ra", "11111");

      //map values for all instructions
      instructions.put("and" ,"000000");
      instructions.put("or"  ,"000000");
      instructions.put("add" ,"000000");
      instructions.put("addi","001000");
      instructions.put("sll" ,"000000");
      instructions.put("sub" ,"000000");
      instructions.put("slt" ,"000000");
      instructions.put("beq" ,"000100");
      instructions.put("bne" ,"000101");
      instructions.put("lw"  ,"100011");
      instructions.put("sw"  ,"101011");
      instructions.put("j"   ,"000010");
      instructions.put("jr"  ,"000000");
      instructions.put("jal" ,"000011");

      instructions2.put("and", "00000 100100");
      instructions2.put("or" , "00000 100101");
      instructions2.put("add", "00000 100000");
      instructions2.put("sll", "000000");
      instructions2.put("sub", "00000 100010");
      instructions2.put("slt", "00000 101010");
      instructions2.put("jr" , "000000000000000 001000");
   }
}