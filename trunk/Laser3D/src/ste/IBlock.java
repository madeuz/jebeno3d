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
public interface IBlock 
{
    /**	Nazwa zak�adki (JTabbedPane) w oknie
    * @return Nazwa zak�aki
    */
    public String getTabTitle();

    /**	Zwraca JComponent (do JTabbedPane) z ewentualnymi opcjami mozliwymi 
    * do ustawienia
    * @return JComponent 
    */
    public JComponent getJComponent();

    /**	Ustawia nastepny blok
    * @return JComponent 
    */
    public void setNext(IBlock aNext);
    
} //koniec interfejsu IBlock
