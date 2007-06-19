/*
 * Main.java
 *
 * Created on 4 czerwiec 2007, 13:40
 *
 */

package pri;

import java.util.Properties;

/**
 *
 * @author mignat
 */
public class Main {
  
    //2007.06.04    7h      7h  MG
    //2007.06.05    4h     11h  MG
    //2007.06.06    6h     17h  MG
    //2007.06.09    3h     20h  MG
    //2007.06.12    4h     24h  MG
    
    public static void main(String[] args) 
    {   /*
        Properties p = System.getProperties();
        p.list(System.out);
        System.out.println("-->" + p.getProperty("os.name") + "<---" );
        System.exit(0);
         */
        javax.swing.SwingUtilities.invokeLater (new Runnable() {
            public void run() {new ste.TController(); }
        });
    }

    
}
