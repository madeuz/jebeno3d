/*
 * TAboutProgramme.java
 *
 * Created on 1 czerwiec 2005, 21:07
 *
 */

package zsi;
import gra.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TAboutProgramme extends TAbstractAbout { //JPanel {
    
    private static JFrame frame;
    private String[] theOpis = {
                        "POLSKA AKADEMIA NAUK",
                        "Instytut Podstawowych Problemów Techniki",
                        "Zak³ad Technologii Inteligentych",
                        "Zespó³ Systemów Inteligentnych",
                    };

    private String[] theProgramme = {
        "Tworzenie map 3D z pomiarów laserowych",
        "kompilacja: czerwiec 2007",
    };
    
    /** Creates a new instance of TAboutProgramme */
    public TAboutProgramme() 
    {
        super();
        frame.getRootPane().setDefaultButton(theOK);
        //Zmniejszam font przycisku Autor
        Font font = theAuthorB.getFont();
        float fontSize = font.getSize2D();
        font = font.deriveFont(fontSize-2);
        theAuthorB.setFont(font);
        theAuthorB.addActionListener(new ActionListener(){
                public void actionPerformed (ActionEvent e)
                {   TAuthor.createWindow(); } //koniec aP
        });        
        add(makeWindow());
        
    } //koniec konstruktora
    
    private JPanel makeWindow()
    {
        JPanel jp = new JPanel(new BorderLayout());
        //jp.add(getInfoPanel(theEndUser, JLabel.CENTER, null, false),
        jp.add(getInfoPanel(theOpis, JLabel.CENTER, "ippt_oryginal.gif", 
                                                            "psa.gif", false),
                BorderLayout.NORTH);

        jp.add(getInfoPanel(theProgramme, JLabel.LEFT, null, null, true),
                BorderLayout.CENTER);
        
        jp.add(getButton(), BorderLayout.SOUTH);
        return jp;
    } //koniec makeWindow
    public void closeWindow()   {   frame.dispose();}    
    
    /************************/
    /*  METODY STATYCZNE    */
    /************************/
    public static void createWindow()
    {
        if (frame != null && frame.isShowing()) { //czyli okno jest ju¿ otwarte
            frame.toFront();
            return;
        } //koniec if (frame != null)
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new TBarIcon("O programie . . .");
        frame.setAlwaysOnTop(true);
        frame.setResizable(false); //nie mo¿na zmieniaæ rozmiarów okna
        JFrame.setDefaultLookAndFeelDecorated(false);
        //frame.setLocation (tgo.getLocation().x+25, tgo.getLocation().y+40);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JComponent newContentPane = new TAboutProgramme();
        newContentPane.setOpaque(true); //content panes must be opaque
        //newContentPane.setPreferredSize(new Dimension(250, 200));

        frame.setContentPane(newContentPane);
        frame.pack();
        
        // polozenie okna
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width/2 - frame.getSize().width/2, 
                    screenSize.height/2 - frame.getSize().height/2);
        frame.setVisible(true);
    } //koniec createWindow
} //koniec klasy TAboutProgramme

class TAuthor extends TAbstractAbout {
    protected static JFrame frame;
    private static byte theMignatCnt = 0; //do pokazywania któregoœ okienka
    private String[] theMG = {"Micha³ Gnatowski", "IPPT PAN" };
    private String[] theBS = {"Barbara Siemi¹tkowska"};
    private String[] theAZ = {"Arkadiusz Zychewicz"};
    TAuthor ()
    {
        frame.getRootPane().setDefaultButton(theOK);
        JPanel jp = new JPanel(new BorderLayout());
        
        JPanel jp1 = new JPanel(new GridLayout(0,1));
        String[] mignatJpg = {"mignat_01.jpg","mignat_02.jpg","mignat_03.jpg"};
        jp1.add(getInfoPanel(theMG, JLabel.LEFT, mignatJpg[theMignatCnt++],
                null, false));
        theMignatCnt %= mignatJpg.length;
        
        jp1.add(getInfoPanel(theBS, JLabel.LEFT, "ippt_oryginal.gif", null, false));
        jp1.add(getInfoPanel(theAZ, JLabel.LEFT, "psa.gif", null, false));
        
        jp.add(jp1, BorderLayout.CENTER);
        jp.add(getButton(), BorderLayout.SOUTH);
        add (jp);
    } //koniec konstruktora

