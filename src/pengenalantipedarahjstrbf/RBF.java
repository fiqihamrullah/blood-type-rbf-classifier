/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

import com.sun.org.apache.bcel.internal.generic.FMUL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Fiqih Amrullah
 */
public class RBF
{  
    private double input[][];
    private double centers[][];
    private double targets[][];
    private double weights[][];
    private double widths[];
    private double dweights[][];
    private double y[];
    private double z[];
    private int currentIterasi;
    private double learningrate;
    private double bias;
    private int currentpattern;
    private double mse;
    private int hiddenlayer;
    private int inputlayer;
     double err=0;
    public RBF(double learningrate )
    {    
       this.learningrate = learningrate;
       bias = 1.0;
       currentpattern =0;
       mse=0;
    }
    
    
    
    public void setInputsAndTargets(double inp[][],double target[][])
    {
           this.input = inp;
           inputlayer = inp[0].length;
           hiddenlayer = 7;//*inp[0].length;
           centers = new double[hiddenlayer][inp[0].length];
           this.targets = target;
           z = new double[target[0].length];        
           y = new double[hiddenlayer];
           weights = new double[y.length][targets[0].length];
           dweights =new double[y.length][targets[0].length];
           generateRandomWeights();
           generateCenterVectors();
    }
    
     public void setInputs(double inp[][],int nOutput)  
    {
           this.input = inp;
              inputlayer = inp[0].length;
            hiddenlayer = 7;//*3;
           centers = new double[hiddenlayer][inp[0].length];           
           z = new double[nOutput];        
           y = new double[hiddenlayer];
           this.targets = new double[1][nOutput];
           weights = new double[y.length][nOutput];
           dweights =new double[y.length][nOutput];
        try {
            loadBobot();
        } catch (IOException ex) {
            Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
        }
           generateCenterVectors();
    }
    
