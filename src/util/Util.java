/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JWindow;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.*;
import java.text.*;
import javax.swing.UIManager.LookAndFeelInfo;
/**
 *
 * @author Fiqih Amrullah
 */
public class Util {
  public static void TengahWindow(Window f){

          // Get the size of the screen
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    // Determine the new location of the window
    int w = f.getSize().width;
    int h = f.getSize().height;
    int x = (dim.width-w)/2;
    int y = (dim.height-h)/2;

    // Move the window
    f.setLocation(x, y);
    }

  

  public static void LookAndFeel(Frame f){
        try{


      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      SwingUtilities.updateComponentTreeUI(f);
      }catch (ClassNotFoundException ex){
              JOptionPane.showMessageDialog(f,"Kelas tak ditemukan.. ulangi installasi");
      }catch (InstantiationException ex){
      }catch (IllegalAccessException ex){
      }catch (UnsupportedLookAndFeelException ex){
          JOptionPane.showMessageDialog(f,"Dak Support");
      }

  }
  
  
  public static void initNimbusTheme()
  {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
           
        }


  }
          


}


