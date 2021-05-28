/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark
 */
//Binary computation https://www.w3resource.com/java-exercises/basic/java-basic-exercise-19.php
//Hex computation https://www.w3resource.com/java-exercises/basic/java-basic-exercise-29.php
public class Pipeline {
    
    public HashMap<Integer, int[]> address_location_map;
    
    JTable tableRegister;
    JTable tableProgram;
    JTable tablePipelineMap;
    JTable tablePipelineInternalRegister;
    
    HashMap<String, Integer> register_alias_map;
    Map<String, String> pipeline_map;
    
    HashMap<String, Integer> pipeline_internal_register_map;
    
    DefaultTableModel register_model;
    DefaultTableModel program_model;
    DefaultTableModel pipeline_map_model;
    DefaultTableModel pipeline_internal_register_model;
    
    List<String> instruction_list;
    
    int cycles = 1;
    
    int previous_pc = 0;
    
    public Pipeline(JTable jTableRegister, JTable jTableProgram, JTable jTablePipelineMap, JTable jTablePipelineRegister)
    {
        instruction_list = new ArrayList<String>() {{
            add("lw");
            add("sw");
            add("add");
            add("addi");
            add("slt");
            add("slti");
            add("sll");
            add("slli");
            add("srl");
            add("srli");
            add("and");
            add("andi");
            add("or");
            add("ori");
            add("xor");
            add("xori");
            add("beq");
            add("bne");
            add("blt");
            add("bge");
        }};
        tableRegister = jTableRegister;
        tableProgram = jTableProgram;
        tablePipelineMap = jTablePipelineMap;
        tablePipelineInternalRegister = jTablePipelineRegister;
        
        register_model = (DefaultTableModel)tableRegister.getModel();
        program_model = (DefaultTableModel)tableProgram.getModel();
        pipeline_map_model = (DefaultTableModel) jTablePipelineMap.getModel();
        pipeline_internal_register_model = (DefaultTableModel) jTablePipelineRegister.getModel();
        
        pipeline_internal_register_map = new HashMap<String, Integer>() {{
            put("IF/ID.IR", 0);
            put("IF/ID.NPC", 1);
            put("PC", 2);
            put("ID/EX.A", 3);
            put("ID/EX.B", 4);
            put("ID/EX.IMM", 5);
            put("ID/EX.IR", 6);
            put("ID/EX.NPC", 7);
            put("EX/MEM.ALUOutput", 8);
            put("EX/MEM.IR", 9);
            put("EX/MEM.N", 10);
            put("EX/MEM.COND", 11);
            put("MEM/WB.LMD", 12);
            put("MEM/WB.IR", 13);
            put("MEM/WB.ALUOutput", 14);
            put("MEM[EX/MEM.ALUOutput", 15);
        }};
        
        pipeline_map = new TreeMap<String, String>();
    }
    
    public void Cycle() throws Exception
    {        
        int ir_row_index = pipeline_internal_register_map.get("PC");
        String current_pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        
        int current_counter_pc = FindTableRowByCounterPC(current_pc_hex);
        String current_state = GetJTableValue(tableProgram, current_counter_pc, 3);
        
        pipeline_map.put(current_pc_hex, current_state);
        
        cycles++;
        pipeline_map_model.addColumn("Cycle "+ (cycles - 1));
        
        for (Map.Entry<String, String> instruction: pipeline_map.entrySet())
        {
            //get state of each pc counter

            int instruction_pc = FindTableRowByCounterPC(instruction.getKey());
            String instruction_state = GetJTableValue(tableProgram, instruction_pc, 3);
            
            if (instruction_state.equals("MEM"))
            {
                WriteBack(instruction_pc);
            }
            else if(instruction_state.equals("EX"))
            {
                Memory(instruction_pc);
            }
            else if (instruction_state.equals("ID"))
            {
                Execute(instruction_pc);
            }
            else if (instruction_state.equals("IF"))
            {
                InstructionDecode(instruction_pc);
            }
            else if (instruction_state.equals(""))//assumes has no state yet
            {
                InstructionFetch(instruction_pc);
            }
        }
        //store each PC in a hashmap
        //each hashmap will act like its own thread
        //each hashmap value will be updated independently
    }
    
    public String ExtractInstruction(String instruction)
    {
            String[] parsed_line = instruction.split(" ");
            
            if (!instruction_list.contains(parsed_line[0])) //if the first param is a label, then remove the label from array
            {
                parsed_line = Arrays.copyOfRange(parsed_line, 1, parsed_line.length); //restructures the array index to original without changing the code below
            }
            
            return parsed_line [0];
    }
    
