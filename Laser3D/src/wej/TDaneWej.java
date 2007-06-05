/*
 * TDaneWej.java
 *
 * Created on 4 czerwiec 2007, 19:38
 *
 * Zamienia dane z lasera do formatu xyz.
 *
 */

package wej;
import ste.IBlock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;

/**
 *
 * @author Michal
 */
public class TDaneWej implements IBlock {
    
    private static final double VER_STEP = 1; //co ile w pionie
    private static final double HOR_STEP = 1; //co ile w poziom
    private final static double MAX_DIST = 8d; //metry
    
    private final static int PTZ_MIN = 1; //min. k¹t w poziomie
    private final static int PTZ_MAX = 90;  //max k¹t w poziomie

    private final static String LASER = "laser";
    private final static String PTZ = "ptz";
    private final static int ILE_LASER = 181; //ile pktów z lasera w 1. linii

    private final static String KOMENTARZ = "//"; //komentarz w oryginale
    private static final String SEPARATOR = " ";
    
    private double theVerStep, theHorStep, theMaxDist; //ustawienia pocz¹tkowe.
    private JSpinner theVerSpi = new JSpinner(new SpinnerNumberModel(
                                                        VER_STEP,0,100,0.1));
    private JSpinner theHorSpi = new JSpinner(new SpinnerNumberModel(
                                                        HOR_STEP,0,100,0.1));
    private JSpinner theMaxSpi= new JSpinner(new SpinnerNumberModel(
                                                        MAX_DIST,0,100,0.1));
    
    private static int theMaxDistCnt = 0; //licznik ile poza zakresem
    private static double thePtzAng = 0; //k¹t obrotu lasera w pionie [rad]
        
    public TDaneWej() { } //koniec konstruktora

