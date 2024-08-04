/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

 
import entity.BloodImage;
import entity.BloodImageCollection;
import entity.NetworkSetting;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane; 
import ui.FormPelatihan;
import util.DataPrinter;
import util.NetworkWriter;

public class NeuralNetwork implements Runnable
{
      private FormPelatihan frmTrain;
      private BackPropagation bp;
      private BloodImageCollection bic;
      private NetworkSetting ns;
         NetRBF rbftrainer;
   
    
      public NeuralNetwork(BloodImageCollection bic,FormPelatihan frmTrainANN,NetworkSetting ns )
      {
         this.bic = bic;
         this.frmTrain = frmTrainANN;
         this.ns = ns;
         rbftrainer = new NetRBF(ns.getNneuron_input(), ns.getNNeuronHidden(), 1) ;
      }   
    
    
    private double[][] createInputs()
    {                 
        double[][] temp = new double[bic.getTotalImages()][bic.getBloodImage(0).getWidth()];        
       // System.out.println("Panjang Fitur:" + bic.getCockImage(0).toOneDimensionalPixelReal().length);      
        for(int i=0;i<temp.length;i++)
        { 
               BloodImage bi = bic.getBloodImage(i); 
    
               PreProcessor pp = new PreProcessor();
               pp.grayscale(bi);             
               pp.binerize(bi);         
        
               double inps[]  =  HistogramExtractor.GetHistogramVector(bi);
               temp[i] = inps;       
               
         
        }
        
        
        return temp;
    }
    
     private int[][] createTargets()
     {
        int t[][] = new int[bic.getTotalImages()][ns.getNNeuronOutput()];
        for(int i=0;i<bic.getTotalImages() ;i++ )
        {           
            int target =bic.getTarget(i);
            t[i] = toTargetUnits(target, ns.getNNeuronOutput());         
        }
        return t;
    }
       
    private int[] toTargetUnits(int target,int nOutput)
    {
        int unittarget [] = new int[nOutput]; 
        
         String biner = Integer.toBinaryString(target);
         
       // String bintarget 
         int akhir = nOutput-biner.length();
         for(int i=0;i<akhir;i++)
         {
             unittarget[i] = 0;
         }
         
         for(int i=akhir;i<akhir+biner.length();i++)
         {
              unittarget[i] = Integer.parseInt(biner.substring(i-akhir,(i-akhir)+1));                                    //biner.toCharArray()[i-akhir];
         }
        
        return unittarget;
        
    }
  
     public void train()
    { 
        new Thread(this).start(); 
       
    }

    @Override
    public void run() 
    {
        double err = ns.getErrTolerance();
        double maxepoch = ns.getMaxEpoch();
        bp = new BackPropagation( createInputs(), createTargets(),  ns.getNNeuronHidden(), ns.getNNeuronOutput(), ns.getLearningRate());
        int epoch =1;
        double MSE=0;
        bp.initBobot();
        System.out.println("==========INISIALISASI BOBOT==========");
        do{
            bp.resetPattern();
                    while(!bp.isLastPattern()) 
                    {   
                            // System.out.println("Pattern " + pattern );
                             bp.feedforward();
                             bp.countError();
                             bp.backPropagate();                  
                             bp.nextPattern();
                    }
           // }
            
            MSE = bp.getRMSE();
            System.out.println("************************* EPOCH Ke-" + String.valueOf(epoch) + " **********************");   
            System.out.println("MSE = " + MSE);
         
            frmTrain.setStatus(String.valueOf(MSE),String.valueOf(epoch));
            epoch++;
        }while (epoch<maxepoch && MSE>err);
        System.out.println("Berhenti Pada Epoch " + epoch);
        try 
       {
            Calendar cal = Calendar.getInstance();
            cal.setTime(Date.from(Instant.now()));

        
            String result = String.format(
                "file-%1$tY-%1$tm-%1$td-%1$tk-%1$tS-%1$tp", cal);

            String folder = "\\" + result;

            //NetworkWriter.writeSetting("setting", "setting", ns);
            NetworkWriter.writeBobotBias("bobot" + folder, "bobotbiashidden", bp.getBobotBiasV() );
            NetworkWriter.writeBobotBias("bobot" + folder, "bobotbiasoutput", bp.getBobotBiasW() );
            NetworkWriter.writeBobotHidden("bobot" + folder, "bobothidden", bp.getBobotWxi());
            NetworkWriter.writeBobotOutput("bobot" + folder, "bobotoutput", bp.getBobotWyi());
            JOptionPane.showMessageDialog(null, "Bobot Berhasil!","Simpan Hasil Training!",JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Training Selesai Dilakukan!");
        System.out.println("Bobot Berhasil Disimpan " + epoch);
    }
    
}
