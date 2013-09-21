/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.test;

import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class SyncMLTest extends GenericDirectoryTester {
    
    public SyncMLTest() {
        super("src/test/examples/syncml",
                WbXmlInitialization.getDefinitionByName("SyncML 1.1"));
    }
    
    @Test(groups = {"xml", "syncml", "type-if-needed" })
    public void testXmlIfNeeded() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true);
    }
    
    @Test(groups = {"xml", "syncml", "type-always" })
    public void testXmlAllways() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true);
    }
    
    @Test(groups = {"xml", "syncml", "type-no" })
    public void testXmlNo() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"wbxml", "syncml", "type-if-needed" })
    public void testWbXMLIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true);
    }
    
    @Test(groups = {"wbxml", "syncml", "type-if-always" })
    public void testWbXMLAllways() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true);
    }
    
    @Test(groups = {"wbxml", "syncml", "type-no" })
    public void testWbXMLNo() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"xml", "syncml", "type-no", "jaxb" })
    public void testXMLJaxb() throws Exception {
        testXmlJaxbDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"wbxml", "syncml", "type-no", "jaxb" })
    public void testWBXMLJaxb() throws Exception {
        testWbXmlJaxbDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
}
