/*
 * Odcinek.java
 *
 * Created on 19 czerwiec 2007, 12:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package odc;

import java.util.*;
import java.io.*;

public class Odcinek implements Comparable<Odcinek>{
    final static double epsilond = 0.10; 
    final static double epsilona = 10.0;
    double c;
    double s;
    double d;
    double sx;
    double sy;
    double sz;
    double sx2;
    double sy2;
    double sz2;
    double sxy;
    double syz;
    double sxz;
    double x1;
    double y1;
    double z1;
    double x2;
    double y2;
    double z2;
    int n;
    int k1;
    int k2;
    int len;
    static int lenn=181;
    static ArrayList <Odcinek>lista;
    static ArrayList <Odcinek> lista2 = new ArrayList <Odcinek>  ();
    static ArrayList tab[] = new  ArrayList [lenn];
    
    static ArrayList wynik[] = new  ArrayList [lenn];
    
    // konstruktor klasy odcinek
    
    public Odcinek(Odcinek o){
        this.c=o.c;
        this.s=o.s;
        this.d=o.d;
        this.sx=o.sx;
        this.sy=o.sz;
        this.x1=o.x1;
        this.y1=o.y1;
        this.z1=o.z1;
        this.x2=o.x2;
        this.y2=o.y2;
        this.z2=o.z2;
        this.n=o.n;
        this.k1=o.k1;
        this.k2=o.k2;
    }
    
    //Tworzenie odcinka na podstawie danych przechowywanych w tablicach x,y,x
    
    public Odcinek(double xx[], double yy[], double zz[], int k1, int k2,int len) {
        this.len=len;
        if((k1<0)||(k2<0)) return;
        
        x1=xx[k1];
        y1=yy[k1];
        z1=zz[k1];
        x2=xx[k2-1];
        y2=yy[k2-1];
        z2=zz[k2-1];
  
        for(int i=k1;i<=k2;i++)
        {
            double x=xx[i];
            double y=yy[i];
            double z=zz[i];
            sx+=x;
            sy+=y;
            sz+=z;
            sxy+=x*y;
            sxz+=x*z;
            syz+=y*z;
            sx2+=x*x;
            sy2+=y*y;
            sz2+=z*z;
            
        }
        n=k2-k1+1;
        double alfa=Math.atan2(-sx*sx+n*sx2,-n*sxy+sx*sy);
   
        d=sx/n*Math.cos(alfa)+sy/n*Math.sin(alfa);
        this.k1=k1;
        this.k2=k2;
        c=Math.cos(alfa);
        s=Math.sin(alfa);
        lista.add(this);
       
        
    }
    
    // por�nywanie odcink�
    
    public int compareTo(Odcinek o1)
    {
        return (this.k1-o1.k1);
    }
    static void tlista(){
        lista = new ArrayList<Odcinek>();
    }
    
    //Sortowanie listy odcink�
    
     static void msort(){
         if(lista==null) return;
         Collections.sort(lista);
     }
     
     // Metoda wypisuje zawarto�listy odcink� do pliku
     void druk(PrintWriter pw){
         double dist =Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
         if(Math.abs(k1-k2)>2){
        if((z1<2.1)&&(z2<2.1)){
            
           pw.println(x1+" "+y1+" "+z1+" "+x2+" "+y2+" "+z2+" "+sx+" "+sy+" "+sz+" "+sx2+" "+sy2+" "+sz2+" "+sxy+"  "+sxz+"  "+syz+"  "+c+" "+s+" "+d+"  "+k1+"  "+k2+"  "+Math.atan2(y1,x1)+"  "+Math.atan2(y2,x2));
        }
    
      }

     } 
     
     // Metoda wypisuje zawarto�listy odcink� do pliku 
     void druk2(){
       System.out.println(n+" "+x1+" "+y1+" "+z1+" "+x2+" "+y2+" "+z2+" "+sx+" "+sy+" "+sz+" "+c+" "+s+" "+d+"  "+k1+"  "+k2);
         
     }
     
     static void druklist(PrintWriter pw){
        System.out.println("druklist"); 
        if(lista==null) System.out.println("lista  null");
        else System.out.println(lista.size());
        msort();
        for(int i=0;i<lista.size();i++){
             lista.get(i).druk(pw);
             lista2.add(lista.get(i));
         }
         
     }
     
   
 } 
       
     
         
    