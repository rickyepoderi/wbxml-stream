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
public class ActiveSyncTest extends GenericDirectoryTester {
    
    public ActiveSyncTest() {
        super("/home/ricky/NetBeansProjects/wbxml-stream/examples/activesync",
                WbXmlInitialization.getDefinitionByName("ActiveSync"));
    }
    
    @Test(groups = {"xml", "activesync", "type-if-needed" })
    public void testXmlIfNeeded() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true);
    }
    
    @Test(groups = {"xml", "activesync", "type-always" })
    public void testXmlAllways() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true);
    }
    
    @Test(groups = {"xml", "activesync", "type-no" })
    public void testXmlNo() throws Exception {
        testXmlDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
    
    @Test(groups = {"wbxml", "activesync", "type-if-needed" })
    public void testWbXMLIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.IF_NEEDED, true);
    }
    
    @Test(groups = {"wbxml", "activesync", "type-if-always" })
    public void testWbXMLAllways() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.ALWAYS, true);
    }
    
    @Test(groups = {"wbxml", "activesync", "type-no" })
    public void testWbXMLNo() throws Exception {
        testWbXmlDirectory(WbXmlEncoder.StrtblType.NO, true);
    }
}
