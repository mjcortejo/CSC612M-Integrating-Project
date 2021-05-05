/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark
 */
//Binary computation https://www.w3resource.com/java-exercises/basic/java-basic-exercise-19.php
//Hex computation https://www.w3resource.com/java-exercises/basic/java-basic-exercise-29.php
public class Pipeline {
    
    
    HashMap<String, int[]> instruction_opcode_map;
    HashMap<String, int[]> funct3_opcode_map;
    HashMap<String, Integer> register_alias_map;
    int[] binary_opcode = new int[32];
    
    public Pipeline()
    {
        instruction_opcode_map = new HashMap<String, int[]>() {{
            put("lw",  new int[] {0,0,0,0,0,1,1});
            put("sw",  new int[] {0,1,0,0,0,1,1});
            put("add", new int[] {0,1,1,0,0,1,1});
            put("addi",new int[] {0,0,1,0,0,1,1});
            put("slt", new int[] {0,1,1,0,0,1,1});
            put("slti",new int[] {0,0,1,0,0,1,1});
            put("sll", new int[] {0,1,1,0,0,1,1});
            put("slli",new int[] {0,0,1,0,0,1,1});
            put("srl", new int[] {0,1,1,0,0,1,1});
            put("srli",new int[] {0,0,1,0,0,1,1});
            put("and", new int[] {0,1,1,0,0,1,1});
            put("andi",new int[] {0,0,1,0,0,1,1});
            put("or",  new int[] {0,1,1,0,0,1,1});
            put("ori", new int[] {0,0,1,0,0,1,1});
            put("xor", new int[] {0,1,1,0,0,1,1});
            put("xori",new int[] {0,0,1,0,0,1,1});
            put("beq", new int[] {1,1,0,0,0,1,1});
            put("bne", new int[] {1,1,0,0,0,1,1});
            put("blt", new int[] {1,1,0,0,0,1,1});
            put("bge", new int[] {1,1,0,0,0,1,1});
        }};
        
        funct3_opcode_map = new HashMap<String, int[]>(){{
            put("lw",  new int[] {0,1,0});
            put("sw",  new int[] {0,1,0});
            put("add", new int[] {0,0,0});
            put("addi",new int[] {0,0,0});
            put("slt", new int[] {0,1,0});
            put("slti",new int[] {0,1,0});
            put("sll", new int[] {0,0,1});
            put("slli",new int[] {0,0,1});
            put("srl", new int[] {1,0,1});
            put("srli",new int[] {1,0,1});
            put("and", new int[] {1,1,1});
            put("andi",new int[] {1,1,1});
            put("or",  new int[] {1,1,0});
            put("ori", new int[] {1,1,0});
            put("xor", new int[] {1,0,0});
            put("xori",new int[] {1,0,0});
            put("beq", new int[] {0,0,0});
            put("bne", new int[] {0,0,1});
            put("blt", new int[] {1,0,0});
            put("bge", new int[] {1,0,1});
        }};
        //this is used when the instruction is invoking the alias name 
        //which will point to a row number (the integer value)
        register_alias_map = new HashMap<String, Integer>() {{ 
            put("t0", 5);
            put("t1", 6);
            put("t2", 7);
            put("s0", 8);
            put("s1", 9);
            put("a0", 10);
            put("a1", 11);
            put("a2", 12);
            put("a3", 13);
            put("a4", 14);
            put("a5", 15);
            put("a6", 16);
            put("a7", 17);
            put("s2", 18);
            put("s3", 19);
            put("s4", 20);
            put("s5", 21);
            put("s6", 22);
            put("s7", 23);
            put("s8", 24);
            put("s9", 25);
            put("s10", 26);
            put("s11", 27);
            put("t3", 28);
            put("t4", 29);
            put("t5", 30);
            put("t6", 31);
        }};
    }
    
    
    
    //we should probably dissect the pipeline's execution as each function (etc. Decode = 1 function, PC = 1 function, 
    //so we can reflect the state of the simulator back to the GUI
    
