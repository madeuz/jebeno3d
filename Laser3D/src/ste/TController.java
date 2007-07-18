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
   
    private ArrayList <TAbstBlock> fBlockAL = new ArrayList<TAbstBlock>(); //lista blokï¿½w
    public TController() {
        //Dodanie blokow od konca (ze wzgledu na metode setNext();

        fBlockAL.add(new TDaneWej());
        fBlockAL.add(new Analizator());
        fBlockAL.add(new Test());
        //fBlockAL.add(new Arek());
        //.... (nastêpne modu³y)
       
        //Wszystkie elementy z wyj¹tkiem ostatniego przekazuj¹ siê do nastêpnego
        for (int i=0; i<fBlockAL.size()-1; i++) {
            TAbstBlock prev = (TAbstBlock)fBlockAL.get(i);
            TAbstBlock next = (TAbstBlock)fBlockAL.get(i+1);
            prev.setNext(next);
        } //koniec for
       
        new TGlowneOkno(this);
    } //koniec kontruktora TController
   
   public ArrayList<TAbstBlock> getBlockAL()   {   return fBlockAL;   }
   
} //koniec klasy TController

//test - do skasowania
class Test extends TAbstBlock {
   
   public TAbstBlock theNext;
   public String getTabTitle() { return "TEST"; }
   public JComponent getJComponent() {
      JPanel jp = new JPanel();
      jp.setBackground(java.awt.Color.yellow);
      jp.add(new JLabel("TEST", JLabel.CENTER));
      return jp;
   } //koniec getJComponent
   
   public void setNext(TAbstBlock aN) {
      theNext = aN;
   }
    public void createObjFile(String aN) { }
    public void checkSettings(){};
} //koniec test
