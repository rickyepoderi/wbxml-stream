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

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlVersion;
import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class VersionDifferencesTest extends GenericDirectoryTester {

    /**
     * Custom handler that checks if log messages are produced.
     */
    private class MyCustomHandler extends Handler {

        private Level level;
        private String substr;
        private int times;
        
        public MyCustomHandler(Level level, String substr) {
            this.level = level;
            this.substr = substr;
            this.times = 0;
        }
        
        @Override
        public void publish(LogRecord lr) {
            if (level.equals(lr.getLevel()) && lr.getMessage().contains(substr)) {
                times++;
            }
        }
        
        public int getTimes() {
            return times;
        }

        @Override
        public void flush() {
            // nothing
        }

        @Override
        public void close() throws SecurityException {
            // nothing
        }
        
    }
    
    public VersionDifferencesTest() {
        super("src/test/examples/version-errors", null);
    }
    
    //
    // 1.0 -> 1.1
    // * charset added to the start element.
    // * The body is added instead of being 1*content (this is very weird). <== not implemented
    // * opaque added as a content.

    // charset
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion10CharsetOK() throws Exception {
        File f = new File(this.getDirectory() + "/conml-001-webxml10-ok.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("CONML");
        Assert.assertTrue(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion10CharsetKO() throws Exception {
        File f = new File(this.getDirectory() + "/conml-001-webxml10-with-charset-error.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("CONML");
        // exception should be thrown cos the charset byte is added
        Assert.assertFalse(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    // opaque added as a content
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion11OpaqueTagOK() throws Exception {
        // opaque tag in version 1.1 should work
        File f = new File(this.getDirectory() + "/wv-069-opaque-tag-wbxml11-ok.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("WV CSP 1.1");
        Assert.assertTrue(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion10OpaqueTagKO() throws Exception {
        // opaque tag in version 1.0 should throw an exception
        File f = new File(this.getDirectory() + "/wv-069-opaque-tag-wbxml10-error.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("WV CSP 1.1");
        Assert.assertFalse(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion11OpaqueTagOK() throws Exception {
        // opaque tag in version 1.1 should work
        File f = new File(this.getDirectory() + "/wv-069-opaque-tag.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("WV CSP 1.1");
        Assert.assertTrue(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }

    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion10OpaqueTagKO() throws Exception {
        // opaque tag in version 1.0 should work but log WARNING throws
        MyCustomHandler hd = new MyCustomHandler(Level.WARNING, "does not accept tag opaques");
        hd.setLevel(Level.WARNING);
        Logger logger = Logger.getLogger("es.rickyepoderi.wbxml");
        logger.addHandler(hd);
        File f = new File(this.getDirectory() + "/wv-069-opaque-tag.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("WV CSP 1.1");
        Assert.assertTrue(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_0, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
        Assert.assertTrue(hd.getTimes() > 0);
        logger.removeHandler(hd);
    }
    
    //
    // 1.1 -> 1.2
    // * switchPage added to element, attrStart, attrValue, extension.
    // * opaque added as attrValue.
    
    // switch tag
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion12SwitchTagOK() throws Exception {
        File f = new File(this.getDirectory() + "/syncml-003-switch-tag-wbxml12-ok.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("SyncML 1.1");
        Assert.assertTrue(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_2, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion11SwitchTagKO() throws Exception {
        File f = new File(this.getDirectory() + "/syncml-003-switch-tag-wbxml12-ok.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("SyncML 1.1");
        // exception should be thrown cos the charset byte is added
        Assert.assertFalse(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion12SwitchTagOK() throws Exception {
        File f = new File(this.getDirectory() + "/syncml-003-switch-tag.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("SyncML 1.1");
        Assert.assertTrue(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_2, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }

    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion11SwitchTagKO() throws Exception {
        File f = new File(this.getDirectory() + "/syncml-003-switch-tag.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("SyncML 1.1");
        Assert.assertFalse(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    // switch attribute
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion12SwitchAttributeOK() throws Exception {
        File f = new File(this.getDirectory() + "/prov-001-switch-attr-wbxml12-ok.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("PROV 1.0");
        Assert.assertTrue(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_2, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion11AttributeTagKO() throws Exception {
        File f = new File(this.getDirectory() + "/prov-001-switch-attr-wbxml11-error.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("PROV 1.0");
        // exception should be thrown cos the charset byte is added
        Assert.assertFalse(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion12SwitchAttributeOK() throws Exception {
        File f = new File(this.getDirectory() + "/prov-001-switch-attr.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("PROV 1.0");
        Assert.assertTrue(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_2, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }

    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion11SwitchAttributeKO() throws Exception {
        File f = new File(this.getDirectory() + "/prov-001-switch-attr.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("PROV 1.0");
        Assert.assertFalse(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    // opaque added for attributes
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion12OpaqueAttributeOK() throws Exception {
        // opaque attr in version 1.2 should work
        File f = new File(this.getDirectory() + "/emn-001-opaque-attr-webxml12-ok.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("EMN 1.0");
        Assert.assertTrue(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_2, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"wbxml", "version-errors"})
    public void testWbXmlVersion11OpaqueAttributeKO() throws Exception {
        // opaque attribute in version 1.1 should throw an exception
        File f = new File(this.getDirectory() + "/emn-001-opaque-attr-webxml11-error.wbxml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("EMN 1.0");
        Assert.assertFalse(this.testWbXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }
    
    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion12OpaqueAttributeOK() throws Exception {
        // opaque attribute in version 1.2 should work
        File f = new File(this.getDirectory() + "/emn-001-opaque-attr.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("EMN 1.0");
        Assert.assertTrue(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_2, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
    }

    @Test(groups = {"xml", "version-errors"})
    public void testXmlVersion11OpaqueAttributeKO() throws Exception {
        // opaque attribute in version 1.1 should work but log WARNING throws
        MyCustomHandler hd = new MyCustomHandler(Level.WARNING, "does not accept attribute opaques");
        hd.setLevel(Level.WARNING);
        Logger logger = Logger.getLogger("es.rickyepoderi.wbxml");
        logger.addHandler(hd);
        File f = new File(this.getDirectory() + "/emn-001-opaque-attr.xml");
        WbXmlDefinition def = WbXmlInitialization.getDefinitionByName("EMN 1.0");
        Assert.assertTrue(this.testXmlFile(f, def, WbXmlVersion.VERSION_1_1, WbXmlEncoder.StrtblType.IF_NEEDED, true, false));
        Assert.assertTrue(hd.getTimes() > 0);
        logger.removeHandler(hd);
    }
    
}