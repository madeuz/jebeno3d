/*
 * TGlowneOkno.java
 *
 * Created on 4 czerwiec 2007, 14:06
 *
 */

package gra;
import ste.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author mignat
 */
public class TGlowneOkno extends TBarIcon {
    
    private final static String TITLE = "Laser 3D (12.cze.2007)";
    private TController fControl;
    private String[] theMenuStrTab = {"Plik", "Pomoc"};
    private JMenu[] jmTab = new JMenu [theMenuStrTab.length]; 

    public TGlowneOkno(TController tc)
    {
        fControl = tc;
        setTitle (TITLE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener (new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {   closeProgramme();   } //koniec windowClosing
        });
        setJMenuBar(getMenu());
        getContentPane().add(makeWindow());
        pack();

        // polozenie okna
        setSize((int)(screenSize.width *7f/8f),(int)(screenSize.height *7f/8f));
        setLocation((int)(screenSize.width/2f -  getSize().width/2f), 
                    (int)(screenSize.height/2f - getSize().height/2f));
        setVisible(true);
    } //koniec konstruktora

    /**********************************/
    /*  metody do obslugi okna        */
    /**********************************/
    private JMenuBar getMenu()
    {
        JMenuBar jmb = new JMenuBar();
        for (byte i=0; i<jmTab.length; i++) {
            jmTab[i] = new JMenu (theMenuStrTab[i]);
            //podczepienie obiektów-akcji
            if (i==0) {
                jmTab[i].add(new JSeparator());
                jmTab[i].add(pliZam);

            } else if (i==jmTab.length-1) { //ostatnia kolumna
                jmTab[i].add(pomPro);
            } //koniec if - else (i==)
            jmb.add(jmTab[i]);
        } //koniec for i
        
        return jmb;
    } //koniec getMenu

    private JComponent makeWindow()
    {
        JPanel jp = new JPanel(new BorderLayout());
        //jp.setBackground(Color.green);
        //1. Tytu³
        JLabel jl = new JLabel("Tworzenie map 3D z pomiarów laserowych", JLabel.CENTER);
        jl.setFont (new Font("System", Font.BOLD, 24));
        jp.add(jl, BorderLayout.NORTH);
        
        JTabbedPane jtb = new JTabbedPane();
        //2. Dodajê wszystkie bloki
        for (IBlock ib : fControl.getBlockAL())
            jtb.addTab(ib.getTabTitle(), ib.getJComponent());
        jp.add(jtb, BorderLayout.CENTER);
        return jp;
    } //koniec makeWindow()
    
    /****************************************************/
    /*  Obs³uga zdarzeñ z wykorzystaniem obiektów-Akcji */
    /****************************************************/
    private Action pliZam = new AbstractAction ("Zamknij", null) {
        public void actionPerformed (ActionEvent e)
        {   closeProgramme(); }
    }; //koniec Action 
    
     // --- Kolumna ostatnia
    private Action pomPro = new AbstractAction ("O programie ...", null) {
        public void actionPerformed (ActionEvent e)
        { zsi.TAboutProgramme.createWindow(); }
    }; //koniec pomPro
    //=== koniec obs³ugi zdarzeñ za pomoc¹ obiektów-Akcji

    private void closeProgramme()
    {
        //TIniStart.writeIni(); //do zapisu wartoœci pocz¹tkowych
        dispose();  
        System.exit(0);
    } //koniec closeProgramme

} //koniec klasy TGlowneOkno
