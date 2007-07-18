/*
 * Analizator.java
 *
 * Created on 19 czerwiec 2007, 12:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package odc;
import ste.TAbstBlock;
import obj.TObjConfig;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

// Aby otrzymac plik z segmentami nalezy wywolac
// (new Analizator(xx,yy,zz,i)).segmenty(MenuPanel.pw());
// xx,yy,zz -tablice odczytow
// i numer ciecia 
// MenuPanel.pw - PrintWriter do ktï¿½ego zapisywane s dane(do zmiany)
// ostatnie dwa parametry okreslaja poczatek i koniec sektora w 
// radianach

public class Analizator extends TAbstBlock {
    protected final static int CO_ILE_OBJ = 1; //co ktory pkt ma wyswietlac
    double xx[];
    double yy[];
    double zz[];
    double ptz;
    int len;
    static double MAX_ERROR = 0.1;
    final static double MAXX =800;
    ArrayList<Odcinek> seglist = new ArrayList<Odcinek>();
    static double Maxerror=0.1;
    private static int theCounter = 0; //licznik pomocniczy MG
    private static int theCntLine = 0; //licznik pomocniczy (do grafiki) MG
    
    
    public Analizator()
    {theGraphSpi = new JSpinner(new SpinnerNumberModel(CO_ILE_OBJ,1,10000,10));
    }
    
    public Analizator(double xx1[], double yy1[], double zz1[], double ptz1) {
        this.xx=xx1;
        this.yy=yy1;
        this.zz=zz1;
        this.ptz=ptz1;
    }
      
   
    class Para{
        int k1;
        int k2;
        Para(int k1, int k2){
            this.k1=k1;
            this.k2=k2;
        }
      
    }
    
   
    
    public void setInputData(double xx1[], double yy1[], double zz1[], double n) {
    //System.out.println ("===\nAnalizator.setInputData xx.1 " + xx1.length + ", yy1.l: " + yy1.length + ", zz1.l: " + zz1.length + ", theCounter: " + (theCounter++));
         //for (int i=0; i<xx1.length; i++)
         //    System.out.println("pkt " + i + ": [" + xx1[i] + ", " + yy1[i] + ", " + zz1[i] + "];");
         
         if(xx1==null) return;
         if(xx1.length==0) return;
         
         Analizator a = new Analizator(xx1,yy1,zz1,(double)n);
         seglist.clear(); //na wszelki wypadek. MG
         seglist=a.segmenty();

         //*******************************************
         // AREK.setInputData(seglist);
         
         //Dodajê odcinki do grafiki
        for (Odcinek odc : seglist) {
            //System.out.println (
            //    "odc. od (" + odc.x1 + ", " + odc.y1 + ", " + odc.z1 + 
            //    ") do (" + odc.x2 + ", " + odc.y2 + ", " + odc.z2 + ");");
            if (theCntLine++ % theGraphQnt == 0) { //biore co ktorys pkt
                fObjConf.setMinMax(odc.x1, odc.y1, odc.z1); //ustawiam min i max (do pod³ogi)
                fObjConf.setMinMax(odc.x2, odc.y2, odc.z2); //ustawiam min i max (do pod³ogi)
                fObjConf.line(odc.x1, odc.y1, odc.z1, odc.x2, odc.y2, odc.z2, "gray", "line");
            } //koniec if
        } //koniec for
    }
    // Generowanie pojedynczego odcinka i dodanie do listy odcinkï¿½
    
   public void lseg(int k1, int k2){
       ArrayList <Para> lista=new ArrayList <Para>();
       lista.add(new Para(k1,k2));
       int j=0;
       while(j<lista.size()){
           Para p2= (Para)lista.get(j);
           k1=p2.k1;
           k2=p2.k2;
           double x1=xx[k1];
           double y1=yy[k1];
           double x2=xx[k2];
           double y2=yy[k2];
           double alfa = Math.atan2(x2-x1,y1-y2);
          // System.out.println("alfa "+alfa);
           double c = Math.cos(alfa);
           double s = Math.sin(alfa);
           double d=x1*c+y1*s;
           double max=0.0;
           int maxind=k1;
           for(int i =k1;i<k2;i++){
           double dd = dist(c,s,d,xx[i],yy[i]);
     
           if (dd>max){
               maxind=i;
               max=dd;
           }      
       }
       if(max>MAX_ERROR){
    
           lista.add(new Para(k1,maxind));
           lista.add(new Para(maxind,k2));
           }
       else {
           //System.out.println("odcinek "+k1+"  "+k2);
       
           new Odcinek(xx,yy, zz, k1, k2,len);}
           j++;
       
       }
       return;
   } 
   

   
   //Generowanie listy odcinkï¿½ i zapis do pliku
    private ArrayList<Odcinek> segmenty()
    {   //System.out.println ("ccc xx.l: " + xx.length);
        if(xx==null) 
        {
            System.out.println("Null pointer");
            System.exit(0);
        }
        if(xx.length==0) 
        {
            System.out.println("Null pointer");
            System.exit(0);
        }
        
        int k1=0;
        int k2=-1;
        Odcinek.tlista();
          ArrayList <Para> lista = new ArrayList <Para> ();
        for(int i=0;i<xx.length;i++){
            if( Math.sqrt(xx[i]*xx[i]+yy[i]*yy[i]+zz[i]*zz[i])< MAXX) k2=i;
            else{
              if (k2 > k1) {
                  lista.add(new Para(k1,k2));
                  lseg(k1, k2);
              }  
              k1=i+1;
             }
        }
        if (k2 > k1)
        {
            lista.add(new Para(k1,k2));
            System.out.println(ptz+"  "+k1+"  "+k2);
            lseg(k1, k2);
        }
         System.out.println(Odcinek.lista.size());
        return Odcinek.lista;
    }
    
   // Oblicza odlegoï¿½punktu c,y od prostej
    
   double dist(double c, double s,double d, double x, double y ) {
        return Math.abs(c*x+s*y-d);
   }

   public String getTabTitle() {
      return "Odcinki";
   }

   public JComponent getJComponent() {
     JPanel jp =  new JPanel();
     // Tutaj dodaæ inne elementy graficzne
     jp.add(getObjP());
     return jp;
   }

    public void setNext(TAbstBlock aN)  {   theNext = aN;   } //koniec setNext
    public void checkSettings() //spr. i pobiera ustawienia poczatkowe
    {
        try {
            theGraphQnt = Integer.parseInt(theGraphSpi.getValue().toString());
        } catch (NumberFormatException err) { //chyba niepotrzebne (?)
            showErr("Bledne ustawienia poczatkowe : " + err +
                    "\nZostana przyjete ustawienia domyslne !");
            theGraphQnt = CO_ILE_OBJ;
        } //koniec try-catch
        
        //Spr. ustawienia pocz¹tkowe nastêpnego modu³u
        if (theNext != null)    theNext.checkSettings();
    } //koniec checkSettings
   
   /************ Micha³ ***************/
    
    public void createObjFile(String aName) 
    {
      int _kropka = aName.lastIndexOf(".");
      if (_kropka != -1) //cz. istnieje kropka w nazwiePliku
         theObjFileN = aName.substring(0, _kropka);
      theObjFileN = theObjFileN.concat("_odc");
      
      //2. Tworze plik .obj
      fObjConf.openFile(theObjFileN);

   } //koniec createObjFile
    //=== Micha³ koniec
}

