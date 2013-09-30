/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.jtable.ActionModel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class ActionModels {
    
    final org.slf4j.Logger logger = LoggerFactory.getLogger(ActionModels.class);

    public ActionModel queueActionModel;
    
    public ActionModel startActionModel;
    
    public ActionModel stopActionModel;
    
    public ActionModel listenerActionModel;
    
    public ActionModel removeActionModel;
    
    private static ActionModels instance;

    public static ActionModels getInstance() {
        if (instance == null) {
            instance = new ActionModels();
        }
        return instance;
    }

    private ActionModels() {
        queueActionModel = createActionModel("music.png", "View");
        startActionModel = createActionModel("start.png", "Start");
        stopActionModel = createActionModel("stop.png", "Stop");
        listenerActionModel = createActionModel("headset.png", "View");
        removeActionModel = createActionModel("remove.png", "Remove");
    }
    
    private ActionModel createActionModel(String strImage, String label) {
        ActionModel actionModel = null;
        try {
            BufferedImage image;
            image = ImageIO.read(getClass().getResourceAsStream(strImage));
            ImageIcon icon = new ImageIcon(image);
            actionModel = new ActionModel(icon, label);
        } catch (IOException ex) {
            logger.error("Error while creating image", ex);
        }
        return actionModel;
    }
   
}
