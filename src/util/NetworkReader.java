/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class NetworkReader {
    public final static String[] listFolders(String dir){
        File f = new File(dir);
        String[] list = null;
        if(f.isDirectory()){
            f.list();
            int sum = 0;
            for(int i=0;i<f.list().length;i++){
                if(listFilesAndFolders(dir)[i].isDirectory()){
                    sum++;
                }
            }
            list = new String[sum];
            int index = 0;
            for(int i=0;i<f.list().length;i++){
                if(listFilesAndFolders(dir)[i].isDirectory()){
                    list[index] = listFilesAndFolders(dir)[i].getName();
                    index++;
                }
            }
        }   
        return list;
    }

    public final static String[] listFiles(String dir){
        File f = new File(dir);
        String[] list = null;
        if(f.isDirectory()){
            f.list();
            int sum = 0;
            for(int i=0;i<f.list().length;i++){
                if(listFilesAndFolders(dir)[i].isFile()){
                    sum++;
                }
            }
            list = new String[sum];
            int index = 0;
            for(int i=0;i<f.list().length;i++){
                if(listFilesAndFolders(dir)[i].isFile()){
                    list[index] = listFilesAndFolders(dir)[i].getName();
                    index++;
                }
            }
        }
        return list;
    }

    public final static File[] listFilesAndFolders(String dir){
        File f = new File(dir);
        File[] list = null;
        if(f.isDirectory()){
            list = new File[f.list().length];
            list = f.listFiles();
        }
        return list;
    }
    
    public final static boolean checkFile(String dir,String file,String format)
    {
        return new File(dir+ "/" + file+ "." + format).exists();
    }
    
  
    
     
    
    
    
    
}
