/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document.opaque;

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.OpaquePlugin;
import es.rickyepoderi.wbxml.document.WbXmlBody;
import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlDocument;
import es.rickyepoderi.wbxml.document.WbXmlElement;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * <p>This is the most complicated opaque in the current implementation. Although
 * specification was not studied it seems that SyncML can include inside the Data
 * a syncml:devinf (which is another language, i.e. another WBXML document).
 * This is really weird and also it complicates the whole process... This
 * opaque has made change a lot of code.</p>
 * 
 * <p>This opaque, change the prefix to all elements to assign the devinf
 * prefix, and then perform another different encoding / parsing. The
 * resulting WBXML document is written as opaque.</p>
 * 
 * @author ricky
 */
public class SyncMLDataOpaque implements OpaquePlugin {

    /**
     * Utility method that assigns the prefix. When a devinf is passed the
     * prefix is assigned cos the TAGS are unknown til the moment and must
     * be prefixed.
     * @param prefix The prefix to set
     * @param element The element to call recursively
     */
    private void setPrefixRecursive(String prefix, WbXmlElement element) {
        // change the element tag
        element.setTag(new StringBuilder(prefix).append(":").append(element.getTagWithoutPrefix()).toString());
        // call recursive
        for (WbXmlContent c: element.getContents()) {
            if (c.isElement()) {
                setPrefixRecursive(prefix, c.getElement());
            }
        }
    }
    
    /**
     * Utility method that assigns the prefix. Initial method to assign the
     * prefix of the devinf namespace.
     * @param devinf The devinf definition
     * @param element The element to assign the prefix
     */
    private void setPrefixForAllTags(WbXmlDefinition devinf, WbXmlElement element) {
        String prefix = devinf.getPrefix("syncml:devinf");
        if (prefix != null && !prefix.isEmpty()) {
            setPrefixRecursive(prefix, element);
        }
    }
    
    /**
     * Method that encode a Data element. If the element is a string or
     * a metinf it is just normally encoded, if it is a devinf a new document
     * is generated in the opaque.
     * @param encoder The encoder doing the encoder processing
     * @param content The content of the Data element
     * @throws IOException Some error writing to the stream
     */
    @Override
    public void encode(WbXmlEncoder encoder, WbXmlContent content) throws IOException {
        // the data can be a string (normal write) and an DevInf element
        if (content.isString()) {
            encoder.writeString(content.getString());
        } else if (content.isElement()) {
            ByteArrayOutputStream bos = null;
            try {
                // it can be a MetaInf or a DevInf
                String prefix = content.getElement().getTagPrefix();
                if ("metinf".equals(prefix)) {
                    encoder.encode(content);
                } else {
                    // if it is a Element it should be a DevInf WbXml data
                    WbXmlDefinition def = WbXmlInitialization.getDefinitionByFPI("-//SYNCML//DTD DevInf 1.1//EN");
                    if (def == null) {
                        throw new IOException("The definition is not defined!");
                    }
                    WbXmlDocument doc = new WbXmlDocument(def, encoder.getIanaCharset());
                    doc.setBody(new WbXmlBody(content.getElement()));
                    bos = new ByteArrayOutputStream();
                    WbXmlEncoder tmp = new WbXmlEncoder(bos, doc, encoder.getType());
                    // the prefix must be set for all the elements
                    setPrefixForAllTags(def, content.getElement());
                    // encode the devinf in the byte array
                    tmp.encode(doc);
                    bos.flush();
                    // create a opaque with the bos array
                    byte[] bytes = bos.toByteArray();
                    //System.err.println();
                    //for (int i = 0; i < bytes.length; i++) {
                    //    System.err.print(WbXmlDefinition.formatUInt8Char(bytes[i]));
                    //    System.err.print(" ");
                    //}
                    //System.err.println();
                    encoder.writeOpaque(bytes);
                }
            } finally {
                if (bos != null) {
                    try {bos.close();} catch(Exception e) {}
                }
            }
            
        } else {
            throw new IOException("The Data should be a String or a Element");
        }
    }

    /**
     * Method that parses the opaque. It generates another WbXmlParser and
     * re-parse the opaque data in a ByteArrayInputStream. The definitions
     * used are appended to the original parser (this is the big difference 
     * that language has introduced).
     * @param parser The parser which is processing the decoding process
     * @param data The data of the opaque
     * @return The content with the another document
     * @throws IOException Some error decoding the other document
     */
    @Override
    public WbXmlContent parse(WbXmlParser parser, byte[] data) throws IOException {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            WbXmlParser tmp = new WbXmlParser(bis);
            WbXmlDocument doc = tmp.parse();
            parser.getdefinitionsUsed().addAll(tmp.getdefinitionsUsed());
            return new WbXmlContent(doc.getBody().getElement());
        } catch (Exception e) {
            // treat it as a string
            return new WbXmlContent(new String(data, parser.getCharset()));
        }
    }
    
}
