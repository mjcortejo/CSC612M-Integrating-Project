/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;

/**
 *
 * @author mark
 */
public class Opcode {
    
    HashMap<String, int[]> instruction_opcode_map;
    HashMap<String, int[]> funct3_opcode_map;
    HashMap<String, int[]> funct7_opcode_map;
    HashMap<String, Integer> register_alias_map;
    HashMap<String, String[]> instruction_parse_map;
    int[] binary_opcode;
    
    JTable jTableRegister;
    JTable jTableProgram;
    JTable jTableMemory;
    HashMap<String, int[]> data_segment_map;
    
    /***
     * Constructor mandatory accepts jTableRegister and a jTableProgram parameter
     * @param jTableRegister_param
     * @param jTableProgram_param 
     */
    public Opcode(JTable jTableRegister_param, JTable jTableProgram_param, JTable jTableMemory_param, HashMap data_segment_map_param)
    {
        jTableRegister = jTableRegister_param;
        jTableProgram = jTableProgram_param;
        jTableMemory = jTableMemory_param;
        
        data_segment_map = data_segment_map_param;
        
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
        
        //add, and, or, xor, slt, sll, and srl
        //slli, srli
        funct7_opcode_map = new HashMap<String, int[]>(){{
           put("add", new int[] {0,0,0,0,0,0,0});
           put("and", new int[] {0,0,0,0,0,0,0});
           put("or", new int[] {0,1,0,0,0,0,0});
           put("xor", new int[] {0,0,0,0,0,0,0});
           put("slt", new int[] {0,0,0,0,0,0,0});
           put("srl", new int[] {0,0,0,0,0,0,0});
           put("slli", new int[] {0,0,0,0,0,0,0});
           put("srli", new int[] {0,0,0,0,0,0,0});
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
    
    /***
     * Generates Opcode from an instruction line
     * @param line
     * @param current_line
     * @return 
     */
    public String GenerateOpcode(String line, int current_line)
    {
        String full_opcode = "";
        binary_opcode = new int[32];
        try
        {
            //checks instruction
            String[] parsed_line = line.split(" ");
            
            if (!instruction_opcode_map.containsKey(parsed_line[0])) //if the first param is a label, then remove the label from array
            {
                parsed_line = Arrays.copyOfRange(parsed_line, 1, parsed_line.length); //restructures the array index to original without changing the code below
            }

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
            int[] funct7_opcode = funct7_opcode_map.get(instruction);

            int rd_table_row;
            int rs1_table_row;
            int rs2_table_row;
            String hexa_value;

            //they are in bits but their value needs to be extracted first
            int[] rd_binary = new int[5]; //5 bits // this is the IMM in the opcode location
            int[] rs1_binary = new int[5]; //5 bits // 
            int[] rs2_binary = new int[5]; //5 bits
            int[] imm_binary;
            
            switch(instruction.toLowerCase())
            {
                case "lw":
                    rd_table_row = GetRegisterTableRow(params[0]);
                    rd_binary = Convert.DecimalToBinary(Integer.toString(rd_table_row)); 
                    
                    int row_index = data_segment_map.get(params[1])[0];
                    int col_index = data_segment_map.get(params[1])[1];
                    rs1_binary = GetIMMBinaryOfOffset(params[1]);
                    
                    String imm_hex = GetMemoryHexValueFromTableRow(row_index, col_index);
                    imm_binary = Convert.HexaToBinary(imm_hex, 12);

                    AddBinaryToOpcode(binary_opcode, instruction_opcode, 6, 0);
                    AddBinaryToOpcode(binary_opcode, rd_binary, 11, 7);
                    AddBinaryToOpcode(binary_opcode, funct3_opcode, 14, 12);
                    AddBinaryToOpcode(binary_opcode, rs1_binary, 19, 15);
                    AddBinaryToOpcode(binary_opcode, imm_binary, 31, 20);
                    
                    binary_opcode = InvertBinary(binary_opcode);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                case "sw":
//                    String[] pre_offset_params = params[1].split("\\("); //this removes the ( in (x8) <-- example
//                    String offset = pre_offset_params[0]; //we should be able to get the offset address
                    String rs1_register = GetTargetOffsetRegister(params[1]);// we will remove the closing parenthesis from x8)
                    String rs2_register = params[0];
                    
                    rd_table_row = GetRegisterTableRow(params[0]);
                    rd_binary = GetIMMBinaryOfOffset(params[1]);
                    rs1_table_row = GetRegisterTableRow(rs1_register);
                    rs1_binary = Convert.IntDecimalToBinary(rs1_table_row);
                    rs2_table_row = GetRegisterTableRow(rs2_register); //rs2_register is the rd, in sw instruction
                    rs2_binary = Convert.IntDecimalToBinary(rs2_table_row);
                    
                    AddBinaryToOpcode(binary_opcode, instruction_opcode, 6, 0);
                    AddBinaryToOpcode(binary_opcode, rd_binary, 11, 7);
                    AddBinaryToOpcode(binary_opcode, funct3_opcode, 14, 12);
                    AddBinaryToOpcode(binary_opcode, rs1_binary, 19, 15);
                    AddBinaryToOpcode(binary_opcode, rs2_binary, 24, 20);
                    
                    binary_opcode = InvertBinary(binary_opcode);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
//              add, and, or, xor, slt, sll, and srl are grouped because they share similar opcode construction
                case "add":
                case "and":
                case "or":
                case "xor":
                case "slt":
                case "sll":
                case "srl":
                    rd_table_row = GetRegisterTableRow(params[0]);
                    rd_binary = Convert.DecimalToBinary(Integer.toString(rd_table_row)); 
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    rs2_table_row = GetRegisterTableRow(params[2]);
                    hexa_value = GetRegisterHexValueFromTableRow(rs1_table_row);
                    rs1_binary = Convert.HexaToBinary(hexa_value);
                    hexa_value = GetRegisterHexValueFromTableRow(rs2_table_row);
                    rs2_binary = Convert.HexaToBinary(hexa_value);
                    
                    AddBinaryToOpcode(binary_opcode, instruction_opcode, 6, 0);
                    AddBinaryToOpcode(binary_opcode, rd_binary, 11, 7);
                    AddBinaryToOpcode(binary_opcode, funct3_opcode, 14, 12);
                    AddBinaryToOpcode(binary_opcode, rs1_binary, 19, 15);
                    AddBinaryToOpcode(binary_opcode, rs2_binary, 24, 20);
                    AddBinaryToOpcode(binary_opcode, funct7_opcode, 31, 25);
                    
                    binary_opcode = InvertBinary(binary_opcode);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                case "addi":
                case "andi":
                case "slti":
                case "ori":
                case "xori":
                    rd_table_row = GetRegisterTableRow(params[0]);
                    rd_binary = Convert.DecimalToBinary(Integer.toString(rd_table_row)); 
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    hexa_value = GetRegisterHexValueFromTableRow(rs1_table_row);
                    rs1_binary = Convert.HexaToBinary(hexa_value); 
//                    imm_binary = Convert.DecimalToBinary(params[2], 12); //replace immediate value here 
                    imm_binary = ExtractImmediateValue(params[2], 12);
                    
                    AddBinaryToOpcode(binary_opcode, instruction_opcode, 6, 0);
                    AddBinaryToOpcode(binary_opcode, rd_binary, 11, 7);
                    AddBinaryToOpcode(binary_opcode, funct3_opcode, 14, 12);
                    AddBinaryToOpcode(binary_opcode, rs1_binary, 19, 15);
                    AddBinaryToOpcode(binary_opcode, imm_binary, 31, 20);
                    
                    binary_opcode = InvertBinary(binary_opcode);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                case "slli":
                case "srli":
                    rd_table_row = GetRegisterTableRow(params[0]);
                    rd_binary = Convert.DecimalToBinary(Integer.toString(rd_table_row)); 
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    hexa_value = GetRegisterHexValueFromTableRow(rs1_table_row);
                    rs1_binary = Convert.HexaToBinary(hexa_value);
                    
                    int[] shamt_binary = Convert.DecimalToBinary(params[2]);
                    
                    AddBinaryToOpcode(binary_opcode, instruction_opcode, 6, 0);
                    AddBinaryToOpcode(binary_opcode, rd_binary, 11, 7);
                    AddBinaryToOpcode(binary_opcode, funct3_opcode, 14, 12);
                    AddBinaryToOpcode(binary_opcode, rs1_binary, 19, 15);
                    AddBinaryToOpcode(binary_opcode, shamt_binary, 24, 20);
                    
                    binary_opcode = InvertBinary(binary_opcode);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                //BEQ, BNE, BLT, BGE
                case "beq":
                case "bne":
                case "blt":
                case "bge":
                    funct7_opcode = new int[7]; //ignore funct7 mapping for branches
                    
                    rs1_table_row = GetRegisterTableRow(params[1]);
                    hexa_value = GetRegisterHexValueFromTableRow(rs1_table_row);
                    rs1_binary = Convert.HexaToBinary(hexa_value);
                    
                    String current_line_hexadecimal = GetProgramHexValueFromTableRow(current_line);
                    String branch_hexadecimal = FindLabelHexValueFromTableRow(params[2]); //params 3 is the target branch label
                    
                    //convert hexadecimals to decimal for easier computation
                    int current_line_integer = Convert.HexToDecimal(current_line_hexadecimal);
                    int branch_integer = Convert.HexToDecimal(branch_hexadecimal);
                    
                    //compute for the hexadecimal difference of label and current source
                    int result = branch_integer - current_line_integer;
                    int[] result_binary = Convert.IntDecimalToBinary(result, 12);
                    
                    rd_binary[4] = result_binary[1]; //res binary 1 == imm[11]
                    for (int i = 11, j = 3; i >= 8; i--, j--)
                    {
                        rd_binary[j] = result_binary[i];
                    }
                    
                    for (int i = 7; i >=2 ; i--)
                    {
                        funct7_opcode[i-1] = result_binary[i];
                    }
                    
                    funct7_opcode[0] = result_binary[0];                    
                    
                    AddBinaryToOpcode(binary_opcode, instruction_opcode, 6, 0);
                    AddBinaryToOpcode(binary_opcode, rd_binary, 11, 7); //to change
                    AddBinaryToOpcode(binary_opcode, funct3_opcode, 14, 12);
                    AddBinaryToOpcode(binary_opcode, rs1_binary, 19, 15);
                    AddBinaryToOpcode(binary_opcode, rs2_binary, 24, 20);
                    AddBinaryToOpcode(binary_opcode, funct7_opcode, 31, 25);
                    
                    binary_opcode = InvertBinary(binary_opcode);
                    full_opcode = Convert.BinaryToHex(binary_opcode);
                    break;
                default: //check if its a label
                    Pattern pattern = Pattern.compile("(\\w+\\:|\\.\\w+)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(line);
                    
                    if (matcher.find())
                    {
                        System.out.println(matcher.group(0));
                        if (matcher.group(0).equals(".data"))
                        {
                            System.out.println("Data Section detected");
                        }
                        else if (matcher.group(0).equals(".text"))
                        {
                            System.out.println("Text Section detected");
                        }
                        else
                        {
                            System.out.println("Found a label");
                        }
                    }
                    else //assumes invalid instruction
                    {
                        throw new Exception("Invalid Instruction "+instruction);
                    }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        PrintBinaryOpcode(binary_opcode);
        return full_opcode;
    }
    
    public static int[] ExtractImmediateValue(String imm_value, int num_bits)
    {
        int[] binary_value;
        if (imm_value.contains("0x"))
        {
            imm_value = imm_value.replace("0x", "");   
            binary_value = Convert.HexaToBinary(imm_value);
        }
        else
        {
            binary_value = Convert.DecimalToBinary(imm_value);
        }
        return binary_value;
    }
    
    /***
     * Update the binary opcode range using the upper and lower bound of the binary list
     * @param opcode_to_apply
     * @param binary_opcode
     * @param upper_bound
     * @param lower_bound 
     */
    public static void AddBinaryToOpcode(int[] opcode_to_apply, int[] binary_opcode, int upper_bound, int lower_bound)
    {
        for (int i = upper_bound, j = 0; i >= lower_bound; i--,j++)
        {
            opcode_to_apply[31-i] = binary_opcode[j];
        }
    }
    
    /***
     * Inverts the Binary Array
     * @param binary_to_invert
     * @return 
     */
    public static int[] InvertBinary(int[] binary_to_invert)
    {
        int[] inverted_bin = new int[binary_to_invert.length];
        for (int i = binary_to_invert.length - 1, j = 0; i >= 0; i--, j++)
        {
            inverted_bin[j] = binary_to_invert[i];
        }
        return inverted_bin;
    }
    
    /***
     * Prints the Binary Opcode
     * @param opcode_to_print 
     */
    public static void PrintBinaryOpcode(int[] opcode_to_print)
    {
        opcode_to_print = InvertBinary(opcode_to_print);
        String bin = "";
        for (int i = opcode_to_print.length - 1; i >= 0 ; i--)
        {
            bin = opcode_to_print[i] + bin;
        }
        System.out.println(bin);
    }
    
    /***
     * Gets the row number for the jTableRegister using the register name
     * @param register
     * @return
     * @throws Exception 
     */
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
    
    /***
     * Gets the Hex Value from jTableRegister using the table row index
     * @param table_row
     * @return 
     */
    public String GetRegisterHexValueFromTableRow(int table_row)
    {
        Object pre_rd_value = jTableRegister.getValueAt(table_row, 2);
        String rd_value = (pre_rd_value == null) ? "" : pre_rd_value.toString();
        
        rd_value = rd_value.replace("0x", ""); //removes the radix
        
        return rd_value;
    }
    
    public String GetRegisterHexValueFromRegisterName(String register_name) throws Exception
    {
        int register_int = GetRegisterTableRow(register_name);
        String hex_value = GetRegisterHexValueFromTableRow(register_int);
        
        return hex_value;
    }
    
    /***
     * Gets the Hex address of a program line from the jTableProgram using table row index
     * @param table_row
     * @return 
     */
    public String GetProgramHexValueFromTableRow(int table_row)
    {
        Object pre_rd_value = jTableProgram.getValueAt(table_row, 0);
        String rd_value = (pre_rd_value == null) ? "" : pre_rd_value.toString();
        
        rd_value = rd_value.replace("0x", ""); //removes the radix
        
        return rd_value;
    }
    
    public String GetMemoryHexValueFromTableRow(int row, int col)
    {
        Object pre_rd_value = jTableMemory.getValueAt(row, col);
        String rd_value = (pre_rd_value == null) ? "" : pre_rd_value.toString();
        
        rd_value = rd_value.replace("0x", ""); //removes the radix
        
        return rd_value;
    }
    
    /***
     * Finds the hex address using a label name from the jTableProgram
     * @param label
     * @return 
     */
    public String FindLabelHexValueFromTableRow(String label)
    {
        String found_label = "";
        for (int i = 0; i < jTableProgram.getRowCount(); i++)
        {
            String temp_label = label + ":"; //because the target label has a colon in it, this'll make things easier
            Object pre_current_label = jTableProgram.getValueAt(i, 2); //index 2 is the actual label row itself
            String current_label = (pre_current_label == null) ? "" : pre_current_label.toString();
            
            if (current_label.contains(temp_label))
            {
                Object pre_found_label = jTableProgram.getValueAt(i, 0); //index 0 is the hexadecimal value of the label
                found_label = (pre_found_label == null) ? "" : pre_found_label.toString();
                
                found_label = found_label.replace("0x", "");
                break;
            }
        }
        return found_label;
    }
    
    /***
     * Converts the Hex offset of an IMM to Binary
     * @param full_param
     * @return 
     */
    public int[] GetIMMBinaryOfOffset(String full_param)
    { //accepts 0x100(x8) (0x100 is hexadecimal)
        String[] pre_offset_params = full_param.split("\\("); //this removes the ( in (x8) <-- example
        String offset = pre_offset_params[0]; //we should be able to get the offset address
        try
        {
            String target = pre_offset_params[1]; //this will error, if there is no offset it's usually a source
        }
        catch(Exception e)
        {
            offset = "0"; //force binary to zero
        }
        
        int[] offset_binary;
                
        if (offset.contains("0x")) //if offset is hex
        {
            offset = offset.replace("0x", "");
            offset_binary = Convert.HexaToBinary(offset, 12); //12 bits for the imm
        }
        else //if offset is decimal, this will also error
        {
            offset_binary = Convert.DecimalToBinary(offset, 12); 
        }
        return offset_binary;
    }
    
    
    public String GetTargetOffsetRegister(String full_param) throws Exception
    {
        String[] pre_offset_params = full_param.split("\\("); //this removes the ( in (x8) <-- example
        String target_register_name = pre_offset_params[1]; //we should be able to get the offset address
        target_register_name = target_register_name.replace(")", ""); // we will remove the closing parenthesis from x8)
       
//        String target_hex_value = GetRegisterHexValueFromRegisterName(target_register_name);
//        int[] target_binary = Convert.HexaToBinary(target_hex_value);
        
        return target_register_name;
    }
}
