/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.gui;

import caja.gui.jtable.ActionModel;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
            new ImageSpec("images/music.png", "View"),
            new ImageSpec("images/start.png", "Start"),
            new ImageSpec("images/stop.png", "Stop"),
            new ImageSpec("images/headset.png", "View"),
            new ImageSpec("images/remove.png", "Remove")
        };
        for (ImageSpec imageSpec : imageSpecs) {
            actionModels.put(imageSpec.icon, createActionModel(imageSpec.icon, imageSpec.label));
        }
    }

    public ActionModel getActionModel(String icon) {
        return actionModels.get(icon);
    }

    private ActionModel createActionModel(String strImage, String label) {
        ActionModel actionModel;
        URL url = Thread.currentThread().getContextClassLoader().getResource(strImage);
        ImageIcon icon = new ImageIcon(url);
        actionModel = new ActionModel(icon, label);
        return actionModel;
    }

    class ImageSpec {

        String icon;
        String label;

        public ImageSpec(String icon, String label) {
            this.icon = icon;
            this.label = label;
        }
    }
}
