/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document;

import java.io.IOException;

/**
 *
 * <p>WBXML defines that attribute and content can have a special kind
 * of opaque data (an array of bytes in length). Usually the language 
 * definitions use those opaque tags to encode special information
 * (for example SI encodes in an opaque data date formats, or WV uses
 * opaques to encode integers and datetime data). Obviously opaque data
 * is unique in every language and special plugins should parse or encode
 * that data</p>
 * 
 * <p>This interface let the user to develop special treatment for some tags
 * or attributes. The definition loads the plugins and the parser and the encoder 
 * call the specified method if such tag or attribute is found. The parser
 * obviously only calls the parse method if it founds a opaque data. The 
 * encoder always calls the encode method, the implementation can encode
 * as an opaque or not.</p>
 * 
 * <p>This interface is used to opaque data in tags and in attributes, 
 * that is the reason a content is used. Attribute plugins MUST return 
 * a string content (obviously an attribute cannot have elements inside).
 * Maybe two interfaces would be better but... </p>
 * 
 * @author ricky
 */
public interface OpaquePlugin {
    
    /**
     * Encode method that encodes a opaque data. The whole WbXMLcontent is passed
     * in case of a TAG, if it is an attribute value plugin the content
     * has the string to deal with. As I said maybe two interfaces would have 
     * been a better idea. The method can encode as opaque or not, is up to
     * the implementation.
     * 
     * @param encoder The encoder used in the encoding process
     * @param content The content to encode (compulsory to string if it is and attribute plugin)
     * @throws IOException Some error in the encoding process
     */
    public void encode(WbXmlEncoder encoder, WbXmlContent content) throws IOException;
    
    /**
     * Parse method that parses an opaque data. The opaque data is read from
     * the WBXML stream and passed in the byte array argument. In this case 
     * the parser only calls the plugin if real opaque data is found. In case
     * of an attribute plugin the content must be a string type.
     * 
     * @param parser The parser doing the parsing
     * @param data The data read from the opaque
     * @return The content representation of the opaque data
     * @throws IOException Some error in the parsing
     */
    public WbXmlContent parse(WbXmlParser parser, byte[] data) throws IOException;
}
