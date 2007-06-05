/*
 * TBarIcon.java
 *
 * Created on 17 sierpieñ 2004, 22:45
 */

package gra;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author  Michal Gnatowski
 */

//klasa tylko wstawia ikonê w pasku tytulowym
public class TBarIcon extends JFrame { //klasa tylko ustawia ikonê w pasku

    public final static  String IMG = "img/"; //ikony
    private static final String NAZWA_PLIKU = IMG + "psa.gif";
    public final static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    /** Creates a new instance of TBarIcon */
    public TBarIcon() {   showBarIcon();    }
    public TBarIcon(String aStr) {   super (aStr);  showBarIcon();    }
    
    private void showBarIcon()
    {	
        //wstawienie ikony w pasku tytulowym
        java.net.URL url = getClass().getResource(NAZWA_PLIKU);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
	setIconImage(img);
    } //koniec showSamsonIcon()
    
    public void checkLocation(JFrame aParent)
    {   checkLocation(aParent.getLocation().x, aParent.getLocation().y);
    } //koniec checkLocation
    
    public void checkLocation(int xx, int yy)
    {
        int _x = xx+25, _y = yy+40;
        int _szerOkn = (int)screenSize.getWidth(); //offset
        int _wysOkn = (int)screenSize.getHeight();
        if (_x + getWidth() > _szerOkn)
            _x = _szerOkn - getWidth();
        if (_y + getHeight() > _wysOkn-35) //35 na pasek startu
            _y = _wysOkn - getHeight() -35;
        if (_x < 0) _x = 0;
        if (_y < 0) _y = 0;
        setLocation (_x, _y);
    } //koniec checkLocation
} //koniec klasy


