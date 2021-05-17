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
    
    public Pipeline()
    {
        
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
