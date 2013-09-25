/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream;

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * <p>The output factory to create stream and event writer for the WBXML
 * format. They supports three properties:</p>
 * 
 * <ul>
 * <li>es.rickyepoderi.wbxml.stream.encodingType: The encoding type. The value
 * should be a WbXmlEncoder.StrblType (default IF_NEEDED).</li>
 * <li>es.rickyepoderi.wbxml.stream.skipSpaces: The encoder will trim
 * spaces. Boolean value (true by default).</li>
 * <li>es.rickyepoderi.wbxml.stream.definition: The definition to force. It
 * should be a WbXmlDefinition (by default is null, no forced definition).</li>
 * </ul>
 * 
 * @author ricky
 */
public class WbXmlOutputFactory extends XMLOutputFactory {

    /**
     * The property for defining the encoding type. The value should be a 
     * WbXmlEncoder.StrtblType (default to IF_NEEDED).
     */
    static public String ENCODING_TYPE_PROPERTY = "es.rickyepoderi.wbxml.stream.encodingType";
    
    /**
     * The property for defining the skip spaces. Boolean value (default true).
     */
    static public String SKIP_SPACES_PROPERTY = "es.rickyepoderi.wbxml.stream.skipSpaces";
    
    /**
     * The property for the definition to use. WbXmlDefnition value (default null,
     * the parser will guess it from public id of the WBXML file.
     */
    static public String DEFINITION_PROPERTY = "es.rickyepoderi.wbxml.stream.definition";
    
    /**
     * Properties of the factory.
     */
    private Map<String,Object> props = null;
    
    /**
     * Empty constructor with the default values.
     */
    public WbXmlOutputFactory() {
        props = new HashMap<String,Object>();
        props.put(ENCODING_TYPE_PROPERTY, WbXmlEncoder.StrtblType.IF_NEEDED);
        props.put(SKIP_SPACES_PROPERTY, Boolean.TRUE);
    }
    
    /**
     * Create a new XMLEventWriter that writes to a stream. 
     * Throws a XMLStreamExcption cos WBXML is binary, use the other methods.
     * @param writer the stream to write to
     * @return The stream writer implementation for WBXML
     * @throws XMLStreamException Some error creating it.
     */
    @Override
    public XMLStreamWriter createXMLStreamWriter(Writer writer) throws XMLStreamException {
        throw new XMLStreamException("The WBXML is a binary format!");
    }