    private void loadBobot() throws IOException{
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("bobot/bobotw.txt"))); 
            String data="";
            int idxy=0;
            while((data=reader.readLine())!=null)
            {
              //  System.out.println(data);
                String wout[] = data.split(";");
                for(int i=0;i<wout.length;i++)
                {
                       weights[idxy][i] = Double.parseDouble(wout[i]);
                }
                idxy++;
            }
           /*
           for(int i=0;i<weights.length;i++){
              for(int j=0;j<weights[i].length;j++){
                  int r = random.nextInt(2);
                  if (r==0)
                  {
                       weights[i][j]=random.nextDouble()/2;
                  }else{
                        weights[i][j]=-1* random.nextDouble()/2;
                  }
                 
              }
          } */
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
     
    private void generateRandomWeights()
    {
       Random random = new  Random();
       
       for(int i=0;i<weights.length;i++){
           for(int j=0;j<weights[i].length;j++){
               int r = random.nextInt(2);
               if (r==0)
               {
                    weights[i][j]=random.nextDouble()/2;
               }else{
                     weights[i][j]=-1* random.nextDouble()/2;
               }
             // System.out.println( weights[i][j]);
           }
       }       
    }
    
   public final void setRBFCentersAndWidthsEqualSpacing(final double minPosition,
			final double maxPosition, final double volumeNeuronRBFWidth, final boolean useWideEdgeRBFs) {
		final int totalNumHiddenNeurons = hiddenlayer;

		final int dimensions = inputlayer;
		final double disMinMaxPosition = Math.abs(maxPosition - minPosition);

	 
		final int expectedSideLength = (int) Math.pow(totalNumHiddenNeurons,
				1.0 / dimensions);
		final double cmp = Math.pow(totalNumHiddenNeurons, 1.0 / dimensions);
 
		widths = new double[totalNumHiddenNeurons];

		for (int i = 0; i < totalNumHiddenNeurons; i++)
                {			 

			final int sideLength = expectedSideLength;			 
			int temp = i;			 
			for (int j = dimensions; j > 0; j--)
                        {
				/*centers[i][j - 1] = ((int) (temp / Math.pow(sideLength, j - 1)) * (disMinMaxPosition / (sideLength )))
						+ minPosition;
                               // JOptionPane.showMessageDialog(null, centers[i][j - 1] );
				temp = temp % (int) (Math.pow(sideLength, j - 1));*/
                            
                            centers[i][j-1] = randomize(-1,1);
                                  
			}
                        
                  
			 

			 
				widths[i] = volumeNeuronRBFWidth;
			 
		}

		 
	}  
   
   public static double randomize(final double min, final double max) {
		final double range = max - min;
		return (range * Math.random()) + min;
	}
    
    private void generateCenterVectors()
    {
        setRBFCentersAndWidthsEqualSpacing(-1, 1,(double) 2.0/hiddenlayer, false);
    }
    
    public boolean isAkhirPattern(){        
        return (currentpattern==input.length);
    }
    
    public void firstPattern()
    {
        currentpattern=0;
        mse=0;
        
    }
    public void nextPattern()
    {
        currentpattern++;
    }
    
    public void countY()
    {
        double squared=0.0;
        double tao =0;
        /*
        for(int m=0;m<centers.length;m++)
        {
           double sq=0.0; 
           int i=0;
           
           for(i=0;i<input.length;i++)
           {
            squared = (input[currentpattern][i] - centers[m][i]);
            sq += squared*squared;
           }
            tao = widths[i];        
            y[m] = Math.exp(-sq/(2*Math.pow(tao,2)));         
        }*/
        
        for(int m=0;m<centers.length;m++)
        {
           double sq=0.0; 
           int i=0;
           tao = widths[i];  
           for(i=0;i<input.length;i++)
           {
            squared = (input[currentpattern][i] - centers[m][i]);
            sq += (squared*squared)/(2*Math.pow(tao,2));
           }                  
            y[m] =0.5 * Math.exp(-sq);         
        }
        
    }
    
    public void countZ()
    {
        double sigmaZ=0;
        for(int j=0;j<targets[currentpattern].length;j++)
        {
             sigmaZ=0;
            for(int m=0;m<y.length;m++)
            {              
                sigmaZ += (weights[m][j]*y[m]);
               //  System.out.println("weights " + weights[m][j]);
            }
            // System.out.println(" SigmaZ: " + sigmaZ);
          //  z[j]=((double) 1/y.length)*sigmaZ;
             z[j] =(double) 1/(1+ Math.exp(-sigmaZ));
            //System.out.println(" Z: " + z[j]);
        }
     //   JOptionPane.showMessageDialog(null, "Siip");
    }
    
    public String compete(){
        String biner = "";
        for(int i=0;i<z.length;i++){
            if (z[i]>0.8){
                z[i]=1; 
            }else if (z[i] < 0.3) {
                z[i] = 0;
            }
            biner += String.valueOf((int)z[i]);
        }
        return biner;
    }
    
    public double countError()
    {
       err=0;
        for(int j=0;j<targets[currentpattern].length;j++)
        {
          //  System.out.println(targets[currentpattern][j]);
            double d = targets[currentpattern][j]-z[j];
            err += Math.pow(d,2);
            d *=z[j]*(1-z[j]);
            int ii=0;
            for(ii=0;ii<y.length;ii++)
            {
                dweights[ii][j] += learningrate * d * y[ii];
            }
              dweights[ii-1][j] += learningrate * d;
        }
        err = err/z.length;
        
       
       // JOptionPane.showMessageDialog(null, err);
        mse +=err;
       
        return mse/input.length;
    }
    
    public void updateBobot(){
        int M= y.length;
        int J =  z.length;
        double errQtarget = err;
        for(int m=0;m<M;m++)
        {
            for(int j=0;j<z.length;j++)
            {               
               // System.out.println("Bobot : " + weights[m][j]);
                weights[m][j] = weights[m][j] + dweights[m][j] ;
              //  System.out.println("Bobot Baru : " + weights[m][j]);
            }
        }
    }
    
    public double[][] getWeights(){
        return weights;
    }
    
    
}
