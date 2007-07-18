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
 * /\  -Z (w tyl)
 * |  /
 * | /
 * |/
 * ------> X (robot stoi wzdloz osi X)
 *
 */

package wej;
import ste.TAbstBlock;
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
public class TDaneWej extends TAbstBlock {
    
    protected final static int CO_ILE_OBJ = 100; //co ktory pkt ma wyswietlac
    
    private final static double VER_STEP = 1; //co ile w pionie
    private final static double HOR_STEP = 1; //co ile w poziom
    private final static double MAX_DIST = 8d; //metry
    
    private final static int PION_MIN = 0; //min. kat w poziomie
    private final static int PION_MAX = 90;  //max kat w poziomie
    
    private final static String LASER = "laser";
    private final static String PTZ = "ptz";
    private final static int ILE_LASER = 181; //ile pktow z lasera w 1. linii
    
    private final static String KOMENTARZ = "//"; //komentarz w oryginale
    private final static String SEPARATOR = " ";
    
    private double theVerStep, theHorStep, theMaxDist; //ustawienia poczatkowe.
    private double theMinPion, theMaxPion; //ustawienia poczatk. (tylko laser)
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
    private static int theMaxDistCnt = 0; //licznik ile poza zakresem
    private static double thePionAng = 0; //kat obrotu lasera w pionie [stopnie]
    
    
    public TDaneWej() 
    { theGraphSpi = new JSpinner(new SpinnerNumberModel(CO_ILE_OBJ,1,10000,10));
    } //koniec konstruktora
    
