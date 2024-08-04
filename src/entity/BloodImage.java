/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.awt.Color;

/**
 *
 * @author Fiqih Amrullah
 */
public class BloodImage
{
  private int widthOri;
  private int heightOri;
  private int pixelasli[][];
  private int pixeloutput[][];
  private double realpixelasli[][];
  private double imgpixelasli[][];
 
  private String name;
  
  public BloodImage() 
  {
     
      
  }

  public void SetName(String name)
  {
      this.name = name;
  }
  
  public String getName()
  {
      return name;
  }
  
    public BloodImage(int widthOri, int heightOri) 
    {
        this.widthOri = widthOri;
        this.heightOri = heightOri;
        pixelasli = new int[heightOri][widthOri];
        pixeloutput = new int[heightOri][widthOri];
  
    }

    public void setPixelAsli(int[][] pixelasli) 
    {
        this.pixelasli = pixelasli;
    }

    public int[][] getPixelAsli() 
    {
        return pixelasli;
    }

    public int getHeight() 
    {
        return heightOri;
    }

    public int getWidth() {
        return widthOri;
    }

    public void setPixel(int h,int w,int pixel) {
        this.pixelasli[h][w] = pixel;
    }

    public int getPixel(int h,int w) 
    {
        return pixelasli[h][w];
    }
    
    public void setPixelOutput(int h,int w,int output)
    {
        pixeloutput[h][w]=output;
    }
    
    public int getPixelOutput(int h,int w)
    {
        return  pixeloutput[h][w];
    }
    
      public int[][] getPixelOutputs()
    {
        int[][] tempoutputs = new int[pixeloutput.length][pixeloutput[0].length];
        for(int i=0;i<tempoutputs.length;i++)
        {
            for(int j=0;j<tempoutputs[i].length;j++)
            {
                tempoutputs[i][j] = pixeloutput[i][j];
            }
        }
        return tempoutputs;
                
    }

    
    void setPixelReal(int h,int w,double realpixel)
    {
        realpixelasli[h][w] = realpixel;
    }
    
    double getPixelReal(int h,int w){
        return realpixelasli[h][w];
    }
    
     public static int[] OneDimensionalPixel(int[][] twodpixel){
       int[] onedpixel = new int[twodpixel.length*twodpixel[0].length];
       int i=0;
       for(int baris=0;baris<twodpixel.length;baris++){
           for(int kolom=0;kolom<twodpixel[0].length;kolom++){
                 onedpixel[i] =  twodpixel[baris][kolom];
                
                 i++;                 
           }
       }
       return onedpixel;
   }
    
 
     
    
    public static int[] OneDimensionalPixel(double[][] twodpixel){
       int[] onedpixel = new int[twodpixel.length*twodpixel[0].length];
       int i=0;
       for(int baris=0;baris<twodpixel.length;baris++){
           for(int kolom=0;kolom<twodpixel[0].length;kolom++){
               int pix = (int)twodpixel[baris][kolom];
               if (pix<0){
                   pix=255;
               }else if (pix > 255){
                   pix = 0;
               }
               Color c = new Color(pix,pix,pix,0);
               onedpixel[i] = c.getRGB();
               i++;

           }
       }
       return onedpixel;
   }
    
    
      public int[] toOneDimensionalPixelOutput(){
       int[] onedpixel = new int[pixeloutput.length*pixeloutput[0].length];
       int i=0;
       for(int baris=0;baris<pixeloutput.length;baris++)
       {
           for(int kolom=0;kolom<pixeloutput[0].length;kolom++)
           {
                 int gray = pixeloutput[baris][kolom];  
                     Color c = new Color(gray, gray, gray,0 );
                 onedpixel[i] =c.getRGB();
                 i++;

           }
       }
       return onedpixel;
   }
  
    
      public int[] toOneDimensionalPixelReal(){
       int[] onedpixel = new int[pixelasli.length*pixelasli[0].length];
       int i=0;
       for(int baris=0;baris<pixelasli.length;baris++){
           for(int kolom=0;kolom<pixelasli[0].length;kolom++){
                 int pix = pixelasli[baris][kolom]; 
                 Color c = new Color(pix );
                 onedpixel[i] =c.getRGB();
                 i++;

           }
       }
       return onedpixel;
   }
  
      
     public int[][] toOutput() 
     {
         int[][] onedpixel = new int[pixeloutput.length][pixeloutput[0].length];
         int i=0;
         for(int baris=0;baris<pixeloutput.length;baris++)
         {
            for(int kolom=0;kolom<pixeloutput[0].length;kolom++)
            {
                 int gray = pixeloutput[baris][kolom];  
                 if (gray==255){
                     onedpixel[baris][kolom] =1;
                      
                 }else {
                     onedpixel[baris][kolom] =0;
                 }
                 

           }
         }
       return onedpixel;
         
     }
      
     public double[] toOneDimensionalPixelDReal(){
       double[] onedpixel = new double[pixelasli.length*pixelasli[0].length];
       int i=0;
       for(int baris=0;baris<pixelasli.length;baris++)
       {
           for(int kolom=0;kolom<pixelasli[0].length;kolom++)
           {
                 double gray = pixeloutput[baris][kolom];                  
                 onedpixel[i] =gray;
                 i++;
                 if (gray>128 )
                 {
                     System.out.print("1" + "\t");
                 }else{
                     System.out.print("0" + "\t");
                 }
           }
           System.out.println();
       }
        System.out.println(); System.out.println();
        
       return onedpixel;
   }
     
   
 
     
 }
