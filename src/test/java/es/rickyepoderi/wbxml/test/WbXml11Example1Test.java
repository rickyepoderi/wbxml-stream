/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.test;

import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlVersion;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.Test;

/**
 * This test tries to parse/encode the example 1 in the documentation of
 * WBXML 1.1 just replacing &nbsp; for &ntilde; to make the text different.
 * There is no example for 1.2 or 1.3 because just changes the version.
 * 
 * @author ricky
 */
public class WbXml11Example1Test extends GenericDirectoryTester {
    
    public WbXml11Example1Test() throws IOException {
        super("src/test/examples/spec-examples/example1-11", null);
        if (WbXmlInitialization.getDefinitionByName("WBXML-EXAMPLE-1") == null) {
            Properties props = new Properties();
            props.load(new FileInputStream("src/test/examples/spec-examples/example1-10/wbxml.example1.properties"));
            WbXmlInitialization.addDefinition(props);
        }
        this.setDefinition(WbXmlInitialization.getDefinitionByName("WBXML-EXAMPLE-1"));
    }
    
    //
    // XML -> WBXML stream 
    //
    
    @Test(groups = {"xml", "wbxml11", "type-if-needed", "stream" })
    public void testXmlStreamIfNeeded() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, false, false);
    }
    
    @Test(groups = {"xml", "wbxml11", "type-always", "stream" })
    public void testXmlStreamAllways() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.ALWAYS, false, false);
    }
    
    @Test(groups = {"xml", "wbxml11", "type-no", "stream" })
    public void testXmlStreamNo() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.NO, false, false);
    }
    
    //
    // XML -> WBXML event
    //
    
    @Test(groups = {"xml", "wbxml11", "type-if-needed", "event" })
    public void testXmlEventIfNeeded() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, false, true);
    }
    
    @Test(groups = {"xml", "wbxml11", "type-always", "event" })
    public void testXmlEventAllways() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.ALWAYS, false, true);
    }
    
    @Test(groups = {"xml", "wbxml11", "type-no", "event" })
    public void testXmlEventNo() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.NO, false, true);
    }
    
    //
    // WBXML -> XML stream
    //
    
    @Test(groups = {"wbxml", "wbxml11", "type-if-needed", "stream" })
    public void testWbXMLStreamIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, false, false);
    }
    
    @Test(groups = {"wbxml", "wbxml11", "type-if-always", "stream" })
    public void testWbXMLStreamAllways() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.ALWAYS, false, false);
    }
    
    @Test(groups = {"wbxml", "wbxml11", "type-no", "stream" })
    public void testWbXMLStreamNo() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.NO, false, false);
    }
    
    //
    // WBXML -> XML stream
    //
    
    @Test(groups = {"wbxml", "wbxml11", "type-if-needed", "event" })
    public void testWbXMLEventIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, false, true);
    }
    
    @Test(groups = {"wbxml", "wbxml11", "type-if-always", "event" })
    public void testWbXMLEventAllways() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.ALWAYS, false, true);
    }
    
    @Test(groups = {"wbxml", "wbxml11", "type-no", "event" })
    public void testWbXMLEventNo() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.NO, false, true);
    }
    
}