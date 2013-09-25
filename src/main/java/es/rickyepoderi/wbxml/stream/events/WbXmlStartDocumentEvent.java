/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream.events;

import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.StartDocument;

/**
 * <p>Implementation of the StartDocument event in the wbxml-stream library.</p>
 * 
 * @author ricky
 */
public class WbXmlStartDocumentEvent extends WbXmlEvent implements StartDocument {
    
    /**
     * version of the document.
     */
    private String version = null;
    
    /**
     * encoding of the document.
     */
    private String encoding = null;
    
    /**
     * standalone.
     */
    private boolean standalone = false;
    
    /**
     * standaloneSet.
     */
    private boolean standaloneSet = false;
    
    /**
     * Constructor that creates the event from a stream positioned in the
     * START_DOCUMENT.
     * @param stream The stream positioned at START_DOCUMENT.
     */
    public WbXmlStartDocumentEvent(WbXmlStreamReader stream) {
        super(stream);
        if (getEventType() != XMLStreamConstants.START_DOCUMENT) {
            throw new IllegalStateException("Not at START_DOCUMENT position!");
        }
        this.version = stream.getVersion();
        this.encoding = stream.getEncoding();
        this.standalone = stream.isStandalone();
        this.standaloneSet = stream.standaloneSet();
    }
    
    /**
     * Returns the system ID of the XML data-
     * @return the system ID, defaults to ""
     */
    @Override
    public String getSystemId() {
        return this.getDefinition().getXmlPublicId();
    }

    /**
     * Returns the encoding style of the XML data. It returns the charset
     * defined in the WBXML document.
     * @return the character encoding, defaults to "UTF-8"
     */
    @Override
    public String getCharacterEncodingScheme() {
        return encoding;
    }

    /**
     * Returns true if CharacterEncodingScheme was set in the encoding 
     * declaration of the document
     * @return always true
     */
    @Override
    public boolean encodingSet() {
        return true;
    }

    /**
     * Returns if this XML is standalone
     * @return Always return false
     */
    @Override
    public boolean isStandalone() {
        return standalone;
    }

    /**
     * Returns true if the standalone attribute was set in the encoding 
     * declaration of the document.
     * @return always return false
     */
    @Override
    public boolean standaloneSet() {
        return standaloneSet;
    }

    /**
     * Returns the version of XML of this XML stream. It always return "1.0"
     * no matter the version of the wbxml document.
     * @return the version of XML, defaults to "1.0"
     */
    @Override
    public String getVersion() {
        return version;
    }
    
    /**
     * The event representation.
     * @return string representation
     */
    @Override
    public String toString() {
        return new StringBuilder("StartDocument: ")
                .append(this.encoding)
                .toString();
    }
}
