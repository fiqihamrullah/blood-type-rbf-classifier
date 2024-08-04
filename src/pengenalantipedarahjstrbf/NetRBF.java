/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pengenalantipedarahjstrbf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ui.FormPelatihan;
 
public class NetRBF {
     
    
   private double DBL_EPSILON= 2.2204460492503131e-016;
    
   int               sizeinput;     // jumlah dimensi input
   int               sizeoutput;    // jumlah dimensi output
   int               sizehidden;    // jumlah neuron hidden layer
   int               maxhidden;     // maksimum jumlah hidden neuron
   int               totalinput;    // 
   int               sizeweight;    // 
   // input vector 
   double            inputvector[]; 
   // hidden layer 
   double            center[];      // center dengan sigma
   double            hout[];        // keluaran hidden layer 
   // output layer 
   double            weight[];      // bobot dengan bias 
   int               actfunc;       // 0 - none, 1 - sigmoid atau 2 - th 
   // temp arrays for learning 
   double            dEc[],dEw[]; 
   int               mepatt;        // pola dengan error tertinggi
   
  public double            mse;           // error jaringan
  public int               epoch;         // 
  public int               neurons;       // jumlah neuron di dalam hidden layer
 
  
  
  
  public   NetRBF(int nin,int nhid,int nout)
  {
     sizeinput=nin; 
     totalinput=sizeinput+1;//+sigma; 
     sizeoutput=nout; 
     sizehidden=0; 
     maxhidden=nhid; 
     sizeweight=maxhidden+1;//+bias 
     actfunc=-1; 
     inputvector = new double[sizeinput];
     center = new double[maxhidden*totalinput];
     weight = new double[sizeweight*sizeoutput];
     hout = new double[maxhidden];
   
  }
  //Mencari index Nilai Minimum di dalam Array
  private int ArrayMinimum(double x[]){
      double val=Double.MAX_VALUE;
      int idx =0;
      for (int i=0;i<x.length;i++)
      {
          if (val > x[i]){
              idx = i;
              val = x[i];
              
          }
      }
      return idx;
  }
  //Mencari index Nilai Maksimum di dalam Array
    private int ArrayMaximum(double x[]){
      double val=0;
      int idx =0;
      for (int i=0;i<x.length;i++)
      {
          if (val < x[i]){
              idx = i;
              val = x[i];
              
          }
      }
      return idx;
  }
  
  private double SETSIGN(double x,double b)
  {
      return (b>=0)?Math.abs(x):-1*Math.abs(x);
  }
    
    
  private int GETSIGN(double x){
      return (x<0.0)?-1:1;
  }
  
   private   void ArrayInitialize(double arr[],double n)
    {
        for(int i=0;i<arr.length;i++)
        {
            arr[i]=n;
        }
    }
   
    private   void ArrayCopy(double arrdest[],double arrsrc[])
    {
        System.arraycopy(arrsrc, 0, arrdest, 0, arrsrc.length);
    }
   
   private   void ArrayInitialize(int arr[],int n)
    {
        for(int i=0;i<arr.length;i++)
        {
            arr[i]=n;
        }
    }
   //Menghitung Input dan Output
  public void Calculate( double in[],double out[])
  {
     if(actfunc<0) 
     { 
         
          ArrayInitialize(out,0.0); 
      return; 
     } 
     
   System.arraycopy(in, 0, inputvector, 0, sizeinput);   
   Calculate(out); 

  } 
  
