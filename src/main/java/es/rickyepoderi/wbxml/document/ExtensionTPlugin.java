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
package es.rickyepoderi.wbxml.document;

import java.io.IOException;

/**
 * <p>Extension plugin for managing EXT_T extensions inside the WBXML.
 * This extensions have associated a long number.</p>
 * 
 * <pre>
 * EXT_T index
 * index = mb_u_int32 // integer index into string table.
 * </pre>
 * 
 * <p>Only WML language uses that extensions to encode the WML variable 
 * names (the real var name is in the string table pomied by the index). 
 * The different EXT_T_[012] are used to different types or variables
 * (escaped, unescaped, noescaped).</p>
 * 
 * <p>For that reason seems reasonable that (just using the same idea than in
 * opaques) the extensions could be assigned for every language using an
 * interface.</p>
 * 
 * @author ricky
 */
public interface ExtensionTPlugin {
    
    /**
     * The method to parse an EXT_T inside a content. The method should
     * return the WbXmlContent that this extension represents.
     * 
     * @param parser The parser doing the parsing
     * @param tagName The tag name that is currently being parsed
     * @param ext The extension found (should be EXT_T_[012]
     * @param value The long value inside the extension
     * @return The WbXmlContent for this extension
     * @throws IOException Some error in the parsing
     */
    public WbXmlContent parseContent(WbXmlParser parser, String tagName, byte ext, long value) throws IOException;
    
    /**
     * The method to parse an EXT_T inside an attribute. The method should
     * return the string that this extension represents.
     * 
     * @param parser The parser doing the parsing
     * @param attrName The attribute name that is currently being parsed
     * @param ext The extension found (should be EXT_I_[012]
     * @param value The numeric value inside the extension
     * @return The string that corresponds to the extension found
     * @throws IOException Some error in the parsing
     */
    public String parseAttribute(WbXmlParser parser, String attrName, byte ext, long value) throws IOException;
}
