/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream.events;

import es.rickyepoderi.wbxml.document.WbXmlElement;
import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.EndElement;

/**
 * <p>Implementation of the EndElement event in the wbxml-stream library.</p>
 * @author ricky
 */
public class WbXmlEndElementEvent extends WbXmlEvent implements EndElement {
    
    /**
     * The element at the EndElement.
     */
    WbXmlElement element = null;
    
    /**
     * Constructor based on the stream reader which is at a EndELement position.
     * @param stream The stream being read and positioned at EndElement
     */
    public WbXmlEndElementEvent(WbXmlStreamReader stream) {
        super(stream);
        if (getEventType() != XMLStreamConstants.END_ELEMENT) {
            throw new IllegalStateException("Not at END_ELEMENT position!");
        }
        element = stream.getCurrentElement();
    }

    /**
     * Get the name of this event
     * @return the qualified name of this event
     */
    @Override
    public QName getName() {
        QName name;
        if (element.isPrefixed()) {
            String namespaceUri = this.getDefinition().getNamespaceURIWithLinked(element.getTagPrefix());
            name = new QName(namespaceUri, element.getTagWithoutPrefix(), element.getTagPrefix());
        } else {
            name = new QName(element.getTag());
        }
        return name;
    }

    /**
     * Returns an Iterator of namespaces that have gone out of scope. Returns 
     * an empty iterator if no namespaces have gone out of scope. It always
     * return the empty list.
     * @return an Iterator over Namespace interfaces, or an empty iterator
     */
    @Override
    public Iterator getNamespaces() {
        return Collections.emptyIterator();
    }
    
    /**
     * The event representation.
     * @return string representation
     */
    @Override
    public String toString() {
        return new StringBuilder("EndElement: ")
                .append(this.element)
                .toString();
    }
}
