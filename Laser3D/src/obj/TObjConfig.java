/*
 * TObjConfig.java
 *
 * Created on 6 marzec 2007, 15:06
 *
 * Klasa s�u�y do prezentacji grafiki
 *
 */

package obj;

import java.io.*;
import javax.swing.*;

/**
 *
 * @author mignat
 */
public class TObjConfig {
    
    private final static float SZER_2 = 0.01f; //p� szeroko�ci sze�cianu
    private final static String FILE_EXTENTION = ".obj";
    private static PrintWriter thePrintW;

    //Otworzenie strumienia do zapisu.
    public static void openFile(String aFileN)
    {
        File lFile = new File(checkExtention(aFileN));
        try {   
            thePrintW = new PrintWriter(new BufferedWriter(
                                            new FileWriter(lFile, false)));
        } catch(IOException e)  {  
            System.out.println("Blad 1: " + e);
        } //koniec try-catch
    } //koniec openFile
    
    public static void closeFile()
    {   thePrintW.close();  }
    
    public static void show(String aFileN)
    {
        System.out.println ("show: aFileN: --|" + aFileN + "|--");
        try {
            Runtime.getRuntime().exec("java ObjLoad " + checkExtention(aFileN));
        } catch (IOException err) {
            JOptionPane.showMessageDialog(null, 
                    err, "B��d", JOptionPane.WARNING_MESSAGE);
        } //koniec try-catch
    } //koniec show
    
    public static void plane(double[][] aPtT, boolean aLeftF, boolean aRightF)
    {
        if (!isDataOk(aPtT, "p�aszczy�nie"))    return;
        StringBuffer outSB = new StringBuffer("# P�aszczyzna\ng Plaszczyzna\nusemtl "). 
                                            append("gray").append("\n");
        //Dodaj� punkty
        for (int i=0; i<aPtT.length; i++) {
            outSB.append("v ").append(aPtT[i][0]).append(" ").
                                append(aPtT[i][1]).append(" ").
                                append(aPtT[i][2]).append("\n");
        } //koniec for i
        //Dodaj� �ciany
        if (aLeftF)
            outSB.append("f ").
                append(-1).append(" ").
                append(-2).append(" ").
                append(-3).append(" ").
                append(-4).append("\n");
        
        if (aRightF)
            outSB.append("f ").
                append(-1).append(" ").
                append(-4).append(" ").
                append(-3).append(" ").
                append(-2).append("\n");
                
        thePrintW.println(outSB);
    } //koniec plane
    
    /* punkty:
     *     6---------7
     *  2,--------3, |
     *  |  |       | |
     *  |  |       | |
     *  |  5-------| 8
     *  1,--------4,
     */
    //Zwraca opis sze�cianu
    public static void cube(double aX, double aY, double aH)
    {   cube(aX, aY, aH, SZER_2);  } //domy�lny rozmiar sze�cianu
    
