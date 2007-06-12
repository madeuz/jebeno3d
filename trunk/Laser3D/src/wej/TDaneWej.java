/*
 * TDaneWej.java
 *
 * Created on 4 czerwiec 2007, 19:38
 *
 * Zamienia dane z lasera do formatu xyz.
 *
 * Format z lasera :
 *
 * Y
 * /\  -Z (w ty�)
 * |  /
 * | /
 * |/
 * ------> X (robot stoi wzd�� osi X)
 *
 */

package wej;
import ste.IBlock;
import obj.TObjConfig;

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
    
    private final static int PION_MIN = 0; //min. k�t w poziomie
    private final static int PION_MAX = 90;  //max k�t w poziomie

    private final static String LASER = "laser";
    private final static String PTZ = "ptz";
    private final static int ILE_LASER = 181; //ile pkt�w z lasera w 1. linii

    private final static String KOMENTARZ = "//"; //komentarz w oryginale
    private static final String SEPARATOR = " ";
    
    private final static int CO_ILE_OBJ = 100; //co kt�ry pkt ma wy�wietla�
    
    private double theVerStep, theHorStep, theMaxDist; //ustawienia pocz�tkowe.
    private double theMinPion, theMaxPion; //ustawienia pocz�tk. (tylko laser)
    private JSpinner theVerSpi = new JSpinner(new SpinnerNumberModel(
                                                        VER_STEP,0,100,0.1));
    private JSpinner theHorSpi = new JSpinner(new SpinnerNumberModel(
                                                        HOR_STEP,0,100,0.1));
    private JSpinner theMaxDistSpi= new JSpinner(new SpinnerNumberModel(
                                                        MAX_DIST,0,100,0.1));
    private JSpinner thePionMinSpi= new JSpinner(new SpinnerNumberModel(
                                                        PION_MIN,-100,100,0.1));
    private JSpinner thePionMaxSpi= new JSpinner(new SpinnerNumberModel(
                                                        PION_MAX,0,100,0.1));
    private int theGraphQnt; //co ile pkt�w bra� do grafiki.
    private JSpinner theGraphSpi = new JSpinner(new SpinnerNumberModel(
                                                    CO_ILE_OBJ,1,10000,10));
    private static int theMaxDistCnt = 0; //licznik ile poza zakresem
    private static double thePionAng = 0; //k�t obrotu lasera w pionie [stopnie]
    private String theObjFileN; //nazwa pliku .obj
    
    public TDaneWej() { } //koniec konstruktora

    public String getTabTitle()  {   return "Dane wej�ciowe";}//Laser/Gazebo ��> xyz"; }
    public JComponent getJComponent()
    {
        JPanel jp = new JPanel();
        jp.setBackground(Color.orange);
        
        //1. Dodanie przycisk�w (laser i Gazebo)
        JPanel jpB = new JPanel (new GridLayout(2,1,10,10)); //do przycisk�w
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

        //2. Dodanie ustawie� pocz�tkowych
        jp.add(getSetP());

        //3. Dodanie danych do wizualizacji
        jp.add(getObjP());

        return jp;
    } //koniec getJComponent
 
    private JComponent getSetP() //panel z ustawieniami pocz�tkowymi
    {
        JPanel jp = new JPanel(new GridLayout(0,2));
        Font font = new Font("System", Font.BOLD, 16);
        String[] lDesc = {  "Odczyt w pionie [�]", 
                            "Odczyt w poziomie [�]",
                            "Maks. odleg�o�� [m.]",
                            "Min. k�t w pione [�]",
                            "Maks. k�t w pione [�]",
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

        //Max odleg�o��
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        theMaxDistSpi.setFont(font);
        jp.add(theMaxDistSpi);

        //Min k�t w pionie
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        thePionMinSpi.setFont(font);
        jp.add(thePionMinSpi);

        //Max k�t w pionie
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        thePionMaxSpi.setFont(font);
        jp.add(thePionMaxSpi);

        jp.setBorder(BorderFactory.createTitledBorder("Ustawienia pocz�tkowe : "));
        return jp;
    } //koniec getSetP
    
    private JComponent getObjP() //panel z ustawieniami do wizualizacji
    {
        JPanel jp = new JPanel(new GridLayout(0,2));
        Font font = new Font("System", Font.BOLD, 16);
        String[] lDesc = {  "Co kt�ry punkt", 
                        };
        
        int _cnt = 0; //licznik pomocniczy
        
        //Co kt�ry punkt
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        
        theGraphSpi.setFont(font);
        jp.add(theGraphSpi);

        JButton objB = new JButton("Poka�");
        objB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {   TObjConfig.show(theObjFileN);   } //koniec aP
        });
        jp.add(objB);
        jp.setBorder(BorderFactory.createTitledBorder("Grafika : "));
        return jp;
    } //koniec getObjP
        
    private void checkSettings() //spr. i pobiera ustawienia pocz�tkowe
    {
        try {
            theVerStep = Double.parseDouble(theVerSpi.getValue().toString());
            theHorStep = Double.parseDouble(theHorSpi.getValue().toString());
            theMaxDist = Double.parseDouble(theMaxDistSpi.getValue().toString());
            theMinPion = Double.parseDouble(thePionMinSpi.getValue().toString());
            theMaxPion = Double.parseDouble(thePionMaxSpi.getValue().toString());
            theGraphQnt = Integer.parseInt(theGraphSpi.getValue().toString());
        } catch (NumberFormatException err) { //chyba niepotrzebne (?)
            showErr("B��dne ustawienia pocz�tkowe : " + err + 
                    "\nZostan� przyj�te ustawienia domy�lne !");
            theVerStep = VER_STEP; theHorStep = HOR_STEP; theMaxDist =MAX_DIST;
            theMinPion = PION_MIN;  theMaxPion = PION_MAX;
            theGraphQnt = CO_ILE_OBJ;
        } //koniec try-catch
    } //koniec checkSettings
    
    private void createObjFile(String aName)
    {
        int _kropka = aName.lastIndexOf(".");
        if (_kropka != -1) //cz. istnieje kropka w nazwiePliku
            theObjFileN = aName.substring(0, _kropka);
        theObjFileN = theObjFileN.concat("_xyz");
        
        //2. Tworz� plik .obj
        TObjConfig.openFile(theObjFileN);
    } //koniec createObjFile
    
    private void readFile(boolean isLaser) //(true-laser, false-gazebo)
    {
        //1. Sprawdzam, czy ustawienia s� prawid�owe. Je�eli tak, to pobieram
        checkSettings(); 
        if (theMaxDistCnt > 0) theMaxDistCnt = 0; //zeruj� licznik odrzuconych danych
        
        //1. Odczyt pliku
        JFileChooser  lReadChF = new JFileChooser(System.getProperty("user.dir"));
        int res = lReadChF.showDialog(null, "Wybierz plik");
        if (res == 1) return; //cz. wci�ni�to CANCEL
        File _file = lReadChF.getSelectedFile();
        if (_file != null)
            createObjFile(_file.getName()); //tworzy plik .obj

        //Odczytuje plik i zapami�tuj� wszystkie linie
        if (readFile1(_file, isLaser)) { //cz. uda�o si� odczyta� plik  
            TObjConfig.createFloor(); //rysow. pod�ogi
            TObjConfig.closeFile(); //Zamykam plik .obj
            JOptionPane.showMessageDialog(null, 
                "K O N I E C !\nPoza zakresem: " + theMaxDistCnt + " punkt�w",
                                    "Koniec", JOptionPane.INFORMATION_MESSAGE);
        } else
            JOptionPane.showMessageDialog(null, 
                    "B��d w odczycie pliku : " + _file,
                    "B��d w odczycie pliku",
                    JOptionPane.WARNING_MESSAGE);
    } //koniec readFile
    
    private boolean readFile1 (File aFile, boolean isLaser)
    {
        boolean outFlag; //info, czy uda�o si� odczyta� plik
        try {
            //1. Zapisuj� wszystko do ArrayList
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
            br.close(); //zamkni�cie strumienia;
            if (lLineAL.size() > 1) {
                String[] linT = new String[lLineAL.size()];
                linT = lLineAL.toArray(linT);
                int _ileCnt = 0; //licznik pomocniczy (do .obj)
                for (int i=0; i<linT.length; i++) {
                    //1. Kasuj� podw�jne spacje
                    while (linT[i].indexOf(SEPARATOR.concat(SEPARATOR)) != -1)
                        linT[i] = linT[i].replaceAll(SEPARATOR.concat(SEPARATOR), SEPARATOR);
                
                    ArrayList<TXyz> lXyzAL = null;
                    if (isLaser) lXyzAL = getElkaALFromLine(linT[i]);
                    else lXyzAL = getGazALFromLine(
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
                    
                    //Dodaj� punkt do pliku .obj
                    //int _ileCnt = 0; //licznik pomocniczy
                    for (int j=0; j<xT.length; j++) {
                        if (_ileCnt++ % theGraphQnt == 0) { //bior� co kt�ry� pkt
                            TObjConfig.setMinMax(xT[j], yT[j], zT[j]);
                            TObjConfig.cube(xT[j], yT[j], zT[j]);
                        } //koniec if
                    } //koniec for j
                } //koniec for i
                
                /************ dane do bloku Basi **************/
                ///BASIA.setInputData(xT, yT, zT);
                    
                outFlag = true;
            } else {
                showErr("Za ma�o danych");
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
    /*  Sekcja z obr�k� danych z lasera */
    /************************************/
    private ArrayList<TXyz> getElkaALFromLine(String aLine)
    {
        ArrayList<TXyz> lXyzAL = new ArrayList<TXyz>();
        
        String[] lStrT = aLine.split(SEPARATOR);
        if (lStrT.length < 4) { //cz. za ma�o danych w wszu
            showErr("b��d w wierszu: " + aLine);
            return lXyzAL;
        } //koniec if

        double lAlfaAng=0, lAlfaStep=0;
        double[] lLasT = new double[ILE_LASER];
        try { 
            if (lStrT[3].trim().equals(LASER)) {
                if (lStrT.length != 372) {
                    showErr("Z�a ilo�� danych w wierszu: " + ", jest: " + lStrT.length);
                    return lXyzAL;
                } //koniec if
                lAlfaAng = Double.parseDouble(lStrT[6]); //radiany
                lAlfaStep = Double.parseDouble(lStrT[8]); //radiany
                int _cnt = 10; //pierwsze 10 liczb nie ma znaczenia
                for (int i=0; i<lLasT.length; i++) {
                    lLasT[i] = Double.parseDouble(lStrT[_cnt]);
                    _cnt += 2;
                    //Obs�uguj� tylko gdy jest dobry k�t w poziomie
                    if (theMinPion <= thePionAng &&  thePionAng <= theMaxPion
                            && lLasT[i]<= theMaxDist) { //Spr. czy nie jest poza zakresem
                        lXyzAL.add(new TXyz(getXYZ(lLasT[i], 
                                Math.toDegrees(lAlfaAng), thePionAng, true)));
                        lAlfaAng += lAlfaStep; //krok bierze z pliku
                    } else { //cz. poza zakresem
                        theMaxDistCnt++;  //cz.ile odrzucono
                    } //koniec if-else
                } //koniec for
            } else if (lStrT[3].trim().equals(PTZ)) { //czyli zmiana k�ta w pionie
                thePionAng = Double.parseDouble(lStrT[7]);
            } //koniec if - else
        } catch (NumberFormatException err) {
            showErr ("B��d w konwersji typ�w w linii: " + aLine);
            return lXyzAL;
        } catch (ArrayIndexOutOfBoundsException err) {
            showErr ("B��d w ilo�ci zmiennch w linii: " + aLine);
            return lXyzAL;
        } //koniec try-catch
       
        return lXyzAL;
    } //koniec getElkaALFromLine
    //=== koniec sekcji z obr�bk� danych z lasera
    
    /************************************/
    /*  Sekcja z obr�k� danych z Gazebo */
    /************************************/
    private ArrayList<TXyz> getGazALFromLine(String[] aLinT, double aVerStep) //czytam 1lini� danych z Gazebo
    {
        ArrayList<TXyz> lXyzAL = new ArrayList<TXyz>();
        
        //1. Zamieniam String na double
        double[] lVarT = new double[aLinT.length];
        for (int i=0; i<lVarT.length; i++) {
            try { lVarT[i] = Double.parseDouble(aLinT[i].replace(',', '.').trim());   }
            catch (NumberFormatException err) {
                showErr("B��d w konwersji typu");
                return lXyzAL;
            } //koniec try-catch
        } //koniec for
        
        for (int i=0; i<lVarT.length; i++) { //ca�y plik
            if (lVarT[i]<= theMaxDist) { //Spr. czy nie jest poza zakresem
                // Dodaj� pkt do listy z pktami
                lXyzAL.add(new TXyz(getXYZ(lVarT[i], theHorStep*i, aVerStep, 
                                                                    false)));

            } else { //cz. za daleko
                theMaxDistCnt++; //zliczam ile poza zakresem
                //System.out.println ("odrzucam: lVarT[" + i + "]: " + lVarT[i]);
             }  //koniec if-else
        } //koniec for i
        return lXyzAL;
    } //koniec getGazALFromLine
    //=== koniec sekcji z obr�bk� danych z Gazebo
    
    private static double[] getXYZ(double aR, double aPoziom, double aPion, 
                                                            boolean isLaserF)
    {
        double[] lOutT = new double[3]; //x, y, z
        double lPionRad = Math.toRadians(aPion);
        double lPoziomRad = Math.toRadians(aPoziom);
        //double _absAlfa = (aAlfa > Math.PI/2d) ? -aAlfa : aAlfa;
        //lOutT[0] = round(aR * Math.cos(lPionRad) * Math.sin(lPoziomRad));//x
        lOutT[0] = round( isLaserF ?  //x
                    aR * Math.cos(lPionRad) * Math.cos(lPoziomRad) :  //laser
                    aR * Math.cos(lPionRad) * Math.sin(lPoziomRad)); //gazebo
        lOutT[1] = round(isLaserF ? //y
                    aR * Math.sin(lPoziomRad) : //laser
                    aR * Math.cos(lPoziomRad)); //gazebo
        lOutT[2] = round(isLaserF ? //h
                    aR * Math.sin(lPionRad) * Math.cos(lPoziomRad) : //laser
                    aR * Math.sin(lPionRad) * Math.sin(lPoziomRad));//gazebo
        return lOutT;
    } //koniec getXYZ

    //Metody pomocnicze
    public static void showErr(String aStr)
    {
        JOptionPane.showMessageDialog(null, 
                aStr, "B��d !", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    } //koniec showErr

    private static double round(double aVal)
    {
        BigDecimal bD = new BigDecimal(aVal); 
        return bD.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 
    } //koniec round
} //koniec klasy TDaneWej
