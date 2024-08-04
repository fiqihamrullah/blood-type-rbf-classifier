/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.BloodImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fiqih Amrullah
 */
public class BloodImageCollection 
{
    List<BloodImage> lstbloodimages;
    List<String> lsttarget;

    public BloodImageCollection() 
    {
        lstbloodimages = new ArrayList<BloodImage>();  
        lsttarget = new ArrayList<String>();
    }
    
    public void addBloodImage(BloodImage bi,String target)
    {
        lstbloodimages.add(bi);
        bi.SetName(target);
        lsttarget.add(target);
    }
    
    public int getTarget(int i)
    {
        return Integer.parseInt(lsttarget.get(i));
    }
    
    public BloodImage getBloodImage(int idx)
    {
        return lstbloodimages.get(idx);
    }
    
    public int getTotalImages()
    {
        return lstbloodimages.size();
    }
    
}
