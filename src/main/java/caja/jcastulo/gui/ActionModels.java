/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.jtable.ActionModel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.slf4j.LoggerFactory;

/**
 * Contains an action model for every icon in this package
 * 
 * @author Carlos Juarez
 */
public class ActionModels {
    
    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(ActionModels.class);
    
    private static ActionModels instance;
    
    private Map<String, ActionModel> actionModels = new HashMap<String, ActionModel>();

    public static ActionModels getInstance() {
        if (instance == null) {
            instance = new ActionModels();
        }
        return instance;
    }

    private ActionModels() {
        ImageSpec[] imageSpecs = new ImageSpec[]{
          new ImageSpec("music.png", "View"),
          new ImageSpec("start.png", "Start"),
          new ImageSpec("stop.png", "Stop"),
          new ImageSpec("headset.png", "View"),
          new ImageSpec("remove.png", "Remove")
        };
        for(ImageSpec imageSpec : imageSpecs){
            actionModels.put(imageSpec.icon, createActionModel(imageSpec.icon, imageSpec.label));
        }
    }
    
    public ActionModel getActionModel(String icon){
        return actionModels.get(icon);
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
    
    class ImageSpec{
        String icon;
        String label;

        public ImageSpec(String icon, String label) {
            this.icon = icon;
            this.label = label;
        }
    }
   
}
