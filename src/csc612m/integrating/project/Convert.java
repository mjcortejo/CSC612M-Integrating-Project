/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc612m.integrating.project;

import java.math.BigInteger;

/**
 *
 * @author mark
 */
public class Convert {
    
    public static int HexToDecimal(String s)
    {
             String digits = "0123456789ABCDEF";
             s = s.toUpperCase();
             int val = 0;
             for (int i = 0; i < s.length(); i++)
             {
                 char c = s.charAt(i);
                 int d = digits.indexOf(c);
                 val = 16*val + d;
             }
             return val;
    }
    
    public static int[] HexaToBinary(String hexa)
    {
        int[] binary_val = new int[5];
        
        if (hexa.isEmpty())
        {
            int i = 1;
            int dec_num = HexToDecimal(hexa);

            /* convert decimal to binary */        
            while(dec_num != 0)
            {
                binary_val[i++] = dec_num%2;
                dec_num = dec_num/2;
            }

        }
        return binary_val;
    }
    
    public static int[] DecimalToBinary(String decimal)
    {
        int[] binary_val = new int[5];

        if (!decimal.isEmpty())
        {
            int i = 0;
            int quot = Integer.parseInt(decimal);
            while(quot != 0)
            {
                binary_val[i++] = quot%2;
                quot = quot/2;
            }
            //zero pad after
        }
        
        return binary_val;
    }
    public static int[] IntDecimalToBinary(int decimal, int num_bits)
    {
        if(num_bits == 0)
        {
            num_bits = 5;
        }
        
        int[] binary_val = new int[num_bits];

        if (decimal != 0)
        {
            int i = 0;
            int quot = decimal;
            while(quot != 0)
            {
                binary_val[i++] = quot%2;
                quot = quot/2;
            }
            //zero pad after
        }
        
        return binary_val;
    }
    
    
    public static String BinaryToHex(int[] binary)
    {
        String bin_string = "";
        for (int bin : binary)
        {
            bin_string = bin + bin_string; //this properly reverses the binary string
        }
        System.out.println("Full binary opcode: " + bin_string);
        BigInteger b = new BigInteger(bin_string, 2);
        String converted_hex = b.toString(16);
        String zero_pads = "";
        
        for (int i = 0; i <= 7 - converted_hex.length(); i++) // 8 bits - length of actual hex
        {
            zero_pads += "0";
        }
        return zero_pads + converted_hex;
    }

    static void HexToDecimal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
