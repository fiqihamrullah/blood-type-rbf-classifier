/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

import entity.BloodImage;

/**
 *
 * @author Fiqih Amrullah
 */
public class PrewittEdgeDetector 
{
     
    private double prewittX (int i, int j,BloodImage img)
    {
        int Sx[][] =new int[][] {{-1,0,1},{-1,0,1},{-1,0,1}};
	int n,m;
        float jumlah = 0;
        for(int x=-1;x<=1;x++)
        {
             for(int y=-1;y<=1;y++)
             {
                 n = x+1;
                 m = y+1;
		jumlah += (img.getPixelOutput(y+j,x+i)*Sx[m][n]);              
            }
    }
    return (jumlah);
    }
    
    private float prewittY(int i, int j, BloodImage img)
    {
	int Sy[][] = {{-1,-1,-1},{0,0,0},{1,1,1}};
	int m,n;    
        float jumlah= 0;
        for(int x=-1; x<=1; x++)
        {
           for(int y=-1; y<=1; y++)
           {
              m = x+1;
              n = y+1;
	      jumlah +=img.getPixelOutput(y+j,x+i)*Sy[n][m];
          }
        }
         return (jumlah);
     }
    
  
     public void detectEdge(BloodImage img)
    {  
        int height = img.getHeight();
        int width = img.getWidth(); 
        int max=0;
        int[][] pixeltemp =new int[height][width];
        System.out.println("MULAI PREWITT  ASLI");
        System.out.println("==========================================");
        for(int i=1;i<height-1;i++)
        {
            for (int j=1;j<width-1;j++)
            {
                    double px =prewittX(j,i,img); 
                    double py =prewittY(j,i,img); 
                    int g=  (int)  Math.sqrt(Math.pow(py,2) + Math.pow(px,2));   
                   
                    //if (j < 21 && i < 21)
                      // System.out.print(g + "\t");
                    
                    pixeltemp[i][j] =g;
                    if(max<g)max=g;
            }
           // if (i<21)
             //  System.out.println("");
         }
        
        
         System.out.println("Max Gradient Prewitt : " + max);
        
         double rasio =(double) max/255; 
         for(int i=1;i<height-1;i++)
         {
                for (int j=1;j<width-1;j++)
                {	  
                    double nbaru = pixeltemp[i][j]/rasio;	 
                    img.setPixelOutput(i, j,(int) nbaru);
                }
         } 
    }
}