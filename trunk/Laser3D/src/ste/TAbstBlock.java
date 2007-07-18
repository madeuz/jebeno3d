/*
 * ILaser.java
 *
 * Created on 4 czerwiec 2007, 19:31
 *
 * Abstrakcyjna klasa, któr¹ musz¹ implementowaæ wszystkie modu³y programu
 *
 */

package ste;
import obj.TObjConfig;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Michal
 */
public abstract class TAbstBlock {
    
    protected String theObjFileN; //nazwa pliku .obj
    protected JCheckBox theRobPozChB = new JCheckBox("", true); //pokaz pozycje robota
    protected TAbstBlock theNext;
    protected TObjConfig fObjConf = new TObjConfig(); //do grafiki
    protected int theGraphQnt; //co ile pktow brac do grafiki.
    protected JSpinner theGraphSpi;
    
    /**	Nazwa zak³adki (JTabbedPane) w oknie
     * @return Nazwa zak³aki
     */
    public abstract String getTabTitle();
    
    /**	Zwraca JComponent (do JTabbedPane) z ewentualnymi opcjami mozliwymi
     * do ustawienia
     * @return JComponent
     */
    public abstract JComponent getJComponent();
    
    /**	Ustawia nastepny blok
     * @return JComponent
     */
    public abstract void setNext(TAbstBlock aNext);
    
    /**	Na podst. pliku Ÿród³owego tworzy plik do grafiki
     * @return String
     */
    public abstract void createObjFile(String aName);
    
    /** Sprawdza i ewentualnie zmienia ustawienia pocz¹tkowe (z grafiki)
     */
    public abstract void checkSettings();
    //=== koniec metod abstrakcyjnych
    
    protected JComponent getObjP() //panel z ustawieniami do wizualizacji
    {
        JPanel jp = new JPanel(new GridLayout(0,2));
        Font font = new Font("System", Font.BOLD, 16);
        String[] lDesc = {"Pokaz pozycje robota",
        "Co ktory punkt",
        };
        
        int _cnt = 0; //licznik pomocniczy
        
        //Pokaz pozycje robota
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        jp.add(theRobPozChB);
        
        //Co ktory punkt
        jp.add(new JLabel(lDesc[_cnt++].concat(" : "), JLabel.RIGHT));
        theGraphSpi.setFont(font);
        jp.add(theGraphSpi);
        
        JButton objB = new JButton("Pokaz");
        objB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //TObjConfig.show(theObjFileN);
                fObjConf.show(theObjFileN);
            } //koniec aP
        });
        jp.add(objB);
        jp.setBorder(BorderFactory.createTitledBorder("Grafika : "));
        return jp;
    } //koniec getObjP
    
    public void closeGraph() //koñczy tworzenie grafiki
    {
        fObjConf.createFloor(); //rysow. podlogi
        //Jezeli zaznaczono, pokazuje pozycje robota
        if (theRobPozChB.isSelected())
            fObjConf.line(0,0,0,0,0,2, "green", "pozycja robota"); //linia pionowa przechodzaca przez (0,0)
        fObjConf.closeFile(); //Zamykam plik .obj
        
        //Koñczy grafikê w nastêpnym module
        if(theNext != null && theNext.isFileOpened()) //cz. nast.modu³ tworzy grafikê
            theNext.closeGraph();
    } //koniec closeGraph
    public boolean isFileOpened()   {   return fObjConf.isFileOpened();    }
    
    //Metody pomocnicze
    protected void showErr(String aStr) {
        JOptionPane.showMessageDialog(null,
                aStr, "Blad !", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    } //koniec showErr
} //koniec interfejsu IBlock