    public String GetJTableValue(JTable targetTable, int row, int column)
    {
        Object pre_value = targetTable.getValueAt(row, column);
        String value = (pre_value == null) ? "" : pre_value.toString();
        
        value = value.replace("0x", ""); //removes the radix
        
        return value;
    }
    
    //IF
    public void InstructionFetch(int instruction_pc){
//        String current_pc_hex = GetJTableValue(tableRegister, 32, 2);

        String instruction_address = GetJTableValue(tableProgram, instruction_pc, 0);  
        
        int ir_row_index = pipeline_internal_register_map.get("PC"); //this is actually not used
//        String current_pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        String current_pc_hex = instruction_address;
        
        int current_pc_program_row = FindTableRowByCounterPC(current_pc_hex);
        
        int current_pc_int = Convert.HexToDecimal(current_pc_hex); 
        
        String if_id_ir_opcode = GetJTableValue(tableProgram, current_pc_program_row, 1);
        ir_row_index = pipeline_internal_register_map.get("IF/ID.IR");
        pipeline_internal_register_model.setValueAt(if_id_ir_opcode, ir_row_index, 1);
        
        int next_pc = current_pc_int + 4;
        int[] next_pc_bin = Convert.IntDecimalToBinary(next_pc, 32); //hex value has 32 bits
        String next_pc_hex = Convert.BinaryToHex(next_pc_bin);
        
        ir_row_index = pipeline_internal_register_map.get("PC");
        pipeline_internal_register_model.setValueAt(next_pc_hex, ir_row_index, 1);
        
        ir_row_index = pipeline_internal_register_map.get("IF/ID.NPC");
        pipeline_internal_register_model.setValueAt(next_pc_hex, ir_row_index, 1);
        
        register_model.setValueAt(next_pc_hex, 32, 2);
        
        int current_counter_pc = FindTableRowByCounterPC(current_pc_hex);
        program_model.setValueAt("IF", current_counter_pc, 3);
        
        
        pipeline_map_model.setValueAt("IF", current_counter_pc, cycles);
        
//        ir_row_index = pipeline_internal_register_map.get("ID/EX.IR");
//        String id_ex_ir_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
    }
    
    //ID
    public void InstructionDecode(int instruction_pc){
        //A <- Regs[IR 19...15]
        //B <- Regs[IR 24...20]
        //Imm <- {Sign_extend[IR 31..20] (immediate ALU) | sign_extend[IR 31..25, 11..7] (branch/store
        String instruction_address = GetJTableValue(tableProgram, instruction_pc, 0);
        
        String instruction_line = GetJTableValue(tableProgram, instruction_pc, 2);  //this is to check if its a branch instruction
        String current_instruction = ExtractInstruction(instruction_line);
        
        int ir_row_index = pipeline_internal_register_map.get("IF/ID.IR");
        String p_ir_model_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        int[] id_ir_full_binary = Convert.HexaToBinary(p_ir_model_hex, 32);
        
        int p_ir_model_int = Convert.HexToDecimal(p_ir_model_hex);
        
        ir_row_index = pipeline_internal_register_map.get("IF/ID.NPC");
        String next_pc = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);

        ir_row_index = pipeline_internal_register_map.get("ID/EX.NPC");
        pipeline_internal_register_model.setValueAt(next_pc, ir_row_index, 1);

        ir_row_index = pipeline_internal_register_map.get("ID/EX.A");
        int[] id_ex_a_binary = GetBinaryFromOpcode(id_ir_full_binary, 19, 15);
        String id_ex_a_hex = Convert.BinaryToHex(id_ex_a_binary);
        int id_ex_a_dec = Convert.HexToDecimal(id_ex_a_hex);
        pipeline_internal_register_model.setValueAt(id_ex_a_hex, ir_row_index, 1);

        ir_row_index = pipeline_internal_register_map.get("ID/EX.B");
        int[] id_ex_b_binary = GetBinaryFromOpcode(id_ir_full_binary, 24, 20);
        String id_ex_b_hex = Convert.BinaryToHex(id_ex_a_binary);
        int id_ex_b_dec = Convert.HexToDecimal(id_ex_b_hex);
        pipeline_internal_register_model.setValueAt(id_ex_b_hex, ir_row_index, 1);
        
//        ir_row_index = pipeline_internal_register_map.get("PC");
//        String current_npc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
//        int current_counter = FindTableRowByCounterPC(current_npc_hex);
//        
//        String current_state = GetJTableValue(tableProgram, current_counter, 3);

