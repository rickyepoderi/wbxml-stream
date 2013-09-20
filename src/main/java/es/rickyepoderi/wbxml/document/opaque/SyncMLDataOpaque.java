/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document.opaque;

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.document.OpaqueContentPlugin;
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
 * a syncml:devinf (which is another language, i.e. another WBXML document) or
 * any other languages.</p>
 * <p>This is really weird and also it complicates the whole process... This
 * opaque has made change a lot of code. The linked definitions were added
 * to a wbxml definition in order to know when a language has another 
 * languages <em>linked</em>.</p>
 * 
 * <p>The implementation do another parsing/decoding process for the
 * second definition.</p>
 * 
 * @author ricky
 */
public class SyncMLDataOpaque implements OpaqueContentPlugin {

    /**
     * Method that encode a Data element. If the element is a string or
     * a metinf it is just normally encoded, if it is a devinf a new document
     * is generated in the opaque.
     * @param encoder The encoder doing the encoder processing
     * @param element The element which content or attribute is being encoded 
     * @param content The content of the Data element
     * @throws IOException Some error writing to the stream
     */
    @Override
    public void encode(WbXmlEncoder encoder, WbXmlElement element, WbXmlContent content) throws IOException {
        // the data can be a string (normal write) and an DevInf element
        if (content.isString()) {
            encoder.writeString(content.getString());
        } else if (content.isElement()) {
            ByteArrayOutputStream bos = null;
            try {
                // it can be a MetaInf or a DevInf
                if (encoder.getDefinition().locateTag(content.getElement().getTag()) != null) {
                    // common element inside the synml definition (do normal encoding)
                    encoder.encode(content);
                } else {
                    // if it is a and element of a linked definition
                    WbXmlDefinition def = encoder.getDefinition().locateLinkedDefinitionForTag(
                            content.getElement().getTag());
                    if (def == null) {
                        throw new IOException(String.format(
                                "The definition for tag %s is not defined as linked!", 
                                content.getElement().getTag()));
                    }
                    WbXmlDocument doc = new WbXmlDocument(def, encoder.getIanaCharset());
                    doc.setBody(new WbXmlBody(content.getElement()));
                    bos = new ByteArrayOutputStream();
                    WbXmlEncoder tmp = new WbXmlEncoder(bos, doc, encoder.getType());
                    // encode the devinf in the byte array
                    tmp.encode();
                    bos.flush();
                    // create a opaque with the bos array
                    byte[] bytes = bos.toByteArray();
                    //System.err.println();
                    //for (int i = 0; i < bytes.length; i++) {
                    //    System.err.print(WbXmlLiterals.formatUInt8Char(bytes[i]));
                    //    System.err.print(" ");
                    //}
                    //System.err.println();
                    //BufferedOutputStream file = new BufferedOutputStream(new FileOutputStream("/home/ricky/lala.wbxml"));
                    //file.write(bytes);
                    //file.close();
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
        WbXmlContent res;
        WbXmlDocument doc = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            WbXmlParser tmp = new WbXmlParser(bis);
            doc = tmp.parse();
            res = new WbXmlContent(doc.getBody().getElement());
        } catch (Exception e) {
            // treat it as a string
            //e.printStackTrace();
            res = new WbXmlContent(new String(data, parser.getCharset()));
        }
        if (res.isElement() && doc != null && parser.getDefinition().getLinkedDef(
                doc.getDefinition().getName()) == null) {
            throw new IOException(String.format(
                                "The definition %s is not defined as linked!", 
                                doc.getDefinition().getName()));
        }
        return res;
    }
    
}
