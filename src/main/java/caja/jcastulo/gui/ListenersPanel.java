/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.jcastulo.shout.ClientSpec;
import caja.jcastulo.shout.ListenerClerkManager;
import caja.jcastulo.shout.ListenerUpdatesListener;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Panel that shows the stream listeners at real time
 * 
 * @author Carlos Juarez
 */
public class ListenersPanel extends javax.swing.JPanel {
    
    /**
     * The listenerClerksManager
     */
    private ListenerClerkManager listenersManager;
    
    /**
     * The current mountPoint for which its listeners will be showed
     */
    private String currentMountPoint;
    
    /**
     * The ListenerUpdatesListener
     */
    private ListenerUpdatesListener listener = new ListenerUpdatesListener() {

        @Override
        public void listenerHasArrived(ClientSpec clientSpec) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        }

        @Override
        public void listenerHasGone(ClientSpec clientSpec) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        }
    };

    /**
     * Creates a new instance of <code>ListenersPanel</code>
     */
    public ListenersPanel() {
        initComponents();
    }

    /**
     * @param currentMountPoint - the currentMountPoint to set
     */
    public void setCurrentMountPoint(String currentMountPoint) {
        this.currentMountPoint = currentMountPoint;
        refresh();
    }
    
    /**
     * Retrieves the most recent listener information and refreshes the table
     */
    private void refresh(){
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        List<ClientSpec> clientSpecs = listenersManager.getClientSpecsByMountpoint(currentMountPoint);
        model.setRowCount(0);
        for(ClientSpec clientSpec : clientSpecs){
            model.addRow(new Object[]{clientSpec.getIp(),clientSpec.getPort()});
        }
    }
    
    /**
     * @param listenersManager - the listenersManager to set
     */
    public void setListenersManager(ListenerClerkManager listenersManager) {
        this.listenersManager = listenersManager;
        this.listenersManager.addListenerUpdatesListener(listener);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Listeners"));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP Address", "Port"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable);
        jTable.getColumnModel().getColumn(0).setMinWidth(200);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(220);
        jTable.getColumnModel().getColumn(0).setMaxWidth(230);
        jTable.getColumnModel().getColumn(1).setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
