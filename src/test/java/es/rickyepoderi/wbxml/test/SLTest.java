/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.test;

import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlVersion;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class SLTest extends GenericDirectoryTester {
    
    public SLTest() {
        super("src/test/examples/sl",
                WbXmlInitialization.getDefinitionByName("SL 1.0"));
    }
    
    @Test(groups = {"xml", "sl", "type-if-needed", "stream" })
    public void testXmlStreamIfNeeded() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-always", "stream" })
    public void testXmlStreamAllways() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-no", "stream" })
    public void testXmlStreamNo() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-needed", "stream" })
    public void testWbXMLStreamIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-always", "stream" })
    public void testWbXMLStreamAllways() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-no", "stream" })
    public void testWbXMLStreamNo() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    
    
    @Test(groups = {"xml", "sl", "type-if-needed", "event" })
    public void testXmlEventIfNeeded() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-always", "event" })
    public void testXmlEventAllways() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-no", "event" })
    public void testXmlEventNo() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-needed", "event" })
    public void testWbXMLEventIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-always", "event" })
    public void testWbXMLEventAllways() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-no", "event" })
    public void testWbXMLEventNo() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    //
    // SL supports WBXML 1.0 => test with 1.0
    //
    
    @Test(groups = {"xml", "sl", "type-if-needed", "stream", "wbxml10" })
    public void testXmlStreamIfNeeded10() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-always", "stream", "wbxml10" })
    public void testXmlStreamAllways10() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-no", "stream", "wbxml10" })
    public void testXmlStreamNo10() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-needed", "stream", "wbxml10" })
    public void testWbXMLStreamIfNeeded10() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-always", "stream", "wbxml10" })
    public void testWbXMLStreamAllways10() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-no", "stream", "wbxml10" })
    public void testWbXMLStreamNo10() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    
    
    @Test(groups = {"xml", "sl", "type-if-needed", "event", "wbxml10" })
    public void testXmlEventIfNeeded10() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-always", "event", "wbxml10" })
    public void testXmlEventAllways10() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"xml", "sl", "type-no", "event", "wbxml10" })
    public void testXmlEventNo10() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-needed", "event", "wbxml10" })
    public void testWbXMLEventIfNeeded10() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-if-always", "event", "wbxml10" })
    public void testWbXMLEventAllways10() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, true, false);
    }
    
    @Test(groups = {"wbxml", "sl", "type-no", "event", "wbxml10" })
    public void testWbXMLEventNo10() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, true, false);
    }
}