        boolean branch_executed = false;
        

        switch (current_instruction)
        {
            case "beq":
            case "bne":
            case "blt":
            case "bge":
                switch(current_instruction)
                {
                    case "beq":
                        if (id_ex_a_dec == id_ex_b_dec) branch_executed = true;
                        break;
                    case "bne":
                        if (id_ex_a_dec != id_ex_b_dec) branch_executed = true;
                        break;
                    case "blt":
                        if (id_ex_a_dec < id_ex_b_dec) branch_executed = true;
                        break;
                    case "bge":
                        if (id_ex_a_dec > id_ex_b_dec) branch_executed = true;
                        break;
                }
                
                if (branch_executed)
                {
                    System.out.println("Branch Instruction Detected");
//                    ir_row_index = pipeline_internal_register_map.get("PC");
//                    String pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                    int pc_dec = Convert.HexToDecimal(instruction_address);
                    
                    ir_row_index = pipeline_internal_register_map.get("ID/EX.IMM");
                    int[] id_ex_rd_binary = GetBinaryFromOpcode(id_ir_full_binary, 11, 7); //this gets the rd section of the result

                    int[] id_ex_imm_binary = GetBinaryFromOpcode(id_ir_full_binary, 31, 25); //this gets the rd section of the result

                    int[] rd_bin_restruct = new int[id_ex_rd_binary.length];

                    rd_bin_restruct[4] = id_ex_rd_binary[3];
                    rd_bin_restruct[3] = id_ex_rd_binary[2];
                    rd_bin_restruct[2] = id_ex_rd_binary[1];
                    rd_bin_restruct[1] = id_ex_rd_binary[0];
                    rd_bin_restruct[0] = id_ex_imm_binary[6];

                    int[] imm_bin_restruct = new int[id_ex_imm_binary.length];

                    imm_bin_restruct[6] = id_ex_imm_binary[5];
                    imm_bin_restruct[5] = id_ex_imm_binary[4];
                    imm_bin_restruct[4] = id_ex_imm_binary[3];
                    imm_bin_restruct[3] = id_ex_imm_binary[2];
                    imm_bin_restruct[2] = id_ex_imm_binary[1];
                    imm_bin_restruct[1] = id_ex_rd_binary[4];
                    imm_bin_restruct[0] = id_ex_imm_binary[0];
                    
                    int[] merged_bin = new int[rd_bin_restruct.length + imm_bin_restruct.length];
                    
                    for (int i = 0; i < rd_bin_restruct.length; i++)
                    {
                        merged_bin[0] = rd_bin_restruct[0];
                    }
                    
                    for (int i = rd_bin_restruct.length, j = 0; j < imm_bin_restruct.length; i++ ,j++)
                    {
                        merged_bin[i] = imm_bin_restruct[j];
                    }
                    
                    int id_ex_imm_dec = Convert.BinaryToDecimal(merged_bin);

                    int next_pc_dec = pc_dec + id_ex_imm_dec;
                    String next_pc_hex = Convert.IntDecimalToHex(next_pc_dec, 32);
                    
                    ir_row_index = pipeline_internal_register_map.get("PC");
                    pipeline_internal_register_model.setValueAt(next_pc_hex, ir_row_index, 1);

                    ir_row_index = pipeline_internal_register_map.get("IF/ID.NPC");
                    pipeline_internal_register_model.setValueAt(next_pc_hex, ir_row_index, 1);
                }
                break;
            default:
                break;
        }

        
        int current_counter_pc = FindTableRowByCounterPC(instruction_address);
        
