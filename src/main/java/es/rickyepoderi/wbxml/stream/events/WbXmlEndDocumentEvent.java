/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream.events;

import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.EndDocument;

/**
 * <p>Implementation of the EndDocument event in the wbxml-stream library.</p>
 * 
 * @author ricky
 */
public class WbXmlEndDocumentEvent extends WbXmlEvent implements EndDocument {
    
    /**
     * Constructor based on the stream reader at a ENdDocument position,
     * @param stream The stream reader.
     */
    public WbXmlEndDocumentEvent(WbXmlStreamReader stream) {
        super(stream);
        if (getEventType() != XMLStreamConstants.END_DOCUMENT) {
            throw new IllegalStateException("Not at END_DOCUMENT position!");
        }
    }
    
    /**
     * The event representation.
     * @return string representation
     */
    @Override
    public String toString() {
        return new StringBuilder("EndDocument: ")
                .toString();
    }
}
