/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.jtable.ActionModel;
import caja.jcastulo.media.entities.AudioMedia;
import caja.jcastulo.stream.StreamListener;
import caja.jcastulo.stream.StreamUpdatable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.slf4j.LoggerFactory;

/**
 * Panel that shows the audio medias of a selected stream also allows to perform update operation such as
 * add, remove songs
 *
 * @author Carlos Juarez
 */
public class MediaQueuePanel extends javax.swing.JPanel implements StreamListener{
    
    /**
     * The logger
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MediaQueuePanel.class);
    
    /**
     * The stream displayed and ready to be updated
     */
    private StreamUpdatable streamUpdatable;
    
    /**
     * The chooser to choose audio files
     */
    private JFileChooser chooser;
    
    /**
     * The listener that performs audio media removals to the stream
     */
    private ActionListener removeListener = new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = (Integer) e.getSource();
            streamUpdatable.removeMedia(index);
        }
    };
    
    private TransferHandler transferHandler = new TransferHandler(){
            
            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }
            
            @Override
            protected Transferable createTransferable(JComponent c) {
                JTable table = (JTable)c;
                return new StringSelection("" + table.getSelectedRow());
            }
            
//            @Override
//            void exportDone(JComponent c, Transferable t, int action) {
//                c.removeSelection();
//            }
            
            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                // we only import Strings
                if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    return false;
                }
                JTable.DropLocation dl = (JTable.DropLocation)info.getDropLocation();
                if (dl.isInsertRow()&& dl.getRow()!=-1) {
                    return true;
                }
                return false;
            }
             
            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                if (!info.isDrop()) {
                    return false;
                }
                // Check for String flavor
                if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, "drop of this type not accepted");
                        }
                    });
                    return false;
                }

                JTable.DropLocation dropLocation = (JTable.DropLocation)info.getDropLocation();
                
//                DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
                final int targetIndex = dropLocation.getRow();
                final int sourceIndex;
                try {
                    Transferable t = info.getTransferable();
                    sourceIndex = new Integer((String)t.getTransferData(DataFlavor.stringFlavor));
                } catch (Exception ex) {
                    logger.error("Exception when moving row", ex);
                    return false;
                }
                if(sourceIndex==targetIndex-1||sourceIndex==targetIndex){
                    return false;
                }
                
                if(dropLocation.isInsertRow()) {
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            JOptionPane.showMessageDialog(null, "movin row from " + sourceIndex + " to " + targetIndex);
//                        }
//                    });
//                    return false;
                    streamUpdatable.moveMedia(sourceIndex, targetIndex);
                    return true;
                }
		return false;
            }
        };

    /**
     * Creates a new instance of <code>MediaQueuePanel</code> class
     */
    public MediaQueuePanel() {
        initComponents();
        jTable.setRowHeight(26);
        jTable.setTransferHandler(transferHandler);
        addButton.setEnabled(false);
    }
    
    /**
     * To set a new streamUpdatable and refresh the panel with the new audio medias
     * 
     * @param streamUpdatable - the streamUpdatable to set
     */
    public void setStreamUpdatable(StreamUpdatable streamUpdatable) {
        if(this.streamUpdatable!=null){
            this.streamUpdatable.removeListener(this);
        }
        this.streamUpdatable = streamUpdatable;
        this.streamUpdatable.addListener(this);
        refresh();
    }

    /**
     * Refreshes the panel with the audio medias
     */
    private void refresh() {
        addButton.setEnabled(true);
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        int i = 0;
        for (AudioMedia media : streamUpdatable.getStreamSpec().getAudioMedias()) {
            ActionModel removeModel = ActionModels.getInstance().getActionModel("remove.png").copy();
            removeModel.setPayload(i);
            model.addRow(new Object[]{++i, media.toString(),removeModel});
        }
        statusLabel.setText(streamUpdatable.currentMetadata().toString());
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
        statusLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Queue"));

        jLabel2.setText("Now Playing ");

        statusLabel.setForeground(new java.awt.Color(0, 153, 255));
        statusLabel.setText("nothing");

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Media", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setDragEnabled(true);
        jTable.setDropMode(javax.swing.DropMode.INSERT_ROWS);
        jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable);
        jTable.getColumnModel().getColumn(0).setMinWidth(20);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        jTable.getColumnModel().getColumn(0).setMaxWidth(30);
        jTable.getColumnModel().getColumn(2).setMinWidth(50);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(2).setMaxWidth(50);
        jTable.getColumnModel().getColumn(2).setCellEditor(new caja.gui.jtable.ButtonEditor(removeListener));
        jTable.getColumnModel().getColumn(2).setCellRenderer(new caja.gui.jtable.ButtonRenderer());

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/caja/jcastulo/gui/add.png"))); // NOI18N
        addButton.setMaximumSize(new java.awt.Dimension(50, 25));
        addButton.setMinimumSize(new java.awt.Dimension(50, 25));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if(chooser==null){
            chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3 files", "mp3");
            chooser.setFileFilter(filter);
        }
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            AudioMedia media = new AudioMedia(chooser.getSelectedFile().getPath());
            streamUpdatable.addMedia(media);
//            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        }
    }//GEN-LAST:event_addButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Invoked when the stream just changed the current audio media
     */
    @Override
    public void mediaChanged() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }
    
    /**
     * Invoked when the stream queue has changed
     */
    @Override
    public void queueChanged(){
        mediaChanged();
    }
}