    public void parse_line(String line, int line_number, JTable jTableRegister)
    {
        try
        {
            String[] parsed_line = line.split(" ");

            String instruction = parsed_line[0];
            ArrayList<String> pre_params = new ArrayList<String>();

            for (int i = 1; i < parsed_line.length; i++) //this loop makes sure that there are no more whitespaces between register params
            {
                pre_params.add(parsed_line[i]);
            }

            String params_full_string = ""; //this will reform it into a full string to we can split it by comma later
            for (String pre_param: pre_params)
            {
                params_full_string += pre_param;
            }

            //now we split by comma
            String[] params = params_full_string.split(",");

            //get instruction first
            int[] instruction_opcode = instruction_opcode_map.get(instruction);
            int[] funct3_opcode = funct3_opcode_map.get(instruction);
            if (instruction_opcode == null)
            {
                //error code
            }

            int rd_table_row = GetRegisterTableRow(params[0]);
            int rs1_table_row;
            int rs2_table_row;
            String hexa_value;

    //        System.out.println(jTableRegister.getColumnCount());
    //        Object pre_rd_value = jTableRegister.getValueAt(table_row, 2);
    //        String rd_value = (pre_rd_value == null) ? "" : pre_rd_value.toString();

            //they are in bits but their value needs to be extracted first
            int[] rd_binary = DecimalToBinary(Integer.toString(rd_table_row)); //5 bits // this is the IMM in the opcode location
            int[] rs1_binary = new int[5]; //5 bits // 
            int[] rs2_binary = new int[5]; //5 bits
            
            switch(instruction.toLowerCase())
            {
                case "lw":
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    hexa_value = GetHexaValueFromTableRow(rs1_table_row, jTableRegister);
                    rs1_binary = HexaToBinary(hexa_value);
                    binary_opcode = AddInstructionBinaryToOpcode(binary_opcode, instruction_opcode);
                    binary_opcode = AddIMMBinaryToOpcode(binary_opcode, rd_binary);
                    binary_opcode = AddFunct3BinaryToOpcode(binary_opcode, funct3_opcode);
                    binary_opcode = AddRS1BinaryToOpcode(binary_opcode, rs1_binary);
                    System.out.println(BinaryToHex(binary_opcode));
                    break;
                case "add":
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    rs2_table_row = GetRegisterTableRow(params[2]);
                    hexa_value = GetHexaValueFromTableRow(rs1_table_row, jTableRegister);
                    rs1_binary = HexaToBinary(hexa_value);
                    hexa_value = GetHexaValueFromTableRow(rs2_table_row, jTableRegister);
                    rs2_binary = HexaToBinary(hexa_value);
                    binary_opcode = AddInstructionBinaryToOpcode(binary_opcode, instruction_opcode);
                    binary_opcode = AddIMMBinaryToOpcode(binary_opcode, rd_binary);
                    binary_opcode = AddFunct3BinaryToOpcode(binary_opcode, funct3_opcode);
                    binary_opcode = AddRS1BinaryToOpcode(binary_opcode, rs1_binary);
                    binary_opcode = AddRS2BinaryToOpcode(binary_opcode, rs2_binary);
                    System.out.println(BinaryToHex(binary_opcode));
                    break;
                default: //error check
                    System.out.println("Invalid instruction "+instruction);
                    break;
            }
        }
        catch(Exception e)
        {
            System.out.println(e + " at line number " + (line_number+1));
        }
        
        //check if the params are correct
        
    }
    
    public int[] AddInstructionBinaryToOpcode(int[] opcode_to_apply, int[] instruction_binary_opcode)
    {
        System.out.println("Instruction binary opcode length "+instruction_binary_opcode.length);
        for (int i = 6, j = 0; i >= 0; i--,j++)
        {
            opcode_to_apply[31-i] = instruction_binary_opcode[j];
        }
        return opcode_to_apply;
    }
    
    public int[] AddIMMBinaryToOpcode(int[] opcode_to_apply, int[] imm_binary_opcode)
    {
        System.out.println("imm binary opcode length "+imm_binary_opcode.length);
        for (int i = 11, j = 0; i >= 7; i--, j++)
        {
            opcode_to_apply[31-i] = imm_binary_opcode[j];
        }
        return opcode_to_apply;
    }
    
    public int[] AddFunct3BinaryToOpcode(int[] opcode_to_apply, int[] funct3_binary_opcode)
    {
        System.out.println("funct3 binary opcode length "+funct3_binary_opcode.length);
        for (int i = 14, j = 0; i >= 12; i--, j++)
        {
            opcode_to_apply[31-i] = funct3_binary_opcode[j];
        }
        return opcode_to_apply;
    }
    
    public int[] AddRS1BinaryToOpcode(int[] opcode_to_apply, int[] rs1_binary_opcode)
    {
        System.out.println("rs1 binary opcode length "+rs1_binary_opcode.length);
        for (int i = 19, j = 0; i >= 15; i--,j++)
        {
            opcode_to_apply[31-i] = rs1_binary_opcode[j];
        }
        return opcode_to_apply;
    }
    
