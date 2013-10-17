/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.utils.WaitDialog;
import caja.jcastulo.shout.ServerListener;
import caja.jcastulo.shout.ServerObservable;
import caja.jcastulo.shout.ServerObservable.ServerStatus;
import caja.jcastulo.shout.ShoutServer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.slf4j.LoggerFactory;

/**
 * Panel that allows to control a
 * <code>ShoutServer</code>
 *
 * @author Carlos Juarez
 */
public class ServerPanel extends javax.swing.JPanel implements ServerListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ServerPanel.class);
    /**
     * Stopped status label
     */
    private static final String STOPPED_STATUS = "Stopped";
    /**
     * Running status label
     */
    private static final String RUNNING_STATUS = "Running";
    /**
     * Start server label
     */
    private static final String START_SERVER = "Start";
    /**
     * Stop server label
     */
    private static final String STOP_SERVER = "Stop";
    /**
     * The shoutcast server
     */
    private ShoutServer shoutServer;
   
    /**
     * Constructs an instance of
     * <code>ServerPanel</code> class
     */
    public ServerPanel() {
        initComponents();
    }

    /**
     * @param shoutServer - shoutServer to set
     */
    public void setShoutServer(ShoutServer shoutServer) {
        this.shoutServer = shoutServer;
        this.shoutServer.addServerListener(this);
        portTextField.setText(Integer.toString(shoutServer.getPort()));
    }

    /**
     * Should be called when closing the main frame to cleanup resources
     */
    public void cleanup() {
        if (shoutServer.isRunning()) {
            shoutServer.shutdown();
            jLabelStatus.setText(STOPPED_STATUS);
            jButtonCtrl.setText(START_SERVER);
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

        jLabel2 = new javax.swing.JLabel();
        portTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jButtonCtrl = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        bitratesCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Server"));

        jLabel2.setText("Port");

        portTextField.setColumns(4);
        try {
            portTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel1.setText("Status");

        jLabelStatus.setForeground(new java.awt.Color(0, 153, 255));
        jLabelStatus.setText(STOPPED_STATUS);

        jButtonCtrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png"))); // NOI18N
        jButtonCtrl.setText(START_SERVER);
        jButtonCtrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCtrlActionPerformed(evt);
            }
        });

        jLabel3.setText("Re-encoding out bitrate");

        bitratesCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "...", "32000", "64000", "96000", "128000" }));
        bitratesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bitratesComboActionPerformed(evt);
            }
        });

        jLabel4.setText("bps");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCtrl, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bitratesCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelStatus)
                    .addComponent(jButtonCtrl)
                    .addComponent(jLabel2)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(bitratesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Runs or stops the server depending of the current status, if the server
     * is running then will be stopped and vice versa
     *
     * @param evt
     */
    private void jButtonCtrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCtrlActionPerformed
        if (!shoutServer.isRunning()) {
            shoutServer.setPort(Integer.parseInt(portTextField.getText()));
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    shoutServer.start();
                    return null;
                }

                @Override
                protected void done() {
                    WaitDialog.hideMsg();
                }
            };
            worker.execute();
            WaitDialog.showMsg();

        } else {
            int responose = JOptionPane.showConfirmDialog(null, "Do you really want to shutdown the server",
                    "Confirm stopping server", JOptionPane.OK_CANCEL_OPTION);
            if (responose == 0) {
                SwingWorker worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        shoutServer.shutdown();
                        return null;
                    }

                    @Override
                    protected void done() {
                        WaitDialog.hideMsg();
                    }
                };
                worker.execute();
                WaitDialog.showMsg();
            }
        }
    }//GEN-LAST:event_jButtonCtrlActionPerformed

    private void bitratesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bitratesComboActionPerformed
        JComboBox combo = (JComboBox) evt.getSource();     
        Object selectedItem = combo.getSelectedItem();
        try {
            int bitrate = Integer.parseInt(selectedItem.toString());
            logger.debug("setting system property bitrate : " + bitrate);
            System.setProperty("bitrate", "" + bitrate);
        } catch (NumberFormatException ex) {
            logger.debug("selectedItem " + selectedItem + " is not integer");
        }
    }//GEN-LAST:event_bitratesComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox bitratesCombo;
    private javax.swing.JButton jButtonCtrl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JFormattedTextField portTextField;
    // End of variables declaration//GEN-END:variables

    /**
     * It is called when the server has changed its status
     *
     * @param serverStatus - the new status
     */
    @Override
    public void notifyNewServerStatus(final ServerStatus serverStatus) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (serverStatus == ServerObservable.ServerStatus.RUNNING) {
                    portTextField.setEnabled(false);
                    bitratesCombo.setEnabled(false);
                    jLabelStatus.setText(RUNNING_STATUS);
                    jButtonCtrl.setText(STOP_SERVER);
                } else {
                    jLabelStatus.setText(STOPPED_STATUS);
                    jButtonCtrl.setText(START_SERVER);
                    portTextField.setEnabled(true);
                    bitratesCombo.setEnabled(true);
                }
            }
        });
    }


}