    /**
     * Create a new XMLEventWriter that writes to a stream 
     * @param out the stream to write to
     * @return The stream writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLStreamWriter createXMLStreamWriter(OutputStream out) throws XMLStreamException {
        return new WbXmlStreamWriter(out, 
                (WbXmlDefinition) props.get(DEFINITION_PROPERTY),
                (WbXmlEncoder.StrtblType) props.get(ENCODING_TYPE_PROPERTY), 
                (Boolean) props.get(SKIP_SPACES_PROPERTY));
    }

    /**
     * Create a new XMLEventWriter that writes to a stream
     * @param out the stream to write to
     * @param encoding the encoding to use
     * @return The stream writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLStreamWriter createXMLStreamWriter(OutputStream out, String encoding) throws XMLStreamException {
        return new WbXmlStreamWriter(out, 
                (WbXmlDefinition) props.get(DEFINITION_PROPERTY),
                (WbXmlEncoder.StrtblType) props.get(ENCODING_TYPE_PROPERTY), 
                (Boolean) props.get(SKIP_SPACES_PROPERTY),
                encoding);
    }

    /**
     * Create a new XMLStreamWriter that writes to a JAXP result. It only works
     * with a StreamResult.
     * @param result the result to write to
     * @return The stream writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        try {
            if (result instanceof StreamResult) {
                return new WbXmlStreamWriter(new FileOutputStream(result.getSystemId()),
                        (WbXmlDefinition) props.get(DEFINITION_PROPERTY),
                        (WbXmlEncoder.StrtblType) props.get(ENCODING_TYPE_PROPERTY),
                        (Boolean) props.get(SKIP_SPACES_PROPERTY));
            } else {
                throw new XMLStreamException("WBXML only support StreamResult!");
            }
        } catch (IOException e) {
            throw new XMLStreamException(e);
        }
    }

    /**
     * Create a new XMLEventWriter that writes to a JAXP result. This method is optional. 
     * It is only valid for a a StreamResult.
     * @param result the result to write to
     * @return The event writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
        try {
            if (result instanceof StreamResult) {
                return new WbXmlEventWriter(new FileOutputStream(result.getSystemId()),
                        (WbXmlDefinition) props.get(DEFINITION_PROPERTY),
                        (WbXmlEncoder.StrtblType) props.get(ENCODING_TYPE_PROPERTY),
                        (Boolean) props.get(SKIP_SPACES_PROPERTY));
            } else {
                throw new XMLStreamException("WBXML only support StreamResult!");
            }
        } catch (IOException e) {
            throw new XMLStreamException(e);
        }
    }

    /**
     * Create a new XMLEventWriter that writes to a stream 
     * @param out the stream to write to 
     * @return The event writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLEventWriter createXMLEventWriter(OutputStream out) throws XMLStreamException {
        return new WbXmlEventWriter(out,
                (WbXmlDefinition) props.get(DEFINITION_PROPERTY),
                (WbXmlEncoder.StrtblType) props.get(ENCODING_TYPE_PROPERTY), 
                (Boolean) props.get(SKIP_SPACES_PROPERTY));
    }

    /**
     * Create a new XMLEventWriter that writes to a stream
     * @param out the stream to write to
     * @param encoding the encoding to use
     * @return The event writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLEventWriter createXMLEventWriter(OutputStream out, String encoding) throws XMLStreamException {
        return new WbXmlEventWriter(out,
                (WbXmlDefinition) props.get(DEFINITION_PROPERTY),
                (WbXmlEncoder.StrtblType) props.get(ENCODING_TYPE_PROPERTY), 
                (Boolean) props.get(SKIP_SPACES_PROPERTY),
                encoding);
    }

    /**
     * Create a new XMLEventWriter that writes to a writer. It is not 
     * supported cos WBXML is a binary stream. Use the rest of methods.
     * @param writer the stream to write to
     * @return The event writer implementation for WBXML
     * @throws XMLStreamException Some error creating it
     */
    @Override
    public XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException {
        throw new XMLStreamException("The WBXML is a binary format!");
    }

    /**
     * Allows the user to set specific features/properties on the underlying 
     * implementation. 
     * @param prop The name of the property
     * @param value The value of the property
     * @throws IllegalArgumentException Unknown property
     * @throws ClassCastException Illegal object for properties
     */
    @Override
    public void setProperty(String prop, Object value) throws IllegalArgumentException, ClassCastException {
       if (DEFINITION_PROPERTY.equals(prop)) {
           props.put(prop, (WbXmlDefinition) value);
       } else if (SKIP_SPACES_PROPERTY.equals(prop)) {
           props.put(prop, (Boolean) value);
       } else if (ENCODING_TYPE_PROPERTY.equals(prop)) {
           props.put(ENCODING_TYPE_PROPERTY, (WbXmlEncoder.StrtblType) value);
       } else {
           throw new IllegalArgumentException(String.format("Invalid property %s", prop));
       }
    }

    /**
     * Get a feature/property on the underlying implementation
     * @param prop The name of the property
     * @return The value of the property
     * @throws IllegalArgumentException 
     */
    @Override
    public Object getProperty(String prop) throws IllegalArgumentException {
        if (!isPropertySupported(prop)) {
            throw new IllegalArgumentException(String.format("Invalid property %s", prop));
        }
        return props.get(prop);
    }

    /**
     * Query the set of properties that this factory supports. 
     * @param prop The name of the property (may not be null) 
     * @return true if the property is supported and false otherwise
     */
    @Override
    public boolean isPropertySupported(String prop) {
        return DEFINITION_PROPERTY.equals(prop) ||
                SKIP_SPACES_PROPERTY.equals(prop) ||
                ENCODING_TYPE_PROPERTY.equals(prop);
    }
    
}