    public void closeWindow()   {   frame.dispose();}
    /************************/
    /*  METODY STATYCZNE    */
    /************************/
    public static void createWindow()
    {
        if (frame != null && frame.isShowing()) { //czyli okno jest ju¿ otwarte
            frame.toFront();
            return;
        } //koniec if (frame != null)
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new TBarIcon("Autorzy");
        frame.setAlwaysOnTop(true);
        frame.setResizable(false); //nie mo¿na zmieniaæ rozmiarów okna
        JFrame.setDefaultLookAndFeelDecorated(false);
        //frame.setLocation (tgo.getLocation().x+25, tgo.getLocation().y+40);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JComponent newContentPane = new TAuthor();
        newContentPane.setOpaque(true); //content panes must be opaque
        //newContentPane.setPreferredSize(new Dimension(250, 200));
                
        frame.setContentPane(newContentPane);
        frame.pack();
        
        // polozenie okna
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width/2 -  frame.getSize().width/2, 
                    screenSize.height/2 -  frame.getSize().height/2);
        frame.setVisible(true);         
    } //koniec createWindow
    
} //koniec klasy TAuthor

abstract class TAbstractAbout extends JPanel {
    
    private Font theFont = new Font ("System", Font.BOLD, 12);
    protected JButton theAuthorB = new JButton("Autorzy");
    protected JButton theOK = new JButton("Ok");
    
    TAbstractAbout()
    {
    } //koniec konstruktora
    
    protected JPanel getInfoPanel(String[] aStrTab, int aLabelVal, 
            String aIconNameL, String aIconNameR, boolean aAuthorFlag)
    {   
        JPanel jp = new JPanel(new BorderLayout());
        JPanel jp1 = new JPanel(); //otoczka dla j11
        JPanel jp11 = new JPanel(new BorderLayout());
        JPanel jp111 = new JPanel(new GridLayout(0,1,0,5));
        if (aIconNameL != null) //czyli jest jakaœ ikona
            jp.add (new JLabel(getIcon(aIconNameL), JLabel.CENTER),
                    BorderLayout.WEST);
        if (aIconNameR != null) //czyli jest jakaœ ikona
            jp.add (new JLabel(getIcon(aIconNameR), JLabel.CENTER),
                    BorderLayout.EAST);
        for (byte i=0; i<aStrTab.length; i++) {	
            JLabel jl = new JLabel(aStrTab[i], aLabelVal);
            jl.setFont(theFont);
            jp111.add(jl);
        } //koniec for
        jp11.add(jp111, BorderLayout.CENTER);
        
        if (aAuthorFlag) {//dodanie przycisku Autor
            JPanel jpB = new JPanel();
            jpB.add(theAuthorB); 
            jp11.add(jpB, BorderLayout.SOUTH);
        } //koniec if (aAuthorFlag
        
        jp1.add(jp11);
        jp.add(jp1, BorderLayout.CENTER);
        jp.add(new JSeparator(), BorderLayout.SOUTH);

        return jp;
    } //koniec getInfoPanel
    
    private Icon getIcon(String name)
    {
        //przygotowanie obrazu gif
        java.net.URL url = getClass().getResource(name);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        return new ImageIcon(img);
    } //koniec getIcon
    
    public abstract void closeWindow();
    protected JPanel getButton()
    {
        JPanel jp = new JPanel();
        theOK.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e)
            {	closeWindow();	}
        });
        jp.add(theOK);
        
        return jp;
    } //koniec getButton
    
} //koniec klasy TABstractAbout