        if(current_counter_pc != -1)
        {
            program_model.setValueAt("ID", current_counter_pc, 3);
            pipeline_map_model.setValueAt("ID", current_counter_pc, cycles);
        }
    }
    
    //EX
    public void Execute(int instruction_pc) throws Exception{
        //we can check invalid registers here
        //ALU Operations

        String instruction_address = GetJTableValue(tableProgram, instruction_pc, 0);

        String instruction_line = GetJTableValue(tableProgram, instruction_pc, 2);  
        String current_instruction = ExtractInstruction(instruction_line);
        
        int ir_row_index = pipeline_internal_register_map.get("PC");
        String current_pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        
        ir_row_index = pipeline_internal_register_map.get("ID/EX.IR");
        String id_ex_ir_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        ir_row_index = pipeline_internal_register_map.get("EX/MEM.IR");
        pipeline_internal_register_model.setValueAt(current_pc_hex, ir_row_index, 1);
        
        //A + B = EX/MEM.ALUOutput
        ir_row_index = pipeline_internal_register_map.get("ID/EX.A");
        String id_ex_a_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        
        ir_row_index = pipeline_internal_register_map.get("ID/EX.B");
        String id_ex_b_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        
        int a;
        int b;
        int imm;
        int ALUOutput_Decimal = 0;
        String ALUOutput_String = "00000000";
        String imm_hex_opcode = "00000000";
        
        switch (current_instruction)
        {
                case "lw":
                    a = Convert.HexToDecimal(id_ex_a_opcode);
                    
                    ir_row_index = pipeline_internal_register_map.get("ID/EX.IMM");
                    imm_hex_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                    imm = Convert.HexToDecimal(imm_hex_opcode);
                    
                    ir_row_index = pipeline_internal_register_map.get("ID/EX.B");
                    String ex_mem_b = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                    pipeline_internal_register_model.setValueAt(ex_mem_b, ir_row_index, 1);
                    
                    ALUOutput_Decimal = a + imm;
                    ALUOutput_String = Convert.IntDecimalToHex(ALUOutput_Decimal, 32);
                    break;
                case "sw":
                    break;
                case "add":
                case "and":
                case "or":
                case "xor":
                case "sll":
                case "slt":
                case "srl":
                    a = Convert.HexToDecimal(id_ex_a_opcode);
                    b = Convert.HexToDecimal(id_ex_b_opcode);
                    
                    switch(current_instruction)
                    {
                            case "add":
                                ALUOutput_Decimal = a + b;
                                break;
                            case "and":
                                ALUOutput_Decimal = a & b;
                                break;
                            case "or":
                                ALUOutput_Decimal = a | b;
                                break;
                            case "xor":
                                ALUOutput_Decimal = a ^ b;
                                break;
                            case "slt":
                                ALUOutput_Decimal = (a < b) ? 1 : 0;
                                break;
                            case "sll":
                                ALUOutput_Decimal = a << b;
                                break;
                            case "srl":
                                ALUOutput_Decimal = a >> b;
                                break;
                    }   
                    ALUOutput_String = Convert.IntDecimalToHex(ALUOutput_Decimal, 32);
                    break;
                case "addi":
                case "andi":
                case "slti":
                case "ori":
                case "xori":
                case "slli":
                case "srli":
                    a = Convert.HexToDecimal(id_ex_a_opcode);
                    
                    ir_row_index = pipeline_internal_register_map.get("ID/EX.IMM");
                    imm_hex_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                    imm = Convert.HexToDecimal(imm_hex_opcode);
                    
                    switch(current_instruction)
                    {
                        case "addi":
                            ALUOutput_Decimal = a + imm;
                            break;
                        case "andi":
                            ALUOutput_Decimal = a & imm;
                            break;
                        case "slti":
                            ALUOutput_Decimal = (a < imm) ? 1 : 0;
                            break;
                        case "ori":
                            ALUOutput_Decimal = a | imm;
                            break;
                        case "xori":
                            ALUOutput_Decimal = a ^ imm;
                            break;
                        case "slli":
                            ALUOutput_Decimal = a << imm;
                            break;
                        case "srli":
                            ALUOutput_Decimal = a >> imm;
                            break;
                            
                    }
                    
                    ALUOutput_String = Convert.IntDecimalToHex(ALUOutput_Decimal, 32);
                    break;
                case "beq":
                case "bne":
                case "blt":
                case "bge":
                    a = Convert.HexToDecimal(id_ex_a_opcode);
                    
                    ir_row_index = pipeline_internal_register_map.get("ID/EX.IMM");
                    imm_hex_opcode = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                    imm = Convert.HexToDecimal(imm_hex_opcode);
                    
                    b = Convert.HexToDecimal(id_ex_b_opcode);
                    
                    switch(current_instruction)
                    {
                        case "beq":
                            ALUOutput_Decimal = (a == b) ? 1 : 0;
                            break;
                        case "bne":
                            ALUOutput_Decimal = (a != b) ? 1 : 0;
                            break;
                        case "blt":
                            ALUOutput_Decimal = (a <= b) ? 1 : 0;
                            break;
                        case "bge":
                            ALUOutput_Decimal = (a >= b) ? 1 : 0;
                            break;
                    }
                    ALUOutput_String = Convert.IntDecimalToHex(ALUOutput_Decimal, 32);
                    break;
        }
        
        ir_row_index = pipeline_internal_register_map.get("EX/MEM.ALUOutput");
        pipeline_internal_register_model.setValueAt(ALUOutput_String, ir_row_index, 1);
        
        int current_counter_pc = FindTableRowByCounterPC(instruction_address);
        
        if(current_counter_pc != -1)
        { 
            program_model.setValueAt("EX", current_counter_pc, 3);
            pipeline_map_model.setValueAt("EX", current_counter_pc, cycles);
        }
    }
    
    public void Memory(int instruction_pc)
    {
        String instruction_address = GetJTableValue(tableProgram, instruction_pc, 0);

        String instruction_line = GetJTableValue(tableProgram, instruction_pc, 2);  
        String current_instruction = ExtractInstruction(instruction_line);
        
        int ir_row_index = pipeline_internal_register_map.get("PC");
        String current_pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        
        
        String id_ex_mem_ir;
        switch (current_instruction)
        {
            case "lw":
                //MEM/WB.IR <- EX/MEM.IR
                ir_row_index = pipeline_internal_register_map.get("EX/MEM.IR");
                id_ex_mem_ir = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                ir_row_index = pipeline_internal_register_map.get("MEM/WB.IR");
                pipeline_internal_register_model.setValueAt(current_pc_hex, ir_row_index, 1);
                break;
            case "sw":
            case "add":
            case "and":
            case "or":
            case "xor":
            case "sll":
            case "slt":
            case "srl":
            case "addi":
            case "andi":
            case "slti":
            case "ori":
            case "xori":
            case "slli":
            case "srli":
            case "beq":
            case "bne":
            case "blt":
            case "bge":
                ir_row_index = pipeline_internal_register_map.get("EX/MEM.IR");
                id_ex_mem_ir = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                ir_row_index = pipeline_internal_register_map.get("MEM/WB.IR");
                pipeline_internal_register_model.setValueAt(current_pc_hex, ir_row_index, 1);
                
                ir_row_index = pipeline_internal_register_map.get("EX/MEM.ALUOutput");
                id_ex_mem_ir = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
                ir_row_index = pipeline_internal_register_map.get("MEM/WB.ALUOutput");
                pipeline_internal_register_model.setValueAt(current_pc_hex, ir_row_index, 1);
                break;
        }
        
        int current_counter_pc = FindTableRowByCounterPC(instruction_address);
        
        if(current_counter_pc != -1)
        {
            program_model.setValueAt("MEM", current_counter_pc, 3);
        }
    }
    
    //WB
    public void WriteBack(int instruction_pc){
        //Regs[11..7] <- ALU Output
        String instruction_address = GetJTableValue(tableProgram, instruction_pc, 0);

        String instruction_line = GetJTableValue(tableProgram, instruction_pc, 2);  
        String current_instruction = ExtractInstruction(instruction_line);
        
        int ir_row_index = pipeline_internal_register_map.get("PC");
        String current_pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        
        int current_counter_pc = FindTableRowByCounterPC(instruction_address);
        
        if(current_counter_pc != -1)
        {
            program_model.setValueAt("WB", current_counter_pc, 3);
        }
        
        pipeline_map.remove(current_pc_hex);
    }
    
    public int FindTableRowByCounterPC(String pc_hex)
    {
        int found_row = -1;
        for (int i = 0; i < tableProgram.getRowCount(); i++)
        {
            Object pre_current_count = tableProgram.getValueAt(i, 0); //index 2 is the actual label row itself
            String current_count = (pre_current_count == null) ? "" : pre_current_count.toString();
            
            if (current_count.contains(pc_hex))
            {
                found_row = i;
                break;
            }
        }
        return found_row;
    }
    
    public int GetRegisterMapIndex(String register_name)
    {
        return pipeline_internal_register_map.get(register_name);
    }
    
    public int[] GetBinaryFromOpcode(int[] opcode_to_find, int upper_bound, int lower_bound)
    {
        int[] new_binary = new int[upper_bound + 1 - lower_bound];
        for (int i = upper_bound, j = 1; i >= lower_bound; i--,j++)
        {
            new_binary[new_binary.length-j] = opcode_to_find[31-i];
        }
        return new_binary;
    }
}
