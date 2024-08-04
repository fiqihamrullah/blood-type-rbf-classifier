/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import entity.BloodImage;
import entity.BloodImageCollection;
import entity.NetworkSetting;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;
import pengenalantipedarahjstrbf.ImageLoader;
import pengenalantipedarahjstrbf.NeuralNetwork;

import javax.swing.JLabel;
import pengenalantipedarahjstrbf.BloodClassifier;
import pengenalantipedarahjstrbf.HistogramExtractor;
import pengenalantipedarahjstrbf.HistogramProjection;
import pengenalantipedarahjstrbf.ImageViewer;
import pengenalantipedarahjstrbf.PreProcessor;
/**
 *
 * @author arifin haka
 */
public class Controller {
    public BloodImageCollection bic;
    private String ErrTolerance;
    private String MaxHidden;
    private String LearningRate;
    private String MaxEpoch;
    private FormPelatihan frmTrainANN;
    public BloodImage imgsamplesuji;
    public String hasil;
    public String GolDarah;
    public void setErrTolerance(String val){
        ErrTolerance=val;
    }
    public void setMaxHidden(String val){
        MaxHidden=val;
    }
    public void setLearningRate(String val){
        LearningRate=val;
    }
    public void setMaxEpoch(String val){
        MaxEpoch=val;
    }
    public void setfrmTrainANN(FormPelatihan val){
        frmTrainANN=val;
    }
    public void addBloodImage(BufferedImage img, String ffname){
        ImageIcon imgicon = new ImageIcon( img ); 
        ImageLoader imgLoad = new ImageLoader();
        imgLoad.loadPixelFrom(imgicon);
        bic.addBloodImage(imgLoad.getMyImage(),ffname);
    }
    public void viewImageLatih(int baris, JLabel jlblOriginalImage,JLabel jlblGrayImage,JLabel jlblPrewittResult){
        BloodImage bi = bic.getBloodImage(baris);
    
        ImageViewer imgV = new ImageViewer();
        imgV.setImage(bi);
        imgV.setViewer(jlblOriginalImage);
        imgV.viewImageDefault();

        PreProcessor pp = new PreProcessor();
        pp.grayscale(bi);

        imgV = new ImageViewer();
        imgV.setImage(bi);
        imgV.setViewer(jlblGrayImage);
        imgV.viewImageOutput();
  
        pp.binerize(bi);

        imgV.setImage(bi);
        imgV.setViewer(jlblPrewittResult);
        imgV.viewImageOutput();


        System.out.println("Ratio : " + HistogramExtractor.GetHistogram(bi));
    }
    public void pelatihan(){
        
        NetworkSetting ns = new NetworkSetting();
        ns.setErrTolerance(Double.parseDouble(ErrTolerance));
        ns.setNNeuronOutput(1);
        ns.setNNeuronHidden(Integer.parseInt(MaxHidden));
        ns.setLearningRate(Double.parseDouble(LearningRate));
        ns.setMaxEpoch(Integer.parseInt(MaxEpoch));  
        System.out.println("Training Siap Dimulai...");
        NeuralNetwork neuralnetwork = new  NeuralNetwork(bic, frmTrainANN ,ns );    
        neuralnetwork.train();
    } 
    
    public void pilihImageUji(BufferedImage img, JLabel jlblGambarUji, JLabel jlblGambarUjiBiner){
        ImageIcon imgicon = new ImageIcon(img); 
        ImageLoader pxlsLoad = new ImageLoader();
        pxlsLoad.readPixelsNormalFrom(imgicon);

        imgsamplesuji = pxlsLoad.getMyImage();

        ImageViewer imgviewer =  new ImageViewer();
        imgviewer.setImage(imgsamplesuji);
        imgviewer.setViewer(jlblGambarUji);
        imgviewer.viewImageDefault();

        PreProcessor pp = new PreProcessor();
        pp.grayscale(imgsamplesuji);

        pp.binerize(imgsamplesuji);

        imgviewer.setImage(imgsamplesuji);
        imgviewer.setViewer(jlblGambarUjiBiner);
        imgviewer.viewImageOutput();
    }
    public String pendeteksian(List<BloodImage> lstsegmentedImages, JLabel jlblCrop1, JLabel jlblCrop2, JLabel jlblCrop3,String strbobotpath)
    {
        
        HistogramProjection hp = new HistogramProjection("");
        lstsegmentedImages = hp.segmentingBloodsHorizontal(imgsamplesuji);         
         
        for(int l=0;l<lstsegmentedImages.size();l++)            
        {
            PreProcessor pp = new PreProcessor();
            pp.grayscale(lstsegmentedImages.get(l));
            
            BloodImage imgcrop = hp.segmentingLetterVertical(lstsegmentedImages.get(l),l);
            lstsegmentedImages.set(l, imgcrop);
        }
        System.out.println("Banyaknya Gambar  Yang Ditemukan " + String.valueOf(lstsegmentedImages.size()));
               
        hasil = "";
        String biner="";
        for(int i=0;i<lstsegmentedImages.size();i++) 
        {           
          BloodImage cti = lstsegmentedImages.get(i);
          
          ImageViewer imgV = new ImageViewer();
          imgV.setImage(cti);
          
          if (i==0) imgV.setViewer(jlblCrop1);
          else if (i==1) imgV.setViewer(jlblCrop2);
          else if (i==2) imgV.setViewer(jlblCrop3);
          
          imgV.viewImageDefault();          
          
          BloodClassifier bc = new BloodClassifier(cti,strbobotpath);
          String ID  = bc.GetID();
          hasil += " " + ID;    
          biner += ID;
          
        }    
      
        
        if (biner.equals("001") || biner.equals("000"))
        {
            GolDarah="O";
        }else if (biner.equals("101") || biner.equals("100"))
        {
            GolDarah="A";
        }else if (biner.equals("011") || biner.equals("010"))
        {
            GolDarah="B";
        }else if (biner.equals("111") || biner.equals("110"))
        {
            GolDarah="AB";
        }else{
              GolDarah="X";
        }
        return GolDarah;
    }
}
