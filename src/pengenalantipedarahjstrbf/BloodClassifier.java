/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

 
import entity.BloodImage;
import entity.NetworkSetting;
import ui.FormPelatihan;


public class BloodClassifier 
{
      private FormPelatihan frmTrain;
      private BackPropagation bp;
      private BloodImage bloodimageuji;
      private NetworkSetting ns;
      private String tipetepi;
      private String strbobotpath;
    
      public BloodClassifier(BloodImage bloodimageuji,String strbobotpath )
      {       
        this.bloodimageuji = bloodimageuji;     
        this.strbobotpath = strbobotpath;
        
      }   
   
    
      private double[][] createInputs()
      {            
           double[][] temp = new double[1][bloodimageuji.getWidth()];
           for(int i=0;i<temp.length;i++)
           { 
                   PreProcessor pp = new PreProcessor();
                   pp.grayscale(bloodimageuji);             
                  // pp.binerize(bloodimageuji);         

                   double inps[]  =  HistogramExtractor.GetHistogramVector(bloodimageuji);
                   temp[i] = inps;                     

           }
           return temp;
        }
    
   public String GetID()
    {
        String id= "";
        BackPropagation bp = new BackPropagation(strbobotpath, createInputs(),  20, 1);
        bp.feedforward();
        id = bp.compete();
        System.out.println(id);
        return id;     
    }
  
}
