/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

import entity.BloodImage;

 
public class HistogramExtractor 
{
      public static double  GetHistogram(BloodImage bi)
      {
        double ratio=0.0d;
        int hist=0;
        for(int baris=0;baris<bi.getHeight();baris++)
        {
           for(int kolom=0;kolom<bi.getWidth();kolom++)
           {
               int val =  bi.getPixelOutput(baris, kolom);         
               if (val==255)
               {
                   hist++;
               }                  
           }
        }
          
        ratio = (double) hist/(bi.getHeight()*bi.getWidth());
        return ratio;
      }
      
      
      public static double[] GetHistogramVector(BloodImage bi)
      {       
        double[] vw = new double[bi.getWidth()];        
        for(int baris=0;baris<bi.getHeight();baris++)
        {
           for(int kolom=0;kolom<bi.getWidth();kolom++)
           {
               int val =  bi.getPixelOutput(baris, kolom);         
               
               if (val==255)
               {
                  vw[kolom]++;
               }                  
           }
        }
        
        for(int w=0;w<vw.length;w++)
        {
            vw[w] = vw[w] / bi.getWidth() ;
        }
        
        return vw;
      }
      
}
