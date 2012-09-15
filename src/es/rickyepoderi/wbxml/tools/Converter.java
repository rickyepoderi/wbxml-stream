/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.tools;

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import es.rickyepoderi.wbxml.stream.WbXmlStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * java -cp wbxml-jaxb.jar es.rickyepoderi.wbxml.tools.Converter wbxml2xml | xml2wbxml {infile} {outfile}
 * 
 * LINKS:
 * ->  
 * http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/com/sun/xml/internal/stream/XMLInputFactoryImpl.java
 * http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/com/sun/org/apache/xerces/internal/util/NamespaceSupport.java
 * http://svn.jettison.codehaus.org/browse/jettison/trunk/src/main/java/org/codehaus/jettison/mapped/MappedXMLStreamReader.java
 * http://msdn.microsoft.com/en-us/library/dd299442(v=exchg.80).aspx
 * http://msdn.microsoft.com/en-us/library/cc425499(v=exchg.80).aspx
 * 
 * @author ricky
 */
public class Converter {
    
    static final public String WBXML_TO_XML = "wbxml2xml";
    static final public String XML_TO_WBXML = "xml2wbxml";
    static final public String DOM = "dom";
    static final public String JAXB = "jaxb";
    
    static public void usage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        sb.append("java -cp wbxml-jaxb.jar ");
        sb.append(Converter.class.getName());
        sb.append(" [dom|jaxb] wbxml2xml|xml2wbxml {infile} {outfile}");
        sb.append(System.getProperty("line.separator"));
        sb.append("        dom: DOM w3c API is used to parse XMLs (default)");
        sb.append(System.getProperty("line.separator"));
        sb.append("       jaxb: JAXB is used instead (it needs JAXB classes - xjc)");
        sb.append(System.getProperty("line.separator"));
        sb.append("  wbxml2xml: convert a XML file into WbXML");
        sb.append(System.getProperty("line.separator"));
        sb.append("  xml2wbxml: convert a WbXML file into XML");
        sb.append(System.getProperty("line.separator"));
        sb.append("     infile: input file (\"-\" means standard input)");
        sb.append(System.getProperty("line.separator"));
        sb.append("    outfile: output file (\"-\" means sntandard output)");
        sb.append(System.getProperty("line.separator"));
        throw new IllegalArgumentException(sb.toString());
    }
    
    static public void main(String[] args) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        XMLStreamReader xmlStreamReader = null;
        XMLStreamWriter xmlStreamWriter = null;
        String action;
        String method;
        String infile;
        String outfile;
        
        Logger logger = Logger.getLogger("es.rickyepoderi.wbxml");
        //logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.FINER);
        
        try {
            // check the number of arguments
            if (args.length != 3 && args.length != 4) {
                usage("Illegal number of arguments.");
            }
            // get the parameters
            if (args.length == 4) {
                method = args[0];
                action = args[1];
                infile = args[2];
                outfile = args[3];
            } else {
                method = "dom";
                action = args[0];
                infile = args[1];
                outfile = args[2];
            }
            // check the method
            if (!DOM.equals(method) && !JAXB.equals(method)) {
                usage("Illegal method, it shoudl be \"dom\" or \"jaxb\".");
            }
            // check the action
            if (!WBXML_TO_XML.equals(action) && !XML_TO_WBXML.equals(action)) {
                usage("Illegal action, it shoudl be \"wbxml2xml\" or \"xml2wbxml\".");
            }
            // check the input file
            if (infile.equals("-")) {
                in = System.in;
            } else {
                File f = new File(infile);
                if (!f.exists() || !f.canRead()) {
                    usage(String.format("File \"%s\" is not readable", infile));
                }
                in = new FileInputStream(f);
            }
            // check output stream
            if (outfile.equals("-")) {
                out = System.out;
            } else {
                File f = new File(outfile);
                f.createNewFile();
                if (!f.canWrite()) {
                    usage(String.format("File \"%s\" is not writable", outfile));
                }
                out = new FileOutputStream(f);
            }
            if (WBXML_TO_XML.equals(action)) {
                if (JAXB.equals(method)) {
                    WbXmlParser parser = new WbXmlParser(in);
                    parser.parse();
                    xmlStreamReader = new WbXmlStreamReader(parser);
                    String clazz = parser.getDefinition().getClazz();
                    JAXBContext jc = JAXBContext.newInstance(Class.forName(clazz));
                    Unmarshaller unmarshaller = jc.createUnmarshaller();
                    Object obj = unmarshaller.unmarshal(xmlStreamReader);
                    Marshaller marshaller = jc.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(obj, out);
                } else if (DOM.equals(method)) {
                    Transformer xformer = TransformerFactory.newInstance().newTransformer();
                    xformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    WbXmlParser parser = new WbXmlParser(in);
                    parser.parse();
                    xmlStreamReader = new WbXmlStreamReader(parser);
                    StAXSource staxSource = new StAXSource(xmlStreamReader);
                    DOMResult domResult = new DOMResult();
                    xformer.transform(staxSource, domResult);
                    Source domSource = new DOMSource(domResult.getNode(), domResult.getSystemId());
                    Result result = new StreamResult(out);
                    xformer.transform(domSource, result);
                }
            } else if (XML_TO_WBXML.equals(action)) {
                if (JAXB.equals(method)) {
                    DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
                    domFact.setNamespaceAware(true);
                    DocumentBuilder domBuilder = domFact.newDocumentBuilder();
                    Document doc = domBuilder.parse(in);
                    Element element = doc.getDocumentElement();
                    //System.err.println("localName=" + element.getLocalName() + " namespace=" + element.getNamespaceURI());
                    WbXmlDefinition definition = WbXmlInitialization.getDefinitionByRoot(
                            element.getLocalName(), element.getNamespaceURI());
                    if (definition == null) {
                        throw new Exception(
                                String.format("Definition for found for name=%s and namespace=%s",
                                element.getLocalName(), element.getNamespaceURI()));
                    }
                    String clazz = definition.getClazz();
                    JAXBContext jc = JAXBContext.newInstance(Class.forName(clazz));
                    Unmarshaller unmarshaller = jc.createUnmarshaller();
                    Object obj = unmarshaller.unmarshal(doc);
                    xmlStreamWriter = new WbXmlStreamWriter(out, definition);
                    Marshaller marshaller = jc.createMarshaller();
                    marshaller.marshal(obj, xmlStreamWriter);
                } else if (DOM.equals(method)) {
                    DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
                    domFact.setNamespaceAware(true);
                    domFact.setIgnoringElementContentWhitespace(true);
                    domFact.setIgnoringComments(true);
                    DocumentBuilder domBuilder = domFact.newDocumentBuilder();
                    Document doc = domBuilder.parse(in);
                    Element element = doc.getDocumentElement();
                    element.normalize();
                    WbXmlDefinition definition = WbXmlInitialization.getDefinitionByRoot(
                            element.getLocalName(), element.getNamespaceURI());
                    if (definition == null) {
                        throw new Exception(
                                String.format("Definition for found for name=%s and namespace=%s",
                                element.getLocalName(), element.getNamespaceURI()));
                    }
                    Transformer xformer = TransformerFactory.newInstance().newTransformer();
                    xmlStreamWriter = new WbXmlStreamWriter(out, definition);
                    Source domSource = new DOMSource(doc);
                    StAXResult staxResult = new StAXResult(xmlStreamWriter);
                    xformer.transform(domSource, staxResult);
                }
            }
        } finally {
            if (in != null) {
                try {in.close();} catch(Exception e){}
            }
            if (out != null) {
                try {out.close();} catch(Exception e){}
            }
            if (xmlStreamReader != null) {
                try {xmlStreamReader.close();} catch(Exception e){}
            }
            if (xmlStreamWriter != null) {
                try {xmlStreamWriter.close();} catch(Exception e){}
            }
        }
    }
}