    public static void cube(double aX, double aY, double aH, double aSzer_2)
    {
        if (!isDataOk(aX, aY, aH, aSzer_2, 0, 0, "sze�cianie"))    return;
        
        //Obliczam pkty sze�cianu
        double minX, maxX, minY, maxY, minH, maxH;
        minX = aX-aSzer_2; maxX = aX+aSzer_2;
        minY = aY-aSzer_2; maxY = aY+aSzer_2;
        minH = aH-aSzer_2; maxH = aH+aSzer_2;
        
        //outSB.append("# Pod�oga\ng podloga\nusemtl red\nv ").
        //StringBuffer outSB = new StringBuffer("# Punkt").append("\nv ").
        StringBuffer outSB = new StringBuffer("# Sze�cian\ng punkt\nusemtl "). 
                                            append("gray").append("\nv ").
                                            //append(aKolStr).append("\nv ").
        
                append(minX).append(" "). //PKT 1
                append(minY).append(" ").
                append(minH).append("\nv ").
                
                append(minX).append(" "). //PKT 2
                append(minY).append(" ").
                append(maxH).append("\nv ").
                
                append(maxX).append(" "). //PKT 3
                append(minY).append(" ").
                append(maxH).append("\nv ").
                
                append(maxX).append(" "). //PKT 4
                append(minY).append(" ").
                append(minH).append("\nv ").
                
                append(minX).append(" "). //PKT 5
                append(maxY).append(" ").
                append(minH).append("\nv ").
                
                append(minX).append(" "). //PKT 6
                append(maxY).append(" ").
                append(maxH).append("\nv ").
                
                append(maxX).append(" "). //PKT 7
                append(maxY).append(" ").
                append(maxH).append("\nv ").
                
                append(maxX).append(" "). //PKT 8
                append(maxY).append(" ").
                append(minH).append("\nf ").
                
                append(-1).append(" "). //PRAWA SCIANA
                append(-4).append(" ").
                append(-3).append(" ").
                append(-2).append("\nf ").
        
                append(-5).append(" "). //LEWA SCIANA
                append(-6).append(" ").
                append(-7).append(" ").
                append(-8).append("\nf ").
                
                append(-1).append(" "). //GORA
                append(-2).append(" ").
                append(-6).append(" ").
                append(-5).append("\nf ").
                
                append(-3).append(" "). //DOL
                append(-4).append(" ").
                append(-8).append(" ").
                append(-7).append("\nf ").
                
                append(-1).append(" "). //PRZOD
                append(-5).append(" ").
                append(-8).append(" ").
                append(-4).append("\nf ").
                
                append(-2).append(" "). //TYL
                append(-3).append(" ").
                append(-7).append(" ").
                append(-6).append("\n");
                
                //append("s off"); //po co to jest ???
        thePrintW.println(outSB);
    } //koniec cube

    /* punkty:
     *     6---------7
     *  2,--------3, |
     *  |  |       | |
     *  |  |       | |
     *  |  |       | |
     *  |  |       | |
     *  |  5-------| 8
     *  1,--------4,
     */
    public static void polygon(double[][] aPtT)
    {
        if (!isDataOk(aPtT, "wielok�cie"))    return;
        
        if (aPtT.length != 8) return ; //za ma�o punkt�w
        if (aPtT[0].length != 3) return ; //za ma�o punkt�w
        StringBuffer outSB = new StringBuffer("# P�cian ");
        outSB.append("\ng p�cian\nusemtl ").append("gray").append("\n");
        for (byte i=0; i<aPtT.length; i++)
            outSB.append("v ").
                    append(aPtT[i][0]).append(" ").
                    append(aPtT[i][1]).append(" ").
                    append(aPtT[i][2]).append("\n");
                
        outSB.append("f ").
                append(-1).append(" "). //PRAWA SCIANA
                append(-4).append(" ").
                append(-3).append(" ").
                append(-2).append("\nf ").

                append(-5).append(" "). //LEWA SCIANA
                append(-6).append(" ").
                append(-7).append(" ").
                append(-8).append("\nf ").

                append(-1).append(" "). //GORA
                append(-2).append(" ").
                append(-6).append(" ").
                append(-5).append("\nf ").

                append(-3).append(" "). //DOL
                append(-4).append(" ").
                append(-8).append(" ").
                append(-7).append("\nf ").

                append(-1).append(" "). //PRZOD
                append(-5).append(" ").
                append(-8).append(" ").
                append(-4).append("\nf ").

                append(-2).append(" "). //TYL
                append(-3).append(" ").
                append(-7).append(" ").
                append(-6).append("\n");
                
        thePrintW.println(outSB);
    } //koniec polygon
    
