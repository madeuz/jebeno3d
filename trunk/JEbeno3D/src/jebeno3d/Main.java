/*
 * Main.java
 *
 * Created on 31 maj 2007, 23:35
 */

package jebeno3d;

/**
 *
 * @author  arkadiusz
 */
public class Main extends javax.swing.JFrame {
  
  /** Creates new form Main */
  public Main() {
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
   // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
   private void initComponents() {
      jTabbedPane1 = new javax.swing.JTabbedPane();
      jPanelWej = new javax.swing.JPanel();
      jButtonWejWiz = new javax.swing.JButton();
      jButtonWejZap = new javax.swing.JButton();
      jButtonOtw = new javax.swing.JButton();
      jPanelKon = new javax.swing.JPanel();
      jButtonKonWiz = new javax.swing.JButton();
      jButtonKonZap = new javax.swing.JButton();
      jPanelWyj = new javax.swing.JPanel();
      jButtonWyjWiz = new javax.swing.JButton();
      jButtonWyjZap = new javax.swing.JButton();

      getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.X_AXIS));

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      jTabbedPane1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
      jButtonWejWiz.setText("Wizualizacja");

      jButtonWejZap.setText("Zapisz");

      jButtonOtw.setText("Otw\u00f3rz");

      javax.swing.GroupLayout jPanelWejLayout = new javax.swing.GroupLayout(jPanelWej);
      jPanelWej.setLayout(jPanelWejLayout);
      jPanelWejLayout.setHorizontalGroup(
         jPanelWejLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanelWejLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanelWejLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanelWejLayout.createSequentialGroup()
                  .addComponent(jButtonWejZap)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jButtonWejWiz))
               .addComponent(jButtonOtw))
            .addContainerGap(196, Short.MAX_VALUE))
      );
      jPanelWejLayout.setVerticalGroup(
         jPanelWejLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelWejLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButtonOtw)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
            .addGroup(jPanelWejLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButtonWejZap)
               .addComponent(jButtonWejWiz))
            .addContainerGap())
      );
      jTabbedPane1.addTab("Wej\u015bcie", jPanelWej);

      jButtonKonWiz.setText("Wizualizacja");

      jButtonKonZap.setText("Zapisz");

      javax.swing.GroupLayout jPanelKonLayout = new javax.swing.GroupLayout(jPanelKon);
      jPanelKon.setLayout(jPanelKonLayout);
      jPanelKonLayout.setHorizontalGroup(
         jPanelKonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanelKonLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButtonKonZap)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonKonWiz)
            .addContainerGap(196, Short.MAX_VALUE))
      );
      jPanelKonLayout.setVerticalGroup(
         jPanelKonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelKonLayout.createSequentialGroup()
            .addContainerGap(213, Short.MAX_VALUE)
            .addGroup(jPanelKonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButtonKonZap)
               .addComponent(jButtonKonWiz, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
      );
      jTabbedPane1.addTab("Konwersja", jPanelKon);

      jButtonWyjWiz.setText("Wizualizacja");

      jButtonWyjZap.setText("Zapisz");

      javax.swing.GroupLayout jPanelWyjLayout = new javax.swing.GroupLayout(jPanelWyj);
      jPanelWyj.setLayout(jPanelWyjLayout);
      jPanelWyjLayout.setHorizontalGroup(
         jPanelWyjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanelWyjLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButtonWyjZap)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonWyjWiz)
            .addContainerGap(196, Short.MAX_VALUE))
      );
      jPanelWyjLayout.setVerticalGroup(
         jPanelWyjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelWyjLayout.createSequentialGroup()
            .addContainerGap(213, Short.MAX_VALUE)
            .addGroup(jPanelWyjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButtonWyjZap)
               .addComponent(jButtonWyjWiz))
            .addContainerGap())
      );
      jTabbedPane1.addTab("Wej\u015bcie", jPanelWyj);

      getContentPane().add(jTabbedPane1);

      pack();
   }// </editor-fold>//GEN-END:initComponents
  
  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Main().setVisible(true);
      }
    });
  }
  
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButtonKonWiz;
   private javax.swing.JButton jButtonKonZap;
   private javax.swing.JButton jButtonOtw;
   private javax.swing.JButton jButtonWejWiz;
   private javax.swing.JButton jButtonWejZap;
   private javax.swing.JButton jButtonWyjWiz;
   private javax.swing.JButton jButtonWyjZap;
   private javax.swing.JPanel jPanelKon;
   private javax.swing.JPanel jPanelWej;
   private javax.swing.JPanel jPanelWyj;
   private javax.swing.JTabbedPane jTabbedPane1;
   // End of variables declaration//GEN-END:variables
  
}