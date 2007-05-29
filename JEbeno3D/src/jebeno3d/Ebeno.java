/*
 * Ebeno.java
 *
 * Created on 1 marzec 2007, 00:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jebeno3d;

import java.util.*;

/**
 * Zawiera wszystkie metody i zmienne potrzebne do dopasowania płaszczyzny
 * (Ebeno). Urzyta metoda to: obracająca się płaszczyzna.
 * 
 * @author arkadiusz Zychewicz
 */
public class
  Ebeno {
  /**
   * parametr przy zmiennej x
   */
  private double A;
  /**
   * parametr przy zmiennej y
   */
  private double B;
  /**
   * parametr przy zmiennej z
   */
  private double C;
  /**
   * parametr wolny
   */
  private double D;
  /**
   * Liczba POMIARÓW składających się na płaszczyznę
   */
  private int NN=0;
  /**
   * Liczba punktów reprezentujących pomiary
   *
   */
  private int NNp=0;
  /**
   * Zawiera maksymalną wysokość na jakiej znajduje się pu należący do płaszczyzny
   */
  double[] max;
  double[] min;
  
  /** Sumy x-ów, y-ów, z-ów */
  private double Sx = 0;
  private double Sy = 0;
  private double Sz = 0;
  /** Sumy x-ów^2, y-ów^2, z-ów^2 */
  private double Sx2 = 0;
  private double Sy2 = 0;
  private double Sz2 = 0;
  /** Sumy x*y-ów, y*z-ów, x*z-ów */
  private double Sxy = 0;
  private double Syz = 0;
  private double Sxz = 0;
  /**
   * Określa czy puy zgromadzone do tej pory są wspólniowe czy nie.
   * Czyli czy leżą na prostej */
  private boolean p_cxuRekto = true;
  private double[][] p_p = new double[3][3];
  private boolean p_cxu3 = false;
  /**
   *
   *
   */
  private boolean r_cxuUnuaRekto = true;
  private int r_lastaK;
  private int r_lastaF;
  /**
   * INNE stałe
   */
  private double minABC = 0.001;
  private etMetodo metodo;
  
  private final int nrEbeno;
  private static int totEbeno;
  /**
   * Tworzy nowy obiekt Ebeno. Inicjalizuje zmienne zerami.
   */
  public Ebeno() {
    this.nrEbeno = totEbeno++;
    A=B=C=D=NN=0;
    this.max = new double[3];
    this.min = new double[3];
    for (int i = 0; i < 3; i++) {
      this.max[i] = -Double.MAX_VALUE;
      this.min[i] =  Double.MAX_VALUE;
    }
  }
  /**
   * Tworzy nowy obiekt Ebeno. Inicjalizuje zmienne zerami.
   * Dodaje płaszczyznę.
   */
  public Ebeno(Rekto r) {
    this();
    this.aldonuRekto(r);
  }
  /**
   * Dodaje jeden pu do już istniejącego zestawu puów tworzących płaszczyznę.
   *  Ostatni parametr to wysokość!
   * @param aX współrzedna x dodawanego puu
   * @param aY współrzedna y dodawanego puu
   * @param aZ współrzedna z dodawanego puu
   */
  public void aldonuPunkto(double aX, double aY, double aZ, boolean cxuSumoj){
    // poprawia Odpowiednie sumy.
    if(cxuSumoj){
      this.Sx += aX;
      this.Sy += aY;
      this.Sz += aZ;
      this.Sx2 += aX*aX;
      this.Sy2 += aY*aY;
      this.Sz2 += aZ*aZ;
      this.Sxy += aX*aY;
      this.Syz += aY*aZ;
      this.Sxz += aX*aZ;
    }
    // ziwększ liczbę puów.
    this.NNp++;
    if(! this.p_cxu3){ //jeżeli NIE MA dobrych trzech puów
      this.p_p[(this.NNp-1)%3][0]=aX;
      this.p_p[(this.NNp-1)%3][1]=aY;
      this.p_p[(this.NNp-1)%3][2]=aZ;
      if(! this.cxuKunRekto(p_p, 0.99999999999999999) && this.NNp >= 3){ //jeżeli NIE są współliniowe
        A = (p_p[1][1] - p_p[0][1])*(p_p[2][2] - p_p[0][2])
        -   (p_p[1][2] - p_p[0][2])*(p_p[2][1] - p_p[0][1]);
        
        B = (p_p[2][0] - p_p[0][0])*(p_p[1][2] - p_p[0][2])
        -   (p_p[1][0] - p_p[0][0])*(p_p[2][2] - p_p[0][2]);
        
        C = (p_p[1][0] - p_p[0][0])*(p_p[2][1] - p_p[0][1])
        -   (p_p[1][1] - p_p[0][1])*(p_p[2][0] - p_p[0][0]);
        
        D = -(p_p[0][0]*A + p_p[0][1]*B + p_p[0][2]*C);
        
        // this.normu();
        if(
          Math.abs(A) > this.minABC ||
          Math.abs(B) > this.minABC ||
          Math.abs(C) > this.minABC
          )
          this.p_cxu3 = true;
      }
    }else{//mamy dobre trzy puy, Hura Hura Hura
      double[] p3 = this.modifuPunkto(aX,aY,aZ);
      double[] t = this.vektoroT(p3);
      //jezeli wektor t*t < dd jest za krotki to
      //pu p3 jest prawie prstopadle nad pSM
      //wtedy zamiast obracac plaszyzna przesuwamy
      double dd = 0.1;
      if(t[0]*t[0]+t[1]*t[1]+t[2]*t[2] > dd){
   /*
        A = (p_p[2][1] - centroDeMaso()[1])*t[2]
          - (p_p[2][2] - centroDeMaso()[2])*t[1];
        B = (p_p[2][2] - centroDeMaso()[2])*t[0]
          - (p_p[2][0] - centroDeMaso()[0])*t[2];
        C = (p_p[2][0] - centroDeMaso()[0])*t[2]
          - (p_p[2][2] - centroDeMaso()[2])*t[0];
        D = -(A*centroDeMaso()[0]
          +   B*centroDeMaso()[1]
          +   C*centroDeMaso()[2]);
    */
        A = (p3[1] - centroDeMaso()[1])*t[2]
          - (p3[2] - centroDeMaso()[2])*t[1];
        
        B = (p3[2] - centroDeMaso()[2])*t[0]
          - (p3[0] - centroDeMaso()[0])*t[2];
        
        C = (p3[0] - centroDeMaso()[0])*t[1]
          - (p3[1] - centroDeMaso()[1])*t[0];
        
        D = -(A*centroDeMaso()[0]
          +   B*centroDeMaso()[1]
          +   C*centroDeMaso()[2]);
        
        
      } else { //cz. nowy pkt nad �rodkiem masy
        D = D - alfa()*(A*p3[0]+B*p3[1]+C*p3[2]);
      } //koniec if-else
    }
    
    this.normu();
    
  }
  /**
   * Dodaje całą prostą.
   * @param r płaszczyzna (rekto), która ma być dodana
   */
  public void aldonuRekto(Rekto r){
    this.r_lastaK = r.prenuNombroK();
    this.r_lastaF = r.prenuNombroF();
    for (int i = 0; i < 3; i++) {
      if(this.max[i] < r.prenuKomenco()[i]) this.max[i] = r.prenuKomenco()[i];
      if(this.max[i] < r.prenuFino()[i])    this.max[i] = r.prenuFino()[i];
      if(this.min[i] > r.prenuKomenco()[i]) this.min[i] = r.prenuKomenco()[i];
      if(this.min[i] > r.prenuFino()[i])    this.min[i] = r.prenuFino()[i];
    }
    if(this.max[0] < r.prenuSx()/r.prenuN() ) this.max[0] = r.prenuSx()/r.prenuN();
    if(this.max[1] < r.prenuSy()/r.prenuN() ) this.max[1] = r.prenuSy()/r.prenuN();
    if(this.max[2] < r.prenuSz()/r.prenuN() ) this.max[2] = r.prenuSz()/r.prenuN();
    
    if(this.min[0] > r.prenuSx()/r.prenuN() ) this.min[0] = r.prenuSx()/r.prenuN();
    if(this.min[1] > r.prenuSy()/r.prenuN() ) this.min[1] = r.prenuSy()/r.prenuN();
    if(this.min[2] > r.prenuSz()/r.prenuN() ) this.min[2] = r.prenuSz()/r.prenuN();
    
    this.NN += r.prenuN();
    //początek ma wagę 25%
    this.aldonuPunkto(
      r.prenuKomenco()[0], r.prenuKomenco()[1], r.prenuKomenco()[2],
      false);
    //dodajemy środek ciężkości w wagą 25%
    this.aldonuPunkto(
      r.prenuSx()/r.prenuN(), r.prenuSy()/r.prenuN(), r.prenuSz()/r.prenuN(),
      false);
    //koniec ma wagę 25% równierz
    this.aldonuPunkto(
      r.prenuFino()[0], r.prenuFino()[1], r.prenuFino()[2],
      false);
    
    this.Sx += r.prenuSx();
    this.Sy += r.prenuSy();
    this.Sz += r.prenuSz();
    this.Sx2 += r.prenuSx2();
    this.Sy2 += r.prenuSy2();
    this.Sz2 += r.prenuSz2();
    this.Sxy += r.prenuSxy();
    this.Syz += r.prenuSyz();
    this.Sxz += r.prenuSxz();
  }
  /** czy prosta należy do płaszczyzny */
  public boolean cxuApartenu(Rekto r,double sojloDeKunrekto){
    if(r.prenuN() == 0 || this.NN == 0) return false;
    return r.cxuSimila(this.r_lastaK, this.r_lastaF, sojloDeKunrekto);
  }
  /** zwraca wspórzędne sirodka masy w kolejności x,y,z */
  private double[] centroDeMaso(){
    double[] cm = new double[3];
    cm[0] = this.Sx/this.NN;
    cm[1] = this.Sy/this.NN;
    cm[2] = this.Sz/this.NN;
    return cm;
  }
  /**
   * Oblicza odległość puu x,y,z od płaszczyzny A,B,C,D
   * @param x współrzędna x puu
   * @param y współrzędna y puu
   * @param z współrzędna z puu
   * @return Odległość puu od płaszczyzny
   */
  protected double dim(double x,double y,double z){
    if(
      Math.abs(A) < this.minABC ||
      Math.abs(B) < this.minABC ||
      Math.abs(C) < this.minABC
      )
      this.p_cxu3 = false;
    return Math.abs(A*x+B*y+C*z+D)/Math.sqrt(A*A+B*B+C*C);
  }
  /** Normuje długość wektora (A,B,C,D) do długości jednostkowej.
   *  Normowanie pozwala zapewnić, że wielkości A,B,C i D nie będą
   *  wybuchały do nieskończoności. (algorytm ma taką tendencję).
   */
  protected void normu(){
  /*
    if(
      Math.abs(A) < this.minABC &&
      Math.abs(B) < this.minABC &&
      Math.abs(C) < this.minABC
      )return;
   */
    if (A == 0 && B == 0 && C == 0 )return;
    
    double Normo = Math.sqrt(A*A+B*B+C*C);
    A = A / Normo;
    B = B / Normo;
    C = C / Normo;
    D = D / Normo;
  }
  /**
   * Zwraca wspócznnik korekty dla nowego puu.
   * @return wagę do dla danego puu
   */
  protected double alfa(){
    return  1.0/((double)this.NNp);
  }
  /**
   * zwraca wektor wychodzący z puu środka masy do nowego puu.
   *  Argument w postaci <b>tablicy</b> wspórzednych wektora (x,y,z)
   * @param pNova pu (x,y,z) koniec wektora.
   * @return Wektor od środka masy do punkut pNova
   */
  protected double[] vektoroT(double[] pNova){
    return vektoroT(pNova[0],pNova[1],pNova[2]);
  }
  /**
   * zwraca wektor wychodzący z puu środka masy do nowego puu.
   *  Argument w postaci wspórzednych wektora (x,y,z)
   * @param px współrzędna x punkut
   * @param py współrzędna y punkut
   * @param pz współrzędna z punkut
   * @return zwraca wektor t (od środka ciężkości do puu p)
   */
  protected double[] vektoroT(double px, double py, double pz){
    double[] t = new double[3];
    t[0] = B*(pz - centroDeMaso()[2])-C*(py - centroDeMaso()[1]);
    t[1] = C*(px - centroDeMaso()[0])-A*(pz - centroDeMaso()[2]);
    t[2] = A*(py - centroDeMaso()[1])-B*(px - centroDeMaso()[0]);
    return t;
  }
  /**
   * zwraca wartość loginczą czy podane trzy puy w p[3][3] są współliniowe,
   * uwzględniając podany próg (sojlo). Próg ma wrartość bezwzględną. Miarą wspóliniowości
   * jest stosunek dwuch krótrszych długości do dłużsego minus jeden.
   * @param p sprawdzany pu
   * @param sojlo próg, dla którego
   * @return prawdę jeżeli puy są współliniowe z dokładnością do progu (sojlo)
   */
  protected boolean cxuKunRekto(double[][] p, double sojloDeKunrekto) {
    
    double[] abc = new double[3];
    for (int i = 0; i < abc.length; i++) {
      abc[i] = Math.sqrt(
        (p[i][0]-p[(i+1)%3][0])*(p[i][0]-p[(i+1)%3][0])+
        (p[i][1]-p[(i+1)%3][1])*(p[i][1]-p[(i+1)%3][1])+
        (p[i][2]-p[(i+1)%3][2])*(p[i][2]-p[(i+1)%3][2]));
      
    }
    
    //double pp = (a+b+c)/2.0;
    //double P = Math.sqrt(pp*(pp-a)*(pp-b)*(pp-c));
    //double Ob2 = (a+b+c)*(a+b+c);
    Arrays.sort(abc);
    double m = (2*abc[2]-abc[0]-abc[1])/(abc[0]+abc[1]);
    if( m > sojloDeKunrekto)
      return true;
    return false;
  }
  /**
   * Modyfikuje nowy pu (x,y,z) zgodnie z wagą alfa()
   * @param x współrzędna x
   * @param y współrzędna y
   * @param z współrzędna z
   * @return Poprawiony pu
   */
  protected double[] modifuPunkto(double x,double y,double z){
    double[] p = new double[3];
    p[0] = x - alfa()*A*this.dim(x,y,z)/Math.sqrt(A*A+B*B+C*C);
    p[1] = y - alfa()*B*this.dim(x,y,z)/Math.sqrt(A*A+B*B+C*C);
    p[2] = z - alfa()*C*this.dim(x,y,z)/Math.sqrt(A*A+B*B+C*C);
    return p;
  }
  /**
   *
   *
   *
   */
  public double[][] prenuPunktojDeEbeno(etMetodo etm){
    
    double[][] pu = new double[4][3];
    //Se etMetodo.turnatoDeEbeno
    double[] tABCD  = new double[]{A,B,C,D};
    double[] tym = null;
    if(etm == etMetodo.PLEJMALGRANDAJ_KVADRATOJ ){
//      tym = this.prenuVektoroABC();
//      tABCD[0] =  tym[0];
//      tABCD[1] =  tym[1];
//      tABCD[2] =  tym[2];
//      tABCD[3] = - this.prenuD();
      
      tym = this.prenuABCD();
      tABCD[0] =  tym[0];
      tABCD[1] =  tym[1];
      tABCD[2] =  tym[2];
      tABCD[3] =  tym[3];
      
      System.out.print("wybrana metoda najmniejszych Kwadartów");

    }
    //szukamy największej wartości by później przez nią gdzielić
    int ind = 0;
    for (int i = 0; i < tABCD.length-1; i++){
      if ( Math.abs(tABCD[i]) > Math.abs(tABCD[ind]) )   ind = i;
    }
    //na podstawie ind wybieramy najkożystniejszą metodę
    //wyznaczenia potrzebnych punktów
    //ind = (int)tym[4];
    switch (ind){
      case 0://A
        pu[0][1] = this.min[1]; //y
        pu[0][2] = this.min[2]; //z
        pu[0][0] =
          -tABCD[1]/tABCD[0]*pu[0][1]
          -tABCD[2]/tABCD[0]*pu[0][2]
          -tABCD[3]/tABCD[0]; //x
        
        pu[1][1] = this.min[1]; //y
        pu[1][2] = this.max[2]; //z
        pu[1][0] =
          -tABCD[1]/tABCD[0]*pu[1][1]
          -tABCD[2]/tABCD[0]*pu[1][2]
          -tABCD[3]/tABCD[0]; //x
        
        pu[2][1] = this.max[1]; //y
        pu[2][2] = this.max[2]; //z
        pu[2][0] =
          -tABCD[1]/tABCD[0]*pu[2][1]
          -tABCD[2]/tABCD[0]*pu[2][2]
          -tABCD[3]/tABCD[0]; //x
        
        pu[3][1] = this.max[1]; //y
        pu[3][2] = this.min[2]; //z
        pu[3][0] =
          -tABCD[1]/tABCD[0]*pu[3][1]
          -tABCD[2]/tABCD[0]*pu[3][2]
          -tABCD[3]/tABCD[0]; //x
        break;
      case 1://B
        pu[0][0] = this.min[0]; //x
        pu[0][2] = this.min[2]; //z
        pu[0][1] =
          - tABCD[0]/tABCD[1]*pu[0][0]
          - tABCD[2]/tABCD[1]*pu[0][2]
          - tABCD[3]/tABCD[1]; //y
        
        pu[1][0] = this.min[0]; //x
        pu[1][2] = this.max[2]; //z
        pu[1][1] =
          - tABCD[0]/tABCD[1]*pu[1][0]
          - tABCD[2]/tABCD[1]*pu[1][2]
          - tABCD[3]/tABCD[1]; //y
        
        pu[2][0] = this.max[0]; //x
        pu[2][2] = this.max[2]; //z
        pu[2][1] =
          -tABCD[0]/tABCD[1]*pu[2][0]
          -tABCD[2]/tABCD[1]*pu[2][2]
          -tABCD[3]/tABCD[1]; //y
        
        pu[3][0] = this.max[0]; //x
        pu[3][2] = this.min[2]; //z
        pu[3][1] =
          -tABCD[0]/tABCD[1]*pu[3][0]
          -tABCD[2]/tABCD[1]*pu[3][2]
          -tABCD[3]/tABCD[1]; //y
        break;
      case 2://C
        pu[0][0] = min[0];
        pu[0][1] = min[1];
        pu[0][2] =
          - tABCD[0]/tABCD[2]*(pu[0][0])
          - tABCD[1]/tABCD[2]*pu[0][1]
          - tABCD[3]/tABCD[2];
        
        pu[1][0] = min[0];
        pu[1][1] = max[1];
        pu[1][2] =
          - tABCD[0]/tABCD[2]*(pu[1][0])
          - tABCD[1]/tABCD[2]*pu[1][1]
          - tABCD[3]/tABCD[2];
        
        pu[2][0] = max[0];
        pu[2][1] = max[1];
        pu[2][2] =
          - tABCD[0]/tABCD[2]*(pu[2][0])
          - tABCD[1]/tABCD[2]*pu[2][1]
          - tABCD[3]/tABCD[2];
        
        pu[3][0] = max[0];
        pu[3][1] = min[1];
        pu[3][2] =
          - tABCD[0]/tABCD[2]*pu[3][0]
          - tABCD[1]/tABCD[2]*pu[3][1]
          - tABCD[3]/tABCD[2];
        break;
    }
    return pu;
  }  
  /**
   * @return tablicę cztero elemętową w koleności:
   *   [0] - X sirodka
   *   [1] - Y środka
   *   [2] - Wyskokość kostki
   *   [3] - szerkokość i głębia podstawy
   */
  public double[] prenuPunktojDeKvadroj(){
    double [] pu = new double[4];
    pu[0] = (this.max[0] + this.min[0])/2.0;
    pu[1] = (this.max[1] + this.min[1])/2.0;
    pu[2] = (this.max[2] + this.min[2])/2.0;
    pu[3] = (this.max[2] - this.min[2])/2.0;
    return pu;
  }
  /**
   *Oblicza wektor normalny do powierzchni, wyliczony na podstawie sum
   *podanych w pliku wejściowym
   */
  public double[] prenuVektoroABC(){
    double [] vn = new double[3];
    /*
    Jama.Matrix m =
      new Jama.Matrix(
      new double[][]{
        {this.Sx2, this.Sxy, this.Sxz},
        {this.Sxy, this.Sy2, this.Syz},
        {this.Sxz, this.Syz, this.Sz2}
    }
    );
    m.times(1/this.NN);
    //m.print(3,2);
    double[] ev = m.eig().getRealEigenvalues();
    int iMin = 0;
    for(int i = 0; i< ev.length; i++)
      if(ev[i]< ev[iMin]) iMin = i;
    
    return m.eig().getV().transpose().getArray()[iMin];
     */
    return vn;
  }
  /**
   *
   */
  public double[] prenuABCD(){
    //z=ax+by+d
    Jama.Matrix
      mABD =  new Jama.Matrix(
      new double[][]{
        {this.Sx2,   this.Sxy,   this.Sx},
        {this.Sxy,   this.Sy2,   this.Sy},
        {this.Sx ,   this.Sy ,    1.0   }
    }
    );
    //x=cz+by+d
    Jama.Matrix
      mCBD =  new Jama.Matrix(
      new double[][]{
        {this.Sz2,   this.Syz,   this.Sz},
        {this.Syz,   this.Sy2,   this.Sy},
        {this.Sz ,   this.Sy ,    1.0   }
    }
    );
    //y=cz+ax+d
    Jama.Matrix
      mCAD =  new Jama.Matrix(
      new double[][]{
        {this.Sz2,   this.Sxz,   this.Sz},
        {this.Sxz,   this.Sx2,   this.Sx},
        {this.Sz ,   this.Sx ,    1.0   }
    }
    );
    
    double[] mDet = new double[]{mABD.det(),mCBD.det(),mCAD.det() };
    int iMax = 0;
    for (int i = 0; i < mDet.length; i++ ){
      if( Math.abs(mDet[i]) > Math.abs(mDet[iMax]) ) iMax = i;
    }
    Jama.Matrix roz = null;
    double[] alRedonato = new double[5];
    switch (iMax){
      case 0://mABD
        roz = mABD.solve(
          new Jama.Matrix( new double[][]{  {this.Sxz},  {this.Syz}, {this.Sz}  }  )
        );
        roz.print(3,2);
        alRedonato[0] = roz.transpose().getArray()[0][0];//a
        alRedonato[1] = roz.transpose().getArray()[0][1];//b
        alRedonato[2] = -1.0;//c
        alRedonato[3] = roz.transpose().getArray()[0][2];//d
        alRedonato[4] = 2;
        break;
      case 1://mCBD
        roz = mCBD.solve(
          new Jama.Matrix(
          new double[][]
        {
          {this.Sxz},
          {this.Sxy},
          {this.Sx}
        }
        )
        );
        alRedonato[0] = -1.0;//a
        alRedonato[1] = roz.transpose().getArray()[0][1];//b
        alRedonato[2] = roz.transpose().getArray()[0][0];//c
        alRedonato[3] = roz.transpose().getArray()[0][2];//d
alRedonato[4] = 0;
        break;
      case 2://mCAD
        roz = mCAD.solve(
          new Jama.Matrix(
          new double[][]
        {
          {this.Syz},
          {this.Sxy},
          {this.Sy}
        }
        )
        );
        alRedonato[0] = roz.transpose().getArray()[0][1];//a
        alRedonato[1] = -1.0;//b
        alRedonato[2] = roz.transpose().getArray()[0][0];//c
        alRedonato[3] = roz.transpose().getArray()[0][2];//d
        alRedonato[4] = 1;
        break;
    }
    return alRedonato;
  }
  /*****************************************
   *    METODY DOSTPU do zmiennych (pól) klasy
   *****************************************/
  // public double prenuA(){return this.prenuVektoroN()[0];}
  // public double prenuB(){return this.prenuVektoroN()[1];}
  // public double prenuC(){return this.prenuVektoroN()[2];}
  public double prenuD(){
    return
      this.prenuVektoroABC()[0]*this.Sx +
      this.prenuVektoroABC()[1]*this.Sy +
      this.prenuVektoroABC()[2]*this.Sz ;
  }
  public double prenuN(){return this.NN;}
  
  public void agorduMetodo(etMetodo met){
    this.metodo = met;
  }
}//class
