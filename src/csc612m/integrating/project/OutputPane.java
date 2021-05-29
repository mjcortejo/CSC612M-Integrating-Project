/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author mark
 */
public class OutputPane {
    
    JTextPane textPane;
    SimpleAttributeSet attributeSet;
    Document textpane;
    
    public OutputPane(JTextPane textPane_param)
    {
        textPane = textPane_param;
        
        attributeSet = new SimpleAttributeSet();
        
        attributeSet = new SimpleAttributeSet();  
        StyleConstants.setItalic(attributeSet, true);  
        StyleConstants.setForeground(attributeSet, Color.BLACK);  
        StyleConstants.setBackground(attributeSet, Color.white);  
        
        textpane = textPane.getDocument();
    }
    
    
     public void Print(String s)
     {
        try {
            textpane.insertString(textpane.getLength(), s+"\n", attributeSet);
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
}
