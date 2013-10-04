/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.jtable.ActionModel;
import caja.gui.utils.WaitDialog;
import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.entities.StreamSpec;
import caja.jcastulo.stream.services.StreamManagersService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class StreamsPanel extends javax.swing.JPanel {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(StreamsPanel.class);
    
    private StreamManagersService streamManagersService;
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String streamName = (String) e.getSource();
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    StreamManager streamManager = streamManagersService.getStreamManagerByName(streamName);
                    if (streamManager.isRunning()) {
                        streamManager.stop();
                    } else {
                        streamManager.start();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    WaitDialog.hideMsg();
                    refresh();
                }
            };
            worker.execute();
            WaitDialog.showMsg();
        }
    };
    
    private ActionListener removeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String streamName = (String) e.getSource();
            final StreamManager streamManager = streamManagersService.getStreamManagerByName(streamName);
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    streamManager.stop();
                    streamManagersService.removeStreamManager(streamName);
                    return null;
                }
                @Override
                protected void done() {
                    WaitDialog.hideMsg();
                    refresh();
                }
            };
            int response;
            if (streamManager.isRunning()) {
                response = JOptionPane.showConfirmDialog(null, "Do you really want to stop and remove this stream?", "Remove stream", JOptionPane.OK_CANCEL_OPTION);
                if (response == 0) {
                    worker.execute();
                    WaitDialog.showMsg();
                }
            } else {
                response = JOptionPane.showConfirmDialog(null, "Do you really want to remove this stream?", "Remove stream", JOptionPane.OK_CANCEL_OPTION);
                if (response == 0) {
                    worker.execute();
                    WaitDialog.showMsg();
                }
            }            
        }
    };

    /**
     * Creates new form StreamsPanel
     */
    public StreamsPanel() {
        initComponents();
        streamsTable.setRowHeight(26);
    }

    public void setStreamManagersService(StreamManagersService streamManagersService) {
        this.streamManagersService = streamManagersService;
        refresh();
    }

    private void refresh() {
        logger.debug("refreshing streamTable");
        DefaultTableModel model = (DefaultTableModel) streamsTable.getModel();
        List<StreamManager> streamManagers = streamManagersService.getAllStreamManagers();
        model.setRowCount(0);
        for (StreamManager streamManager : streamManagers) {
            ActionModel actionModel;
            if (streamManager.isRunning()) {
                actionModel = ActionModels.getInstance().getActionModel("stop.png").copy();
            } else {
                actionModel = ActionModels.getInstance().getActionModel("start.png").copy();
            }
            actionModel.setPayload(streamManager.getStreamName());
            ActionModel removeModel = ActionModels.getInstance().getActionModel("remove.png").copy();
            removeModel.setPayload(streamManager.getStreamName());
            model.addRow(new Object[]{streamManager.getStreamName(), streamManager.getMountPoint(), actionModel, removeModel});
        }
    }

    public void cleanup() {
        List<StreamManager> streamManagers = streamManagersService.getAllStreamManagers();
        for (StreamManager streamManager : streamManagers) {
            streamManager.stop();
        }
        streamManagersService.persistStreamSpecs();
    }

    public JTable getStreamsTable() {
        return streamsTable;
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
        streamsTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Streams"));

        streamsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Mount Point", "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        streamsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        streamsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(streamsTable);
        streamsTable.getColumnModel().getColumn(0).setMinWidth(100);
        streamsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        streamsTable.getColumnModel().getColumn(1).setMinWidth(100);
        streamsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        streamsTable.getColumnModel().getColumn(2).setMinWidth(30);
        streamsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        streamsTable.getColumnModel().getColumn(2).setMaxWidth(30);
        streamsTable.getColumnModel().getColumn(2).setCellEditor(new caja.gui.jtable.ButtonEditor(actionListener));
        streamsTable.getColumnModel().getColumn(2).setCellRenderer(new caja.gui.jtable.ButtonRenderer());
        streamsTable.getColumnModel().getColumn(3).setMinWidth(30);
        streamsTable.getColumnModel().getColumn(3).setPreferredWidth(30);
        streamsTable.getColumnModel().getColumn(3).setMaxWidth(30);
        streamsTable.getColumnModel().getColumn(3).setCellEditor(new caja.gui.jtable.ButtonEditor(removeListener));
        streamsTable.getColumnModel().getColumn(3).setCellRenderer(new caja.gui.jtable.ButtonRenderer());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/caja/jcastulo/gui/add.png"))); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(50, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(50, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String streamName = JOptionPane.showInputDialog(null, "Stream name");
        if (streamName != null && !streamName.equals("")) {
            StreamSpec streamSpec = new StreamSpec(streamName);
            streamManagersService.addStreamManager(streamSpec);
            refresh();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable streamsTable;
    // End of variables declaration//GEN-END:variables
}
