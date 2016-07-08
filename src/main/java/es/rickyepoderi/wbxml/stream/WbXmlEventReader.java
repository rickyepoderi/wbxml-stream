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

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;

/**
 * <p>Class that implements the XMlEventReader for the WbXML format. It uses
 * the WbXmlStreamReader internally, all the events are constructed using
 * the stream implementation and methods.</p>
 * 
 * <p>It was deeply considered to just implement this reader as a class
 * that extends XMLStreamReaderImpl. This class is the internal class in javaSE
 * that implements XMLEventReader. There is a constructor which uses a 
 * XMLStreamReader, so it could have been implemented just using that class
 * and creating it using a WbXmlStreamReader. Some tests were done and it was
 * perfectly functional. Besides that solution would have been saved the 
 * implementation of any event. But finally it was decided to implement
 * the real WbXmlEventReader to avoid being dependant of internal JavaSE classes</p>
 * 
 * @author ricky
 */
public class WbXmlEventReader implements XMLEventReader {

    /**
     * Logger of the class.
     */
    protected static final Logger log = Logger.getLogger(WbXmlEventReader.class.getName());
    
    /**
     * The WbXmlStreamReader which is used internally.
     */
    private WbXmlStreamReader stream = null;
    
    /**
     * The current event.
     */
    private XMLEvent currentEvent = null;
    
    /**
     * The backup if we need to restore the internal stream reader.
     */
    private StreamRestorePoint backup;
    
    /**
     * The next event.
     */
    private XMLEvent nextEvent = null;
    
    /**
     * Filter to use in the reader (if used).
     */
    private EventFilter filter = null;
    
    /**
     * The XMLEventAllocator used to parse the stream reader.
     * NOTE: The StAX API (weird) has no method to obtain the default
     *       event allocator (no factory), so one is provided by wbxml-stream
     */
    private XMLEventAllocator allocator = null;
    
    /**
     * Constructor using the WbXmlStreamReader.
     * @param stream The stream reader not modified after creation.
     * @param allocator The allocator to use to parse the stream
     */
    public WbXmlEventReader(WbXmlStreamReader stream, XMLEventAllocator allocator) throws XMLStreamException {
        this.stream = stream;
        this.allocator = allocator;
        currentEvent = null;
        nextEvent = constructEvent(stream);
        this.advanceNextEvent();
    }
    
    /**
     * Constructor using a previously parsed parser.
     * @param parser The parser which already has parsed a WBXML file
     * @param allocator The allocator to use to parse the stream
     * @throws XMLStreamException Some error with the parsing
     */
    public WbXmlEventReader(WbXmlParser parser, XMLEventAllocator allocator) throws XMLStreamException {
        stream = new WbXmlStreamReader(parser);
        this.allocator = allocator;
        currentEvent = null;
        nextEvent = constructEvent(stream);
        this.advanceNextEvent();
    }
    
    /**
     * Constructor using only the input stream. Definition guessed.
     * @param is The InputStream to read the WBXML
     * @param allocator The allocator to use to parse the stream
     * @throws IOException Some exception with the file
     * @throws XMLStreamException Some exception with the parsing
     */
    public WbXmlEventReader(InputStream is, XMLEventAllocator allocator) throws IOException, XMLStreamException {
        this(is, null, allocator);
    }
    
    /**
     * Constructor using all the possible parameters.
     * @param is The InputStream to read the WBXML
     * @param definition The definition to use (forced). If null it is guessed.
     * @param allocator The allocator to use to parse the stream
     * @throws IOException Some exception with the file
     * @throws XMLStreamException Some exception with the parsing
     */
    public WbXmlEventReader(InputStream is, WbXmlDefinition definition, XMLEventAllocator allocator) throws IOException, XMLStreamException {
        this(new WbXmlStreamReader(is, definition), allocator);
    }
    
    /**
     * Assigns a filter to the current event reader.
     * @param filter The filter to use
     * @throws XMLStreamException Some error adjusting the next event
     */
    public void assignFilter(EventFilter filter) throws XMLStreamException {
        this.filter = filter;
        this.advanceNextEvent();
    }
    
    /**
     * Return the parser used for parsing.
     * @return The parser
     */
    public WbXmlParser getParser() {
        return stream.getParser();
    }
    
    /**
     * Constructs the events based on the one read from the XMLStreamWriter.
     * @param stream The stream at the correct point of reading
     * @return The next event constructed based on the next element
     * @throws XMLStreamException Some error
     */
    private XMLEvent constructEvent(WbXmlStreamReader stream
            ) throws XMLStreamException {
        log.log(Level.FINE, "constructEvent()");
        XMLEvent event = allocator.allocate(stream);
        log.log(Level.FINE, "constructEvent(): {0} {1}", new Object[]{event.getEventType(), event});
        return event;
    }
    
    private void advanceNextEvent() throws XMLStreamException {
        if (filter != null) {
            backup = stream.backup();
            XMLEvent curr = this.currentEvent;
            while (nextEvent != null && !filter.accept(nextEvent)) {
                this.internalNextEvent();
            }
            this.currentEvent = curr;
        }
    }
    
