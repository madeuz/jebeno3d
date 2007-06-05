/*
 * ILaser.java
 *
 * Created on 4 czerwiec 2007, 19:31
 *
 * Interfejs, kt�ry musz� implementowa� wszystkie modu�y programu
 *
 */

package ste;
import javax.swing.*;

/**
 *
 * @author Michal
 */
public interface IBlock {
    /**	Nazwa zak�adki (JTabbedPane) w oknie
    * @return Nazwa zak�aki
    */
    public String getTabTitle();

    /**	Zwraca JComponent (do JTabbedPane) z ewentualnymi opcjami mo�liwymi 
    * do ustawienia
    * @return JComponent 
    */
    public JComponent getJComponent();

} //koniec interfejsu IBlock
