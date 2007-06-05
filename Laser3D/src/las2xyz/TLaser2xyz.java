/*
 * TLaser2xyz.java
 *
 * Created on 4 czerwiec 2007, 19:38
 *
 * Zamienia dane z lasera do formatu xyz.
 *
 */

package las2xyz;
import ste.IBlock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Michal
 */
public class TLaser2xyz implements IBlock {
    
    private static final double VER_STEP = Math.toRadians(1); //co ile w pionie
    private static final double HOR_STEP = Math.toRadians(1); //co ile w poziom
    private final static double MAX_DIST = 8d; //metry
    private static final String SPACJA = " ";
    private double theVerStep, theHorStep, theMaxDist; //ustawienia pocz�tkowe.
    private JSpinner theVerSpi = new JSpinner(new SpinnerNumberModel(
                                    Math.toDegrees(VER_STEP),0,100,0.1));
    private JSpinner theHorSpi = new JSpinner(new SpinnerNumberModel(
                                    Math.toDegrees(HOR_STEP),0,100,0.1));
    private JSpinner theMaxSpi= new JSpinner(new SpinnerNumberModel(
                                    MAX_DIST,0,100,0.1));
    
    public TLaser2xyz() 
    {
    } //koniec konstruktora

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
        
        return jp;
    } //koniec getJComponent
 
    private JComponent getSetP() //panel z ustawieniami pocz�tkowymi
    {
        JPanel jp = new JPanel(new GridLayout(0,2));
        Font font = new Font("System", Font.BOLD, 16);
        String[] lDesc = {  "Odczyt w pionie [�]", 
                            "Odczyt w poziomie [�]",
                            "Maks. odleg�o�� [m.]",
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
        theMaxSpi.setFont(font);
        jp.add(theMaxSpi);

        jp.setBorder(BorderFactory.createTitledBorder("Ustawienia pocz�tkowe : "));
        return jp;
    } //koniec getSetP
    
    private void checkSettings() //spr. i pobiera ustawienia pocz�tkowe
    {
        try {
            theVerStep = Double.parseDouble(theVerSpi.getValue().toString());
            theHorStep = Double.parseDouble(theHorSpi.getValue().toString());
            theMaxDist = Double.parseDouble(theMaxSpi.getValue().toString());
        } catch (NumberFormatException err) { //chyba niepotrzebne (?)
            showErr("B��dne ustawienia pocz�tkowe : " + err + 
                    "\nZostan� przyj�te ustawienia domy�lne !");
            theVerStep = VER_STEP; theHorStep = HOR_STEP; theMaxDist =MAX_DIST;
        } //koniec try-catch
    } //koniec checkSettings
    
    private void readFile(boolean isLaser) //(true-laser, false-gazebo)
    {
        //1. Sprawdzam, czy ustawienia s� prawid�owe. Je�eli tak, to pobieram
        checkSettings(); 
        
        //1. Odczyt pliku
        JFileChooser  lReadChF = new JFileChooser(System.getProperty("user.dir"));
        int res = lReadChF.showDialog(null, "Wybierz plik");
        if (res == 1) return; //cz. wci�ni�to CANCEL
        File _file = lReadChF.getSelectedFile();
        
        //Odczytuje plik i zapami�tuj� wszystkie linie
        if (readFile1(_file, isLaser)) { //cz. uda�o si� odczyta� plik  
            JOptionPane.showMessageDialog(null, 
                "K O N I E C !", "Koniec", JOptionPane.INFORMATION_MESSAGE);
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
            BufferedReader br = new BufferedReader(
                                    new FileReader(aFile));
            String line = null;
            ArrayList<String> lLineAL = new ArrayList<String>();
            while((line = br.readLine()) != null) {
                if (!line.startsWith("//") && line.length()>0) { //komentarz
                    //Spr. czy jest komentarz w linii 
                    if (line.indexOf("//") > 0) //czyli jest komentarz
                        line = line.substring(0, line.indexOf("//"));
                    lLineAL.add(line.trim());
                } //koniec if
            } //koniec while
            br.close(); //zamkni�cie strumienia;
            if (lLineAL.size() > 1) {
                String[] linT = new String[lLineAL.size()];
                linT = lLineAL.toArray(linT);
                
                //1. Kasuj� podw�jne spacje
                for (int i=0; i<linT.length; i++) {
                    while (linT[i].indexOf(SPACJA.concat(SPACJA)) != -1)
                        linT[i] = linT[i].replaceAll(SPACJA.concat(SPACJA), SPACJA);
                
                    if (isLaser) ;
                    else    analizeGazLine(linT[i].split(" "), i*theHorStep);
                } //koniec for
                /*
                StringBuffer lXyzSB = analizeFile(linT);
                
                //String _objStr = TConvert2Obj.convert(TConvert.getCellAL());
                 
                String _objFileN = aFile.getName().substring(0, 
                                                 aFile.getName().indexOf('.'));
                writeXYZFile(_objFileN, lXyzSB.toString()); //tworz� plik xyz
                writeFile(_objFileN, _objStr);
                */
                outFlag = true;
            } else {
                showErr("Za ma�o danych");
                outFlag = false;
            }
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
 //.....
    
    //=== koniec sekcji z obr�bk� danych z lasera
    
    /************************************/
    /*  Sekcja z obr�k� danych z Gazebo */
    /************************************/
    private void analizeGazLine(String[] aLinT, double aHorStep) //czytam 1lini� danych z Gazebo
    {
        ArrayList lxAL = new ArrayList();
        //1. Zamieniam String na double
        double[] lVarT = new double[aLinT.length];
        for (int i=0; i<lVarT.length; i++) {
            try { lVarT[i] = Double.parseDouble(aLinT[i].trim());   }
            catch (NumberFormatException err) {
                showErr("B��d w konwersji typu");
                return;
            } //koniec try-catch
        } //koniec for
        
        System.out.println("analizeGazLine aLinT.l: " + aLinT.length);
        for (int i=0; i<lVarT.length; i++) { //ca�y plik
            if (lVarT[i]<= theMaxDist) { //Spr. czy nie jest poza zakresem
System.out.println ("Licz� lVarT[" + i + "]: " + lVarT[i] + ", poziom: " + 
                                                    i + ", pion: " + aHorStep);
                //double[] lXyzT = getXYZ(aVarT[i], aPtz, HOR_STEP*i);
                /*
                //2. Dodaj� dane do SB
                lOutSB.append(lXyzT[0]).append(" ").
                        append(lXyzT[1]).append(" ").
                        append(lXyzT[2]).append("\n");
                 */
            } else { //cz. za daleko
System.out.println ("Odrzucam lVarT[" + i + "]: " + lVarT[i] + ", poziom: " + 
                                                    i + ", pion: " + aHorStep);
                /*
                theMaxDistCnt++; //zliczam ile poza zakresem
                
                System.out.println ("odrzucam: aVarT[" + i + "]: " + aVarT[i] + 
                                    ", aAlfa: " + Math.toDegrees(HOR_STEP*i) + 
                                    ", aPtz: " + Math.toDegrees(aPtz));
                 */
             }  //koniec if-else
        } //koniec for i
        
        //String _maxStr = theMaxSpi.getValue().toString();
        //double _max = Double.parseDouble(theMaxSpi.getValue().toString());
        
        /*
        //1. Zamieniam biegunowe na xyz
        for (int i=0; i<aVarT.length; i++) {
            if (aVarT[i]<= MAX_DIST) { //Spr. czy nie jest poza zakresem
                double[] lXyzT = getXYZ(aVarT[i], aPtz, HOR_STEP*i);
                //2. Dodaj� dane do SB
                lOutSB.append(lXyzT[0]).append(" ").
                        append(lXyzT[1]).append(" ").
                        append(lXyzT[2]).append("\n");
            } else //cz. za daleko
                theMaxDistCnt++; //zliczam ile poza zakresem
                
                System.out.println ("odrzucam: aVarT[" + i + "]: " + aVarT[i] + 
                                    ", aAlfa: " + Math.toDegrees(HOR_STEP*i) + 
                                    ", aPtz: " + Math.toDegrees(aPtz));
                 
        } //koniec for i
*/
    } //koniec analizeGazLine
    //=== koniec sekcji z obr�bk� danych z Gazebo
    
    /*
    private static double[] getXYZ(double aR, double aPtz, double aAlfa)
    {
        double[] lOutT = new double[3]; //x, y, z


System.out.println ("getXYZ aR: " + aR + ", aPtz: " + aPtz + ", aAlfa: " + aAlfa + 
        ", aPtz(stopnie): " + Math.toDegrees(aPtz) + ", aAlfa: " + Math.toDegrees(aAlfa));

        //double _absAlfa = (aAlfa > Math.PI/2d) ? -aAlfa : aAlfa;
        lOutT[0] = round(aR * Math.cos(aPtz) * Math.sin(aAlfa));//x
        lOutT[2] = round(aR * Math.cos(aAlfa)); //y
        lOutT[1] = round(aR * Math.sin(aPtz) * Math.sin(aAlfa));//h

        //fPixAL.add(new TPixel(lOutT[0], lOutT[2], lOutT[1]));
        return lOutT;
    } //koniec getXYZ
      */
    
    //Metody pomocnicze
    public static void showErr(String aStr)
    {
        JOptionPane.showMessageDialog(null, 
                aStr, "B��d !", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    } //koniec showErr

    
} //koniec klasy TLaser2xyz
