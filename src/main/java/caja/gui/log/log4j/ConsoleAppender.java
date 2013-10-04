/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.log.log4j;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4j console appender
 * 
 * @author Carlos Juarez
 */
public class ConsoleAppender extends AppenderSkeleton {

    /**
     * textComponent where the logs will be appended
     */
    private JTextComponent textComponent;
    
    /**
     * EOL string to be added at the end of every log
     */
    private final String EOL = System.getProperty("line.separator");
    
    /**
     * document taken from the textComponent it is just a convenience reference to easier access
     */
    private Document document;
    
    /**
     * limitLinesListener that would control the limit of lines that the document can have
     */
    private DocumentListener limitLinesListener;
    
    /**
     * Holds look and feel attributes for every level. The log messages will be displayed with
     * this attributes
     */
    private Map<Level, SimpleAttributeSet> attributeSets;

    /**
     * Constructs an instance of <code>ConsoleAppender</code> class
     */
    public ConsoleAppender() {
//        this.textComponent.setEditable(false);
//        this.document = textComponent.getDocument();
        attributeSets = new HashMap<Level, SimpleAttributeSet>();
        attributeSets.put(Level.FATAL, new SimpleAttributeSet());
        StyleConstants.setForeground(attributeSets.get(Level.FATAL), Color.YELLOW);
        attributeSets.put(Level.ERROR, new SimpleAttributeSet());
        StyleConstants.setForeground(attributeSets.get(Level.ERROR), Color.RED);
        attributeSets.put(Level.INFO, new SimpleAttributeSet());
        StyleConstants.setForeground(attributeSets.get(Level.INFO), new Color(0,99,0));
        attributeSets.put(Level.DEBUG, new SimpleAttributeSet());
        StyleConstants.setForeground(attributeSets.get(Level.DEBUG), Color.BLUE);
    }

    /**
     * @param textComponent - textComponent to set
     */
    public void setTextComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
//        document = textComponent.getDocument();
        textComponent.setEditable(false);
        document = textComponent.getDocument();
    }

    /**
     * Allows to set the limit of lines in the document
     * @param lines 
     */
    public void setMessageLines(int lines) {
        if (limitLinesListener != null) {
            document.removeDocumentListener(limitLinesListener);
        }
        limitLinesListener = new LimitLinesDocumentListener(lines, true);
        document.addDocumentListener(limitLinesListener);
    }

    @Override
    protected void append(LoggingEvent event) {
        if (textComponent == null) {
            return;
        }
        String message = event.getMessage().toString();
        if (message.length() == 0) {
            return;
        }
        if(requiresLayout()){
            message = getLayout().format(event);
        }
        handleAppend(message, event.getLevel());
    }

    /**
     * Appends a message to the document and used the look and feel attributes given by the level
     * 
     * @param message - the message to append
     * @param level  - the level of the message
     */
    private void handleAppend(String message, Level level) {
        String line = message + EOL;
        try {
            int offset = document.getLength();
            document.insertString(offset, line, attributeSets.get(level));
            textComponent.setCaretPosition(document.getLength());

        } catch (BadLocationException ble) {
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