    public void setNext(TAbstBlock aN)  {   theNext = aN;   } //koniec setNext
    public String getTabTitle()  {   return "Dane wejsciowe";}//Laser/Gazebo ï¿½ï¿½> xyz"; }
    public JComponent getJComponent() {
        JPanel jp = new JPanel();
        jp.setBackground(Color.orange);
        
        //1. Dodanie przyciskow (laser i Gazebo)
        JPanel jpB = new JPanel(new GridLayout(2,1,10,10)); //do przyciskow
        JButton lasB = new JButton("Wczytaj plik z lasera");
        lasB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { readFile(true); }
        });
        jpB.add(lasB);
        
        JButton gazB = new JButton("Wczytaj plik z Gazebo");
        gazB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { readFile(false); }
        });
        jpB.add(gazB);
        jp.add(jpB);
        
        //2. Dodanie ustawien poczatkowych
        jp.add(getSetP());
        
        //3. Dodanie danych do wizualizacji
        jp.add(getObjP());
        
        return jp;
    } //koniec getJComponent
    
    private JComponent getSetP() //panel z ustawieniami poczatkowymi
    {
        JPanel jp = new JPanel(new GridLayout(0,2));
        Font font = new Font("System", Font.BOLD, 16);
        String[] lDesc = {  "Odczyt w pionie [st.]",
        "Odczyt w poziomie [st.]",
        "Maks. odleglosc [m.]",
        "Min. kat w pionie [st.]",
        "Maks. kat w pionie [st.]",
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
        
        //Max odleglosc
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        theMaxDistSpi.setFont(font);
        jp.add(theMaxDistSpi);
        
        //Min kat w pionie
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        thePionMinSpi.setFont(font);
        jp.add(thePionMinSpi);
        
        //Max kat w pionie
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        thePionMaxSpi.setFont(font);
        jp.add(thePionMaxSpi);
        
        jp.setBorder(BorderFactory.createTitledBorder("Ustawienia poczatkowe : "));
        return jp;
    } //koniec getSetP
    
    public void checkSettings() //spr. i pobiera ustawienia poczatkowe
    {
        try {
            theVerStep = Double.parseDouble(theVerSpi.getValue().toString());
            theHorStep = Double.parseDouble(theHorSpi.getValue().toString());
            theMaxDist = Double.parseDouble(theMaxDistSpi.getValue().toString());
            theMinPion = Double.parseDouble(thePionMinSpi.getValue().toString());
            theMaxPion = Double.parseDouble(thePionMaxSpi.getValue().toString());
            theGraphQnt = Integer.parseInt(theGraphSpi.getValue().toString());
        } catch (NumberFormatException err) { //chyba niepotrzebne (?)
            showErr("Bledne ustawienia poczatkowe : " + err +
                    "\nZostana przyjete ustawienia domyslne !");
            theVerStep = VER_STEP; theHorStep = HOR_STEP; theMaxDist =MAX_DIST;
            theMinPion = PION_MIN;  theMaxPion = PION_MAX;
            theGraphQnt = CO_ILE_OBJ;
        } //koniec try-catch
        
        //Spr. ustawienia pocz¹tkowe nastêpnego modu³u
        if (theNext != null)    theNext.checkSettings();
    } //koniec checkSettings
    
    public void createObjFile(String aName) {
        int _kropka = aName.lastIndexOf(".");
        if (_kropka != -1) //cz. istnieje kropka w nazwiePliku
            theObjFileN = aName.substring(0, _kropka);
        theObjFileN = theObjFileN.concat("_xyz");
        
        //2. Tworze plik .obj
        //TObjConfig.openFile(theObjFileN);
        System.out.println("otwieram plik theObjFileN: --|" + theObjFileN + "|--");
        fObjConf.openFile(theObjFileN);
        
        //3. Przekazujê nastêpnym obiektom nazwê pliku
        theNext.createObjFile(aName);
    } //koniec createObjFile
    
    private void readFile(boolean isLaser) //(true-laser, false-gazebo)
    {
        //1. Sprawdzam, czy ustawienia sa prawidlowe. Jezeli tak, to pobieram
        checkSettings();
        if (theMaxDistCnt > 0) theMaxDistCnt = 0; //zeruje licznik odrzuconych danych
        
        //1. Odczyt pliku
        JFileChooser  lReadChF = new JFileChooser(System.getProperty("user.dir"));
        int res = lReadChF.showDialog(null, "Wybierz plik");
        if (res == 1) return; //cz. wcisnieto CANCEL
        File _file = lReadChF.getSelectedFile();
        if (_file != null)
            createObjFile(_file.getName()); //tworzy plik .obj
        
        //Odczytuje plik i zapamietuje wszystkie linie
        if (readFile1(_file, isLaser)) { //cz. udalo sie odczytac plik
            //TObjConfig.createFloor(); //rysow. podlogi
            closeGraph(); //zamyka grafikê
            /*
            fObjConf.createFloor(); //rysow. podlogi
            //Jezeli zaznaczono, pokazuje pozycje robota
            if (theRobPozChB.isSelected())
                fObjConf.line(0,0,0,0,0,2, "green"); //linia pionowa przechodzaca przez (0,0)
            fObjConf.closeFile(); //Zamykam plik .obj
             */
            JOptionPane.showMessageDialog(null,
                    "K O N I E C !\nPoza zakresem: " + theMaxDistCnt + " punktow",
                    "Koniec", JOptionPane.INFORMATION_MESSAGE);
        } else
            JOptionPane.showMessageDialog(null,
                    "Blad w odczycie pliku : " + _file,
                    "Blad w odczycie pliku",
                    JOptionPane.WARNING_MESSAGE);
    } //koniec readFile
    
    private boolean readFile1(File aFile, boolean isLaser) {
        boolean outFlag; //info, czy udalo sie odczytac plik
        try {
            //1. Zapisuje wszystko do ArrayList
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
            br.close(); //zamkniecie strumienia;
            if (lLineAL.size() > 1) {
                String[] linT = new String[lLineAL.size()];
                linT = lLineAL.toArray(linT);
                int _ileCnt = 0; //licznik pomocniczy (do .obj)
                for (int i=0; i<linT.length; i++) {
                    //1. Kasuje podwojne spacje
                    while (linT[i].indexOf(SEPARATOR.concat(SEPARATOR)) != -1)
                        linT[i] = linT[i].replaceAll(SEPARATOR.concat(SEPARATOR), SEPARATOR);
                    
                    ArrayList<TXyz> lXyzAL = null;
                    if (isLaser) lXyzAL = getElkaALFromLine(linT[i]);
                    else lXyzAL = getGazALFromLine(
                            linT[i].split(SEPARATOR), i*theVerStep);
                    
                    if(lXyzAL.size() == 0) //ignorujê tê liniê (np. ## Player version 1.6.5 )
                        continue;
                    
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
                    
                    //Dodaje punkt do pliku .obj
                    //int _ileCnt = 0; //licznik pomocniczy
                    for (int j=0; j<xT.length; j++) {
                        if (_ileCnt++ % theGraphQnt == 0) { //biore co ktorys pkt
                            fObjConf.setMinMax(xT[j], yT[j], zT[j]);
                            fObjConf.cube(xT[j], yT[j], zT[j]);
                        } //koniec if
                    } //koniec for j
                    /************ dane do bloku Basi **************/
                    ((odc.Analizator)theNext).setInputData(xT, yT, zT, thePionAng);//i);
                    
                } //koniec for i
                
                outFlag = true;
            } else {
                showErr("Za malo danych");
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
    /*  Sekcja z obroka danych z lasera */
    /************************************/
    private ArrayList<TXyz> getElkaALFromLine(String aLine) {
        ArrayList<TXyz> lXyzAL = new ArrayList<TXyz>();
        
        String[] lStrT = aLine.split(SEPARATOR);
        if (lStrT.length < 4) { //cz. za malo danych w wszu
            showErr("blad w wierszu: " + aLine);
            return lXyzAL;
        } //koniec if
        double lAlfaAng=0, lAlfaStep=0;
        double[] lLasT = new double[ILE_LASER];
        try {
            if (lStrT[3].trim().equals(LASER)) {
                if (lStrT.length != 372) {
                    showErr("Zla ilosc danych w wierszu: " + ", jest: " + lStrT.length);
                    return lXyzAL;
                } //koniec if
                lAlfaAng = Double.parseDouble(lStrT[6]); //radiany
                lAlfaStep = Double.parseDouble(lStrT[8]); //radiany
                int _cnt = 10; //pierwsze 10 liczb nie ma znaczenia
                for (int i=0; i<lLasT.length; i++) {
                    lLasT[i] = Double.parseDouble(lStrT[_cnt]);
                    _cnt += 2;
                    //Obsluguje tylko gdy jest dobry kat w poziomie
                    if (theMinPion <= thePionAng &&  thePionAng <= theMaxPion
                            && lLasT[i]<= theMaxDist) { //Spr. czy nie jest poza zakresem
                        
                        lXyzAL.add(new TXyz(getXYZ(lLasT[i],
                                Math.toDegrees(lAlfaAng), thePionAng, true)));
                        lAlfaAng += lAlfaStep; //krok bierze z pliku
                    } else { //cz. poza zakresem
                        theMaxDistCnt++;  //cz.ile odrzucono
                    } //koniec if-else
                } //koniec for
            } else if (lStrT[3].trim().equals(PTZ)) { //czyli zmiana kata w pionie
                thePionAng = Double.parseDouble(lStrT[7]);
            } //koniec if - else
        } catch (NumberFormatException err) {
            showErr("Blad w konwersji typow w linii: " + aLine);
            return lXyzAL;
        } catch (ArrayIndexOutOfBoundsException err) {
            showErr("Blad w ilosci zmiennch w linii: " + aLine);
            return lXyzAL;
        } //koniec try-catch
        
        return lXyzAL;
    } //koniec getElkaALFromLine
    //=== koniec sekcji z obrobka danych z lasera
    
    /************************************/
    /*  Sekcja z obroka danych z Gazebo */
    /************************************/
    private ArrayList<TXyz> getGazALFromLine(String[] aLinT, double aVerStep) //czytam 1linie danych z Gazebo
    {
        ArrayList<TXyz> lXyzAL = new ArrayList<TXyz>();
        
        //1. Zamieniam String na double
        double[] lVarT = new double[aLinT.length];
        for (int i=0; i<lVarT.length; i++) {
            try { lVarT[i] = Double.parseDouble(aLinT[i].replace(',', '.').trim());   } catch (NumberFormatException err) {
                showErr("Blad w konwersji typu");
                return lXyzAL;
            } //koniec try-catch
        } //koniec for
        
        for (int i=0; i<lVarT.length; i++) { //caly plik
            if (lVarT[i]<= theMaxDist) { //Spr. czy nie jest poza zakresem
                // Dodaje pkt do listy z pktami
                lXyzAL.add(new TXyz(getXYZ(lVarT[i], theHorStep*i, aVerStep,
                        false)));
            } else { //cz. za daleko
                theMaxDistCnt++; //zliczam ile poza zakresem
            }  //koniec if-else
        } //koniec for i
        return lXyzAL;
    } //koniec getGazALFromLine
    //=== koniec sekcji z obrobka danych z Gazebo
    
    private static double[] getXYZ(double aR, double aPoziom, double aPion,
            boolean isLaserF) {
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
    private static double round(double aVal) {
        BigDecimal bD = new BigDecimal(aVal);
        return bD.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    } //koniec round
} //koniec klasy TDaneWej
