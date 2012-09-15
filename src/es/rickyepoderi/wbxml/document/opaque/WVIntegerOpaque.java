/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document.opaque;

import es.rickyepoderi.wbxml.document.OpaquePlugin;
import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p>Again the WV language let encode integers as opaque data. The especification
 * says the following:</p>
 * 
 * <p>An integer number can be encoded as OPAQUE (0xC3) followed by an 
 * mb_u_int32 that contains the length of the integer, then the actual bytes 
 * of the integer in big -endian order (most significant bytes first).
 * Example : The 2001 (0x07D1) number would be encoded as: 0xC3 0x02 0x07 0xD1</p>
 * 
 * <p>In this case integers are always encoded as opaque.</p>
 * 
 * @author ricky
 */
public class WVIntegerOpaque implements OpaquePlugin {

    /**
     * The method get the string, decode as long and then write the bytes one
     * by one (until the four bytes are written or zero reached.</p>
     * @param encoder The encoder being used in the encoding process
     * @param content The content with the integer value as a string
     * @throws IOException Some error
     */
    @Override
    public void encode(WbXmlEncoder encoder, WbXmlContent content) throws IOException {
        if (!content.isString()) {
            throw new IOException("The content is not a String value!");
        }
        long v = Long.decode(content.getString());    
        byte[] bytes = new byte[4];
        int i;
        for (i = 3; i >= 0 && v > 0; i--) {
            bytes[i] = (byte) (v & 0xFF);
            v >>= 8;
        }
        int start = i + 1;
        encoder.writeOpaque(Arrays.copyOfRange(bytes, start, bytes.length));
    }

    /**
     * Method that gat the opaque data and reconstructs the integer as a
     * string content.
     * @param parser The parser doing the parsing/decoding process
     * @param data The data with the integer bytes (four at maximum)
     * @return The string content with the numeric value
     * @throws IOException Some error
     */
    @Override
    public WbXmlContent parse(WbXmlParser parser, byte[] data) throws IOException {
        long v = 0;
        for (int i = 0; i< data.length; i++) {
            v = (v << 8) | (data[i] & 0xFF);
        }
        return new WbXmlContent(Long.toString(v));
    }
    
}
