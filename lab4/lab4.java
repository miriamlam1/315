// Name: Riti Mittal and Miriam Lam
// Section: 315-05
// Description: This program is a MIPS pipelined processor simulator. It will
//model the flow of instructions through a pipelined processor.

import java.util.*;
import java.io.*;
import java.lang.String;

public class lab4
{
    private static Map<Integer, String> instructions_addr = null;
    private static Map<String, Integer> labels_addr = null;
    private static Map<String, Integer> registers = null;
    private static int register[] = new int[32];
    private static int data_mem[] = new int[8192];
    private static int line_count = 0; //pc number
    private static int cycle_count = 0; //cycles
    private static int instr_count = 0; //instructions
    private static int is_stall = 0; // pc when you check stall (pc +1)
    private static String p[] = new String[4]; // pipeline
    private static String lw_reg = "$0"; //register in lw to check
    private static int branch_taken = -1; // pc when you check branch (pc + 3)
    private static int branch_pc = -1; // pc to branch to
    private static int line_count_next= -1; // line count to jump to
    private static int jump_taken= 0; // jump flag: 1 if going to jump 0 if already jumped or not jump
    private static boolean placeholder = false; // placeholder true makes it so the next values are not performed

    // and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, jal
    public static int step()
    {
        if (instructions_addr.get(line_count) == null)
        {
            cycle_count+=3;
            float CPI = (float) cycle_count / (float) instr_count;
            System.out.println("Program complete");
            System.out.format("CPI = %.3f	Cycles = %d	Instructions = %d", CPI, cycle_count, instr_count);
            return -1;
        }
        String[] in = (instructions_addr.get(line_count)).split("\\s+");
        int r1 = 0;
        int r2 = 0;
        int r3 = 0;
        try
        {
            r1 = registers.get(in[1]);
            r2 = registers.get(in[2]);
            r3 = registers.get(in[3]);
        }
        catch (NullPointerException npe)
        {
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
        //System.out.println("\n" + Arrays.toString(in));
        String op = in[0];
        if(placeholder == false)
        {
            switch (op)
            {
                case "add":
                    register[r1] = register[r2] + register[r3];
                    line_count = line_count + 1;
                    break;
                case "sub":
                    register[r1] = register[r2] + register[r3];
                    line_count = line_count + 1;
                    break;
                case "addi":
                    register[r1] = register[r2] + Integer.parseInt(in[3]);
                    line_count = line_count + 1;
                    break;
                case "or":
                    register[r1] = register[r2] | register[r3];
                    line_count = line_count + 1;
                    break;
                case "and":
                    register[r1] = register[r2] & register[r3];
                    line_count = line_count + 1;
                    break;
                case "sll":
                    register[r1] = register[r2] << Integer.parseInt(in[3]);
                    line_count = line_count + 1;
                    break;
                case "slt":
                    if (register[r2] < register[r3])
                    {
                        register[r1] = 1;
                    } else
                    {
                        register[r1] = 0;
                    }
                    line_count = line_count + 1;
                    break;
                case "beq":
                    if (register[r1] == register[r2])
                    {
                        branch_pc = labels_addr.get(in[3]);
                        branch_taken = line_count + 3;
                        placeholder = true;
                    }
                    line_count++;
                    break;
                case "bne":
                    if (register[r1] != register[r2])
                    {
                        branch_pc = labels_addr.get(in[3]);
                        branch_taken = line_count + 3;
                        placeholder = true;
                    }
                    line_count = line_count + 1;
                    break;
                case "lw":
                    r2 = Integer.parseInt(in[2].substring(0, in[2].indexOf("(")));
                    r3 = registers.get(in[3].substring(0, in[3].indexOf(")"))); // imm
                    register[r1] = data_mem[r2 + register[r3]];
                    line_count = line_count + 1;
                    is_stall = line_count + 1;
                    lw_reg = in[1];
                    break;
                case "sw":
                    r2 = Integer.parseInt(in[2].substring(0, in[2].indexOf("(")));
                    r3 = registers.get(in[3].substring(0, in[3].indexOf(")"))); // imm
                    data_mem[r2 + register[r3]] = register[r1];
                    line_count = line_count + 1;
                    break;
                case "j":
                    if (jump_taken < 1)
                    {
                        line_count_next = labels_addr.get(in[1]);
                        jump_taken = 1;
                        placeholder = true;
                        cycle_count++;
                    }
                    line_count++;
                    break;
                case "jr":
                    if (jump_taken < 1)
                    {
                        line_count_next = register[31];
                        placeholder = true;
                        jump_taken = 1;
                    }
                    line_count++;
                    break;
                case "jal":
                    if (jump_taken < 1)
                    {
                        register[31] = line_count + 1;
                        line_count_next = labels_addr.get(in[1]);
                        placeholder = true;
                        jump_taken = 1;
                    }
                    line_count++;
                    break;
                default:
                    line_count++;
            }
        }
        else
        {
            line_count ++;
        }
        pipe_shift(op);
        instr_count++;
        cycle_count++;
        return 1;
    }

    public static boolean check_stall(String[] in)
    {
        String reg1 = "";
        String reg2 = "";
        String reg3 = "";
        try {
            reg1 = in[1];
            reg2 = in[2];
            reg3 = in[3];
        }
        catch (NullPointerException npe)
        {
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
        if (is_stall == line_count && lw_reg != null)
        {
            if (lw_reg.equals(reg1) || lw_reg.equals(reg2) || lw_reg.equals(reg3))
            {
                stall(in[0]);
                lw_reg = null;
                return true;
            }
        }
        return false;
    }

    public static boolean check_branch()
    {
        if (line_count == branch_taken)
        {
            squash();
            branch_taken = -1;
            line_count = branch_pc;
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void pipe_shift(String op)
    {
        for (int i = 3; i > 0; i--)
        {
            p[i] = p[i - 1];
        }
        p[0] = op;
    }

    public static void squash()
    {
        p[3] = p[2];
        p[0] = "squash";
        p[1] = "squash";
        p[2] = "squash";
        is_stall = 0;
        cycle_count ++;
        instr_count -=2;
        placeholder = false;
    }

    public static void stall(String op)
    {
        p[3] = p[2];
        p[2] = p[1];
        p[1] = "stall";
        cycle_count++;
        p[0] = op;
    }

    public static void j_squash(String op)
    {
        p[3] = p[2];
        p[2] = p[1];
        p[0] = "squash";
        p[1] = op;
        cycle_count++;
        placeholder = false;
    }

    public static int run(String[] words)
    {
      int step_count = 0;
      if (words.length == 1)
      {
           step_count = 1;
      }
      else
      {
           step_count = Integer.parseInt(words[1]);
      }

      String[] in = {""};
      for (int i = 0; i < step_count; i++)
      {
           if (line_count > 0)
           {
               in = (instructions_addr.get(line_count - 1)).split("\\s+"); //add
           }
           if (jump_taken > 0)
           {
               j_squash(in[0]);
               line_count = line_count_next;
               jump_taken = -1;
               break;
           }
           else if (check_branch() == true)
           {
               break;
           }
           if (check_stall(in) == true)
           {
               break;
           }
           else
           {
               if (step()<0)
               {
                   return -1;
               }
           }
      }
      return 1;
  }



   /* public static int step_ui(String[] words)
    {
        int step_count = 0;
        if (words.length == 1)
        {
            step_count = 1;
        }
        else
        {
            step_count = Integer.parseInt(words[1]);
        }

        String[] in = {""};
        for (int i = 0; i < step_count; i++)
        {
            if (line_count > 0)
            {
                in = (instructions_addr.get(line_count - 1)).split("\\s+"); //add
            }
            if (jump_taken > 0)
            {
                j_squash(in[0]);
                line_count = line_count_next;
                jump_taken = -1;
                print_pipeline();
                break;
            }
            else if (check_branch() == true)
            {
                print_pipeline();
                break;
            }
            if (check_stall(in) == true)
            {
                print_pipeline();
                break;
            }
            else
            {
                if (step()<0)
                {
                    return -1;
                }
                print_pipeline();
            }
        }
        return 1;
    }*/

    public static void options(String input)
    {
        String[] words = input.split("\\s+");
        switch (input) {
            case "h":
                print_help();
                break;
            case "p":
               print_pipeline();
               break;
            case "d":
                print_all(line_count);
                break;
            case "c":
                clear();
                System.out.println("        Simulator reset\n");
                break;
            case "r":
                while (run(words) > 0)
                {
                }
                break;
            case "q":
                System.exit(1);
        }

        // STEP !!!!!!!!!
        if (input.contains("s"))
        {
            run(words);
            print_pipeline();
        }

        else if (input.contains("m"))
        {
            System.out.println();
            for (int i = Integer.parseInt(words[1]); i <= Integer.parseInt(words[2]); i++)
            {
                System.out.println("[" + i + "]" + " = " + data_mem[i]);
            }
            System.out.println();
        }
    }

    public static void print_pipeline()
    {
        System.out.print("pc	if/id	id/exe	exe/mem	mem/wb");
        System.out.format("\n%d       %s   %s   %s    %s", line_count, p[0], p[1], p[2], p[3]);
    }

    public static void print_all(int pc)
    {
        System.out.println("\npc = " + pc + "\n$0 = " + 0 + "          $v0 = " + register[2] + "         $v1 = "
                + register[3] + "         $a0 = " + register[4] + "\n$a1 = " + register[5] + "         $a2 = "
                + register[6] + "         $a3 = " + register[7] + "         $t0 = " + register[8] + "\n$t1 = "
                + register[9] + "         $t2 = " + register[10] + "         $t3 = " + register[11] + "         $t4 = "
                + register[12] + "\n$t5 = " + register[13] + "         $t6 = " + register[14] + "         $t7 = "
                + register[15] + "         $s0 = " + register[16] + "\n$s1 = " + register[17] + "         $s2 = "
                + register[18] + "         $s3 = " + register[19] + "         $s4 = " + register[20] + "\n$s5 = "
                + register[21] + "         $s6 = " + register[22] + "         $s7 = " + register[23] + "         $t8 = "
                + register[24] + "\n$t9 = " + register[25] + "         $sp = " + register[29] + "         $ra = "
                + register[31] + "\n");
    }

    public static void clear()
    {
        for (int i = 0; i < 31; i++)
        {
            register[i] = 0;
        }
        for (int i = 0; i < 8192; i++)
        {
            data_mem[i] = 0;
        }
        line_count = instr_count = cycle_count = 0;
    }

    public static void print_help()
    {
        System.out.println("\nh = show help\n" + "d = dump register state\n"
                + "p = show pipeline registers\n"
                + "s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)\n"
                + "s num = step through num clock cycles\n" + "r = run until the program ends and display timing summary\n"
                + "m num1 num2 = display data memory from location num1 to num2\n"
                + "c = clear all registers, memory, and the program counter to 0\n" + "q = exit the program");
    }

    public static void mapping()
    {
        instructions_addr = new HashMap<Integer, String>();
        labels_addr = new HashMap<String, Integer>();
        registers = new HashMap<String, Integer>();
        for (int i = 3; i >= 0; i--)
        {
            p[i] = "empty";
        }
        registers.put("$0", 0);
        registers.put("$zero", 0);
        registers.put("$v0", 2);
        registers.put("$v1", 3);
        registers.put("$a0", 4);
        registers.put("$a1", 5);
        registers.put("$a2", 6);
        registers.put("$a3", 7);
        registers.put("$t0", 8);
        registers.put("$t1", 9);
        registers.put("$t2", 10);
        registers.put("$t3", 11);
        registers.put("$t4", 12);
        registers.put("$t5", 13);
        registers.put("$t6", 14);
        registers.put("$t7", 15);
        registers.put("$s0", 16);
        registers.put("$s1", 17);
        registers.put("$s2", 18);
        registers.put("$s3", 19);
        registers.put("$s4", 20);
        registers.put("$s5", 21);
        registers.put("$s6", 22);
        registers.put("$s7", 23);
        registers.put("$t8", 24);
        registers.put("$t9", 25);
        registers.put("$sp", 29);
        registers.put("$ra", 31);
    }

public static Scanner scanning(String filename)
{
    try
    {
        return (new Scanner(new File(filename)));
    }
    catch (FileNotFoundException e)
    {
        return null;
    }
}

public static void script_run(Scanner scan)
{
    while (scan.hasNextLine())
    {
        String line = scan.nextLine();
        System.out.println("\nmips> " + line);
        options(line);
    }
}

public static void user_input()
{
    Scanner scan = new Scanner(System.in);
    String input = new String();
    while (input != "q")
    {
        System.out.print("\nmips> ");
        input = scan.nextLine();
        options(input);
    }
    scan.close();
}

public static void main(String[] args)
{
    mapping();
    Scanner scan = scanning(args[0]);
    Scanner scan_script = null;
    if (args.length > 1)
    {
        scan_script = scanning(args[1]);
    }
    first_pass(scan);
    scan.close();
    if (scan_script == null)
    {
        user_input();
    }
    else
    {
        script_run(scan_script);
    }
}

// reads through the file, saving labels and instructions to addresses
public static void first_pass(Scanner scan)
{
    int line_no = 0;
    while (scan.hasNextLine())
    {
        String line = scan.nextLine();
        line = line.replace(":", ": ");
        line = line.replace("#", " # ");
        line = line.replaceAll(",", " ");
        line = line.replaceAll("\\$", " \\$");
        line = line.trim();
        if (line.contains("#"))
        {
            line = line.substring(0, line.indexOf("#"));
        }
        if (line == null || line.length() == 0 || line.equals("\\s+"))
        {
        }
        else
        {
            if (line.contains(":"))
            {
                String label = line.substring(0, line.indexOf(":")); // init label to have the label name
                labels_addr.put(label, line_no);
                line = line.substring(line.indexOf(":") + 1, line.length()); // line is the rest
                line = line.trim();
                if (line.length() < 2)
                {
                    labels_addr.put(label, line_no);
                }
                else
                {
                    instructions_addr.put(line_no, line);
                    line_no++;
                }
            }
            else
            {
                instructions_addr.put(line_no, line);
                line_no++;
            }
        }
    }
}
}
