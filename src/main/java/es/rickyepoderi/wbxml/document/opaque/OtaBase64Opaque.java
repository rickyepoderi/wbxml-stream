/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document.opaque;

import es.rickyepoderi.wbxml.document.OpaqueAttributePlugin;
import es.rickyepoderi.wbxml.document.WbXmlAttribute;
import es.rickyepoderi.wbxml.document.WbXmlElement;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>OTA definition has a property which is an icon (binary image). In
 * XML the Base64 representation of the binary is used.</p>
 * 
 * @author ricky
 */
public class OtaBase64Opaque implements OpaqueAttributePlugin {

    /**
     * Encode method that encodes a opaque data. The Base64 is decoded in case 
     * of ICON in order to save into the WBXML the binary image.
     * 
     * @param encoder The encoder used in the encoding process
     * @param element The element which content or attribute is being encoded 
     * @param attr The attribute which is being encoded
     * @param value The value string is being encoded
     * @throws IOException Some error in the encoding process
     */
    @Override
    public void encode(WbXmlEncoder encoder, WbXmlElement element, 
            WbXmlAttribute attr, String value) throws IOException {
        boolean isIcon = false;
        // only value for <PARM NAME="ICON" VALUE="xxx"/> should be encoded
        if (element.getTag().equals("PARM")) {
            WbXmlAttribute name = element.getAttribute("NAME");
            if (name != null && name.getValue().equals("ICON")) {
                isIcon = true;
                BASE64Decoder dec = new BASE64Decoder();
                encoder.writeOpaque(dec.decodeBuffer(value));
            }
        }
        if (!isIcon) {
            // not encode just common attribute encode
            encoder.encodeAttributeValue(value);
        }
    }

    /**
     * Parse method that parses an opaque data. The binary is encoded into
     * Base64 (the new lines are removed).
     * 
     * @param parser The parser doing the parsing
     * @param data The data read from the opaque
     * @return The string value the opaque represents
     * @throws IOException Some error in the parsing
     */
    @Override
    public String parse(WbXmlParser parser, byte[] data) throws IOException {
        // get the data and show it as Base64
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(data).replaceAll(System.getProperty("line.separator"), "");
    }
    
}