    /**
     * Get the next XMLEvent
     * @return The next event read from the stream
     * @throws XMLStreamException if there is an error with the underlying XML.
     * @throws NoSuchElementException iteration has no more elements.
     */
    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        log.log(Level.FINE, "nextEvent()");
        this.internalNextEvent();
        this.advanceNextEvent();
        log.log(Level.FINE, "nextEvent(): {0}", currentEvent);
        return currentEvent;
    }
    
    /**
     * Get the next XMLEvent internally. Just advancing one event.
     * @return The next event read from the stream
     * @throws XMLStreamException if there is an error with the underlying XML.
     * @throws NoSuchElementException iteration has no more elements.
     */
    protected XMLEvent internalNextEvent() throws XMLStreamException {
        log.log(Level.FINE, "internalNextEvent()");
        if (nextEvent == null) {
            throw new NoSuchElementException();
        } else {
            currentEvent = nextEvent;
            if (currentEvent.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                stream.next();
                nextEvent = constructEvent(stream);
            } else {
                nextEvent = null;
            }
        }
        log.log(Level.FINE, "internalNextEvent(): {0}", currentEvent);
        return currentEvent;
    }

    /**
     * Check if there are more events. Returns true if there are more events 
     * and false otherwise.
     * @return true if the event reader has more events, false otherwise
     */
    @Override
    public boolean hasNext() {
        log.log(Level.FINE, "hasNext()");
        boolean result = (nextEvent != null);
        log.log(Level.FINE, "hasNext(): {0}", result);
        return result;
    }

    /**
     * Check the next XMLEvent without reading it from the stream. Returns null 
     * if the stream is at EOF or has no more XMLEvents. A call to peek() will 
     * be equal to the next return of next().
     * @return The next element of the stream but not forwarding the position
     * @throws XMLStreamException 
     */
    @Override
    public XMLEvent peek() throws XMLStreamException {
        log.log(Level.FINE, "peek(): {0}", nextEvent);
        return nextEvent;
    }

    /**
     * Reads the content of a text-only element. Precondition: the current event 
     * is START_ELEMENT. Postcondition: The current event is the corresponding 
     * END_ELEMENT.
     * @return The string value found in the value of the element
     * @throws XMLStreamException if the current event is not a START_ELEMENT 
     *         or if a non text element is encountered
     */
    @Override
    public String getElementText() throws XMLStreamException {
        log.log(Level.FINE, "getElementText()");
        if (!(currentEvent instanceof StartElement)) {
            throw new XMLStreamException("Not in a START_ELEMENT!");
        }
        if (filter != null && backup != null) {
            // the stream can be moved to the next valid event
            stream.restore(backup);
            nextEvent = constructEvent(stream);
        }
        StringBuilder sb = new StringBuilder();
        XMLEvent event = this.internalNextEvent();
        while (!(event instanceof EndElement)) {
            if (event instanceof Characters) {
                Characters chars = (Characters) event;
                sb.append(chars.getData());
            } else if (event instanceof StartElement) {
                throw new XMLStreamException("Another START_ELEMENT found while iterating!");
            }
            // all the rest are ignored
            event = this.internalNextEvent();
        }
        this.advanceNextEvent();
        log.log(Level.FINE, "getElementText(): {0}", sb.toString());
        return sb.toString();
    }

    /**
     * Skips any insignificant space events until a START_ELEMENT or END_ELEMENT 
     * is reached. If anything other than space characters are encountered, an 
     * exception is thrown. This method should be used when processing element-only 
     * content because the parser is not able to recognize ignorable whitespace 
     * if the DTD is missing or not interpreted.
     * @return The next StartElement or EndElement
     * @throws XMLStreamException if anything other than space characters are encountered
     */
    @Override
    public XMLEvent nextTag() throws XMLStreamException {
        log.log(Level.FINE, "nextTag()");
        if (filter != null && backup != null) {
            // the stream can be moved to the next valid event
            stream.restore(backup);
            nextEvent = constructEvent(stream);
        }
        XMLEvent event = this.internalNextEvent();
        while (!(event instanceof EndElement) && !(event instanceof StartElement)) {
            if (event instanceof Characters) {
                Characters chars = (Characters) event;
                if (!chars.isIgnorableWhiteSpace()) {
                    throw new XMLStreamException("Non-ignorable CHARACTERS found!");
                }
            }
            event = this.internalNextEvent();
        }
        this.advanceNextEvent();
        log.log(Level.FINE, "nextTag(): {0}", event);
        return event;
    }

    /**
     * Get the value of a feature/property from the underlying implementation
     * @param name The name of the property
     * @return The value of the property
     * @throws IllegalArgumentException 
     */
    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        log.log(Level.FINE, "getProperty()");
        return stream.getProperty(name);
    }

    /**
     * Frees any resources associated with this Reader. This method does not 
     * close the underlying input source.
     * @throws XMLStreamException 
     */
    @Override
    public void close() throws XMLStreamException {
        log.log(Level.FINE, "close()");
        stream.close();
    }

    /**
     * Returns the next element in the iteration.
     * @return  The next element
     */
    @Override
    public Object next() {
        try {
            return nextEvent();
        } catch(XMLStreamException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Removes from the underlying collection the last element returned by the 
     * iterator (optional operation). This method can be called only once per 
     * call to next. The behavior of an iterator is unspecified if the underlying 
     * collection is modified while the iteration is in progress in any way 
     * other than by calling this method.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("The WbXmlEventReader is not modifiable!");
    }
    
}
