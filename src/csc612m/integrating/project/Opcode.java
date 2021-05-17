/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;

/**
 *
 * @author mark
 */
public class Opcode {
    
    HashMap<String, int[]> instruction_opcode_map;
    HashMap<String, int[]> funct3_opcode_map;
    HashMap<String, Integer> register_alias_map;
    int[] binary_opcode = new int[32];
    
    public Opcode()
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
    
    public String GenerateOpcode(String line, JTable jTableRegister)
    {
        String full_opcode = "";
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

            int rd_table_row = GetRegisterTableRow(params[0]);
            int rs1_table_row;
            int rs2_table_row;
            String hexa_value;

            //they are in bits but their value needs to be extracted first
            int[] rd_binary = Convert.DecimalToBinary(Integer.toString(rd_table_row)); //5 bits // this is the IMM in the opcode location
            int[] rs1_binary = new int[5]; //5 bits // 
            int[] rs2_binary = new int[5]; //5 bits
            
            switch(instruction.toLowerCase())
            {
                case "lw":
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    hexa_value = GetHexaValueFromTableRow(rs1_table_row, jTableRegister);
                    rs1_binary = Convert.HexaToBinary(hexa_value);
                    binary_opcode = AddInstructionBinaryToOpcode(binary_opcode, instruction_opcode);
                    binary_opcode = AddIMMBinaryToOpcode(binary_opcode, rd_binary);
                    binary_opcode = AddFunct3BinaryToOpcode(binary_opcode, funct3_opcode);
                    binary_opcode = AddRS1BinaryToOpcode(binary_opcode, rs1_binary);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                case "add":
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    rs2_table_row = GetRegisterTableRow(params[2]);
                    hexa_value = GetHexaValueFromTableRow(rs1_table_row, jTableRegister);
                    rs1_binary = Convert.HexaToBinary(hexa_value);
                    hexa_value = GetHexaValueFromTableRow(rs2_table_row, jTableRegister);
                    rs2_binary = Convert.HexaToBinary(hexa_value);
                    binary_opcode = AddInstructionBinaryToOpcode(binary_opcode, instruction_opcode);
                    binary_opcode = AddIMMBinaryToOpcode(binary_opcode, rd_binary);
                    binary_opcode = AddFunct3BinaryToOpcode(binary_opcode, funct3_opcode);
                    binary_opcode = AddRS1BinaryToOpcode(binary_opcode, rs1_binary);
                    binary_opcode = AddRS2BinaryToOpcode(binary_opcode, rs2_binary);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                default: //error check
                    throw new Exception("Invalid instruction "+instruction);
                
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return full_opcode;
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
    
}
