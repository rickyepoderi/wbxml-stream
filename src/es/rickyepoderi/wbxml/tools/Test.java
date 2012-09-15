package es.rickyepoderi.wbxml.tools;


import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.bind.drmrel.OExContext;
import es.rickyepoderi.wbxml.bind.drmrel.OExRights;
import es.rickyepoderi.wbxml.bind.si.Si;
import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import es.rickyepoderi.wbxml.stream.WbXmlStreamWriter;
import es.rickyepoderi.wbxml.bind.syncml.SyncML;
import es.rickyepoderi.wbxml.bind.wvcsp.WVCSPMessage;
import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ricky
 */
public class Test {
 
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger("es.rickyepoderi.wbxml");
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.FINE);
        
        JAXBContext jc;
        Unmarshaller unmarshaller;
        FileOutputStream writer;
        FileInputStream reader;
        XMLStreamReader xmlStreamReader;
        XMLStreamWriter xmlStreamWriter;
        Marshaller marshaller;
        /*
        jc = JAXBContext.newInstance(OExRights.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        //unmarshaller.setSchema(null);
        OExRights rights = (OExRights) unmarshaller.unmarshal(
                new File("/home/ricky/NetBeansProjects/wbxml-jaxb/examples/drmrel/drmrel-003.xml"));
        writer = new FileOutputStream("/home/ricky/drmrel.wbxml");
        xmlStreamWriter = new WbXmlStreamWriter(writer, WbXmlInitialization.getDefinitionByName("drmrel 1.0"));
        marshaller = jc.createMarshaller();
        marshaller.marshal(rights, xmlStreamWriter);
        xmlStreamWriter.close();
        */
        jc = JAXBContext.newInstance(SyncML.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        //unmarshaller.setSchema(null);
        SyncML syncml = (SyncML) unmarshaller.unmarshal(
                new File("/home/ricky/NetBeansProjects/wbxml-jaxb/examples/syncml/syncml-001.xml"));
        writer = new FileOutputStream("/home/ricky/syncml.wbxml");
        xmlStreamWriter = new WbXmlStreamWriter(writer);
        marshaller = jc.createMarshaller();
        marshaller.marshal(syncml, xmlStreamWriter);
        xmlStreamWriter.close();
        /*
        jc = JAXBContext.newInstance(Si.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        //unmarshaller.setSchema(null);
        Si si = (Si) unmarshaller.unmarshal(
                new File("/home/ricky/NetBeansProjects/wbxml-jaxb/examples/si/si-001.xml"));
        System.err.println("action: " + si.getIndication().getAction());
        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writer = new FileOutputStream("/home/ricky/si.wbxml");
        xmlStreamWriter = new WbXmlStreamWriter(writer);
        marshaller = jc.createMarshaller();
        marshaller.marshal(si, xmlStreamWriter);
        xmlStreamWriter.close();
        //bos.close();
        //for (int i = 0; i < bos.toByteArray().length; i++) {
        //    System.err.println(WbXmlDefinition.formatUInt8Char(bos.toByteArray()[i]));
        //}
        
        jc = JAXBContext.newInstance(WVCSPMessage.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        //unmarshaller.setSchema(null);
        WVCSPMessage wvcsp = (WVCSPMessage) unmarshaller.unmarshal(
                new File("/home/ricky/NetBeansProjects/wbxml-jaxb/examples/wv/wv-013.xml"));
        writer = new FileOutputStream("/home/ricky/wvcsp.wbxml");
        xmlStreamWriter = new WbXmlStreamWriter(writer, WbXmlInitialization.getDefinitionByName("WV CSP 1.1"));
        marshaller = jc.createMarshaller();
        marshaller.marshal(wvcsp, xmlStreamWriter);
        xmlStreamWriter.close();
        
        jc = JAXBContext.newInstance(OExRights.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        reader = new FileInputStream("/home/ricky/drmrel.wbxml");
        xmlStreamReader = new WbXmlStreamReader(reader);
        OExRights rights = (OExRights) unmarshaller.unmarshal(xmlStreamReader);
        marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(rights, System.out);
        xmlStreamReader.close();
        
        jc = JAXBContext.newInstance(SyncML.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        reader = new FileInputStream("/home/ricky/syncml.wbxml");
        xmlStreamReader = new WbXmlStreamReader(reader);
        SyncML syncml = (SyncML) unmarshaller.unmarshal(xmlStreamReader);
        marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(syncml, System.out);
        xmlStreamReader.close();
        
        jc = JAXBContext.newInstance(Si.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        reader = new FileInputStream("/home/ricky/si.wbxml");
        xmlStreamReader = new WbXmlStreamReader(reader);
        Si si = (Si) unmarshaller.unmarshal(xmlStreamReader);
        marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(si, System.out);
        xmlStreamReader.close();
        
        jc = JAXBContext.newInstance(WVCSPMessage.class);
        System.err.println(jc.getClass());
        unmarshaller = jc.createUnmarshaller();
        reader = new FileInputStream("/home/ricky/wvcsp.wbxml");
        xmlStreamReader = new WbXmlStreamReader(reader);
        WVCSPMessage wvcsp = (WVCSPMessage) unmarshaller.unmarshal(xmlStreamReader);
        marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(wvcsp, System.out);
        xmlStreamReader.close();
        */
    }
 
}