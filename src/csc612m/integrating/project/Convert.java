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
    
    /***
     * Converts string Hexadecimal to int Decimal
     * @param s
     * @return 
     */
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
    
    /***
     * Converts string Hexadecimal to int list Binary without num_bits parameter
     * @param hexa
     * @return 
     */
    public static int[] HexaToBinary(String hexa)
    {
        return HexaToBinary(hexa, 0);
    }
    
    /***
     * Converts string Hexadecimal to int list Binary
     * Accepts number of bits parameters if bits >5
     * @param hexa
     * @param num_bits
     * @return 
     */
    public static int[] HexaToBinary(String hexa, int num_bits)
    {
        if (num_bits == 0)
        {
            num_bits = 5;
        }
        int[] binary_val = new int[num_bits];
        
        if (!hexa.isEmpty())
        {
            int i = 1;
            int dec_num = HexToDecimal(hexa);

            if ( dec_num < 0)
            {
                dec_num =  0 - dec_num;
            }
            /* convert decimal to binary */        
            while(dec_num != 0)
            {
                binary_val[i++] = dec_num%2;
                dec_num = dec_num/2;
            }

        }
        return binary_val;
    }
    
    /***
     * Converts string Decimal to int list Binary
     * @param decimal
     * @return 
     */
    public static int[] DecimalToBinary(String decimal)
    {
        return DecimalToBinary(decimal, 0);
    }
    
    /***
     * Converts string Decimal to int list Binary
     * Accepts num_bits parameter for bits >5
     * @param decimal
     * @param num_bits
     * @return 
     */
    public static int[] DecimalToBinary(String decimal, int num_bits)
    {
        
//        if (Integer.parseInt(decimal) < 0)
//        {
//            decimal = String.valueOf(0 - Integer.parseInt(decimal));
//        }
        if(num_bits == 0)
        {
            num_bits = 5;
        }
        
        int[] binary_val = new int[num_bits];

        if (!decimal.isEmpty())
        {
            if (Integer.parseInt(decimal) >= 0)
            {
                int i = 0;
                int quot = Integer.parseInt(decimal);
                while(quot != 0)
                {
                    binary_val[i++] = quot%2;
                    quot = quot/2;
                }
            }
            else
            {
                int dec = Integer.parseInt(decimal);
                String result = Integer.toBinaryString(dec);
                for (int i = result.length() - 1, j = 0; j < num_bits; i--, j++)
                {
                    System.out.println(result.charAt(i));
                    binary_val[j++] = Integer.parseInt(String.valueOf(result.charAt(i)));
                }
            }
            
            //zero pad after
        }
        
        return binary_val;
    }
    /***
     * Converts integer Decimal to int list Binary
     * Also accepts number for bits of >5
     * @param decimal
     * @param num_bits
     * @return 
     */
    public static int[] IntDecimalToBinary(int decimal, int num_bits)
    {
        //temp
        
        if (decimal < 0)
        {
            decimal = 0 - decimal;
        }
        
        if(num_bits == 0)
        {
            num_bits = 5;
        }
        
        int[] binary_val = new int[num_bits];

        if (decimal >= 0)
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
            else
            {
                String result = Integer.toBinaryString(decimal);
                for (int i = result.length() - 1, j = 0; j < num_bits; i--, j++)
                {
                    System.out.println(result.charAt(i));
                    binary_val[j++] = Integer.parseInt(String.valueOf(result.charAt(i)));
                }
            }
        
        return binary_val;
    }
    
    /***
     * Converts int Decimal to int list Binary, without the num_bits parameter
     * @param decimal
     * @return 
     */
    
    public static int[] IntDecimalToBinary(int decimal)
    {
        return IntDecimalToBinary(decimal, 0);
    }
    
    public static String IntDecimalToHex(int decimal)
    {
        int[] binary = IntDecimalToBinary(decimal);
        return BinaryToHex(binary);
    }
    
    public static String IntDecimalToHex(int decimal, int num_bits)
    {
        int[] binary = IntDecimalToBinary(decimal, num_bits);
        return BinaryToHex(binary);
    }
    
    public static String DecimalToHex(String decimal)
    {
        int[] binary = DecimalToBinary(decimal);
        return BinaryToHex(binary);
    }
    
    public static String DecimalToHex(String decimal, int num_bits)
    {
        int[] binary = DecimalToBinary(decimal, num_bits);
        return BinaryToHex(binary);
    }
    
    public static int BinaryToDecimal(int[] binary)
    {
        int dec_value = 0;
        int current_bit = 1;
        for (int i = binary.length - 1; i >= 0; i--)
        {
            if (binary[i] == 1)
            {
                dec_value += current_bit;
            }
            current_bit = current_bit + current_bit;
        }
        
        return dec_value;
    }
    
    /***
     * Converts int list Binary to string Hexadecimal
     * @param binary
     * @return 
     */
    public static String BinaryToHex(int[] binary)
    {
        String bin_string = "";
        for (int bin : binary)
        {
            bin_string = bin + bin_string; //this properly reverses the binary string
        }
        BigInteger b = new BigInteger(bin_string, 2);
        String converted_hex = b.toString(16);
        String zero_pads = "";
        
        for (int i = 0; i <= 7 - converted_hex.length(); i++) // 8 bits - length of actual hex
        {
            zero_pads += "0";
        }
        return zero_pads + converted_hex;
    }
}
