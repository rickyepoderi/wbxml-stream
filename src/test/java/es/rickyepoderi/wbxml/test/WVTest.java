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
public class WVTest extends GenericDirectoryTester {
    
    public WVTest() {
        super("/home/ricky/NetBeansProjects/wbxml-stream/examples/wv",
                WbXmlInitialization.getDefinitionByName("WV CSP 1.1"));
    }
    
    @Test(groups = {"xml", "wv", "type-if-needed" })
    public void testXmlIfNeeded() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true);
    }
    
    @Test(groups = {"xml", "wv", "type-always" })
    public void testXmlAllways() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true);
    }
    
    @Test(groups = {"xml", "wv", "type-no" })
    public void testXmlNo() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"wbxml", "wv", "type-if-needed" })
    public void testWbXMLIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true);
    }
    
    @Test(groups = {"wbxml", "wv", "type-if-always" })
    public void testWbXMLAllways() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true);
    }
    
    @Test(groups = {"wbxml", "wv", "type-no" })
    public void testWbXMLNo() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"xml", "wv", "type-no", "jaxb" })
    public void testXMLJaxb() throws Exception {
        testXmlJaxbDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"wbxml", "si", "type-no", "jaxb" })
    public void testWBXMLJaxb() throws Exception {
        testWbXmlJaxbDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
}
