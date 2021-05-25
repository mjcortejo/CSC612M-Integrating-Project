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
    
    JTable tableRegister;
    JTable tableProgram;
    JTable tablePipelineMap;
    JTable tablePipelineInternalRegister;
    
    HashMap<String, Integer> register_alias_map;
    HashMap<String, String> pipeline_map;
    HashMap<String, Integer> pipeline_internal_register_map;
    
    DefaultTableModel register_model;
    DefaultTableModel program_model;
    DefaultTableModel pipeline_map_model;
    DefaultTableModel pipeline_internal_register_model;
    
    int cycles = 0;
    
    String IF = "";
    String ID = "";
    String EX = "";
    String MEM = "";
    String WB = "";
    
    public Pipeline(JTable jTableRegister, JTable jTableProgram, JTable jTablePipelineMap, JTable jTablePipelineRegister)
    {
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
    }
    
    public void Cycle()
    {
        cycles++;
        pipeline_map_model.addColumn("Cycle "+cycles);
        //extract instruction using current NPC
        //get first program counter to see if it matches in ID
        InstructionFetch();
        InstructionDecode();
    }
    
    public String GetJTableValue(JTable targetTable, int row, int column)
    {
        Object pre_value = targetTable.getValueAt(row, column);
        String value = (pre_value == null) ? "" : pre_value.toString();
        
        value = value.replace("0x", ""); //removes the radix
        
        return value;
    }
    
    //IF
    public void InstructionFetch(){
        //IR <-- Mem[PC}
        //NPC <-- PC + 4
        String current_pc_hex = GetJTableValue(tableRegister, 32, 2);
        
        int ir_row_index = pipeline_internal_register_map.get("PC");
        pipeline_internal_register_model.setValueAt(current_pc_hex, ir_row_index, 1);
        
        int current_pc_int = Convert.HexToDecimal(current_pc_hex);
        
        String ir_opcode = GetJTableValue(tableProgram, current_pc_int, 1);
        ir_row_index = pipeline_internal_register_map.get("IF/ID.IR");
        pipeline_internal_register_model.setValueAt(ir_opcode, ir_row_index, 1);
        
        int next_pc = current_pc_int + 4;
        int[] next_pc_bin = Convert.IntDecimalToBinary(next_pc, 32); //hex value has 32 bits
        String next_pc_hex = Convert.BinaryToHex(next_pc_bin);
        
        ir_row_index = pipeline_internal_register_map.get("IF/ID.NPC");
        pipeline_internal_register_model.setValueAt(next_pc_hex, ir_row_index, 1);
        
        register_model.setValueAt(next_pc_hex, 32, 0);
        
        int current_counter_pc = FindTableRowByCounterPC(current_pc_hex);
        program_model.setValueAt("IF", current_counter_pc, 3);
    }
    
    //ID
    public void InstructionDecode(){
        //A <- Regs[IR 19...15]
        //B <- Regs[IR 24...20]
        //Imm <- {Sign_extend[IR 31..20] (immediate ALU) | sign_extend[IR 31..25, 11..7] (branch/store
        int ir_row_index = pipeline_internal_register_map.get("IF/ID.IR");
        String p_ir_model_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);
        int[] id_ir_full_binary = Convert.HexaToBinary(p_ir_model_hex, 32);
        
        int p_ir_model_int = Convert.HexToDecimal(p_ir_model_hex);
        
        if (p_ir_model_int != 0)
        {
            ir_row_index = pipeline_internal_register_map.get("IF/ID.NPC");
            String current_pc_hex = GetJTableValue(tablePipelineInternalRegister, ir_row_index, 1);

            ir_row_index = pipeline_internal_register_map.get("ID/EX.NPC");
            pipeline_internal_register_model.setValueAt(current_pc_hex, ir_row_index, 1);
            
            ir_row_index = pipeline_internal_register_map.get("ID/EX.A");
            int[] id_ex_a_binary = GetBinaryFromOpcode(id_ir_full_binary, 19, 15);
            String id_ex_a_hex = Convert.BinaryToHex(id_ex_a_binary);
            pipeline_internal_register_model.setValueAt(id_ex_a_hex, ir_row_index, 1);
            
            ir_row_index = pipeline_internal_register_map.get("ID/EX.B");
            int[] id_ex_b_binary = GetBinaryFromOpcode(id_ir_full_binary, 24, 20);
            String id_ex_b_hex = Convert.BinaryToHex(id_ex_a_binary);
            pipeline_internal_register_model.setValueAt(id_ex_b_hex, ir_row_index, 1);
            
            ir_row_index = pipeline_internal_register_map.get("ID/EX.IMM");
            int[] id_ex_imm_binary = GetBinaryFromOpcode(id_ir_full_binary, 31, 20);
            String id_ex_imm_hex = Convert.BinaryToHex(id_ex_imm_binary);
            pipeline_internal_register_model.setValueAt(id_ex_imm_hex, ir_row_index, 1);
        }
    }
    
    //EX
    public void Execute(){
        //we can check invalid registers here
        //ALU Operations
    }
    
    public void Memory()
    {
    }
    
    //WB
    public void WriteBack(){
        //Regs[11..7] <- ALU Output
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
