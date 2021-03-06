/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.utils;

import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 * Panel which is displayed as a modal wait message. This is a singleton class as only one wait message should be
 * showed at a time
 * 
 * @author Carlos Juarez
 */
public class WaitDialog extends javax.swing.JDialog {
    
    /**
     * Single instance
     */
    private static WaitDialog waitDialog;

    /**
     * Creates a new instance of <code>WaitDialog</code> class
     */
    private WaitDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }
    
    /**
     * Shows the wait dialog
     */
    public static void showMsg(){
        showMsg(null);
    }
    
    public static void showMsg(WindowListener wl){
            if(waitDialog==null){
                waitDialog = new WaitDialog((JFrame) null, true);
            }
            if(wl!=null){
                waitDialog.addWindowListener(wl);
            }
            waitDialog.setVisible(true);
    }
    
    /**
     * Hides the wait dialog
     */
    public static void hideMsg(){
            if(waitDialog!=null){
                WindowListener[] windowListeners = waitDialog.getWindowListeners();
                for(WindowListener wl : windowListeners){
                    waitDialog.removeWindowListener(wl);
                }
                waitDialog.setVisible(false);
            }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setName("dialog"); // NOI18N
        setResizable(false);

        progressBar.setIndeterminate(true);
        progressBar.setString("Running...");
        progressBar.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
