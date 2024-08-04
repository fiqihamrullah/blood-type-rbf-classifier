/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

import entity.BloodImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Fiqih Amrullah
 */
public class PreProcessor 
{

    public PreProcessor() 
    {
    }
    
    public  static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) 
    {        
       BufferedImage dbi = null;
       if(sbi != null) 
       {
           dbi = new BufferedImage(dWidth, dHeight, imageType);
           Graphics2D g = dbi.createGraphics();
           AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
           g.drawRenderedImage(sbi, at);
        }
        return dbi;
     }
    
     public static BufferedImage resizeImageWithHint(BufferedImage originalImage){
         
       int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	BufferedImage resizedImage = new BufferedImage(100, 100, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, 100, 100, null);
	g.dispose();	
	g.setComposite(AlphaComposite.Src);
 
	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);
 
	return resizedImage;
    }	
    
    
    public void grayscale(BloodImage hs)
    {
        int height = hs.getHeight();
        int width =  hs.getWidth();
        for(int baris=0;baris<height;baris++)
        {
           for(int kolom=0;kolom<width;kolom++)
           {
                    int pxl =  hs.getPixel(baris, kolom);                                       
                    Color c = new Color(pxl);
                    int merah = c.getRed();
                    int hijau = c.getGreen();
                    int biru = c.getBlue();  
                    int gray = (merah+hijau+biru)/3 ;
                    hs.setPixelOutput(baris, kolom, gray);
           }
       }
    }
    
    
    
    
  
   
  
    
    public void binerize(BloodImage nbi)
    {
        int src[]= nbi.toOneDimensionalPixelOutput();
        int out[] = new int[src.length];
        OtsuThresholder thresholder = new OtsuThresholder();
        int threshold = thresholder.doThreshold(src, out);
        for(int baris=0;baris<nbi.getHeight();baris++)
        {
           for(int kolom=0;kolom<nbi.getWidth();kolom++)
           {
                    int gray =  nbi.getPixelOutput(baris, kolom);                  
                   // System.out.println(threshold);
                    if (gray>=threshold)
                    {
                       nbi.setPixelOutput(baris, kolom, 0);
                    }else{
                       nbi.setPixelOutput(baris,kolom,255);
                    }                         

           }
       }
    }
    
   
}
