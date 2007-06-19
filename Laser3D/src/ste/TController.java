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
   
   private ArrayList <IBlock> fBlockAL = new ArrayList<IBlock>(); //lista blokï¿½w
   public TController() {
      //Dodanie blokow od konca (ze wzgledu na metode setNext();

       fBlockAL.add(new TDaneWej());
       fBlockAL.add(new Analizator());
       fBlockAL.add(new Test());
       //fBlockAL.add(new Arek());
       //.... (nastêpne modu³y)
       
       //Wszystkie elementy z wyj¹tkiem ostatniego przekazuj¹ siê do nastêpnego
       for (int i=0; i<fBlockAL.size()-1; i++) {
           IBlock prev = (IBlock)fBlockAL.get(i);
           IBlock next = (IBlock)fBlockAL.get(i+1);
           prev.setNext(next);
       } //koniec for
           
       /*
      //1. Tworzï¿½ blok 2
      Analizator ana = new Analizator();
      
      //1. Tworzï¿½ blok 1
      TDaneWej tdw = new TDaneWej();
      tdw.setNext(ana);
      
      fBlockAL.add(tdw);
      fBlockAL.add(ana);
      //2. Tworzyï¿½ kolejne bloki
      //fBlockAL.add(...)
      */
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
