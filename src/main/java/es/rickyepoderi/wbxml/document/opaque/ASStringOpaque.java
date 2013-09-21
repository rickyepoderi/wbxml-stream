/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document.opaque;

import es.rickyepoderi.wbxml.document.OpaqueContentPlugin;
import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlElement;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;

/**
 * <p>Microsoft Exchange ActiveSync uses opaque for two elements:</p>
 * 
 * <ul>
 * <li><a href="http://msdn.microsoft.com/en-us/library/ee159339%28EXCHG.80%29.aspx">ConversationId</a></li>
 * <li><a href="http://msdn.microsoft.com/en-us/library/ee203955%28EXCHG.80%29.aspx">ConversationIndex</a></li>
 * </ul>
 * 
 * <p>It seems that in the XSD are just string type. So these elements are going
 * to be treated just as simple strings.</a>
 * 
 * @author ricky
 */
public class ASStringOpaque implements OpaqueContentPlugin {

    /**
     * The encoding is just passing the string bytes as an opaque.
     * @param encoder The encoder being used
     * @param ewlement The element which content or attribute is being encoded 
     * @param content The content to add to the WBXML document
     * @throws IOException IOException Some error
     */
    @Override
    public void encode(WbXmlEncoder encoder, WbXmlElement ewlement, WbXmlContent content) throws IOException {
        if (!content.isString()) {
            throw new IOException("The content is not a String!");
        }
        String value = content.getString();
        if (value != null) {
            encoder.writeOpaque(value.getBytes(encoder.getIanaCharset().getCharset()));
        }
    }

    /**
     * Method that parses the string.
     * 
     * @param parser The parser which is performing the decoding/parsing process
     * @param data The data read from the opaque value
     * @return The string content representing the date time
     * @throws IOException
     */
    @Override
    public WbXmlContent parse(WbXmlParser parser, byte[] data) throws IOException {
        String value =  new String(data, parser.getDocument().getCharset().getCharset());
        WbXmlContent content = new WbXmlContent(value);
        //System.err.println(value);
        return content;
    }
    
}
