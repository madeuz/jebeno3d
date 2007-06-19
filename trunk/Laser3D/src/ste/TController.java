/*
 * TController.java
 *
 * Created on 4 czerwiec 2007, 14:02
 *
 */

package ste;
import gra.*;
import odc.Analizator;
import wej.TDaneWej;

import java.util.*;
import javax.swing.*; //do klasy TEST
/**
 *
 * @author mignat
 */
public class TController {
   
   private ArrayList <IBlock> fBlockAL = new ArrayList<IBlock>(); //lista blok�w
   public TController() {
      //Dodanie blokow od konca (ze wzgledu na metode setNext();
      
      //1. Tworz� blok 2
      Analizator ana = new Analizator();
      
      
      //1. Tworz� blok 1
      TDaneWej tdw = new TDaneWej();
      tdw.setNext(ana);
      
      fBlockAL.add(tdw);
      fBlockAL.add(ana);
      //2. Tworzy� kolejne bloki
      //fBlockAL.add(...)
      
      new TGlowneOkno(this);
   } //koniec kontruktora TController
   
   public ArrayList<IBlock> getBlockAL()   {   return fBlockAL;   }
   
} //koniec klasy TController

//test - do skasowania
class Test implements IBlock {
   
   public IBlock theNext;
   public String getTabTitle() { return "TEST"; }
   public JComponent getJComponent() {
      JPanel jp = new JPanel();
      jp.setBackground(java.awt.Color.yellow);
      jp.add(new JLabel("TEST", JLabel.CENTER));
      return jp;
   } //koniec getJComponent
   
   public void setNext(IBlock aN) {
      theNext = aN;
   }
} //koniec test
