/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mark
 */
public class Pipeline {
    
    HashMap<String, String> opcode_map;
    
    public Pipeline()
    {
        opcode_map = new HashMap<String, String>() {{
            put("lw",  "0000011");
            put("sw",  "0100011");
            put("add", "0110011");
            put("addi","0010011");
            put("slt", "0110011");
            put("slti","0010011");
            put("sll", "0110011");
            put("slli","0010011");
            put("srl", "0110011");
            put("srli","0010011");
            put("and", "0110011");
            put("andi","0010011");
            put("or",  "0110011");
            put("ori", "0010011");
            put("xor", "0110011");
            put("xori","0010011");
            put("beq", "1100011");
            put("bne", "1100011");
            put("blt", "1100011");
            put("bge", "1100011");
        }};
    }
    
    //we should probably dissect the pipeline's execution as each function (etc. Decode = 1 function, PC = 1 function, 
    //so we can reflect the state of the simulator back to the GUI
    
    public void parse_line(String line, int line_number)
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
        
        switch(instruction.toLowerCase())
        {
            case "lw":
                load_word(params[0], params[1]);
                break;
            case "sw":
                store_word(params[0], params[1]);
            default:
                System.out.println("Invalid instruction at line number "+line_number);
                break;
        }
        
        //check if the params are correct
        
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
}
