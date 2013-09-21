/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document;

import java.io.IOException;

/**
 * <p>WBXML defines that attribute and content can have a special kind
 * of opaque data (an array of bytes in length). Usually the language 
 * definitions use those opaque tags to encode special information
 * (for example SI encodes in an opaque data date formats, or WV uses
 * opaques to encode integers and datetime data). Obviously opaque data
 * is unique in every language and special plugins should parse or encode
 * that data</p>
 * 
 * <p>This interface let the user to develop special treatment for attreibute.
 * The definition loads the attr plugins and the parser and the encoder 
 * call the specified method if such attribute is found. The parser
 * obviously only calls the parse method if it finds an opaque data. The 
 * encoder always calls the encode method, the implementation can encode the
 * attribute value vas an opaque or not.</p>
 * 
 * <p>This interface is used for attributes. There is another very similar
 * interface for tags / contents.</p>
 * 
 * @author ricky
 */
public interface OpaqueAttributePlugin {
    
    /**
     * Encode method that encodes a opaque data. All the elements in the current
     * encoding are passed. There opaques so weird that is better to have all
     * the possible status of the encoding.
     * 
     * @param encoder The encoder used in the encoding process
     * @param element The element which content or attribute is being encoded 
     * @param attr The attribute which is being encoded
     * @param value The value string is being encoded
     * @throws IOException Some error in the encoding process
     */
    public void encode(WbXmlEncoder encoder, WbXmlElement element, 
            WbXmlAttribute attr, String value) throws IOException;
    
    /**
     * Parse method that parses an opaque data. The opaque data is read from
     * the WBXML stream and passed in the byte array argument. In this case 
     * the parser only calls the plugin if real opaque data is found.
     * 
     * @param parser The parser doing the parsing
     * @param data The data read from the opaque
     * @return The string value the opaque represents
     * @throws IOException Some error in the parsing
     */
    public String parse(WbXmlParser parser, byte[] data) throws IOException;
}