    public static void line(double aX1, double aY1, double aZ1, 
                                            double aX2, double aY2, double aZ2)
    {
        if (!isDataOk(aX1, aY1, aZ1, aX2, aY2, aZ2, "linii"))    return;
        //Przedstawiam lini� jako p�cian
        double[][] pT = {
            {aX1-5*SZER_2, aY1, aZ1+5*SZER_2},
            {aX1+5*SZER_2, aY1, aZ1+5*SZER_2},
            {aX1-5*SZER_2, aY1, aZ1-5*SZER_2},
            {aX1+5*SZER_2, aY1, aZ1-5*SZER_2},
            
            {aX2-5*SZER_2, aY1, aZ2+5*SZER_2},
            {aX2+5*SZER_2, aY1, aZ2+5*SZER_2},
            {aX2-5*SZER_2, aY1, aZ2-5*SZER_2},
            {aX2+5*SZER_2, aY1, aZ2-5*SZER_2},
        };
        polygon(pT);
    } //koniec line
    
    //Rysowanie pod�ogi
    public static void createFloor(double xMin, double yMin, 
                                        double xMax, double yMax, double aWys)
    {
        if (!isDataOk(xMin, yMin, xMax, yMax, 0, 0, "pod�odze"))    return;
        double _min = aWys;
        StringBuffer lOutSB = new StringBuffer();
        lOutSB.append("# Pod�oga\ng podloga\nusemtl red\nv ").
                append(xMin).append(" ").
                append(yMin).append(" ").
                append(_min).append("\nv ").
                
                append(xMin).append(" ").
                append(yMax).append(" ").
                append(_min).append("\nv ").

                append(xMax).append(" ").
                append(yMax).append(" ").
                append(_min).append("\nv ").
                
                append(xMax).append(" ").
                append(yMin).append(" ").
                append(_min).append("\nf ").
                
                append(-1).append(" ").
                append(-2).append(" ").
                append(-3).append(" ").
                append(-4).append("\n").
                                
                append("s off");
        thePrintW.println(lOutSB);
    } //koniec createFloor
    
    //Sprawdzenie i ewentualnie dopisanie rozszerzenia
    public static String checkExtention (String aStr)
    {
        if (!aStr.endsWith(FILE_EXTENTION))
            aStr = aStr.concat(FILE_EXTENTION);
        return aStr;
    } //koniec checkExtention
    
    private static boolean isDataOk(double[][] aT, String aGdzie)
    {
        for (int i=0; i<aT.length; i++) {
            for (int j=0; j<aT[i].length; j++) {
                if (Double.isNaN(aT[i][j])) {
                    JOptionPane.showMessageDialog(null, "Jedna z warto�ci jest NaN "
                            + "w " + aGdzie, "GRAFIKA", 
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                } else if (Double.isInfinite(aT[i][j])) {
                    JOptionPane.showMessageDialog(null, "Jedna z warto�ci jest niesko�czono�ci� "
                            + "w " + aGdzie, "GRAFIKA", 
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                } //koniec if
            } //koniec for j
        } //koniec for i
        return true;
    } //koniec isDataOk
    
    private static boolean isDataOk(double aP1, double aP2, double aP3, 
                                    double aP4, double aP5, double aP6,
                                                            String aGdzie)
    {
        if (Double.isNaN(aP1) || Double.isNaN(aP2) || Double.isNaN(aP3) || 
                Double.isNaN(aP4) || Double.isNaN(aP5) || Double.isNaN(aP6)) {
            JOptionPane.showMessageDialog(null, "Jedna z warto�ci jest NaN "
                            + "w " + aGdzie, "GRAFIKA", JOptionPane.ERROR_MESSAGE);
            return false;
            
        } else if (Double.isInfinite(aP1) || Double.isInfinite(aP2) || 
                            Double.isInfinite(aP3) || Double.isInfinite(aP4) || 
                            Double.isInfinite(aP5) || Double.isInfinite(aP6)) {
            JOptionPane.showMessageDialog(null, "Jedna z warto�ci jest niesko�czono�ci� "
                            + "w " + aGdzie, "GRAFIKA", JOptionPane.ERROR_MESSAGE);
            return false;
        } //koniec if-else
        return true;
    } //koneic isDataOk
} //koniec klasy TObjConfig
