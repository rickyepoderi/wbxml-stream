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

import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlLiterals;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;
import es.rickyepoderi.wbxml.document.ExtensionIPlugin;
import es.rickyepoderi.wbxml.document.ExtensionTPlugin;

/**
 * <p>Extension to convert the extensions into a proper variable name in WML.
 * The same class is used to parse EXT_I and EXT_T extensions and in both
 * types, attribute and tag values.</p>
 * 
 * @author ricky
 */
public class WMLVariableExtension implements ExtensionIPlugin, ExtensionTPlugin {

    /**
     * Method that constructs the correct variable name for the extension.
     * @param ext The extension being found
     * @param value The variable name in the extension
     * @return The correct representation of the string
     * @throws IOException Some error
     */
    static public String parse(byte ext, String value) throws IOException {
        String var = null;
        switch(ext) {
            case WbXmlLiterals.EXT_I_0:
            case WbXmlLiterals.EXT_T_0:
                var = "$(" + value + ":escape)";
                break;
            case WbXmlLiterals.EXT_I_1:
            case WbXmlLiterals.EXT_T_1:
                var = "$(" + value + ":unesc)";
                break;
            default:
                var = "$(" + value + ")";
                break;
        }
        return var;
    }
    
    /**
     * Converts the extension to a variable name in WML.
     * @param parser The parser doing the parsing
     * @param tagName The tag name that is currently being parsed
     * @param ext The extension found (should be EXT_I_[012]
     * @param value The string value inside the extension
     * @return The WbXmlContent for this extension
     * @throws IOException Some error in the parsing
     */
    @Override
    public WbXmlContent parseContent(WbXmlParser parser, String tagName, byte ext, String value) throws IOException {
        return new WbXmlContent(parse(ext, value));
    }

    /**
     * Converts the extension to a variable name in WML.
     * @param parser The parser doing the parsing
     * @param tagName The tag name that is currently being parsed
     * @param ext The extension found (should be EXT_T_[012]
     * @param value The long value inside the extension
     * @return The WbXmlContent for this extension
     * @throws IOException Some error in the parsing
     */
    @Override
    public WbXmlContent parseContent(WbXmlParser parser, String tagName, byte ext, long value) throws IOException {
        return this.parseContent(parser, tagName, ext, parser.getDocument().getStrtbl().getString(value));
    }

    /**
     * Converts the extension to a variable name in WML.
     * @param parser The parser doing the parsing
     * @param attrName The attribute name that is currently being parsed
     * @param ext The extension found (should be EXT_I_[012]
     * @param value The string value inside the extension
     * @return The WbXmlContent for this extension
     * @throws IOException Some error in the parsing
     */
    @Override
    public String parseAttribute(WbXmlParser parser, String attrName, byte ext, String value) throws IOException {
        return parse(ext, value);
    }

    /**
     * Converts the extension to a variable name in WML.
     * @param parser The parser doing the parsing
     * @param attrName The attribute name that is currently being parsed
     * @param ext The extension found (should be EXT_T_[012]
     * @param value The numeric value inside the extension
     * @return The WbXmlContent for this extension
     * @throws IOException Some error in the parsing
     */
    @Override
    public String parseAttribute(WbXmlParser parser, String attrName, byte ext, long value) throws IOException {
        return parse(ext, parser.getDocument().getStrtbl().getString(value));
    }
    
}
