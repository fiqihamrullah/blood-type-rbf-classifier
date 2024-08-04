/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.BloodImage;

 

/**
 *
 * @author Fiqih Amrullah
 */
public class DataPrinter
{
    public static int IMG_ORIGINAL=0;
    public static int IMG_MODIFIED=1;
    public static int IMG_BINARY=2;
    
    public static void printMatrix(String label,int[][] data) 
    {
        System.out.println(label);
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                System.out.print(data[i][j] + "\t");
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println();
    }
    
    public static void printMatrix(String label,int[][] data,int n) 
    {
        System.out.println(label);
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                
                if (j < n) 
                {
                  System.out.print(data[i][j] + "\t");
                }else {
                    break;
                }
            }
            if (i < n) 
            {
                 System.out.println();
            }else {
                 break;
            }
           
        }
        
        System.out.println();
        System.out.println();
    }
    
    
     public static void printMatrix(String label,double[][] data) 
    {
        System.out.println(label);
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                System.out.print(data[i][j] + "\t");
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println();
    }
    
    public static void printMatrix(String label,double[][] data,int n) 
    {
        System.out.println(label);
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                
                if (j < n) 
                {
                  System.out.print(data[i][j] + "\t");
                }else {
                    break;
                }
            }
            if (i < n) 
            {
                 System.out.println();
            }else {
                 break;
            }
           
        }
        
        System.out.println();
        System.out.println();
    }
    
     public static void printMatrix(String label,BloodImage img,int type) 
    {
        System.out.println(label);
        for(int i=0;i<img.getHeight();i++)
        {
            for(int j=0;j<img.getWidth();j++)
            {
                if (type==IMG_ORIGINAL) 
                {
                       int px = img.getPixel(i, j);
                       
                       int r = (px>>16) & 0xff;                     
                       int g = (px>>8) & 0xff;
                       int b = px & 0xff;
                       
                       int p =  (r<<16) | (g<<8) | b;
                       System.out.print(p + "[" + r + "," + g + "," + b + "]"  + "\t"); 
                   
                }else if (type==IMG_MODIFIED) 
                {
                   System.out.print(img.getPixelOutput(i, j) + "\t");
                } 
            }
            System.out.println();
        }
           System.out.println();
           System.out.println();
    }
    
    public static void printMatrix(String label,BloodImage img,int type,int n) 
    {
        System.out.println(label);
        for(int i=0;i<img.getHeight();i++)
        {
            for(int j=0;j<img.getWidth();j++)
            {
                
                if (j < n) 
                {
                    if (type==IMG_ORIGINAL) 
                    {
                       int px = img.getPixel(i, j);
                       
                       int r = (px>>16) & 0xff;                     
                       int g = (px>>8) & 0xff;
                       int b = px & 0xff;
                       
                       int p =  (r<<16) | (g<<8) | b;
                        
                       System.out.print(p + "[" + r + "," + g + "," + b + "]"  + "\t");                       
                         
                    }else if (type==IMG_MODIFIED) 
                    {
                       System.out.print(img.getPixelOutput(i, j) + "\t");
                    }          
                }else 
                {
                    break;
                }
            }
            if (i < n) 
            {
                 System.out.println();
            }else {
                 break;
            }
           
        }
        
        System.out.println();
           System.out.println();
    }
    
}
