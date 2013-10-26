/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.utils.WaitDialog;
import caja.jcastulo.shout.ListenerClerkManager;
import caja.jcastulo.shout.ShoutServer;
import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.services.StreamManagersService;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main frame of this application. Beans are retrieved from the Spring context
 * then they are set in the corresponding GUI components.
 *
 * @author Carlos Juarez
 */
public class Main extends javax.swing.JFrame {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    /**
     * Spring context
     */
    private ApplicationContext context;

    /**
     * Constructs an instance of
     * <code>Main</code> class
     */
    public Main() {
        logger.debug("initializating components");
        initComponents();
        logger.debug("components initialized");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width - getSize().width) / 2, (dim.height - getSize().height) / 2);
        logger.debug("initializating context");
        initContext();
        logger.debug("contex initialized");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        horizontalSplit = new javax.swing.JSplitPane();
        consolePanel = new caja.gui.log.ConsolePanel();
        verticalSplit = new javax.swing.JSplitPane();
        topLeftPanel = new javax.swing.JPanel();
        serverPanel = new caja.jcastulo.gui.ServerPanel();
        streamsPanel = new caja.jcastulo.gui.StreamsPanel();
        topRightPanel = new javax.swing.JPanel();
        mediaQueuePanel = new caja.jcastulo.gui.MediaQueuePanel();
        listenersPanel = new caja.jcastulo.gui.ListenersPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JCastulo");
        setMinimumSize(new java.awt.Dimension(1020, 600));
        setPreferredSize(new java.awt.Dimension(1020, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        horizontalSplit.setDividerLocation(-3);
        horizontalSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        horizontalSplit.setRightComponent(consolePanel);

        verticalSplit.setMaximumSize(new java.awt.Dimension(2147483647, 240));
        verticalSplit.setPreferredSize(new java.awt.Dimension(1003, 240));

        topLeftPanel.setMinimumSize(new java.awt.Dimension(333, 300));
        topLeftPanel.setPreferredSize(new java.awt.Dimension(333, 300));

        javax.swing.GroupLayout topLeftPanelLayout = new javax.swing.GroupLayout(topLeftPanel);
        topLeftPanel.setLayout(topLeftPanelLayout);
        topLeftPanelLayout.setHorizontalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addGroup(topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(streamsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(serverPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE))
                .addContainerGap())
        );
        topLeftPanelLayout.setVerticalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addComponent(serverPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(streamsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
        );

        verticalSplit.setLeftComponent(topLeftPanel);

        javax.swing.GroupLayout topRightPanelLayout = new javax.swing.GroupLayout(topRightPanel);
        topRightPanel.setLayout(topRightPanelLayout);
        topRightPanelLayout.setHorizontalGroup(
            topRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topRightPanelLayout.createSequentialGroup()
                .addComponent(mediaQueuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listenersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        topRightPanelLayout.setVerticalGroup(
            topRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mediaQueuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(listenersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        verticalSplit.setRightComponent(topRightPanel);

        horizontalSplit.setTopComponent(verticalSplit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(horizontalSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(horizontalSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Must be called when this frame is closing to clean up some stuff
     *
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                serverPanel.cleanup();
                streamsPanel.cleanup();
                return null;
            }

            @Override
            protected void done() {
                WaitDialog.hideMsg();
            }
        };
        worker.execute();
        WaitDialog.showMsg();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                initUI();
//                Main main = new Main();
//                main.setVisible(true);

            }
        });
    }

    protected static void initUI() {
        final JDialog dialog = new JDialog((JFrame) null,true);
        dialog.setUndecorated(true);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Please wait...");
        panel.add(label);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {Main main;
                main = new Main();
                dialog.setVisible(false);
                main.setVisible(true);
            }
        });
        dialog.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private caja.gui.log.ConsolePanel consolePanel;
    private javax.swing.JSplitPane horizontalSplit;
    private caja.jcastulo.gui.ListenersPanel listenersPanel;
    private caja.jcastulo.gui.MediaQueuePanel mediaQueuePanel;
    private caja.jcastulo.gui.ServerPanel serverPanel;
    private caja.jcastulo.gui.StreamsPanel streamsPanel;
    private javax.swing.JPanel topLeftPanel;
    private javax.swing.JPanel topRightPanel;
    private javax.swing.JSplitPane verticalSplit;
    // End of variables declaration//GEN-END:variables

    /**
     * Retrieves context beans and set them to the corresponding panels
     */
    private void initContext() {
        //init Spring IoC
        context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/datasource.xml", "META-INF/spring/jpa-tx-config.xml", "META-INF/spring/jpa-service-context.xml", "META-INF/spring/root-context.xml"});
        serverPanel.setShoutServer(context.getBean("shoutServer", ShoutServer.class));
        listenersPanel.setListenersManager(context.getBean("listenersManager", ListenerClerkManager.class));
        final StreamManagersService streamManagersService = context.getBean("streamManagersService", StreamManagersService.class);
        streamsPanel.setStreamManagersService(streamManagersService);
        streamsPanel.getStreamsTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                Integer index = ((DefaultListSelectionModel) event.getSource()).getLeadSelectionIndex();
                if (!((DefaultListSelectionModel) event.getSource()).isSelectionEmpty()) {
//                    System.out.println("event : " + event);
                    DefaultTableModel model = (DefaultTableModel) streamsPanel.getStreamsTable().getModel();

                    StreamManager streamManager = streamManagersService.getAllStreamManagers().get(index);
                    mediaQueuePanel.setStreamUpdatable(streamManager.getProcessor());
                    listenersPanel.setCurrentMountPoint(streamManager.getMountPoint());
                }
            }
        });
        consolePanel.clearLogs();
    }
}
