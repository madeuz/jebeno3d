/*
 * ILaser.java
 *
 * Created on 4 czerwiec 2007, 19:31
 *
 * Interfejs, który musz¹ implementowaæ wszystkie modu³y programu
 *
 */

package ste;
import javax.swing.*;

/**
 *
 * @author Michal
 */
public interface IBlock {
    /**	Nazwa zak³adki (JTabbedPane) w oknie
    * @return Nazwa zak³aki
    */
    public String getTabTitle();

    /**	Zwraca JComponent (do JTabbedPane) z ewentualnymi opcjami mo¿liwymi 
    * do ustawienia
    * @return JComponent 
    */
    public JComponent getJComponent();

} //koniec interfejsu IBlock
