/*
 * TController.java
 *
 * Created on 4 czerwiec 2007, 14:02
 *
 */

package ste;
import gra.*;
import wej.TDaneWej;

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
        //Dodanie blokow od konca (ze wzgledu na metode setNext();
        
        //1. Tworzê blok 2
        Test t = new Test();
fBlockAL.add(new Test());

        //1. Tworzê blok 1
        TDaneWej tdw = new TDaneWej();
        tdw.setNext(t);
        fBlockAL.add(tdw);
        
        //2. Tworzyæ kolejne bloki
        //fBlockAL.add(...)
        
        new TGlowneOkno(this);
    } //koniec kontruktora TController
    
    public ArrayList<IBlock> getBlockAL()   {   return fBlockAL;   }
    
} //koniec klasy TController

//test - do skasowania
class Test implements IBlock {
    
    public IBlock theNext;
    public String getTabTitle() { return "TEST"; }
    public JComponent getJComponent()   
    {   
        JPanel jp = new JPanel();
        jp.setBackground(java.awt.Color.yellow);
        jp.add(new JLabel ("TEST", JLabel.CENTER));
        return jp;
    } //koniec getJComponent
    
    public void setNext(IBlock aN) {
        theNext = aN;
    }
} //koniec test
