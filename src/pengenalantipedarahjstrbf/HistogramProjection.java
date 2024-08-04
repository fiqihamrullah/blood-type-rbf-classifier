/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

import entity.BloodImage;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Fiqih Amrullah
 */
public class HistogramProjection
{
    private int horiz[];
    private int vertz[];
    
    private String filename;

    public HistogramProjection(String filename) 
    {
        this.filename = filename;
    }
    
    public List<BloodImage> doSegmentedBlood(BloodImage imgbloods)
    {
        List<BloodImage> lstsegmentedImage = new ArrayList<BloodImage>();
        List<Rectangle> rects = new ArrayList<Rectangle>();
        horiz = new int[imgbloods.getWidth()];
        vertz = new int[imgbloods.getHeight()];
        
        for(int i=0;i<imgbloods.getHeight();i++)
        {
          for(int j=0;j<imgbloods.getWidth();j++)
          {
              //System.out.println(imglongletter.getPixelOutput(i, j));
                if (imgbloods.getPixelOutput(i, j)==0) 
                {
                    horiz[j]++;
                    vertz[i]++;                    
                }
          }            
        }
        
         
       
        for(int i=0;i<horiz.length;i++)
        {
            System.out.print(horiz[i] + "\t");
        }
        System.out.println();System.out.println();
        
        for(int i=0;i<vertz.length;i++)
        {
            System.out.print(vertz[i] + "\t");
        }
        System.out.println();System.out.println(); 
        
        Rectangle rect = new Rectangle(-1, -1, 0, 0);
        for(int i=0;i<horiz.length;i++)
        {  
           // System.out.print(horiz[i]);
            if (horiz[i]>5 && rect.x==-1)
            {
               // System.out.println("Masuk pada i=" + i);
               rect.x = i;
            }
            
            if (horiz[i]<1 && rect.x!=-1)
            {
                rect.width = i - rect.x;
                rects.add(rect);
               // System.out.println("X:" + rect.x + ",Width:" + rect.width);
                rect = new Rectangle(-1, -1, 0, 0);              
            } 
        }
        
        for(int i=0;i<vertz.length;i++)
        { 
            if (vertz[i]>3 && rect.y==-1)
            {
                rect.y=i;
            }
            
            if (vertz[i]<1 && rect.y!=-1)
            {
                rect.height=i- rect.y;
                break;
            }
        }
        
        for(int i=0;i<rects.size();i++)
        {
            rects.get(i).y = rect.y;
            rects.get(i).height = rect.height;
        }
        
        
        for(int i=0;i<rects.size();i++)
        {
           // try {
                Rectangle rectku = rects.get(i);
                BloodImage img = new BloodImage(rectku.width, rectku.height);
                ImageLoader imgloader = new ImageLoader();
                imgloader.loadImageFrom(imgbloods, img, rectku);
                img.SetName(imgbloods.getName());
                
                BufferedImage image = new BufferedImage(rectku.width ,  rectku.height , BufferedImage.TYPE_INT_RGB);
                image.setRGB(0, 0, rectku.width , rectku.height ,img.toOneDimensionalPixelOutput(), 0, rectku.width);
                
              //  ImageIO.write(image, "jpg", new File("croppedtemplates/" + filename + ".jpg"));
                
                ImageIcon imgIcon = new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_DEFAULT));
                imgloader.readPixelsNormalFrom(imgIcon);
                img = imgloader.getMyImage();
                lstsegmentedImage.add(img);
           // }
            // System.out.println("Y:" + rect.y + ",Height:" + rect.height);
            //catch (IOException ex) 
            //{
              //  Logger.getLogger(HistogramProjection.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
        return lstsegmentedImage;
    }
    
    public List<BloodImage> segmentingBloodsHorizontal(BloodImage imgbloods)
    {
        List<BloodImage> lstsegmentedImage = new ArrayList<BloodImage>();
        List<Rectangle> rects = new ArrayList<Rectangle>();
        horiz = new int[imgbloods.getWidth()];
        vertz = new int[imgbloods.getHeight()];
        
        for(int i=0;i<imgbloods.getHeight();i++)
        {
          for(int j=0;j<imgbloods.getWidth();j++)
          {
              //System.out.println(imglongletter.getPixelOutput(i, j));
                if (imgbloods.getPixelOutput(i, j)==255) 
                {
                    horiz[j]++;
                    vertz[i]++;                    
                }
          }            
        }        
         
       
        for(int i=0;i<horiz.length;i++)
        {
            System.out.print(horiz[i] + "\t");
        }
        System.out.println();System.out.println();
        
        for(int i=0;i<vertz.length;i++)
        {
            System.out.print(vertz[i] + "\t");
        }
        System.out.println();System.out.println(); 
        
        Rectangle rect = new Rectangle(-1, -1, 0, 0);
        for(int i=0;i<horiz.length;i++)
        {  
           // System.out.print(horiz[i]);
            if (horiz[i]>3 && rect.x==-1)
            {
               // System.out.println("Masuk pada i=" + i);
               rect.x = i;
            }
            
            if (horiz[i]<5 && rect.x!=-1)
            {
                rect.width = i - rect.x;
               
                if (rect.width>50) 
                {
                     System.out.println("Lebar:" + rect.width);
                   rects.add(rect);
                }
               // System.out.println("X:" + rect.x + ",Width:" + rect.width);
                rect = new Rectangle(-1, -1, 0, 0);              
            } 
        }
        
        for(int i=0;i<vertz.length;i++)
        { 
            if (vertz[i]>3 && rect.y==-1)
            {
                rect.y=i;
            }
            
            if (vertz[i]<1 && rect.y!=-1)
            {
                rect.height=i- rect.y;
                break;
            }
        }
        
        for(int i=0;i<rects.size();i++)
        {
            rects.get(i).y = 0;
            rects.get(i).height = imgbloods.getHeight();//  rect.height;
        }
        
        
        for(int i=0;i<rects.size();i++)
        {
           try {
                Rectangle rectku = rects.get(i);
                BloodImage img = new BloodImage(rectku.width, rectku.height);
                ImageLoader imgloader = new ImageLoader();
                imgloader.loadImageFrom(imgbloods, img, rectku);
                img.SetName(imgbloods.getName());
                
                BufferedImage image = new BufferedImage(rectku.width ,  rectku.height , BufferedImage.TYPE_INT_RGB);
                image.setRGB(0, 0, rectku.width , rectku.height ,img.toOneDimensionalPixelOutput(), 0, rectku.width);
                
                ImageIO.write(image, "jpg", new File("croppedtemplates/" + (i+1) + ".jpg"));
                
                ImageIcon imgIcon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                imgloader.readPixelsNormalFrom(imgIcon);
                img = imgloader.getMyImage();
                lstsegmentedImage.add(img);
           }
            // System.out.println("Y:" + rect.y + ",Height:" + rect.height);
            catch (IOException ex) 
            {
                 Logger.getLogger(HistogramProjection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lstsegmentedImage;
    }
    
     public BloodImage segmentingLetterVertical(BloodImage imgbloods, int ii)
    {
        BloodImage  lstsegmentedImage = new BloodImage();
        List<Rectangle> rects = new ArrayList<Rectangle>();
        horiz = new int[imgbloods.getWidth()];
        vertz = new int[imgbloods.getHeight()];
        
        for(int i=0;i<imgbloods.getHeight();i++)
        {
          for(int j=0;j<imgbloods.getWidth();j++)
          {
              //System.out.println(imgbloods.getPixelOutput(i, j));
                if (imgbloods.getPixelOutput(i, j)==255) 
                {
                    horiz[j]++;
                    vertz[i]++;                    
                }
          }            
        }
        
         
       
        for(int i=0;i<horiz.length;i++)
        {
            System.out.print(horiz[i] + "\t");
        }
        System.out.println();System.out.println();
        
        for(int i=0;i<vertz.length;i++)
        {
            System.out.print(vertz[i] + "\t");
        }
        System.out.println();System.out.println(); 
        
        Rectangle rect = new Rectangle(-1, -1, 0, 0);
        rect.x = 0;
        rect.width = imgbloods.getWidth(); 
        rects.add(rect);
        
        for(int i=0;i<vertz.length;i++)
        { 
            if (vertz[i]>3 && rect.y==-1)
            {
                rect.y=i;
            }
            
            if (vertz[i]<1 && rect.y!=-1)
            {
                rect.height=i- rect.y;
                if (rect.height>10) 
                {
                   break;
                }
            }
        }
        
        for(int i=0;i<rects.size();i++)
        {
            rects.get(i).y = rect.y;
            rects.get(i).height = rect.height;
        }
        
        
        
            try {
                Rectangle rectku = rects.get(0);
                BloodImage img = new BloodImage(rectku.width, rectku.height);
                ImageLoader imgloader = new ImageLoader();
                imgloader.loadImageFrom(imgbloods, img, rectku);
                img.SetName(imgbloods.getName());
                
                BufferedImage image = new BufferedImage(rectku.width ,  rectku.height , BufferedImage.TYPE_INT_RGB);
                image.setRGB(0, 0, rectku.width , rectku.height ,img.toOneDimensionalPixelOutput(), 0, rectku.width);
                
                ImageIO.write(image, "jpg", new File("croppedtemplates/x-" + (ii+1) + ".jpg"));
                
                ImageIcon imgIcon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                imgloader.readPixelsNormalFrom(imgIcon);
                img = imgloader.getMyImage();
                lstsegmentedImage = img;
            }
            // System.out.println("Y:" + rect.y + ",Height:" + rect.height);
            catch (IOException ex) 
            {
              //  Logger.getLogger(HistogramProjection.class.getName()).log(Level.SEVERE, null, ex);
           }
         
        return lstsegmentedImage;
    }
    
    
    
    
    
}
