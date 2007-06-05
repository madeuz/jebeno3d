/*
 * TController.java
 *
 * Created on 4 czerwiec 2007, 14:02
 *
 */

package ste;
import gra.*;
import las2xyz.TLaser2xyz;

import java.util.*;
import javax.swing.*; //do klasy TEST
/**
 *
 * @author mignat
 */
public class TController {

    private ArrayList <IBlock> fBlockAL = new ArrayList<IBlock>(); //lista bloków
    public TController() 
    {
        //1. Tworzê blok 1
        fBlockAL.add(new TLaser2xyz());
        
fBlockAL.add(new Test());
        
        //2. Tworzyæ kolejne bloki
        //fBlockAL.add(...)
        
        new TGlowneOkno(this);
    } //koniec kontruktora TController
    
    public ArrayList<IBlock> getBlockAL()   {   return fBlockAL;   }
    
} //koniec klasy TController

//test - do skasowania
class Test implements IBlock {
    
    public String getTabTitle() { return "TEST"; }
    public JComponent getJComponent()   
    {   
        JPanel jp = new JPanel();
        jp.setBackground(java.awt.Color.yellow);
        jp.add(new JLabel ("TEST", JLabel.CENTER));
        return jp;
    } //koniec getJComponent
} //koniec test
