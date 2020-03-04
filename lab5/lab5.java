// Name: Riti Mittal and Miriam Lam
// Section: 315-05
// Description: This program is a MIPS emulator. It models the execution of
//instructions on a MIPS simulator.

import java.util.*;
import java.io.*;
import java.lang.String;

public class lab5 {
    private static Map<Integer, String> instructions_addr = null;
    private static Map<String, Integer> labels_addr = null;
    private static Map<String, Integer> registers = null;
    private static int register[] = new int[32];
    private static int data_mem[] = new int[8192];
    private static int line_count = 0;
    private static int GHR = 2;
    private static int correct_predictions = 0;
    private static int total_predictions = 0;

    public static void main(String[] args) {
        mapping();
        Scanner scan = scanning(args[0]);
        Scanner scan_script = null;
        if (args.length > 1) {
            scan_script = scanning(args[1]);
        }
        if (args.length > 2){
            GHR = Integer.parseInt(args[2]);
        }
        first_pass(scan);
        scan.close();
        if (scan_script == null) {
            user_input();
        } else {
            script_run(scan_script);
        }
    }

    // reads through the file, saving labels and instructions to addresses
    public static void first_pass(Scanner scan) {
        int line_no = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            line = line.replace(":", ": ");
            line = line.replace("#", " # ");
            line = line.replaceAll(",", " ");
            line = line.replaceAll("\\$", " \\$");
            line = line.trim();

            if (line.contains("#")) {
                line = line.substring(0, line.indexOf("#"));
            }

            if (line == null || line.length() == 0 || line.equals("\\s+")) {
            } else {
                if (line.contains(":")) {
                    String label = line.substring(0, line.indexOf(":")); // init label to have the label name
                    labels_addr.put(label, line_no);
                    line = line.substring(line.indexOf(":") + 1, line.length()); // line is the rest
                    line = line.trim();
                    if (line.length() < 2) {
                        labels_addr.put(label, line_no);
                    } else {
                        instructions_addr.put(line_no, line);
                        line_no++;
                    }
                } else {
                    instructions_addr.put(line_no, line);
                    line_no++;
                }
            }
        }
    }

    // and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, jal
    public static int step() {
        if (instructions_addr.get(line_count) == null) {
            // System.exit(1);
            return -1;
        }
        String[] in = (instructions_addr.get(line_count)).split("\\s+");
        int r1 = 0;
        int r2 = 0;
        int r3 = 0;
        try {
            r1 = registers.get(in[1]);
            r2 = registers.get(in[2]);
            r3 = registers.get(in[3]);
        } catch (NullPointerException npe) {
            // sike no exception
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        System.out.println(line_count+Arrays.toString(in));
        String op = in[0];
        switch (op) {
            case "add":
                register[r1] = register[r2] + register[r3];
                line_count = line_count + 1;
                break;

            case "sub":
                register[r1] = register[r2] - register[r3];
                line_count = line_count + 1;
                break;

            case "addi":
                register[r1] = register[r2] + Integer.parseInt(in[3]);
                line_count = line_count + 1;
                break;

            case "beq":
                if (register[r1] == register[r2]) {
                    line_count = labels_addr.get(in[3]);
                } else {
                    line_count++;
                }
                break;

            case "bne":
                if (register[r1] != register[r2]) {
                    line_count = labels_addr.get(in[3]);
                } else {
                    line_count = line_count + 1;
                }
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
                if (register[r2] < register[r3]) {
                    register[r1] = 1;
                } else {
                    register[r1] = 0;
                }
                line_count = line_count + 1;
                break;

            case "lw":
                r2 = Integer.parseInt(in[2].substring(0, in[2].indexOf("(")));
                r3 = registers.get(in[3].substring(0, in[3].indexOf(")"))); // imm
                register[r1] = data_mem[r2 + register[r3]];
                line_count = line_count + 1;
                break;

            case "sw":
                r2 = Integer.parseInt(in[2].substring(0, in[2].indexOf("(")));
                r3 = registers.get(in[3].substring(0, in[3].indexOf(")"))); // imm
                data_mem[r2 + register[r3]] = register[r1];
                line_count = line_count + 1;
                break;

            case "j":
                line_count = labels_addr.get(in[1]);
                break;

            case "jr":
                line_count = register[31];
                break;

            case "jal":
                register[31] = line_count + 1;
                line_count = labels_addr.get(in[1]);
                break;

            default:
                line_count = line_count + 1;
        }
        return 1;
    }

    public static Scanner scanning(String filename) {
        try {
            return (new Scanner(new File(filename)));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static void script_run(Scanner scan) {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            System.out.println("mips> " + line);
            options(line);
        }
    }

    public static void user_input() {
        Scanner scan = new Scanner(System.in);
        String input = new String();
        while (input != "q") {
            System.out.print("mips> ");
            input = scan.nextLine();
            options(input);
        }
        scan.close();
    }

    public static void print_datamem(){
        for(int i=0;i< 8192; i+=2){
            if(data_mem[i]!=0){
                System.out.println("("+data_mem[i]+","+ data_mem[i+1]+")");
            }
        }
    }

    public static void options(String input) {
        switch (input) {
            case "h":
                print_help();
                return;
            case "d":
                print_all(line_count);
                return;
            case "c":
                clear();
                return;
            case "r":
                while (step() > 0) {
                }
                return;
            case "b":
                print_bresenham();
                break;
            case "o":
                print_datamem();
                break;
            case "q":
                System.exit(1);
        }
        int step_count = 0;
        String[] words = input.split("\\s+");
        if (input.contains("s")) {
            if (words.length == 1) {
                step_count = 1;
            } else {
                step_count = Integer.parseInt(words[1]);
            }
            for (int i = 0; i < step_count; i++) {
                step();
            }
            System.out.println("        " + step_count + " instruction(s) executed");
        } 
        else if (input.contains("m")) {
            System.out.println();
            for (int i = Integer.parseInt(words[1]); i <= Integer.parseInt(words[2]); i++) {
                System.out.println("[" + i + "]" + " = " + data_mem[i]);
            }
            System.out.println();
        }
    }

    public static void print_bresenham(){
        float accuracy = 0;
        if(total_predictions == 0){
            accuracy = 0;
        } else{
        accuracy = correct_predictions/total_predictions;
        }
        System.out.format("\naccuracy %.2f ", accuracy);
        System.out.format("(%d correct predictions, %d predictions)", correct_predictions, total_predictions);
    }

    public static void print_all(int pc) {
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

    public static int clear() {
        for (int i = 0; i < 31; i++) {
            register[i] = 0;
        }
        for (int i = 0; i < 8192; i++) {
            data_mem[i] = 0;
        }
        System.out.println("        Simulator reset\n");
        return 0;
    }

    public static void print_help() {
        System.out.println("\nh = show help\n" + "d = dump register state\n"
                + "s = single step through the program (i.e. execute 1 instruction and stop)\n"
                + "s num = step through num instructions of the program\n" + "r = run until the program ends\n"
                + "m num1 num2 = display data memory from location num1 to num2\n"
                + "c = clear all registers, memory, and the program counter to 0\n" + "q = exit the program\n");
    }

    public static void mapping() {
        instructions_addr = new HashMap<Integer, String>();
        labels_addr = new HashMap<String, Integer>();
        registers = new HashMap<String, Integer>();
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
}