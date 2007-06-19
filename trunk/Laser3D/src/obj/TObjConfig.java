/*
 * TObjConfig.java
 *
 * Created on 6 marzec 2007, 15:06
 *
 * Klasa sluly do prezentacji grafiki
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
    
    private final static double INF = 999999; //nieskolczonoll
    private final static float SZER_2 = 0.01f; //pl szerokolci Szescianu
    private final static String FILE_EXTENTION = ".obj";
    private static PrintWriter thePrintW;
    private static double theMinX=INF, theMaxX=-1*INF,
            theMinY=INF, theMaxY=-1*INF, theMinH=INF, theMaxH=-1*INF;
    
    //Otworzenie strumienia do zapisu.
    public static void openFile(String aFileN) {
        //Kasujl min-max podlogi
        theMinX=INF; theMaxX=-1*INF; theMinY=INF; theMaxY=-1*INF;
        theMinH=INF; theMaxH=-1*INF;
        
        File lFile = new File(checkExtention(aFileN));
        if (lFile.exists()) lFile.delete();
        try {
            thePrintW = new PrintWriter(new BufferedWriter(
                    new FileWriter(lFile, false)));
        } catch(IOException e)  {
            System.out.println("Blad 1: " + e);
        } //koniec try-catch
    } //koniec openFile
    
    public static void closeFile() {
        thePrintW.close();  }
    
    public static void show(String aFileN) {
        try {
            if(System.getProperty("os.name").trim().startsWith("Linux") ){
                
            } else {
                Runtime.getRuntime().exec(
                        new String[] {"cmd", "/c", "start java ObjLoad " +
                                checkExtention(aFileN)});
            } //konic try-catch
        } catch (IOException err) {
            JOptionPane.showMessageDialog(null,
                    err, "Blad", JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException err) {
            JOptionPane.showMessageDialog(null,
                    err, "Blad", JOptionPane.WARNING_MESSAGE);
        } //koniec try-catch
    } //koniec show
    
    public static void plane(double[][] aPtT, boolean aLeftF, boolean aRightF) {
        if (!isDataOk(aPtT, "plaszczylnie"))    return;
        StringBuffer outSB = new StringBuffer("# Plaszczyzna\ng Plaszczyzna\nusemtl ").
                append("gray").append("\n");
        //Dodajl punkty
        for (int i=0; i<aPtT.length; i++) {
            outSB.append("v ").append(aPtT[i][0]).append(" ").
                    append(aPtT[i][1]).append(" ").
                    append(aPtT[i][2]).append("\n");
        } //koniec for i
        //Dodajl lciany
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
    //Zwraca opis Szescianu
    public static void cube(double aX, double aY, double aH) {
        cube(aX, aY, aH, SZER_2);  } //domyllny rozmiar Szescianu
    
    public static void cube(double aX, double aY, double aH, double aSzer_2) {
        if (!isDataOk(aX, aY, aH, aSzer_2, 0, 0, "Szescianie"))    return;
        
        //Obliczam pkty Szescianu
        double minX, maxX, minY, maxY, minH, maxH;
        minX = aX-aSzer_2; maxX = aX+aSzer_2;
        minY = aY-aSzer_2; maxY = aY+aSzer_2;
        minH = aH-aSzer_2; maxH = aH+aSzer_2;
        
        //outSB.append("# Podloga\ng podloga\nusemtl red\nv ").
        //StringBuffer outSB = new StringBuffer("# Punkt").append("\nv ").
        StringBuffer outSB = new StringBuffer("# Szescian\ng punkt\nusemtl ").
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
    public static void polygon(double[][] aPtT) { polygon(aPtT, "gray"); }
    public static void polygon(double[][] aPtT, String aColStr) {
        if (!isDataOk(aPtT, "wielokacie"))    return;
        
        if (aPtT.length != 8) return ; //za malo punktlw
        if (aPtT[0].length != 3) return ; //za malo punktlw
        StringBuffer outSB = new StringBuffer("# Plcian ");
        //outSB.append("\ng plcian\nusemtl ").append("gray").append("\n");
        outSB.append("\ng plcian\nusemtl ").append(aColStr).append("\n");
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
            double aX2, double aY2, double aZ2) {
        line(aX1, aY1, aZ1, aX2, aY2, aZ2, "gray");
    } //koniec line
    public static void line(double aX1, double aY1, double aZ1,
            double aX2, double aY2, double aZ2, String aColStr) {
        if (!isDataOk(aX1, aY1, aZ1, aX2, aY2, aZ2, "linii"))    return;
        //Przedstawiam linil jako plcian
        int _factor = 2;
        double[][] pT = {
            {aX1-_factor*SZER_2, aY1, aZ1+_factor*SZER_2},
            {aX1+_factor*SZER_2, aY1, aZ1+_factor*SZER_2},
            {aX1-_factor*SZER_2, aY1, aZ1-_factor*SZER_2},
            {aX1+_factor*SZER_2, aY1, aZ1-_factor*SZER_2},
            
            {aX2-_factor*SZER_2, aY1, aZ2+_factor*SZER_2},
            {aX2+_factor*SZER_2, aY1, aZ2+_factor*SZER_2},
            {aX2-_factor*SZER_2, aY1, aZ2-_factor*SZER_2},
            {aX2+_factor*SZER_2, aY1, aZ2-_factor*SZER_2},
        };
        polygon(pT, aColStr);
    } //koniec line
    
    //Zapamiltujl min i max
    public static void setMinMax(double aX, double aY, double aH) {
        if (aX < theMinX)   theMinX = aX;
        if (aX > theMaxX)   theMaxX = aX;
        if (aY < theMinY)   theMinY = aY;
        if (aY > theMaxY)   theMaxY = aY;
        if (aH < theMinH)   theMinH = aH;
        if (aH > theMaxH)   theMaxH = aH;
    } //koniec setMinMax
    
    //Rysowanie podlogi
    public static void createFloor() {
        createFloor(theMinX, theMinY, theMaxX, theMaxY, 0);
    } //koniec createFloor
    
    public static void createFloor(double xMin, double yMin,
            double xMax, double yMax, double aWys) {
        if (!isDataOk(xMin, yMin, xMax, yMax, 0, 0, "podlodze"))    return;
        double _min = aWys;
        StringBuffer lOutSB = new StringBuffer();
        lOutSB.append("# Podloga\ng podloga\nusemtl red\nv ").
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
    public static String checkExtention(String aStr) {
        if (!aStr.endsWith(FILE_EXTENTION))
            aStr = aStr.concat(FILE_EXTENTION);
        return aStr;
    } //koniec checkExtention
    
    private static boolean isDataOk(double[][] aT, String aGdzie) {
        for (int i=0; i<aT.length; i++) {
            for (int j=0; j<aT[i].length; j++) {
                if (Double.isNaN(aT[i][j])) {
                    JOptionPane.showMessageDialog(null, "Jedna z wartosci jest NaN "
                            + "w " + aGdzie, "GRAFIKA",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                } else if (Double.isInfinite(aT[i][j])) {
                    JOptionPane.showMessageDialog(null, "Jedna z wartosci jest nieskonczonoscia "
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
            String aGdzie) {
        if (Double.isNaN(aP1) || Double.isNaN(aP2) || Double.isNaN(aP3) ||
                Double.isNaN(aP4) || Double.isNaN(aP5) || Double.isNaN(aP6)) {
            JOptionPane.showMessageDialog(null, "Jedna z wartosci jest NaN "
                    + "w " + aGdzie, "GRAFIKA", JOptionPane.ERROR_MESSAGE);
            return false;
            
        } else if (Double.isInfinite(aP1) || Double.isInfinite(aP2) ||
                Double.isInfinite(aP3) || Double.isInfinite(aP4) ||
                Double.isInfinite(aP5) || Double.isInfinite(aP6)) {
            JOptionPane.showMessageDialog(null, "Jedna z wartosci jest nieskonczonoscia "
                    + "w " + aGdzie, "GRAFIKA", JOptionPane.ERROR_MESSAGE);
            return false;
        } //koniec if-else
        return true;
    } //koneic isDataOk
} //koniec klasy TObjConfig
