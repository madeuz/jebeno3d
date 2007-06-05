/*
 * TXyz.java
 *
 * Created on 5 czerwiec 2007, 13:32
 *
 * Klasa reprezentuje punkt xyz
 */

package wej;

/**
 *
 * @author mignat
 */
public class TXyz {
    
    double theX, theY, theZ;
    public TXyz(double[] aT) 
    {   if (aT.length == 3) {
            theX = aT[0];
            theY = aT[1];
            theZ = aT[2];
        } //koniec if
    } //koniec konstruktora
    
    public double getX()    {   return theX;    }
    public double getY()    {   return theY;    }
    public double getZ()    {   return theZ;    }
    
} //koniec klasy TXyz
