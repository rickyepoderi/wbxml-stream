/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *    
 * Linking this library statically or dynamically with other modules 
 * is making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *    
 * As a special exception, the copyright holders of this library give 
 * you permission to link this library with independent modules to 
 * produce an executable, regardless of the license terms of these 
 * independent modules, and to copy and distribute the resulting 
 * executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the 
 * license of that module.  An independent module is a module which 
 * is not derived from or based on this library.  If you modify this 
 * library, you may extend this exception to your version of the 
 * library, but you are not obligated to do so.  If you do not wish 
 * to do so, delete this exception statement from your version.
 *
 * Project: github.com/rickyepoderi/wbxml-stream
 * 
 */
package es.rickyepoderi.wbxml.stream;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * <p>This class is the implementation of the WbXmlStreamReader with a 
 * StreamFilter attached. The idea is quite simple, an internal reader is
 * used normally for all the common methods (this class is just a wrapper
 * for almost all methods) but the next() method is different. The reader
 * calculates the next event using the methods backup and restore of the 
 * original one. That way the next() method returns the next position that
 * accepts the filter and the hasNext() method if there is a next valid
 * position calculated. Other methods like nextTag or getElementText (that
 * alters also the position of the internal reader) needs to calculate the
 * next valid position at the end.</p>
 * 
 * @author ricky
 * @see WbXmlStreamReader
 * @see XMLStreamReader
 */
public class WbXmlFilterStreamReader implements XMLStreamReader {

    /**
     * Logger of the class.
     */
    protected static final Logger log = Logger.getLogger(WbXmlStreamReader.class.getName());
    
    /**
     * The internal reader.
     */
    private WbXmlStreamReader reader;
    
    /**
     * The next valid (accepted by the filter) position of the reader.
     */
    private StreamRestorePoint nextPosition;
    
    /**
     * The filter to apply to the reader.
     */
    private StreamFilter filter;
    
    /**
     * Constructor using the properties.
     * @param reader The internal reader
     * @param filter The filter to apply
     * @throws XMLStreamException Some error calculating the next valid position
     */
    public WbXmlFilterStreamReader(WbXmlStreamReader reader, StreamFilter filter) throws XMLStreamException {
        this.reader = reader;
        this.filter = filter;
        this.calculateNextPosition();
    }
    
    /**
     * The private method that calculates the next position valid (accepted by
     * the filter) in the reader. The state of the reader is backed up at the
     * begining, then the next valid position is calculated and finally the
     * initial state is restored.
     * @throws XMLStreamException Some problem with the reader 
     */
    private void calculateNextPosition() throws XMLStreamException {
        StreamRestorePoint currentPosition = reader.backup();
        log.log(Level.FINER, "current position: {0}", currentPosition);
        // calculate the next position that accepts the filter
        if (reader.hasNext()) {
            reader.next();
            while (reader.hasNext() && !filter.accept(reader)) {
                reader.next();
            }
            if (filter.accept(reader)) {
                nextPosition = reader.backup();
            } else {
                nextPosition = null;
            }
        } else {
            nextPosition = null;
        }
        log.log(Level.FINER, "next position: {0}", nextPosition);
        // restore current position
        reader.restore(currentPosition);
    }

    /**
     * The next position is saved in the internal property <em>nextPosition</em>.
     * The reader is restored to that position and the next one is calculated
     * again for a later call.
     * @return The current event of the reader
     * @throws XMLStreamException Some error inside the real reader
     */
    @Override
    public int next() throws XMLStreamException {
        if (nextPosition == null) {
            throw new XMLStreamException("End of document reached!");
        }
        // the next should return the next position accepted by the filter
        reader.restore(nextPosition);
        // now the next valid position should be calculated
        this.calculateNextPosition();
        // return the current status
        return reader.getEventType();
    }

    /**
     * In the filtered stream the end is marked by the <em>nextPosition</em>
     * property. If it is null there is no next element.
     * @return true if no more elements are found
     * @throws XMLStreamException Some error
     */
    @Override
    public boolean hasNext() throws XMLStreamException {
        return nextPosition != null;
    }

    /**
     * The method acts exactly the same than in the real wbxml reader but the
     * next element is calculated at the end. This should be done cos this 
     * method modifies the position of the reader.
     * @return The element text (see original)
     * @throws XMLStreamException Some error
     */
    @Override
    public String getElementText() throws XMLStreamException {
        String result = reader.getElementText();
        // we need to calculate the next position cos getElementText moves the reader
        this.calculateNextPosition();
        return result;
    }

    /**
     * Same as the previous method the nextTag moves the position of the reader
     * to the next tag if exists, so the next position should be re-calculated
     * at the end. See original method.
     * @return The event of the next tag
     * @throws XMLStreamException Some error
     */
    @Override
    public int nextTag() throws XMLStreamException {
        int result = reader.nextTag();
        // we need to calculate the next position cos nextTag move the reader
        this.calculateNextPosition();
        return result;
    }
    
    //
    // JUST WRAPPER METHODS
    //
    
    /**
     * {@inheritDoc}
     */@Override
    public Object getProperty(String string) throws IllegalArgumentException {
        return reader.getProperty(string);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        reader.require(type, namespaceURI, localName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws XMLStreamException {
        reader.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI(String prefix) {
        return reader.getNamespaceURI(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStartElement() {
        return reader.isStartElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEndElement() {
        return reader.isEndElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCharacters() {
        return reader.isCharacters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWhiteSpace() {
        return reader.isWhiteSpace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttributeValue(String namespaceURI, String localName) {
        return reader.getAttributeValue(namespaceURI, localName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttributeCount() {
        return reader.getAttributeCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QName getAttributeName(int i) {
        return reader.getAttributeName(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttributeNamespace(int i) {
        return reader.getAttributeNamespace(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttributeLocalName(int i) {
        return reader.getAttributeLocalName(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttributePrefix(int i) {
        return reader.getAttributePrefix(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttributeType(int i) {
        return reader.getAttributeType(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttributeValue(int i) {
        return reader.getAttributeValue(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAttributeSpecified(int i) {
        return reader.isAttributeSpecified(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNamespaceCount() {
        return reader.getNamespaceCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespacePrefix(int i) {
        return reader.getNamespacePrefix(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI(int i) {
        return reader.getNamespaceURI(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceContext getNamespaceContext() {
        return reader.getNamespaceContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEventType() {
        return reader.getEventType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return reader.getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char[] getTextCharacters() {
        return reader.getTextCharacters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        return reader.getTextCharacters(sourceStart, target, targetStart, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTextStart() {
        return reader.getTextStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTextLength() {
        return reader.getTextLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEncoding() {
        return reader.getEncoding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasText() {
        return reader.hasText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getLocation() {
        return reader.getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QName getName() {
        return reader.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return reader.getLocalName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasName() {
        return reader.hasName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI() {
        return reader.getNamespaceURI();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return reader.getPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return reader.getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStandalone() {
        return reader.isStandalone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean standaloneSet() {
        return reader.standaloneSet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCharacterEncodingScheme() {
        return reader.getCharacterEncodingScheme();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPITarget() {
        return reader.getPITarget();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPIData() {
        return reader.getPIData();
    }
    
}
