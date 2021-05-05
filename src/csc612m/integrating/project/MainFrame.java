/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    Pipeline pipeline;
    DefaultTableModel register_table;
    HashMap<String, Integer> register_alias_map;
    
    public MainFrame() {
        initComponents();
        pipeline = new Pipeline();
        
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
        
        System.out.println("table has " +jTableRegister.getRowCount() + "rows");
        this.register_table = (DefaultTableModel)jTableRegister.getModel();
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
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
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
                {"x0", "zero", "0x00000000"},
                {"x1", "ra", "0x00000000"},
                {"x2", "sp", "0x00000000"},
                {"x3", "gp", "0x00000000"},
                {"x4", "tp", "0x00000000"},
                {"x5", "t0", "0x00000000"},
                {"x6", "t1", "0x00000000"},
                {"x7", "t2", "0x00000000"},
                {"x8", "s0", "0x00000000"},
                {"x9", "s1", "0x00000000"},
                {"x10", "a0", "0x00000000"},
                {"x11", "a1", "0x00000000"},
                {"x12", "a2", "0x00000000"},
                {"x13", "a3", "0x00000000"},
                {"x14", "a4", "0x00000000"},
                {"x15", "a5", "0x00000000"},
                {"x16", "a6", "0x00000000"},
                {"x17", "a7", "0x00000000"},
                {"x18", "s2", "0x00000000"},
                {"x19", "s3", "0x00000000"},
                {"x20", "s4", "0x00000000"},
                {"x21", "s5", "0x00000000"},
                {"x22", "s6", "0x00000000"},
                {"x23", "s7", "0x00000000"},
                {"x24", "s8", "0x00000000"},
                {"x25", "s9", "0x00000000"},
                {"x26", "s10", "0x00000000"},
                {"x27", "s11", "0x00000000"},
                {"x28", "t3", "0x00000000"},
                {"x29", "t4", "0x00000000"},
                {"x30", "t5", "0x00000000"},
                {"x31", "t6", "0x00000000"},
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

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Address", "Word", "Byte 0", "Byte 1", "Byte 2", "Byte 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            jTable2.getColumnModel().getColumn(5).setResizable(false);
        }

        jTabbedPane2.addTab("Memory", jScrollPane3);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1147, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBtnAssemble)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnRun)
                    .addComponent(jBtnNextLine)
                    .addComponent(jBtnPrevLine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        /**
         * This methods loads all the instruction lines in memory, and also checks for errors
         * @param evt 
         */
    private void jBtnAssembleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAssembleActionPerformed
        // TODO add your handling code here:
        lines = jEditorPane1.getText().split("\n");
        lines_length = lines.length;
        current_line = 0;
        System.out.println("COMPILED");
        System.out.println("Got "+lines_length + "lines");
    }//GEN-LAST:event_jBtnAssembleActionPerformed

    private void jBtnNextLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNextLineActionPerformed
        // TODO add your handling code here:
        if (current_line > lines_length)
        {
            System.out.println("End of text");
        }
        else
        {
            ReadCurrentLine();
            current_line++;   
        }
    }//GEN-LAST:event_jBtnNextLineActionPerformed

    /**
     * To be removed
     * @param evt 
     */
    private void jBtnPrevLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPrevLineActionPerformed
        // TODO add your handling code here:
        if (current_line < 0)
        {
            System.out.println("Beginning of text");
        }
        else
        {
            ReadCurrentLine();
            current_line--;
        }
    }//GEN-LAST:event_jBtnPrevLineActionPerformed

    private void jBtnRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRunActionPerformed
        // TODO add your handling code here:
        for (String line: lines)
        {
            ReadCurrentLine();
            current_line++;
        }
    }//GEN-LAST:event_jBtnRunActionPerformed

    String[] lines;
    int lines_length;
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
    
    
    
    public void ReadCurrentLine()
    {
        //parse or read line here
        System.out.println(lines[current_line]);
        pipeline.parse_line(lines[current_line], current_line, jTableRegister);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAssemble;
    private javax.swing.JButton jBtnNextLine;
    private javax.swing.JButton jBtnPrevLine;
    private javax.swing.JButton jBtnRun;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTableRegister;
    private javax.swing.JTextPane jTextOutput;
    // End of variables declaration//GEN-END:variables
}
