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
   private static Map<String, Integer> label_addresses = null;

   public static void main(String []args)
   {
      File file = new File(args[0]);
      //File file = new File("test2.asm");
      mapping();
      pass(file,1);
      pass(file,2);
   }

   public static void pass(File file, int pass_number){
      try
      {
         Scanner scan = new Scanner(file);
         int i = 0;
         int count = 0;

         while(scan.hasNextLine())
         {
            String line  = scan.nextLine();
            line = line + " # # # # ";
            line = line.replace(":",": ");
            line = line.replace("#", " # ");
            line = line.replaceAll(","," ");
            line = line.replaceAll("\\$", " \\$");
            line = line.trim();

            String[] words = line.split("\\s+");

            if(line== null || line.length()==0 || line.equals("")){
               //pass
            }
            else if(words[0].contains("#")){
               //pass
            }
            else if (pass_number == 1){
               i = first_pass(words, i);
            }
            else{

               line = line.substring(line.indexOf(":")+1, line.length()-1);
               line = line.trim();
               words = line.split("\\s+");
               if (words[0].contains("#")){
               }
               else{
                  second_pass(words,count);
                  count++;
               }
            }
         }

         scan.close();
      }

      catch (FileNotFoundException e)
      {
         System.out.println(e);
         System.exit(-1);
      }
   }

   public static int first_pass(String[] words, int i){
      for (String word : words) {
         if (word.contains("#")){
            return i;
         }
         if(is_instruction(word)){ //if line has an instruction
            i++;
            return i;
         }
         if (word.contains(":")){
            String label = word.replace(":","");
            label_addresses.put(label, i);
            return (i+1);
         }
      }
      System.out.println(words[0]+ "invalid instruction: "+words[0]);
      System.exit(-1);
      return i;
   }

   public static boolean is_instruction(String word){
      if(instructions.get(word)!=null){
         return true;
      }
      return false;
   }

   public static void second_pass(String[] words, int count){
      String inst1 = instructions.get(words[0]);
      String inst2 = instructions2.get(words[0]);
      String rd = registers.get(words[1]);

      String rs = registers.get(words[2]);
      String rt = registers.get(words[3]);

      String jumpers = "j jal";
      String output;

      if((is_instruction(words[0]))== false){
         System.out.println("invalid instruction: " + words[0]);
         System.exit(-1);
      }

      if (words[0].compareTo("sw")==0 ||words[0].compareTo("lw")==0){
         String immediate2 = words[2].substring(0, words[2].indexOf("("));
         int imm2 = Integer.parseInt(immediate2);
         immediate2 = leadingZeros(imm2, 16);
         String paren = words[3].substring(words[3].indexOf("$"), words[3].indexOf(")"));
         paren = registers.get(paren);
         output = (inst1 + " " + paren + " " + rd + " " + immediate2).toString();
      }

      else if (jumpers.contains(words[0])){
         int targ = label_addresses.get(words[1]);
         String target = leadingZeros(targ,26);
         output = (inst1 + " " + rd + " " + rs + " " + target + " "+ inst2).toString();
      }
      else if((words[0].compareTo("jr")==0)){
         output = (inst1 + " " + rd + " " + inst2).toString();
      }

      else if(words[0].compareTo("addi")==0){
         int add_imm = Integer.parseInt(words[3]);
         String addimm = leadingZeros(add_imm, 16);
         output = (inst1 + " " + rs + " " + rd + " " + addimm).toString();
      }

      else if(words[0].compareTo("sll")==0){
         int add_imm = Integer.parseInt(words[3]);
         String addimm = leadingZeros(add_imm, 5);
         output = (inst1 + " 00000 "+ rs + " " + rd + " "  + addimm + " " + inst2).toString();
      }
      else if(words[0].contains("beq") || words[0].contains("bne")){
         int off = label_addresses.get(words[3]);
         off = off - (count + 1);
         String offset = leadingZeros(off,16);
         output = (inst1 + " " + rd + " " +  " " + rs + " " + offset + " " + inst2).toString();
      }

      else {
         output = (inst1 + " " + rs + " " + rt + " " + rd + " " + inst2).toString();
      }

      output = output.replaceAll("null","");
      output = output.replaceAll("\\s+"," ");
      output = output.trim();
      if(output.length() > 1){
         System.out.println(output);
      }

   }

   public static String leadingZeros(int num, int len)
   {
      String binary = Integer.toBinaryString(num);
      int lengthBin = binary.length();
      if (lengthBin > len){
         binary = binary.substring(lengthBin-len, binary.length());
      }
      int numZeros = len - lengthBin;
      for (int i = 0; i < numZeros; i++)
      {
         binary = "0"+ binary;
      }
      return(binary);
   }

   public static void mapping()
   {
      registers = new HashMap<String, String>();
      instructions = new HashMap<String, String>();
      instructions2 = new HashMap<String, String>();
      label_addresses = new HashMap<String, Integer>();

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
