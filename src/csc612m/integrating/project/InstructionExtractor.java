/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JTable;

/**
 *
 * @author mark
 */
public class InstructionExtractor {
    HashMap<String, String[]> instruction_parse_map;
    HashMap<String, int[]> data_segment_map;
    
    JTable jTableProgram;
    
    ArrayList instruction_list = new ArrayList<String>() {{
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
    
    public InstructionExtractor(JTable jTableProgram_param, HashMap data_segment_map_param)
    {
        jTableProgram = jTableProgram_param;
        data_segment_map = data_segment_map_param;
        
        instruction_parse_map = new HashMap<String, String[]>();
    }
    
    public HashMap<String, String[]> ExtractParams() //this function targets the <PC>, <line params> output
    {
        for (int i = 0; i < jTableProgram.getRowCount(); i++)
        {
            String hex_address = GetJTableValue(jTableProgram, i, 0);
            ArrayList<String> parsed_params = new ArrayList<String>();
            int line_number = FindTableRowByCounterPC(hex_address);
            String line = GetJTableValue(jTableProgram, line_number, 2);
            String[] parsed_line = ParseLine(line);
            
            String offset;
            String target;
            
            switch (parsed_line[0])
            {
                    case "lw":
                        offset = GetImmOfOffset(parsed_line[2]);
                        target = GetTargetOffsetRegister(parsed_line[2]);
                        instruction_parse_map.put(hex_address, new String[]{parsed_line[1], offset, target});
                        break;
                    case "sw":
                        offset = GetImmOfOffset(parsed_line[2]);
                        target = GetTargetOffsetRegister(parsed_line[2]);
                        instruction_parse_map.put(hex_address, new String[]{parsed_line[1], offset, target});
                         break;
                    case "add":
                    case "and":
                    case "or":
                    case "xor":
                    case "sll":
                    case "slt":
                    case "srl":
                        instruction_parse_map.put(hex_address, new String[]{parsed_line[1], parsed_line[2], parsed_line[3]});
                        break;
                    case "addi":
                    case "andi":
                    case "slti":
                    case "ori":
                    case "xori":
                    case "slli":
                    case "srli":
                        instruction_parse_map.put(hex_address, new String[]{parsed_line[1], parsed_line[2], parsed_line[3]});
                        break;
                    case "beq":
                    case "bne":
                    case "blt":
                    case "bge":
                        String branch_hexadecimal = FindTableRowFromLabelHexValue(parsed_line[3]); //params 3 is the target branch label
                        
                        int current_line_integer = Convert.HexToDecimal(hex_address);
                        int branch_integer = Convert.HexToDecimal(branch_hexadecimal);
                        
                        int result = branch_integer - current_line_integer;
                        
                        instruction_parse_map.put(hex_address, new String[]{parsed_line[1], parsed_line[2], branch_hexadecimal}); //branch_hexadecimal is target of 
                        break;  

            }
        }
        return instruction_parse_map;
    }
    
    public int FindTableRowByCounterPC(String pc_hex)
    {
        int found_row = -1;
        for (int i = 0; i < jTableProgram.getRowCount(); i++)
        {
            Object pre_current_count = jTableProgram.getValueAt(i, 0); //index 2 is the actual label row itself
            String current_count = (pre_current_count == null) ? "" : pre_current_count.toString();
            
            if (current_count.contains(pc_hex))
            {
                found_row = i;
                break;
            }
        }
        return found_row;
    }
    
    public String GetJTableValue(JTable targetTable, int row, int column)
    {
        Object pre_value = targetTable.getValueAt(row, column);
        String value = (pre_value == null) ? "" : pre_value.toString();
        
//        value = value.replace("0x", ""); //removes the radix
        
        return value;
    }
    
    public String FindTableRowFromLabelHexValue(String label)
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
    
    
    public String[] ParseLine(String line)
    {
        String[] parsed_line = line.split(" ");
        ArrayList<String> inst_params = new ArrayList<String>();
        if (!instruction_list.contains(parsed_line[0])) //if the first param is a label, then remove the label from array
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
        inst_params.add(instruction);
        Collections.addAll(inst_params, params);
        
        String[] array_inst_params = new String[inst_params.size()];
        
        array_inst_params = inst_params.toArray(array_inst_params);

        return array_inst_params; //sw, x2, x3 etc
    }
    
    public String GetImmOfOffset(String full_param)
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
        
        if (offset.contains("0x")) //if offset is hex
        {
            offset = offset.replace("0x", "");
            offset = String.valueOf(Convert.HexToDecimal(offset));
        }
        return offset;
    }
    
    public String GetTargetOffsetRegister(String full_param)
    {
        //check for word name instead
        String[] pre_offset_params = full_param.split("\\("); //this removes the ( in (x8) <-- example
        String target_register_name;
        
        if (pre_offset_params.length > 1)
        {
            target_register_name = pre_offset_params[1]; //we should be able to get the offset address
            target_register_name = target_register_name.replace(")", ""); // we will remove the closing parenthesis from x8)
        }
        else
        {
            target_register_name = pre_offset_params[0];
        }
               
//        String target_hex_value = GetRegisterHexValueFromRegisterName(target_register_name);
//        int[] target_binary = Convert.HexaToBinary(target_hex_value);
        
        return target_register_name;
    }
}
