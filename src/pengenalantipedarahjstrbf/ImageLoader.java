/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

 
import entity.BloodImage;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;

/**
 *
 * @author Fiqih Amrullah
 */
public class ImageLoader 
{
    BloodImage bli;
    public ImageLoader() 
    {
        bli = new BloodImage();
    }
    
    public void loadPixelFrom(ImageIcon imgic)
    {   
      
      PixelGrabber pxlgrabber = new PixelGrabber(imgic.getImage(),0,0,imgic.getIconWidth(), imgic.getIconHeight(),false);
      pxlgrabber.startGrabbing();
      
      int pixels[];
      int pixelasli[][];
      try
      {
        if(pxlgrabber.grabPixels())
        {
            pixels = (int[])pxlgrabber.getPixels();
            
            BufferedImage image = new BufferedImage(imgic.getIconWidth() , imgic.getIconHeight() , BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, imgic.getIconWidth() , imgic.getIconHeight() ,pixels, 0, imgic.getIconWidth());                
                     
            //Resize
            ImageIcon imgIcon = new ImageIcon(PreProcessor.resizeImageWithHint(image)); 
            
            
            bli = new BloodImage(imgIcon.getIconWidth(), imgIcon.getIconHeight());             
            pxlgrabber = new PixelGrabber(imgIcon.getImage(),0,0,bli.getWidth(), bli.getHeight(),false);
            
            pxlgrabber.startGrabbing();           
            
            if(pxlgrabber.grabPixels())
            {
            pixels = (int[])pxlgrabber.getPixels();
            pixelasli  = new int [bli.getHeight()][bli.getWidth()];
            int wpx = 0;
            int hpx = 0;
            for(int i =0;i<pixels.length;i++)
            {   
                 int pixel = pixels[i];                 
                 Color c = new Color(pixel);
                 int merah = c.getRed();
                 int hijau = c.getGreen();
                 int biru = c.getBlue();             
                 
                 pixelasli[hpx][wpx] = pixel;   
               //  hs.setPixelOutput(hpx, wpx, gray);
                 wpx++;
                 if (wpx==bli.getWidth())
                 {
                    wpx=0;
                    hpx++;

                }
            }         
       
            bli.setPixelAsli(pixelasli);          
           }
            //System.out.println("Sukses");
        }
    }catch(InterruptedException ex){}
    }
    
    public void loadPixelFrom(BufferedImage bi)
    {
     
         ImageIcon imgic = new ImageIcon(bi);
         
      
      PixelGrabber pxlgrabber = new PixelGrabber(imgic.getImage(),0,0,imgic.getIconWidth(), imgic.getIconHeight(),false);
      pxlgrabber.startGrabbing();
      
      int pixels[];
      int pixelasli[][];
      try
      {
        if(pxlgrabber.grabPixels())
        {
            pixels = (int[])pxlgrabber.getPixels();
            
            BufferedImage image = new BufferedImage(imgic.getIconWidth() , imgic.getIconHeight() , BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, imgic.getIconWidth() , imgic.getIconHeight() ,pixels, 0, imgic.getIconWidth());                
                     
            //Resize
            ImageIcon imgIcon = new ImageIcon(PreProcessor.resizeImageWithHint(image)); 
            
            
            bli = new BloodImage(imgIcon.getIconWidth(), imgIcon.getIconHeight());             
            pxlgrabber = new PixelGrabber(imgIcon.getImage(),0,0,bli.getWidth(), bli.getHeight(),false);
            
            pxlgrabber.startGrabbing();           
            
            if(pxlgrabber.grabPixels())
            {
            pixels = (int[])pxlgrabber.getPixels();
            pixelasli  = new int [bli.getHeight()][bli.getWidth()];
            int wpx = 0;
            int hpx = 0;
            for(int i =0;i<pixels.length;i++)
            {   
                 int pixel = pixels[i];                 
                 Color c = new Color(pixel);
                 int merah = c.getRed();
                 int hijau = c.getGreen();
                 int biru = c.getBlue();             
                 int gray = (merah+hijau+biru)/3 ; 
                 pixelasli[hpx][wpx] = pixel;   
               //  hs.setPixelOutput(hpx, wpx, gray);
                 wpx++;
                 if (wpx==bli.getWidth())
                 {
                    wpx=0;
                    hpx++;

                }
            }         
       
            bli.setPixelAsli(pixelasli);          
           }
            //System.out.println("Sukses");
        }
    }catch(InterruptedException ex){}
    }
    
       public void loadImageFrom(BloodImage imgbloods,BloodImage imgblood, Rectangle rectbound)
    {
        for(int i=rectbound.y;i<rectbound.y + rectbound.height;i++)
        {
            for(int j=rectbound.x;j<rectbound.x+rectbound.width;j++)
            {
                imgblood.setPixelOutput(i-rectbound.y, j-rectbound.x, imgbloods.getPixelOutput(i, j));
                imgblood.setPixel(i-rectbound.y, j-rectbound.x, imgbloods.getPixel(i, j));
                
            }            
        }
        
    }
    
    public void readPixelsNormalFrom(ImageIcon imgic)
    {
      bli = new BloodImage(imgic.getIconWidth(), imgic.getIconHeight()); 
      PixelGrabber pxlgrabber = new PixelGrabber(imgic.getImage(),0,0,bli.getWidth(), bli.getHeight(),false);
      pxlgrabber.startGrabbing();
      int pixels[];
      int pixelasli[][];
       
      try{
        if(pxlgrabber.grabPixels())
        {
            pixels = (int[])pxlgrabber.getPixels();
           
                
              pixelasli  = new int [bli.getHeight()][bli.getWidth()];
              int wpx = 0;
              int hpx = 0;
              for(int i =0;i<pixels.length;i++)
              {    
                 int pixel = pixels[i]; 
                 Color c = new Color(pixel);
                 int merah = c.getRed();
                 int hijau = c.getGreen();
                 int biru = c.getBlue();
                     
              
                 pixelasli[hpx][wpx] = pixel; 
               //  bli.setPixelOutput(hpx, wpx, gray);
             //   System.out.println(gray);
                 wpx++;
                 if (wpx==bli.getWidth())
                 {
                  //  System.out.println("");
                    wpx=0;
                    hpx++;
                 }
               }          
               bli.setPixelAsli(pixelasli);
         
           
            System.out.println("Sukses");
        }
    }catch(InterruptedException ex){}
    }

    public BloodImage getMyImage() {
        return bli;
    }
    
    
    
    
    
}
