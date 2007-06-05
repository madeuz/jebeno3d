/*
 * Main.java
 *
 * Created on 4 czerwiec 2007, 13:40
 *
 */

package pri;

/**
 *
 * @author mignat
 */
public class Main {
  
    //2007.06.04    7h      7h  MG
    //2007.06.05    4h     11h  MG
    
    public static void main(String[] args) 
    {   
        javax.swing.SwingUtilities.invokeLater (new Runnable() {
            public void run() {new ste.TController(); }
        });
    }

    
}
