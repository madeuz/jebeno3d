/*
 * Analizator.java
 *
 * Created on 19 czerwiec 2007, 12:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package odc;

//package genplaszczyzn;
import java.awt.Button;
import java.util.*;
import java.io.*;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ste.IBlock;


// Aby otrzymac plik z segmentami nalezy wywolac
// (new Analizator(xx,yy,zz,i)).segmenty(MenuPanel.pw());
// xx,yy,zz -tablice odczytow
// i numer ciecia 
// MenuPanel.pw - PrintWriter do kt�ego zapisywane s dane(do zmiany)
// ostatnie dwa parametry okreslaja poczatek i koniec sektora w 
// radianach


public class Analizator implements IBlock {
    double xx[];
    double yy[];
    double zz[];
    int len;
    static double MAX_ERROR = 0.1;
    ArrayList seglist = new ArrayList();
    static double Maxerror=0.1;
    class Para{
        int k1;
        int k2;
        Para(int k1, int k2){
            this.k1=k1;
            this.k2=k2;
        }
      
    }
    
    // konstruktor klasy
    public Analizator(){}
    //public Analizator(double xx[], double yy[], double zz[], int n) {
    public void setInputData(double xx[], double yy[], double zz[], int n) {
        this.xx=xx;
        this.yy=yy;
        this.zz=zz;
        this.len=n;
    }
    
    // Generowanie pojedynczego odcinka i dodanie do listy odcink�
    
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
           System.out.println("odcinek "+k1+"  "+k2);
       
           new Odcinek(xx,yy, zz, k1, k2,len);}
           j++;
       
       }
       return;
   } 
   
   
   //Generowanie listy odcink� i zapis do pliku
    public ArrayList segmenty(PrintWriter pw)
    {
        int k1=0;
        int k2=-1;

        Odcinek.tlista();
        lseg(0, len);
        Odcinek.druklist(pw);
        return Odcinek.lista;
    }
    
   // Oblicza odlego�punktu c,y od prostej
    
   double dist(double c, double s,double d, double x, double y ) {
        return Math.abs(c*x+s*y-d);
   }

   public String getTabTitle() {
      return "Odcinki";
   }

   public JComponent getJComponent() {
     JPanel jp =  new JPanel();
     Button b = new Button("Analizator TEST");
     jp.add(b);
     return jp;
   }

   public void setNext(IBlock aNext) {
   }
}

