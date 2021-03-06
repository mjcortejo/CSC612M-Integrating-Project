/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author mark
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    Pipeline pipeline;
    InstructionExtractor instruction_extractor;
    Opcode opcode;
    OutputPane outputpane;
    DefaultTableModel memory_table;
    DefaultTableModel program_table;
    DefaultTableModel pipeline_map_table;
    HashMap<String, Integer> register_alias_map;
    HashMap<String, int[]> data_segment_map;
    HashMap<Integer, int[]> address_location_map;
    
    SimpleAttributeSet attributeSet;
    
    Document textpane;
    
    HashMap<String, String[]> instruction_parse_map;
    
    public MainFrame() {
        initComponents();

        outputpane = new OutputPane(jTextOutput);
        
        
        data_segment_map = new HashMap<String, int[]>();
        address_location_map = new HashMap<Integer, int[]>();
        instruction_parse_map = new HashMap<String, String[]>();
        
        register_alias_map = new HashMap<String, Integer>() {{ //this is used when the instruction is invoking the alias name which will point to a row number (the integer value)
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
        pipeline = new Pipeline(jTableRegister, jTableProgram, jTablePipelineMap, jTablePipelineRegister, jTableMemory, register_alias_map);
        pipeline.outputpane = outputpane;
        
        PopulateDataSegmentAddress();
        
//        attributeSet = new SimpleAttributeSet();
//        
//        attributeSet = new SimpleAttributeSet();  
//        StyleConstants.setItalic(attributeSet, true);  
//        StyleConstants.setForeground(attributeSet, Color.BLACK);  
//        StyleConstants.setBackground(attributeSet, Color.white);  
//        
//        textpane = jTextOutput.getDocument();
        
    }
    
    /***
     * Populates the Data Segment addresses with default hex values
     */
    public void PopulateDataSegmentAddress()
    {
        this.memory_table = (DefaultTableModel)jTableMemory.getModel();
        for (int i = 0; i < 2048; i+=32)
        {
            Vector cell = new Vector(); //this stores the value of each 'cell' per address row
            int[] decimal_to_binary = Convert.IntDecimalToBinary(i, 12); //12 bits == 2048 (according to specs)
            String binary_to_hex = Convert.BinaryToHex(decimal_to_binary);
            cell.add(binary_to_hex); //this is just the label
            
            int address_row_location = 0;
            
            if (i % 32 == 0) address_row_location = i / 32;
            else address_row_location = (i + 1) / 32;
            
            for (int j = 0; j < 28; j+=4) //add half bytes
            {
                binary_to_hex = Convert.BinaryToHex(Convert.IntDecimalToBinary(0, 12));
                cell.add(binary_to_hex);
                String memory_hex = Convert.BinaryToHex(Convert.IntDecimalToBinary(i+j, 12));
                
                int address_col_location = 0;
                
                if (j % 4 == 0) address_col_location = j / 4;
                else address_col_location = (j+1) / 4;
                System.out.println(i+j);
                address_location_map.put(i+j, new int[]{ address_row_location, address_col_location + 1});
//                data_segment_map.put(memory_hex, binary_to_hex);
            }
            this.memory_table.addRow(cell);
        }
        
        for (int i = 0; i < 2048; i+=32) // to test memory cells
        {        
            int address_row_location = 0;
            
            if (i % 32 == 0) address_row_location = i / 32;
            else address_row_location = (i + 1) / 32;
            
            for (int j = 0; j < 28; j+=4) //add half bytes
            {
                int address_col_location = 0;
                
                if (j % 4 == 0) address_col_location = (j / 4) + 1;
                else address_col_location = ((j+1) / 4) + 1;
                this.memory_table.getValueAt(address_row_location, address_col_location);
            }
        }
        pipeline.address_location_map = address_location_map;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableRegister = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProgram = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableMemory = new javax.swing.JTable();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTablePipelineMap = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTablePipelineRegister = new javax.swing.JTable();
        jBtnRun = new javax.swing.JButton();
        jBtnNextLine = new javax.swing.JButton();
        jBtnPrevLine = new javax.swing.JButton();
        jBtnAssemble = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextOutput = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableRegister.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"x0", "zero", "00000000"},
                {"x1", "ra", "00000000"},
                {"x2", "sp", "00000000"},
                {"x3", "gp", "00000000"},
                {"x4", "tp", "00000000"},
                {"x5", "t0", "00000000"},
                {"x6", "t1", "00000000"},
                {"x7", "t2", "00000000"},
                {"x8", "s0", "00000000"},
                {"x9", "s1", "00000000"},
                {"x10", "a0", "00000000"},
                {"x11", "a1", "00000000"},
                {"x12", "a2", "00000000"},
                {"x13", "a3", "00000000"},
                {"x14", "a4", "00000000"},
                {"x15", "a5", "00000000"},
                {"x16", "a6", "00000000"},
                {"x17", "a7", "00000000"},
                {"x18", "s2", "00000000"},
                {"x19", "s3", "00000000"},
                {"x20", "s4", "00000000"},
                {"x21", "s5", "00000000"},
                {"x22", "s6", "00000000"},
                {"x23", "s7", "00000000"},
                {"x24", "s8", "00000000"},
                {"x25", "s9", "00000000"},
                {"x26", "s10", "00000000"},
                {"x27", "s11", "00000000"},
                {"x28", "t3", "00000000"},
                {"x29", "t4", "00000000"},
                {"x30", "t5", "00000000"},
                {"x31", "t6", "00000000"},
                {null, "pc", "0x00000000"}
            },
            new String [] {
                "Name", "Alias", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableRegister.setColumnSelectionAllowed(true);
        jTableRegister.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableRegister);
        jTableRegister.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTableRegister.getColumnModel().getColumnCount() > 0) {
            jTableRegister.getColumnModel().getColumn(0).setResizable(false);
            jTableRegister.getColumnModel().getColumn(1).setResizable(false);
            jTableRegister.getColumnModel().getColumn(2).setResizable(false);
        }

        jTabbedPane1.addTab("Registers", jScrollPane2);

        jScrollPane4.setViewportView(jEditorPane1);

        jTabbedPane2.addTab("Source Code", jScrollPane4);

        jTableProgram.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Address", "Opcode", "Instruction", "Stage"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableProgram);
        if (jTableProgram.getColumnModel().getColumnCount() > 0) {
            jTableProgram.getColumnModel().getColumn(0).setPreferredWidth(1);
        }

        jTabbedPane3.addTab("Program", jScrollPane1);

        jTableMemory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Address", "Value (+0)", "Value (+4)", "Value (+8)", "Value (+10)", "Value (+14)", "Value (+18)", "Value (+1c)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMemory.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTableMemory);
        if (jTableMemory.getColumnModel().getColumnCount() > 0) {
            jTableMemory.getColumnModel().getColumn(0).setResizable(false);
            jTableMemory.getColumnModel().getColumn(1).setResizable(false);
            jTableMemory.getColumnModel().getColumn(2).setResizable(false);
            jTableMemory.getColumnModel().getColumn(3).setResizable(false);
            jTableMemory.getColumnModel().getColumn(4).setResizable(false);
            jTableMemory.getColumnModel().getColumn(5).setResizable(false);
            jTableMemory.getColumnModel().getColumn(6).setResizable(false);
            jTableMemory.getColumnModel().getColumn(7).setResizable(false);
        }

        jTabbedPane3.addTab("Memory", jScrollPane3);

        jTabbedPane2.addTab("Execution", jTabbedPane3);

        jTablePipelineMap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instruction", "Address"
            }
        ));
        jScrollPane6.setViewportView(jTablePipelineMap);

        jTabbedPane4.addTab("Map", jScrollPane6);

        jTablePipelineRegister.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"IF/ID.IR", "00000000"},
                {"IF/ID.NPC", "00001000"},
                {"PC", "00001000"},
                {"ID/EX.A", "00000000"},
                {"ID/EX.B", "00000000"},
                {"ID/EX.IMM", "00000000"},
                {"ID/EX.IR", "00000000"},
                {"ID/EX.NPC", "00000000"},
                {"EX/MEM.ALUoutput", "00000000"},
                {"EX/MEM.IR", "00000000"},
                {"EX/MEM.B", "00000000"},
                {"EX/MEM.COND", "00000000"},
                {"MEM/WB.LMD", "00000000"},
                {"MEM/WB.IR", "00000000"},
                {"MEM/WB.ALUoutput", "00000000"},
                {"MEM[EX/MEM.ALUoutput]", "00000000"}
            },
            new String [] {
                "Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTablePipelineRegister);

        jTabbedPane4.addTab("Registers", jScrollPane8);

        jTabbedPane2.addTab("Pipelining", jTabbedPane4);

        jBtnRun.setText("Run");
        jBtnRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRunActionPerformed(evt);
            }
        });

        jBtnNextLine.setText("Next");
        jBtnNextLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNextLineActionPerformed(evt);
            }
        });

        jBtnPrevLine.setText("Prev");
        jBtnPrevLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPrevLineActionPerformed(evt);
            }
        });

        jBtnAssemble.setText("Assemble");
        jBtnAssemble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAssembleActionPerformed(evt);
            }
        });

        jLabel1.setText("Output");

        jScrollPane5.setViewportView(jTextOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jBtnAssemble, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jBtnPrevLine)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBtnRun)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBtnNextLine))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBtnAssemble)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnRun)
                    .addComponent(jBtnNextLine)
                    .addComponent(jBtnPrevLine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        /**
         * This methods loads all the instruction lines in memory, and also checks for errors
         * @param evt 
         */
    private void jBtnAssembleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAssembleActionPerformed
        // TODO add your handling code here:
        try
        {
            lines = jEditorPane1.getText().split("\n");
        
            this.memory_table = (DefaultTableModel)jTableMemory.getModel();

            int current_parse_line = 0;
            int sourcecode_section_state = 0;

            if (sourcecode_section_state == 0)//initial state
            {
                for (int i = 0; i < lines.length; i++)
                {
                    String current = lines[i];
                    if(current.contains(".data"))
                    {
                        sourcecode_section_state = 1;
                        current_parse_line = i;
                        break;
                    }
                    else if (current.contains(".text"))
                    {
                        sourcecode_section_state = 2;
                        current_parse_line = i;
                    }
                }
            }

            int current_memory_row = 0;
            int current_memory_col = 1;

            String pattern = "(\\w+:) (.\\w+) (0x[a-z0-9]+|\\d+)";
            Pattern variable_pattern = Pattern.compile(pattern);

            if (sourcecode_section_state == 1) //currently in .data section
            {
                for (int i = current_parse_line; i < lines.length; i++)
                {
                    String current = lines[i];
                    if(current.contains(".text"))
                    {
                        sourcecode_section_state = 2;
                        current_parse_line = i;
                        break;
                    }

                    Matcher m = variable_pattern.matcher(current);

                    if(m.find())
                    {
                        try
                        {
                            String var_name = m.group(1);
                            String data_type = m.group(2);
                            String value = m.group(3);
                            int value_int = ExtractImmediateValueToDecimal(value);

                            if (current_memory_col > 7)
                            {
                                current_memory_row++;
                                current_memory_col = 1;
                            }
                            data_segment_map.put(var_name.replace(":", ""), new int[]{current_memory_row, current_memory_col, value_int});
                            current_memory_col++;
                        }
                        catch(Exception ex)
                        {
                            outputpane.Print("Invalid line at "+(current_parse_line + 1) + " with instruction "+lines[i]);
                        }
                    }
                    current_parse_line = i;

                }

                for (Map.Entry<String, int[]> pair: data_segment_map.entrySet())
                {
                    int row = pair.getValue()[0];
                    int column = pair.getValue()[1];
                    int value = pair.getValue()[2];
                    int[] data_value_binary = Convert.IntDecimalToBinary(value, 32);
                    String data_value_hex = Convert.BinaryToHex(data_value_binary);
                    memory_table.setValueAt(data_value_hex, row, column);
                }
                sourcecode_section_state = 2;
            }

            if (sourcecode_section_state == 2)//final state
            {
                pipeline.data_segment_map = data_segment_map;
                PopulateProgramTextSegmentAddress(current_parse_line);
            }
            outputpane.Print("Finish Compiling");
        }
        catch(Exception ex)
        {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            outputpane.Print(ex.getMessage()); 
        }
        
        
    }//GEN-LAST:event_jBtnAssembleActionPerformed

    public static int ExtractImmediateValueToDecimal(String imm_value)
    {
        int int_value = 0;
        if (imm_value.contains("0x"))
        {
            imm_value = imm_value.replace("0x", "");
            int_value = Convert.HexToDecimal(imm_value);
        }
        else
        {
            int_value = Integer.parseInt(imm_value);
        }
        return int_value;
    }

    /***
     * Populates the jTableProgram table from the source code
     * @param current_parse_line 
     */
    public void PopulateProgramTextSegmentAddress(int current_parse_line)
    {
        opcode = new Opcode(jTableRegister, jTableProgram, jTableMemory, data_segment_map);
        opcode.outputpane = outputpane;
        instruction_extractor = new InstructionExtractor(jTableProgram, data_segment_map);
        instruction_extractor.outputpane = outputpane;
        lines = jEditorPane1.getText().split("\n");
        this.program_table = (DefaultTableModel)jTableProgram.getModel();
        this.pipeline_map_table = (DefaultTableModel)jTablePipelineMap.getModel();
        for (int i = 4096, j = current_parse_line + 1; j < lines.length && i < 8192; i+=4, j++)
        {
            Vector cell = new Vector(); //this stores the value of each 'cell' per address row
            int[] decimal_to_binary = Convert.IntDecimalToBinary(i, 13); //13 bits == 4096 (according to specs)
            String binary_to_hex = Convert.BinaryToHex(decimal_to_binary);
            cell.add(binary_to_hex);
            cell.add(""); //empty opcodes column for now
            cell.add(lines[j]);
            
            Vector pipeline_cell = new Vector();
            pipeline_cell.add(binary_to_hex);
            pipeline_cell.add(lines[j]);
            
            this.program_table.addRow(cell);
            this.pipeline_map_table.addRow(pipeline_cell);
        }
                
        DefaultTableModel program_model = (DefaultTableModel)jTableProgram.getModel();
        
        instruction_parse_map = instruction_extractor.ExtractParams();
        
        //assign opcodes after loading the instructions in memory
        for (int i = current_parse_line + 1, j = 0; i < lines.length; i++, j++)
        {
            String full_opcode = opcode.GenerateOpcode(lines[i], j);
            program_model.setValueAt(full_opcode, j, 1);
        }
        
        pipeline.instruction_parse_map = instruction_parse_map;
        
        outputpane.Print("Finished Generating Opcodes");
    }
    private void jBtnNextLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNextLineActionPerformed
        try {
            // TODO add your handling code here:
            boolean cycling = ReadCurrentLine();
            
            if(!cycling)
            {
                outputpane.Print("No More Iterations");
            }
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            outputpane.Print(ex.getMessage());
        }
    }//GEN-LAST:event_jBtnNextLineActionPerformed

    /**
     * To be removed
     * @param evt 
     */
    private void jBtnPrevLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPrevLineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnPrevLineActionPerformed

    private void jBtnRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRunActionPerformed
        // TODO add your handling code here:
        boolean is_ok = true;
        
        while (is_ok)
        {
            try {
                boolean cycling = ReadCurrentLine();
                if (!cycling)
                {
                    is_ok = false;
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                outputpane.Print("Error at line "+(pipeline.currently_visited_program_number) + ex.getMessage());
                break;
            }
        }

    }//GEN-LAST:event_jBtnRunActionPerformed

    String[] lines;
    int current_line;
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    
    
    public boolean ReadCurrentLine() throws Exception
    {
            //parse or read line here
//        opcode.GenerateOpcode(lines[current_line], jTableRegister);
            return pipeline.Cycle();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAssemble;
    private javax.swing.JButton jBtnNextLine;
    private javax.swing.JButton jBtnPrevLine;
    private javax.swing.JButton jBtnRun;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTableMemory;
    private javax.swing.JTable jTablePipelineMap;
    private javax.swing.JTable jTablePipelineRegister;
    private javax.swing.JTable jTableProgram;
    private javax.swing.JTable jTableRegister;
    private javax.swing.JTextPane jTextOutput;
    // End of variables declaration//GEN-END:variables
}