    public String getTabTitle()  {   return "Dane wejœciowe";}//Laser/Gazebo ––> xyz"; }
    public JComponent getJComponent()
    {
        JPanel jp = new JPanel();
        jp.setBackground(Color.orange);
        
        //1. Dodanie przycisków (laser i Gazebo)
        JPanel jpB = new JPanel (new GridLayout(2,1,10,10)); //do przycisków
        JButton lasB = new JButton("Wczytaj plik z lasera");
        lasB.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) { readFile(true); }
        });
        jpB.add(lasB);
        
        JButton gazB = new JButton("Wczytaj plik z Gazebo");
        gazB.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) { readFile(false); }
        });
        jpB.add(gazB);
        jp.add(jpB);

        //2. Dodanie ustawieñ pocz¹tkowych
        jp.add(getSetP());
        
        return jp;
    } //koniec getJComponent
 
    private JComponent getSetP() //panel z ustawieniami pocz¹tkowymi
    {
        JPanel jp = new JPanel(new GridLayout(0,2));
        Font font = new Font("System", Font.BOLD, 16);
        String[] lDesc = {  "Odczyt w pionie [°]", 
                            "Odczyt w poziomie [°]",
                            "Maks. odleg³oœæ [m.]",
                        };
        
        int _cnt = 0; //licznik pomocniczy
        
        //Odczyt w pionie
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        theVerSpi.setFont(font);
        jp.add(theVerSpi);

        //Odczyt w poziomie
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        theHorSpi.setFont(font);
        jp.add(theHorSpi);

        //Max odleg³oœæ
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        theMaxSpi.setFont(font);
        jp.add(theMaxSpi);

        jp.setBorder(BorderFactory.createTitledBorder("Ustawienia pocz¹tkowe : "));
        return jp;
    } //koniec getSetP
    
    private void checkSettings() //spr. i pobiera ustawienia pocz¹tkowe
    {
        try {
            theVerStep = Double.parseDouble(theVerSpi.getValue().toString());
            theHorStep = Double.parseDouble(theHorSpi.getValue().toString());
            theMaxDist = Double.parseDouble(theMaxSpi.getValue().toString());
        } catch (NumberFormatException err) { //chyba niepotrzebne (?)
            showErr("B³êdne ustawienia pocz¹tkowe : " + err + 
                    "\nZostan¹ przyjête ustawienia domyœlne !");
            theVerStep = VER_STEP; theHorStep = HOR_STEP; theMaxDist =MAX_DIST;
        } //koniec try-catch
    } //koniec checkSettings
    
    private void readFile(boolean isLaser) //(true-laser, false-gazebo)
    {
        //1. Sprawdzam, czy ustawienia s¹ prawid³owe. Je¿eli tak, to pobieram
        checkSettings(); 
        if (theMaxDistCnt > 0) theMaxDistCnt = 0; //zerujê licznik odrzuconych danych
        
        //1. Odczyt pliku
        JFileChooser  lReadChF = new JFileChooser(System.getProperty("user.dir"));
        int res = lReadChF.showDialog(null, "Wybierz plik");
        if (res == 1) return; //cz. wciœniêto CANCEL
        File _file = lReadChF.getSelectedFile();
        
        //Odczytuje plik i zapamiêtujê wszystkie linie
        if (readFile1(_file, isLaser)) { //cz. uda³o siê odczytaæ plik  
            JOptionPane.showMessageDialog(null, 
                "K O N I E C !", "Koniec", JOptionPane.INFORMATION_MESSAGE);
        } else
            JOptionPane.showMessageDialog(null, 
                    "B³¹d w odczycie pliku : " + _file,
                    "B³¹d w odczycie pliku",
                    JOptionPane.WARNING_MESSAGE);
    } //koniec readFile
    
    private boolean readFile1 (File aFile, boolean isLaser)
    {
        boolean outFlag; //info, czy uda³o siê odczytaæ plik
        try {
            //1. Zapisujê wszystko do ArrayList
            BufferedReader br = new BufferedReader(new FileReader(aFile));
            String line = null;
            ArrayList<String> lLineAL = new ArrayList<String>();
            while((line = br.readLine()) != null) {
                if (!line.startsWith(KOMENTARZ) && line.length()>0) { //komentarz
                    //Spr. czy jest komentarz w linii 
                    if (line.indexOf(KOMENTARZ) > 0) //czyli jest komentarz
                        line = line.substring(0, line.indexOf(KOMENTARZ));
                    lLineAL.add(line.trim());
                } //koniec if
            } //koniec while
            br.close(); //zamkniêcie strumienia;
            if (lLineAL.size() > 1) {
                String[] linT = new String[lLineAL.size()];
                linT = lLineAL.toArray(linT);
                
                for (int i=0; i<linT.length; i++) {
                    //1. Kasujê podwójne spacje
                    while (linT[i].indexOf(SEPARATOR.concat(SEPARATOR)) != -1)
                        linT[i] = linT[i].replaceAll(SEPARATOR.concat(SEPARATOR), SEPARATOR);
                
                    ArrayList<TXyz> lXyzAL = null;
                    if (isLaser) lXyzAL = createElkaALFromLine(linT[i]);
                    else    lXyzAL = createGazALFromLine(
                                        linT[i].split(SEPARATOR), i*theVerStep);
                    
                    //Zamieniam dane z fXyzAL na 3x[]
                    double[] xT = new double[lXyzAL.size()];
                    double[] yT = new double[xT.length];
                    double[] zT = new double[xT.length];
                    int _cnt = 0; //licznik pomocniczy
                    for (TXyz xyz : lXyzAL) {
                        xT[_cnt] = xyz.getX();
                        yT[_cnt] = xyz.getY();
                        zT[_cnt++] = xyz.getZ();
                    } //koniec for xyz
                } //koniec for i
                
                /************ dane do bloku Basi **************/
                ///BASIA.setInputData(xT, yT, zT);
                    
                outFlag = true;
            } else {
                showErr("Za ma³o danych");
                outFlag = false;
            } //koniec if-else
        } catch (FileNotFoundException err) {
            //System.err.println ("ERR_01: " + err);
            showErr("ERR_01: " + err);
            outFlag = false;
        } catch (IOException err) {
            //System.err.println ("ERR_02: " + err);
            showErr("ERR_02: " + err);
            outFlag = false;
        } //koniec try-catch
        return outFlag;
    } //koniec readFile1
    /************************************/
    /*  Sekcja z obrók¹ danych z lasera */
    /************************************/
    private ArrayList<TXyz> createElkaALFromLine(String aLine)
    {
        ArrayList<TXyz> lXyzAL = new ArrayList<TXyz>();
        System.out.println ("Analizujê liniê: --|" + aLine + "|--");
        
        String[] lStrT = aLine.split(SEPARATOR);
        if (lStrT.length < 4) { //cz. za ma³o danych w wszu
            showErr("b³¹d w wierszu: " + aLine);
            return lXyzAL;
        } //koniec if

        double lAlfaAng=0, lAlfaStep=0;
        double[] lLasT = new double[ILE_LASER];
        try { 
            if (lStrT[3].trim().equals(LASER)) {
                if (lStrT.length != 372) {
                    showErr("Z³a iloœæ danych w wierszu: " + ", jest: " + lStrT.length);
                    return lXyzAL;
                } //koniec if
                lAlfaAng = Double.parseDouble(lStrT[6]); //radiany
                lAlfaStep = Double.parseDouble(lStrT[8]); //radiany
                int _cnt = 10; //pierwsze 10 liczb nie ma znaczenia
                for (int i=0; i<lLasT.length; i++) {
                    lLasT[i] = Double.parseDouble(lStrT[_cnt]);
                    _cnt += 2;
                } //koniec for

                //Obs³ugujê tylko gdy jest dobry k¹t w poziomie
                //..................
                /*
                if (Math.toRadians(PTZ_MIN) <= thePtzAng &&  
                            thePtzAng <= Math.toRadians(PTZ_MAX))  //roboczo
                    pw.println(calcXYZ(thePtzAng, lLasT));
*/
            } else if (lStrT[3].trim().equals(PTZ)) { //czyli zmiana k¹ta w pionie
                thePtzAng = Math.toRadians(Double.parseDouble(lStrT[7]));
            } //koniec if - else
        } catch (NumberFormatException err) {
            showErr ("B³¹d w konwersji typów w linii: " + aLine);
            return lXyzAL;
        } catch (ArrayIndexOutOfBoundsException err) {
            showErr ("B³¹d w iloœci zmiennch w linii: " + aLine);
            return lXyzAL;
        } //koniec try-catch
       
        return lXyzAL;
    } //koniec createElkaALFromLine
    
    //---
    
    private static StringBuffer calcXYZ(double aPtzAng, double[] aLasT)
    {
        StringBuffer _outSB = new StringBuffer();
        //K¹t w poziomie zmienia siê od -pi/2 do pi/2
        double lAlfaAng = -Math.PI/2d;
        double xx, yy, zz;
        for (int i=0; i<aLasT.length; i++) {
//System.out.println("\n---\naPtzAng: " + Math.toDegrees(aPtzAng) + ", lAlfaAng: " + Math.toDegrees(lAlfaAng) + ", aLasT[" + i + "]: " + aLasT[i]);
         
            if (aLasT[i] < MAX_DIST) {

                xx = round(aLasT[i] * Math.cos(aPtzAng) * Math.cos(lAlfaAng));//x
                zz = round(aLasT[i] * Math.sin(lAlfaAng)); //y
                yy = round(aLasT[i] * Math.sin(aPtzAng) * Math.cos(lAlfaAng));//h

                _outSB.append(xx).append(SEPARATOR).append(yy).append(SEPARATOR).
                    append(zz).append(SEPARATOR).append("\n");
            } else {
//System.out.println ("max aLasT[" + i + "]: " + aLasT[i] + ", aPtzAng: " + Math.toDegrees(aPtzAng));
            } //koniec if-else
            lAlfaAng += HOR_STEP;
        } //koniec if
        return _outSB;
    } //koniec calcXYZ

    //=== koniec sekcji z obróbk¹ danych z lasera
    
    /************************************/
    /*  Sekcja z obrók¹ danych z Gazebo */
    /************************************/
    private ArrayList<TXyz> createGazALFromLine(String[] aLinT, double aVerStep) //czytam 1liniê danych z Gazebo
    {
        ArrayList<TXyz> lXyzAL = new ArrayList<TXyz>();
        
        //1. Zamieniam String na double
        double[] lVarT = new double[aLinT.length];
        for (int i=0; i<lVarT.length; i++) {
            try { lVarT[i] = Double.parseDouble(aLinT[i].trim());   }
            catch (NumberFormatException err) {
                showErr("B³¹d w konwersji typu");
                return lXyzAL;
            } //koniec try-catch
        } //koniec for
        
        System.out.println("analizeGazLine aLinT.l: " + aLinT.length);
        for (int i=0; i<lVarT.length; i++) { //ca³y plik
            if (lVarT[i]<= theMaxDist) { //Spr. czy nie jest poza zakresem
                // Dodajê pkt do listy z pktami
                lXyzAL.add(new TXyz(getXYZ(lVarT[i], aVerStep, theHorStep*i)));

            } else { //cz. za daleko
//System.out.println ("Odrzucam lVarT[" + i + "]: " + lVarT[i] + ", poziom: " + i*theHorStep + ", pion: " + aVerStep);
                theMaxDistCnt++; //zliczam ile poza zakresem
                /*                
                System.out.println ("odrzucam: aVarT[" + i + "]: " + aVarT[i] + 
                                    ", aAlfa: " + Math.toDegrees(HOR_STEP*i) + 
                                    ", aPtz: " + Math.toDegrees(aPtz));
                 */
             }  //koniec if-else
        } //koniec for i
        return lXyzAL;
    } //koniec analizeGazLine
    //=== koniec sekcji z obróbk¹ danych z Gazebo
    
    private static double[] getXYZ(double aR, double aPion, double aPoziom)
    {
        double[] lOutT = new double[3]; //x, y, z


System.out.println ("getXYZ aR: " + aR + ", pion: " + aPion + 
                                                    ", aPoziom: " + aPoziom);

        double lPionRad = Math.toRadians(aPion);
        double lPoziomRad = Math.toRadians(aPoziom);
        //double _absAlfa = (aAlfa > Math.PI/2d) ? -aAlfa : aAlfa;
        lOutT[0] = round(aR * Math.cos(lPionRad) * Math.sin(lPoziomRad));//x
        lOutT[1] = round(aR * Math.cos(lPoziomRad)); //y
        lOutT[2] = round(aR * Math.sin(lPionRad) * Math.sin(lPoziomRad));//h

        return lOutT;
    } //koniec getXYZ

    
    //Metody pomocnicze
    public static void showErr(String aStr)
    {
        JOptionPane.showMessageDialog(null, 
                aStr, "B³¹d !", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    } //koniec showErr

    private static double round(double aVal)
    {
        BigDecimal bD = new BigDecimal(aVal); 
        return bD.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 
    } //koniec round

} //koniec klasy TDaneWej
