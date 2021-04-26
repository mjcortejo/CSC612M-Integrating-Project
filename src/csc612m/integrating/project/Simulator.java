/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.util.HashMap;

/**
 *
 * @author mark
 */
public class Simulator {
    
    HashMap<String, String> opcode_map;
    
    public Simulator()
    {
        opcode_map = new HashMap<String, String>() {{
            put("LW", "");
            put("SW", "");
            put("ADD", "");
            put("ADDI", "");
            put("SLT", "");
            put("SLTI", "");
            put("SLL", "");
            put("SLLI", "");
            put("SRL", "");
            put("SRLI", "");
            put("AND", "");
            put("ANDI", "");
            put("OR", "");
            put("ORI", "");
            put("XOR", "");
            put("XORI", "");
            put("BEQ", "");
            put("BNE", "");
            put("BLT", "");
            put("BGE", "");
        }};
    }
    
    public void parse_line(String line, int line_number)
    {
        String instruction = opcode_map.get(line);
        if (instruction.isBlank())
        {
            System.out.println("Error on line "+line_number);
        }
        else
        {
            //parse command here
        }
    }
}
