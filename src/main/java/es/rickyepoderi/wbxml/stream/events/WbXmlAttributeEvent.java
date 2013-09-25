/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream.events;

import es.rickyepoderi.wbxml.document.WbXmlAttribute;
import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;

/**
 * <p>Implementation of the Attribute event in the wbxml-stream library.</p>
 * 
 * @author ricky
 */
class WbXmlAttributeEvent extends WbXmlEvent implements Attribute {

    WbXmlAttribute attr = null;
    
    /**
     * Constructor for the StartElement event.
     * @param attr The attribute of the StartElement.
     * @param stream The stream for the location.
     */
    public WbXmlAttributeEvent(WbXmlAttribute attr, WbXmlStreamReader stream) {
        super(stream, XMLStreamConstants.ATTRIBUTE);
        if (stream.getEventType() != XMLStreamConstants.START_ELEMENT) {
            throw new IllegalStateException("Not at START_ELEMENT position!");
        }
        this.attr = attr;
    }
    
    /**
     * Returns the QName for this attribute
     * @return The QName for the attribute
     */
    @Override
    public QName getName() {
        QName name;
        if (attr.isPrefixed()) {
            String namespaceUri = this.getDefinition().getNamespaceURIWithLinked(attr.getNamePrefix());
            name = new QName(namespaceUri, attr.getNameWithoutPrefix(), attr.getNamePrefix());
        } else {
            name = new QName(attr.getName());
        }
        return name;
    }

    /**
     * Gets the normalized value of this attribute
     * @return The value of the attribute
     */
    @Override
    public String getValue() {
        return attr.getValue();
    }

    /**
     * Gets the type of this attribute, default is the String "CDATA".
     * This implementation always return CDATA.
     * @return the type as a String, default is "CDATA"
     */
    @Override
    public String getDTDType() {
        return "CDATA";
    }

    /**
     * A flag indicating whether this attribute was actually specified in the 
     * start-tag of its element, or was defaulted from the schema. 
     * It always return true;
     * @return 
     */
    @Override
    public boolean isSpecified() {
        return true;
    }
    
}