  public int  Learn(FormPelatihan ftrain,int npat, double inp[], double res[],int nep,double err)
  {
     int i,ii,j,jj,k,hc; 
     int ce=0,cer=0,bestepoch=0,iw=0,addepoch=0,cnt=0; 
     int tsw=sizeweight*sizeoutput; 
     int tsc=totalinput*maxhidden; 
     double r,d,toler; 
     double dw[],dc[],copycenter[],copyweight[]; 
     int dwsign[],dcsign[]; 
     // errors 
     double pmse=0.0,maxerror,minerror=Double.MAX_VALUE; 
     // parameter default pembelajaran
     double minsigma=0.001; 
     double mindistance=1e-3; 
     double minadd=1e-6; 
     double d0=0.0125; 
     double dmin=0.0; 
     double dmax=50.0; 
     double plus=1.2; 
     double minus=0.5; 
     double minimprov=1e-7; 
      // mengalokasikan memory untuk array
     dwsign = new int[tsw];     
     dw = new double[tsw];    
     dcsign = new int[tsc];      
     dc = new double[tsc];    
     dEw = new double[tsw];  
     dEc =new double[tsc];     
     copyweight = new double[tsw];      
     copycenter = new double[tsc];      
     ArrayInitialize(dcsign,1); 
     ArrayInitialize(dc,d0); 
     ArrayInitialize(dwsign,1); 
     ArrayInitialize(dw,d0); 
     // menggunakan algoritma APC untuk menentukan hidden , atau center
    ftrain.appendLog("Center Vector Dibangkitkan------------------");
   
     if(APC(npat,inp)>maxhidden) return(-3);// membutuhkan lebih neuron hidden
       
   
   neurons=sizehidden; 
   hc=sizehidden; 
   double ds=center[totalinput-1];// sigma default 
   // memilih fungsi aktivasi untuk layer output
   double min=res[ArrayMinimum(res)]; 
   double max=res[ArrayMaximum(res)]; 
  
   if(max>1.0 || min<-1.0) actfunc=0;     // none 
   else if(min<0.0) actfunc=2;            // menggunakan th 
   else actfunc=1;                        // menggunakan sigmoid 
      ftrain.appendLog("Bobot Dibangkitkan------------------");
   // menggunakan algoritma SVD untuk menentukan bobot dari layer output
   SVD(npat,inp,res);    
    
   // update hidden dan output layer
   for(epoch=1; epoch<=nep; epoch++) 
     { 
       
      maxerror=0.0; 
      mse= CalculateError(npat,inp,res); 
      if (epoch%10==0)
       {
           ftrain.setStatus(String.valueOf(mse),String.valueOf(epoch));    
       }
      if(mse<minerror) 
        { 
         minerror=mse; 
         if(minerror<err) break; 
         bestepoch=epoch; 
         ce=0; 
         // menyimpan center dan weight terbaik
         ArrayCopy(copycenter,center); 
         ArrayCopy(copyweight,weight); 
         hc=sizehidden; 
        } 
      toler=pmse<=1.0?minimprov:minimprov*pmse; 
      if(Math.abs(pmse-mse)>toler) cnt=0; 
      else if(++cnt>5) break; // selesai jika ada sedikit sekali peningkatan
      // update layer output 
      for(i=0; i<tsw; i++) 
        { 
         d=dEw[i]*dwsign[i]; 
         if(d>0.0) 
           { 
            dw[i]=Math.min(dw[i]*plus,dmax); 
            dwsign[i]=GETSIGN(dEw[i]); 
            weight[i]-=dwsign[i]*dw[i]; 
           } 
         else if(d<0.0) 
           { 
            if(mse>pmse) weight[i]+=dwsign[i]*dw[i]; 
            dw[i]=Math.max(dw[i]*minus,dmin); 
            dwsign[i]=0; 
           } 
         else 
           { 
            dwsign[i]=GETSIGN(dEw[i]); 
            weight[i]-=dwsign[i]*dw[i]; 
           } 
        } 
      // update hidden layer  	
      for(i=0,ii=0; i<sizehidden; i++,ii+=totalinput) 
        { 
         for(j=0,jj=ii; j<totalinput; j++,jj++) 
           { 
            d=dEc[jj]*dcsign[jj]; 
            if(d>0.0) 
              { 
               dc[jj]=Math.min(dc[jj]*plus,dmax); 
               dcsign[jj]=GETSIGN(dEc[jj]); 
               center[jj]-=dcsign[jj]*dc[jj]; 
              } 
            else if(d<0.0) 
              { 
               if(mse>pmse) center[jj]+=dcsign[jj]*dc[jj]; 
               dc[jj]=Math.max(dc[jj]*minus,dmin); 
               dcsign[jj]=0; 
              } 
            else 
              { 
               dcsign[jj]=GETSIGN(dEc[jj]); 
               center[jj]-=dcsign[jj]*dc[jj]; 
              } 
           } 
         // jika sigma kurang dari zero
         if(center[jj-1]<=0) center[jj-1]=ds;// reset sigma 
                                             // jika sigma terlalu dekat
         if(center[jj-1]<minsigma) 
           {// menghapus neuron
            System.arraycopy(center, (sizehidden-1)*totalinput, center, ii, totalinput);      
            System.arraycopy(dc, (sizehidden-1)*totalinput, dc, ii, totalinput);     
            System.arraycopy(dEc, (sizehidden-1)*totalinput, dEc, ii, totalinput);                  
           
            for(j=0,jj=0; j<sizeoutput; j++,jj+=sizeweight) 
              { 
               weight[jj+i]=weight[jj+sizehidden-1]; 
               dw[jj+i]=dw[jj+sizehidden-1]; 
               dEw[jj+i]=dEw[jj+sizehidden-1]; 
              } 
            sizehidden--; 
            i--; 
           } 
        } 
      // jika dua hidden neuron terlalu dekat
      if(mindistance>0.0) 
        { 
         for(i=0,ii=0; i<sizehidden; i++,ii+=totalinput) 
            for(j=i+1,jj=j*totalinput; j<sizehidden; j++,jj+=totalinput) 
              { 
               d=0.0; 
               for(k=0; k<sizeinput; k++) 
                 { 
                  r=center[ii+k]-center[jj+k]; 
                  d+=r*r; 
                 } 
               if(Math.sqrt(d)<mindistance) 
                 {//  menghapus neuron yg terlalu dekat
                   System.arraycopy(center, (sizehidden-1)*totalinput, center, ii, totalinput);      
                   System.arraycopy(dc, (sizehidden-1)*totalinput, dc, ii, totalinput);      
                 System.arraycopy(dEc, (sizehidden-1)*totalinput, dEc, ii, totalinput);      
                 
                  for(j=0,jj=0; j<sizeoutput; j++,jj+=sizeweight) 
                    { 
                     weight[jj+i]=weight[jj+sizehidden-1]; 
                     dw[jj+i]=dw[jj+sizehidden-1]; 
                     dEw[jj+i]=dEw[jj+sizehidden-1]; 
                    } 
                  sizehidden--; 
                  i--; 
                  break; 
                 } 
              } 
        } 
      // ... menambahkan hidden neuron pada pola yg memiliki error terbesar
      if((++ce>20 || cnt>3) && sizehidden<maxhidden && minerror>minadd && epoch-addepoch>50) 
        { 
         addepoch=epoch; 
         ii=sizehidden*totalinput; 
          System.arraycopy(inp,  mepatt*sizeinput, center,ii, sizeinput);      
         
         center[ii+sizeinput]=ds; 
         for(i=0; i<sizeoutput; i++) weight[i*sizeweight+sizehidden]=res[mepatt*sizeoutput+i]; 
         sizehidden++; 
         ce=0; 
         cnt=0; 
        } 
      pmse=mse; 
     } 
   if(mse>minerror) 
     {// mengembalikan center dan bobot dari yg terbaik
      mse=minerror; 
      ArrayCopy(center,copycenter); 
      ArrayCopy(weight,copyweight); 
      sizehidden=hc; 
       ftrain.appendLog("Epoch " + String.valueOf(mse) + ",mse terbaik:" + String.valueOf(mse));  
     } 
   neurons=sizehidden; 
// 
  
  
   return(0); 
  } 
  public void Save() throws IOException
  {
      
        File newFile = new File("bobot/bobotdancenter.txt");
        FileWriter fw = new FileWriter(newFile);
        StringBuilder content= new StringBuilder();
        String bobot = "";
        content.append(String.valueOf(sizehidden)+ "\n");
        content.append(String.valueOf(actfunc) + "\n");
        content.append(String.valueOf(mse) + "\n");
        content.append(String.valueOf(center.length) + "\n");
              
        
        for(int i=0;i<center.length;i++) 
        {
              bobot += String.valueOf(center[i]) + ";";
        }
        content.append(bobot + "\n");
        content.append(String.valueOf(weight.length) + "\n");
        bobot="";
        for(int i=0;i<weight.length;i++) 
        {
              bobot += String.valueOf(weight[i]) + ";";
        }
        content.append(bobot + "\n");        
        
        fw.write(content.toString());
        fw.flush();
        fw.close();
       
  }
  
