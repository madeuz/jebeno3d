package jebeno3d;

import java.util.StringTokenizer;
import static java.lang.System.out;
/*
 * Rekto.java
 *
 * Created on 5 marzec 2007, 11:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author arkadiusz
 */
public class Rekto {
  
  private double ptz;
  private double[] komenco = new double[3];
  private double[] fino = new double[3];
  private double Sx,Sy,Sz;
  private double Sx2,Sy2,Sz2;
  private double Sxy,Syz,Sxz;
  private double d ;
  private int n ;
  private double sinalfa ;
  private double cosalfa ;
  private int nombroK;
  private int nombroF;
  public static int NombroDeRektoj = 0;
  
  public boolean cxuPtz = true;
  public boolean cxuN = true;
  public boolean cxuKomenco = true;
  public boolean cxuFino = true;
  public boolean cxuSumoj = true;
  public boolean cxuSumoj2 = true;
  public boolean cxuSumojxyz = true;
  public boolean cxuCos = true;
  public boolean cxuSin = true;
  public boolean cxuD = true;
  public boolean cxuNombroK = true;
  public boolean cxuNombroF = true;
  
  
  /**
   * Creates a new instance of Rekto
   */
  public Rekto() {
    this.Sx = 0;
    this.Sx = 0;
    this.Sx = 0;
    this.n = 0;
    this.d = Double.NaN;
    this.sinalfa = Double.NaN;
    this.cosalfa = Double.NaN;
    this.nombroK = -1;
    this.nombroF = -1;
    this.NombroDeRektoj++;
  }
  /**
   *
   * @param en
   * @param separator
   */
  public void kreuElVerso(String verso, String separator ){
    this.NombroDeRektoj++;
    StringTokenizer
      st = new StringTokenizer(verso, separator);
    if(this.cxuPtz)
      this.ptz = Double.valueOf(st.nextToken());
    if(this.cxuN)
      this.n = Integer.valueOf(st.nextToken());
    if(this.cxuKomenco){
      this.komenco[0] = Double.valueOf(st.nextToken());
      this.komenco[1] = Double.valueOf(st.nextToken());
      this.komenco[2] = Double.valueOf(st.nextToken());
    }
    if(this.cxuFino){
      this.fino[0] = Double.valueOf(st.nextToken());
      this.fino[1] = Double.valueOf(st.nextToken());
      this.fino[2] = Double.valueOf(st.nextToken());
    }
    if(this.cxuSumoj){
      this.Sx = Double.valueOf(st.nextToken());
      this.Sy = Double.valueOf(st.nextToken());
      this.Sz = Double.valueOf(st.nextToken());
    }
    if(this.cxuSumoj2){
      this.Sx2 = Double.valueOf(st.nextToken());
      this.Sy2 = Double.valueOf(st.nextToken());
      this.Sz2 = Double.valueOf(st.nextToken());
    }
    if(this.cxuSumojxyz){
      this.Sxy = Double.valueOf(st.nextToken());
      this.Sxz = Double.valueOf(st.nextToken());
      this.Syz = Double.valueOf(st.nextToken());
    }
    if(this.cxuCos)
      this.cosalfa = Double.valueOf(st.nextToken());
    if(this.cxuSin)
      this.sinalfa = Double.valueOf(st.nextToken());
    if(this.cxuD)
      this.d = Double.valueOf(st.nextToken());
    if(this.cxuNombroK)
      this.nombroK = Integer.valueOf(st.nextToken());
    if(this.cxuNombroF)
      this.nombroF = Integer.valueOf(st.nextToken());
    //!!!!!!!!!!!!!!!!!!!!!!!!!!11
    if(!this.cxuN) this.n = this.nombroF - this.nombroK;
    
  }
  
  public boolean cxuSimila(Rekto r, double sojlo){
    return cxuSimila(r.prenuNombroK(), r.prenuNombroF(), sojlo);
  }
  public boolean cxuSimila(int K, int F, double sojlo){
    //część wspólna (iloczyn) zbiorów to:
    //     najmniejszy koniec - największy początek
    int ProduktoDeAroj =  Math.min(this.nombroF,F) - Math.max(this.nombroK,K);
    //zbiory nie mają części wspólnej.
    if (ProduktoDeAroj <= 0) return false;
    //suma zbiorów to:
    //     najmniejszy poczatek - największy koniec
    // przy zalożeniu że mają nie zerową część wspólną.
    int SumoDeAroj =  Math.max(this.nombroF,F) - Math.max(this.nombroK,K);
    
//miara podobieństwa
    // 1.0 => podobny w 100%
    // 0.0 => w ogóle nie podobny
    // ujemny => zbiory rozłączne;
    double similo = (double) ProduktoDeAroj / (double)SumoDeAroj;
    if(similo < sojlo)
      return false;
    else
      return true;
  }
  
  
  public double prenuSx(){return this.Sx;}
  public double prenuSy(){return this.Sy;}
  public double prenuSz(){return this.Sz;}
  
  public double prenuSx2(){return this.Sx2;}
  public double prenuSy2(){return this.Sy2;}
  public double prenuSz2(){return this.Sz2;}
  
  public double prenuSxy(){return this.Sxy;}
  public double prenuSyz(){return this.Syz;}
  public double prenuSxz(){return this.Sxz;}
  
  public double prenuD(){ return this.d ;}
  public double prenuSinAlfa(){return this.sinalfa ;}
  public double prenuCosAlfa(){return this.cosalfa ;}
  public int prenuNombroK(){return this.nombroK;}
  public int prenuNombroF(){return this.nombroF;}
  public double prenuPtz(){return this.ptz;}
  public int prenuN(){return this.n;}
  public double[] prenuKomenco(){
    for (int i = 0; i < fino.length; i++){
      if(Math.abs(komenco[i]) < 1e-10) komenco[i]=0;
    }
//    komenco[0] = this.Sx/this.n - (this.fino[0]-this.komenco[0])/2;
//    komenco[1] = this.Sy/this.n - (this.fino[1]-this.komenco[1])/2;
//    komenco[2] = this.Sz/this.n - (this.fino[2]-this.komenco[2])/2;
    return this.komenco ;
  }
  public double[] prenuFino(){
    for (int i = 0; i < fino.length; i++)
      if(Math.abs(fino[i]) < 1e-10) fino[i]=0;
    
//    fino[0] = this.Sx/this.n + (this.fino[0]-this.komenco[0])/2;
//    fino[1] = this.Sy/this.n + (this.fino[1]-this.komenco[1])/2;
//    fino[2] = this.Sz/this.n + (this.fino[2]-this.komenco[2])/2;
    
    return this.fino ;
  }
  
/* Nie używane, być może nie powinno być możliwość
 * nadpisywania zmiennych prywatnych
 public void agorduPtz(double ptz){this.ptz = ptz;}
 public void agorduN(int n){this.n = n;}
 public void agorduKomenco(double[] k){this.komenco = k;}
 public void agorduFino(double[] f){this.fino = f;}
 public void agorduSx(double sx){this.Sx = sx;}
 public void agorduSy(double sy){this.Sy = sy;}
 public void agorduSz(double sz){this.Sz = sz;}
 public void agorduSxyz(double sx, double sy, double sz){
  this.Sx = sx;
  this.Sy = sy;
  this.Sz = sz;
 }
 public void agorduD(double d){this.d = d;}
 public void agorduSinAlfa(double sa){this.sinalfa = sa;}
 public void agorduCosAlfa(double ca){this.cosalfa = ca;}
 */
  
  
}
