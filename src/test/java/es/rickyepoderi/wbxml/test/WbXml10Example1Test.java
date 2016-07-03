/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *    
 * Linking this library statically or dynamically with other modules 
 * is making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *    
 * As a special exception, the copyright holders of this library give 
 * you permission to link this library with independent modules to 
 * produce an executable, regardless of the license terms of these 
 * independent modules, and to copy and distribute the resulting 
 * executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the 
 * license of that module.  An independent module is a module which 
 * is not derived from or based on this library.  If you modify this 
 * library, you may extend this exception to your version of the 
 * library, but you are not obligated to do so.  If you do not wish 
 * to do so, delete this exception statement from your version.
 *
 * Project: github.com/rickyepoderi/wbxml-stream
 * 
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
 * WBXML 1.0 just replacing &nbsp; for &ntilde; to make the text different.
 * 
 * @author ricky
 */
public class WbXml10Example1Test extends GenericDirectoryTester {
    
    public WbXml10Example1Test() throws IOException {
        super("src/test/examples/spec-examples/example1-10", null);
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
    
    @Test(groups = {"xml", "wbxml10", "type-if-needed", "stream" })
    public void testXmlStreamIfNeeded() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, false, false);
    }
    
    @Test(groups = {"xml", "wbxml10", "type-always", "stream" })
    public void testXmlStreamAllways() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, false, false);
    }
    
    @Test(groups = {"xml", "wbxml10", "type-no", "stream" })
    public void testXmlStreamNo() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, false, false);
    }
    
    //
    // XML -> WBXML event
    //
    
    @Test(groups = {"xml", "wbxml10", "type-if-needed", "event" })
    public void testXmlEventIfNeeded() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, false, true);
    }
    
    @Test(groups = {"xml", "wbxml10", "type-always", "event" })
    public void testXmlEventAllways() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, false, true);
    }
    
    @Test(groups = {"xml", "wbxml10", "type-no", "event" })
    public void testXmlEventNo() throws Exception {
        testXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, false, true);
    }
    
    //
    // WBXML -> XML stream
    //
    
    @Test(groups = {"wbxml", "wbxml10", "type-if-needed", "stream" })
    public void testWbXMLStreamIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, false, false);
    }
    
    @Test(groups = {"wbxml", "wbxml10", "type-if-always", "stream" })
    public void testWbXMLStreamAllways() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, false, false);
    }
    
    @Test(groups = {"wbxml", "wbxml10", "type-no", "stream" })
    public void testWbXMLStreamNo() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, false, false);
    }
    
    //
    // WBXML -> XML stream
    //
    
    @Test(groups = {"wbxml", "wbxml10", "type-if-needed", "event" })
    public void testWbXMLEventIfNeeded() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, false, true);
    }
    
    @Test(groups = {"wbxml", "wbxml10", "type-if-always", "event" })
    public void testWbXMLEventAllways() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.ALWAYS, false, true);
    }
    
    @Test(groups = {"wbxml", "wbxml10", "type-no", "event" })
    public void testWbXMLEventNo() throws Exception {
        testWbXmlDirectory(WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.NO, false, true);
    }
    
}
