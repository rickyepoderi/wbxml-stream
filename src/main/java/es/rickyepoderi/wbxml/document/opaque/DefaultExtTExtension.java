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
package es.rickyepoderi.wbxml.document.opaque;

import es.rickyepoderi.wbxml.definition.WbXmlExtensionDef;
import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;
import es.rickyepoderi.wbxml.document.ExtensionTPlugin;

/**
 * <p>Default extension for tokens that transforms the numeric token in the
 * corresponding string value as defined in the language definition. This is
 * used inside WV (Wireless Village) language but it can be used in any language
 * that defines extension tokens and maps this class to the EXT_T extensions.</p>
 * 
 * <p>Although <em>wbxml-stream</em> gives this feature by default it is 
 * important to consider that the characteristic is an extension of the standard
 * (WBXML specification defines the extension tokens but the specific behavior 
 * is defined by the language that uses them).</p>
 * 
 * @author ricky
 */
public class DefaultExtTExtension implements ExtensionTPlugin {

    /**
     * Any token EXT_T is located in the defined language map and the number
     * is converted in the associated string.
     * @param parser The parser doing the parsing
     * @param attrName The attribute name that is currently being parsed
     * @param ext The extension found (should be EXT_I_[012]
     * @param value The numeric value inside the extension
     * @return The string that corresponds to the extension found
     * @throws IOException Some error in the parsing
     */
    @Override
    public String parseAttribute(WbXmlParser parser, String attrName, byte ext, long value) throws IOException {
        WbXmlExtensionDef extdef = parser.getDocument().getDefinition().locateExtension(value);
        if (extdef == null) {
            throw new IOException(String.format("Unknown extension (%d)", value));
        }
        return extdef.getValue();
    }

    /**
     * Any token EXT_T is located in the defined language map and the number
     * is converted in the associated string.
     * @param parser The parser doing the parsing
     * @param tagName The tag name that is currently being parsed
     * @param ext The extension found (should be EXT_T_[012]
     * @param value The long value inside the extension
     * @return The WbXmlContent for this extension
     * @throws IOException Some error in the parsing
     */
    @Override
    public WbXmlContent parseContent(WbXmlParser parser, String tagName, byte ext, long value) throws IOException {
        WbXmlExtensionDef extdef = parser.getDocument().getDefinition().locateExtension(value);
        if (extdef == null) {
            throw new IOException(String.format("Unknown extension (%d)", value));
        }
        return new WbXmlContent(extdef.getValue());
    }
}