  public void Load()
  {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("bobot/bobotdancenter.txt")));
            String data="";
            sizehidden = Integer.parseInt(reader.readLine());
            neurons = sizehidden;
            actfunc = Integer.parseInt(reader.readLine());
            mse = Double.parseDouble(reader.readLine());
            //System.out.println(mse);
            
            center = new double[Integer.parseInt(reader.readLine())];
            data = reader.readLine();
            //System.out.println(data);
            String arrdata[] = data.split(";");
            for(int i=0;i<center.length;i++)
            {
                center[i] = Double.parseDouble(arrdata[i]);
            }
           // System.out.println(center[0]);
            
            weight = new double[Integer.parseInt(reader.readLine())];
            data = reader.readLine();
            //System.out.println(data);
            arrdata  = data.split(";");
            for(int i=0;i<weight.length;i++)
            {
                weight[i] = Double.parseDouble(arrdata[i]);
            }
            //System.out.println(weight[0]);
           // System.out.println(arrdata.length);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NetRBF.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (IOException ex) {
            Logger.getLogger(NetRBF.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(NetRBF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
  }
   
   
 protected   void  Calculate(double out[])
 {
    CalculateHiddenLayer(); 
    // System.out.println(out.length);
   for(int i=0,ii=0; i<sizeoutput; i++,ii+=sizeweight) 
     { 
      double d=-weight[ii+sizeweight-1];//bias 
      for(int j=0; j<sizehidden; j++) d+=weight[ii+j]*hout[j]; 
      if(actfunc==2) 
        {//th 
         if(d>20.0) d=1.0; 
         else if(d<-20.0) d=-1.0; 
         else 
           { 
            d=Math.exp(d+d); 
            d=(d-1.0)/(d+1.0); 
           } 
        } 
      else if(actfunc==1) d=d>40.0?1.0:d<-40.0?0.0:1.0/(1.0+Math.exp(-d));//sigmoid 
     
      out[i]=d; 
     } 
     
 }
 protected  void  CalculateHiddenLayer()
 {
     for(int i=0,ii=0; i<sizehidden; i++,ii+=totalinput) 
     { 
      int j=0; 
      double d,sum=0.0; 
      while(j<sizeinput) 
        { 
         d=inputvector[j]-center[ii+j++]; 
         sum+=d*d; 
        } 
      d=center[ii+j];//sigma 
      hout[i]=Math.exp(-sum/(2.0*d*d)); 
     } 
 }
 protected  double  CalculateError(int npat, double inp[], double res[])
 {
   int i,ii,j,jj; 
   double d,r,sse,maxerror=0.0,kt=1.0; 
   double target[],out[]; 
   out = new double[sizeoutput];
   target = new double[sizeoutput];
   for(int ix =0;ix<dEc.length;ix++)
   {
        dEc[ix]=0;
   }
  
   for(int ix =0;ix<dEw.length;ix++)
   {
        dEw[ix]=0;
   }
  
   
   mse=0.0; 
   for(int pat=0; pat<npat; pat++) 
     { 
      // memperoleh pola
     
      System.arraycopy(inp, pat*sizeinput, inputvector, 0, sizeinput);
      System.arraycopy(res, pat*sizeoutput, target, 0, sizeoutput);
      
      
      // menghitung keluaran dari pola 
      Calculate(out); 
      // menghitung error dan perubahan error
      sse=0.0; 
      for(i=0,ii=0; i<sizeoutput; i++,ii+=sizeweight) 
        { 
         target[i]=out[i]-target[i]; 
         d=target[i]; 
         sse+=d*d; 
         if(actfunc>0) d*=actfunc==2?1-out[i]*out[i]:out[i]*(1.0-out[i]);//th or sigmoid 
         d*=kt; 
         for(j=0,jj=ii; j<sizehidden; j++,jj++) dEw[jj]+=d*hout[j]; 
         dEw[ii+sizeweight-1]+=d;//dEdbias 
        } 
      for(i=0,ii=0; i<sizehidden; i++,ii+=totalinput) 
        { 
         double x,sum1=0.0,sum2,sum3=0.0; 
         for(j=0,jj=0; j<sizeoutput; j++,jj+=sizeweight) sum1+=target[j]*weight[jj+i]; 
         r=center[ii+sizeinput];//sigma 
         x=r*r; 
         sum2=kt*hout[i]*sum1/x; 
         for(j=0,jj=ii; j<sizeinput; j++,jj++) 
           { 
            d=inputvector[j]-center[jj]; 
            dEc[jj]+=sum2*d; 
            sum3+=d*d; 
           } 
         dEc[jj]+=kt*hout[i]*sum1*sum3/(x*r);//dEdsigma 
        } 
      if(sse>maxerror) 
        { 
         mepatt=pat; 
         maxerror=sse; 
        } 
      mse+=sse/2; 
     } 
   return(mse/npat); 
 }
 protected int  APC(int npat, double inp[])
 {
   int n,i,j,ii,jj, k=0; 
   double d, s, r, R0=Double.MAX_VALUE; 
   int nn[]; 
   double tc[]; 
   nn = new int[maxhidden];
   tc = new double[maxhidden*sizeinput];   
   // calculate R0 
   s=0.0; 
   for(i=0; i<npat-1; i++) 
      for(j=i+1; j<npat; j++) 
        { 
         d=0.0; 
         for(n=0; n<sizeinput; n++) 
           { 
            r=inp[i*sizeinput+n]-inp[j*sizeinput+n]; 
            d+=r*r; 
            
          //  System.out.println(r);
            
        //    System.out.println(inp[i*sizeinput+n] +  " - " + inp[j*sizeinput+n]);
           } 
         if(d<R0) 
           { 
            R0=d; 
            s+=d; 
            k++; 
           } 
        } 
   
   R0=s/(double)k; 
  // System.out.println("R0 " + R0);
   // menghitung center dari layer hidden
   n=1; 
   nn[0]=1; 
    
   System.arraycopy(inp, 0, tc, 0, sizeinput);
  
    
   for(i=1,ii=sizeinput; i<npat; i++,ii+=sizeinput) 
     { 
      for(j=0,jj=0; j<n; j++,jj+=sizeinput) 
        { 
         d=0.0; 
         for(k=0; k<sizeinput; k++) 
           { 
            r=inp[ii+k]-tc[jj+k]; 
            d+=r*r; 
        //    System.out.println(r);
           // System.out.println(d);
          // System.out.println(inp[ii+k] +  " - " + tc[jj+k]);
           } 
         if(d<=R0) 
           { 
                
            for(k=0; k<sizeinput; k++) 
               tc[jj+k]=(tc[jj+k]*nn[j]+inp[ii+k])/(nn[j]+1); 
            nn[j]++; 
            break; 
           } 
        } 
      if(j==n) 
        { 
         
         n++; 
         k=nn.length;
         if(n==k) 
           { 
            k+=10; 
            nn = new int[k];
            tc = new double[k*sizeinput] ;
            
           } 
         nn[n-1]=1; 
         System.arraycopy(inp, ii, tc, (n-1)*sizeinput, sizeinput);
          
        } 
     } 
   if(n>maxhidden) 
     { 
      if(n==npat) return(n); 
      else     return(APC(n,tc)); 
     } 
   sizehidden=n; 
   double max=0.0, min=Double.MAX_VALUE; 
   for(i=0; i<n-1; i++) 
      for(j=i+1; j<n; j++) 
        { 
         s=0.0; 
         for(k=0; k<sizeinput; k++) 
           { 
            r=tc[i*sizeinput+k]-tc[j*sizeinput+k]; 
            s+=r*r; 
           } 
         if(s>max) max=s; 
        } 
   for(i=0; i<n; i++) 
     { 
      System.arraycopy(tc, i*sizeinput, center, i*totalinput, sizeinput);
       
      center[i*totalinput+sizeinput]=max/Math.sqrt(n); 
     } 
   //System.out.println("Jumlah N : " +n);
   return(n); 
 }
 
 protected void SVD(int npat, double inp[], double res[])
 {
     int i,j,k,jj,ii; 
   int n=0,nm=0,sw=sizehidden+1; 
   double s,c,f,g,h,x,y,z; 
  //svd matrix
   double cim[],im[],v[],w[],temp[];
   im = new double[npat*sw];
   cim = new double[npat*sw];
   v = new double[sw*sw] ;
   w = new double[sw];
   temp = new double[sw] ;
  // JOptionPane.showMessageDialog(null, sw);
   
   for(i=0,ii=0; i<npat; i++,ii+=sw) 
     { 
       System.arraycopy(inp, i*sizeinput, inputvector, 0, sizeinput);
      
     
      Calculate(temp); 
      for(j=0; j<sizehidden; j++) cim[ii+j]=hout[j]; 
      cim[ii+j]=-1.0; 
     } 
   for(int nout=0; nout<sizeoutput; nout++) 
     { 
      ArrayCopy(im,cim); 
     
      g=c=0.0; 
      for(i=0,ii=0; i<sw; i++,ii+=sw) 
        { 
         n=i+2; 
         temp[i]=c*g; 
         g=s=c=0.0; 
         if(i<npat) 
           { 
            for(k=i,jj=ii; k<npat; k++,jj+=sw) c+=Math.abs(im[jj+i]); 
            if(c!=0.0) 
              { 
               for(k=i,jj=ii; k<npat; k++,jj+=sw) 
                 { 
                  im[jj+i]/=c; 
                  s+=im[jj+i]*im[jj+i]; 
                 } 
               f=im[ii+i]; 
               g=-SETSIGN(Math.sqrt(s),f); 
               h=f*g-s; 
               im[ii+i]=f-g; 
               for(j=n-1; j<sw; j++) 
                 { 
                  s=0.0; 
                  for(k=i,jj=ii; k<npat; k++,jj+=sw) s+=im[jj+i]*im[jj+j]; 
                  f=s/h; 
                  for(k=i,jj=ii; k<npat; k++,jj+=sw) im[jj+j]+=f*im[jj+i]; 
                 } 
               for(k=i,jj=ii; k<npat; k++,jj+=sw) im[jj+i]*=c; 
              } 
           } 
         w[i]=c*g; 
         g=s=c=0.0; 
         if(i+1<=npat && i+1!=sw) 
           { 
            for(k=n-1; k<sw; k++) c+=Math.abs(im[ii+k]); 
            if(c!=0.0) 
              { 
               for(k=n-1; k<sw; k++) 
                 { 
                  im[ii+k]/=c; 
                  s+=im[ii+k]*im[ii+k]; 
                 } 
               f=im[ii+n-1]; 
               g=-SETSIGN(Math.sqrt(s),f); 
               h=f*g-s; 
               im[ii+n-1]=f-g; 
               for(k=n-1; k<sw; k++) temp[k]=im[ii+k]/h; 
               for(j=n-1,jj=j*sw; j<npat; j++,jj+=sw) 
                 { 
                  s=0.0; 
                  for(k=n-1; k<sw; k++) s+=im[jj+k]*im[ii+k]; 
                  for(k=n-1; k<sw; k++) im[jj+k]+=s*temp[k]; 
                 } 
               for(k=n-1; k<sw; k++) im[ii+k]*=c; 
              } 
           } 
        } 
      for(i=sw-1,n=sw; i>=0; i--,n--) 
        { 
         if(i<sw-1) 
           { 
            if(g!=0.0) 
              { 
               for(j=n; j<sw; j++) v[j*sw+i]=(im[i*sw+j]/im[i*sw+n])/g; 
               for(j=n; j<sw; j++) 
                 { 
                  s=0.0; 
                  for(k=n; k<sw; k++) s+=im[i*sw+k]*v[k*sw+j]; 
                  for(k=n; k<sw; k++) v[k*sw+j]+=s*v[k*sw+i]; 
                 } 
              } 
            for(j=n; j<sw; j++) v[i*sw+j]=v[j*sw+i]=0.0; 
           } 
         v[i*sw+i]=1.0; 
         g=temp[i]; 
        } 
      for(i=Math.min(npat,sw)-1,n=i+1,ii=i*sw; i>=0; i--,n--,ii-=sw) 
        { 
         g=w[i]; 
         for(j=n; j<sw; j++) im[ii+j]=0.0; 
         if(g!=0.0) 
           { 
            for(j=n; j<sw; j++) 
              { 
               for(s=0.0,k=n,jj=n*sw; k<npat; k++,jj+=sw) s+=im[jj+i]*im[jj+j]; 
               f=(s/im[i*sw+i])/g; 
               for(k=i,jj=i*sw; k<npat; k++,jj+=sw) im[jj+j]+=f*im[jj+i]; 
              } 
            for(j=i; j<npat; j++) im[j*sw+i]/=g; 
           } 
         else for(j=i; j<npat; j++) im[j*sw+i]=0.0; 
         im[ii+i]++; 
        } 
      for(k=sw-1; k>=0; k--) 
        { 
         for(int m=0; m<30; m++) 
           { 
            boolean flag=true; 
            for(n=k; n>=0; n--) 
              { 
               nm=n-1; 
               if(Math.abs(temp[n])<DBL_EPSILON) 
                 { 
                  flag=false; 
                  break; 
                 } 
               if(Math.abs(w[nm])<DBL_EPSILON) break; 
              } 
            if(flag) 
              { 
               c=0.0; 
               s=1.0; 
               for(i=n; i<k+1; i++) 
                 { 
                  f=s*temp[i]; 
                  temp[i]*=c; 
                  if(Math.abs(f)<=DBL_EPSILON) break; 
                  h=Math.sqrt(f*f+w[i]*w[i]); 
                  c=w[i]/h; 
                  w[i]=h; 
                  s=-f*h; 
                  for(j=0,jj=0; j<npat; j++,jj+=sw) 
                    { 
                     y=im[jj+nm]; 
                     z=im[jj+i]; 
                     im[jj+nm]=y*c+z*s; 
                     im[jj+i]=z*c-y*s; 
                    } 
                 } 
              } 
            z=w[k]; 
            if(n==k) 
              { 
               if(z<0.0) 
                 { 
                  w[k]=-z; 
                  for(j=0,jj=0; j<sw; j++,jj+=sw) v[jj+k]=-v[jj+k]; 
                 } 
               break; 
              } 
            x=w[n]; 
            nm=k-1; 
           
            f=((w[nm]-z)*(w[nm]+z)+(temp[nm]-temp[k])*(temp[nm]+temp[k]))/(2.0*temp[k]*w[nm]); 
            f=((x-z)*(x+z)+temp[k]*((w[nm]/(f+SETSIGN(Math.sqrt(f*f+1.0),f)))-temp[k]))/x; 
            c=s=1.0; 
            for(j=n,i=n+1; j<=nm; j++,i++) 
              { 
               h=s*temp[i]; 
               g=c*temp[i]; 
               temp[j]=Math.sqrt(f*f+h*h); 
               c=f/temp[j]; 
               s=h/temp[j]; 
               f=x*c+g*s; 
               g=g*c-x*s; 
               h=w[i]*s; 
               y=w[i]*c; 
               for(jj=0,ii=0; jj<sw; jj++,ii+=sw) 
                 { 
                  x=v[ii+j]; 
                  z=v[ii+i]; 
                  v[ii+j]=x*c+z*s; 
                  v[ii+i]=z*c-x*s; 
                 } 
               z=Math.sqrt(f*f+h*h); 
               w[j]=z; 
               if(z!=0) 
                 { 
                  z=1.0/z; 
                  c=f*z; 
                  s=h*z; 
                 } 
               f=c*g+s*y; 
               x=c*y-s*g; 
               for(jj=0,ii=0; jj<npat; jj++,ii+=sw) 
                 { 
                  y=im[ii+j]; 
                  z=im[ii+i]; 
                  im[ii+j]=y*c+z*s; 
                  im[ii+i]=z*c-y*s; 
                 } 
              } 
            temp[n]=0.0; 
            temp[k]=f; 
            w[k]=x; 
           } 
        } 
      
      for(j=0; j<sw; j++) 
        { 
         s=0.0; 
         if(w[j]>1e-20) 
           { 
            for(i=0; i<npat; i++) s+=im[i*sw+j]*res[i*sizeoutput+nout]; 
            s/=w[j]; 
           } 
         temp[j]=s; 
        } 
     
      for(j=0; j<sw; j++) 
        { 
         s=0; 
         for(i=0; i<sw; i++) s+=v[j*sw+i]*temp[i]; 
         if(j<sw-1) weight[nout*sizeweight+j]=s; 
         else     weight[(nout+1)*sizeweight-1]=s;   //bias 
        } 
     } 
    
 } 
   
}
