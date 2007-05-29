package j3Dobj;
/*
 * KreuObj2.java
 *
 * Created on 6 marzec 2007, 15:06
 *
 * Klasa s�u�y do tworzenia
 *
 */

import java.io.*;

/**
 *
 * @author mignat
 */
public class KreuObj2 {
    
    private final static float SZER_2 = 0.01f; //p� szeroko�ci sze�cianu
    private final static String FILE_EXTENTION = ".obj";
    private static PrintWriter thePrintW;

    /**
    * Otworzenie strumienia do zapisu.
    * @param aFileN 
    */
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
    /** */
    public static void closeFile()
    {   thePrintW.close();  }
    
    /**
    * 
    * @param aPtT 
    * @param aLeftF 
    * @param aRightF 
    */
    public static void plane(double[][] aPtT, boolean aLeftF, boolean aRightF)
    {
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
    
    /**
    * punkty:
    *     6---------7
    *  2,--------3, |
    *  |  |       | |
    *  |  |       | |
    *  |  5-------| 8
    *  1,--------4,     
    * Zwraca opis sześcianu
    * @param aX 
    * @param aY 
    * @param aH 
    */
    public static void cube(double aX, double aY, double aH)
    {   cube(aX, aY, aH, SZER_2);  } //domy�lny rozmiar sze�cianu
    /**
    * 
    * @param aX 
    * @param aY 
    * @param aH 
    * @param aSzer_2 
    */
    public static void cube(double aX, double aY, double aH, double aSzer_2)
    {
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
//===
    
    /**
    * Rysowanie podłogi
    * @param xMin 
    * @param yMin 
    * @param xMax 
    * @param yMax 
    * @param aWys 
    */
    public static void createFloor(double xMin, double yMin, 
                                            double xMax, double yMax, int aWys)
    {
        //double _min = Math.min(0, aWys); //gdy pod�oga jest na wys<0 
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
    
    /**
    * Sprawdzenie i ewentualnie dopisanie rozszerzenia
    * @param aStr 
    * @return 
    */
    public static String checkExtention (String aStr)
    {
        if (!aStr.endsWith(FILE_EXTENTION))
            aStr = aStr.concat(FILE_EXTENTION);
        return aStr;
    } //koniec checkExtention
} //koniec klasy KreuObj2