    public int[] AddRS2BinaryToOpcode(int[] opcode_to_apply, int[] rs2_binary_opcode)
    {
        System.out.println("rs2 binary opcode length "+rs2_binary_opcode.length);
        for (int i = 24, j = 0; i >= 20; i--,j++)
        {
            opcode_to_apply[31-i] = rs2_binary_opcode[j];
        }
        return opcode_to_apply;
    }
    
    public int GetRegisterTableRow(String register) throws Exception{
        int table_row = 0;
        if (register.charAt(0) == 'x') //this will check if the name called starts with xN or its original register name (etc. t0, a1)
        {
            String rownum_string = Character.toString(register.charAt(1));
            table_row = Integer.parseInt(rownum_string);
        }
        else
        {
            if (register_alias_map.get(register) == null)
            { //error check
//                System.out.println("Invalid Register Name "+register);
                throw new Exception("Invalid Register Name " +register);
            }
            else
            {
                table_row = register_alias_map.get(register);
            }
        }
        
        return table_row;
    }
    
    public String GetHexaValueFromTableRow(int table_row, JTable jTableRegister)
    {
        Object pre_rd_value = jTableRegister.getValueAt(table_row, 2);
        String rd_value = (pre_rd_value == null) ? "" : pre_rd_value.toString();
        
        rd_value = rd_value.replace("0x", ""); //removes the radix
        
        return rd_value;
    }
    
    public int HexToDecimal(String s)
    {
             String digits = "0123456789ABCDEF";
             s = s.toUpperCase();
             int val = 0;
             for (int i = 0; i < s.length(); i++)
             {
                 char c = s.charAt(i);
                 int d = digits.indexOf(c);
                 val = 16*val + d;
             }
             return val;
    }
    
    public int[] HexaToBinary(String hexa)
    {
        int[] binary_val = new int[5];
        
        if (hexa.isEmpty())
        {
            int i = 1;
            int dec_num = HexToDecimal(hexa);

            /* convert decimal to binary */        
            while(dec_num != 0)
            {
                binary_val[i++] = dec_num%2;
                dec_num = dec_num/2;
            }

            System.out.print("Equivalent Binary Number is: ");
        }
        return binary_val;
    }
    
    public int[] DecimalToBinary(String decimal)
    {
        int[] binary_val = new int[5];

        if (!decimal.isEmpty())
        {
            int i = 0;
            int quot = Integer.parseInt(decimal);
            while(quot != 0)
            {
                binary_val[i++] = quot%2;
                quot = quot/2;
            }
            //zero pad after
            System.out.println("Binary number is "+ binary_val);
        }
        
        return binary_val;
    }
    
    public String BinaryToHex(int[] binary)
    {
        String bin_string = "";
        for (int bin : binary)
        {
            bin_string += bin;
        }
//        for (int i = binary.length - 1; i >= 0; i--)
//        {
//            bin_string += binary[i];
//        }
//        System.out.println(bin_string);
        BigInteger b = new BigInteger(bin_string, 2);
        String converted_hex = b.toString(16);
        String zero_pads = "";
        
        for (int i = 0; i <= 7 - converted_hex.length(); i++) // 8 bits - length of actual hex
        {
            zero_pads += "0";
        }
        return zero_pads + converted_hex;
    }
    
    //IF
    public void instruction_fetch(){
        //IR <-- Mem[PC}
        //NPC <-- PC + 4
    }
    
    //ID
    public void instruction_decode(){
        //A <- Regs[IR 19...15]
        //B <- Regs[IR 24...20]
        //Imm <- {Sign_extend[IR 31..20] (immediate ALU) | sign_extend[IR 31..25, 11..7] (branch/store)}
    }
    
    //EX
    public void execute(){
        //we can check invalid registers here
        //ALU Operations
    }
    
    //WB
    public void write_back(){
        //Regs[11..7] <- ALU Output
    }
    
    
    
    public void load_word(String rd, String rs1)
    {
        //TODO
        //we should check invalid registers here as well
        
    }
    
    public void store_word(String rd, String rs1)
    {
        //TODO
    }
    
    public void add_register(String rd, String rs1, String rs2)
    {
        //TODO
    }
    
    public void add_immediate(String rd, String rs1, String imm)
    {
        //TODO
    }
}
