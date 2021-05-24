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
    HashMap<String, Integer> register_alias_map;
    DefaultTableModel register_model;
    DefaultTableModel program_model;
    
    public Pipeline(JTable jTableRegister, JTable jTableProgram)
    {
        tableRegister = jTableRegister;
        tableProgram = jTableProgram;
        register_model = (DefaultTableModel)tableRegister.getModel();
        program_model = (DefaultTableModel)tableProgram.getModel();
        
    }
    
    public void Start()
    {
        //start counting
        InstructionFetch();
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
        int current_pc_int = Convert.HexToDecimal(current_pc_hex);
        
        String ir = GetJTableValue(tableProgram, current_pc_int, 1);
        
        int next_pc = current_pc_int + 4;
        int[] next_pc_bin = Convert.DecimalToBinary(current_pc_hex, 32); //hex value has 32 bits
        String next_pc_hex = Convert.BinaryToHex(next_pc_bin);
        
        register_model.setValueAt("0x"+next_pc_hex, 0, 0);
    }
    
    //ID
    public void InstructionDecode(){
        //A <- Regs[IR 19...15]
        //B <- Regs[IR 24...20]
        //Imm <- {Sign_extend[IR 31..20] (immediate ALU) | sign_extend[IR 31..25, 11..7] (branch/store)}
    }
    
    //EX
    public void Execute(){
        //we can check invalid registers here
        //ALU Operations
    }
    
    //WB
    public void WriteBack(){
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
