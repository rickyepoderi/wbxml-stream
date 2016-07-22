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

import es.rickyepoderi.wbxml.document.OpaqueContentPlugin;
import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlElement;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlParser;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>This opaque is quite strange in WML. The opaque is used not to create a
 * real opaque but to check if there are variables defined in the content
 * value and transform them into EXT_I or EXT_T tokens</p>
 * 
 * <p>The WML language specifies in the chapter 14 of the document
 * <a href="http://technical.openmobilealliance.org/Technical/release_program/docs/Browsing/V2_3-20070227-C/WAP-191-WML-20000219-a.pdf">WAP-191-WML-20000219-a.pdf</a>
 * some peculiar characteristics of its WBXML representation. Specifically i says: 
 * "All valid variable references must be converted to variable reference tokens".
 * That means that all variables should be replaced by a token. This plugin
 * does exactly that for attribute values that can have variables.</p>
 * 
 * <p>Obviously the opaque should be added to all the tags that can have
 * variables inside it.</p>
 * 
 * @author ricky
 */
public class WMLVariableContentOpaque implements OpaqueContentPlugin {

    /**
     * The pattern that corresponds to a WML variable based on the definition
     * of the <em>WAP-191-WML-20000219-a.pdf</em>. Chapter 10.3.1 Variable Substitution.
     */
    static private Pattern varPattern = 
            Pattern.compile("(\\$[\\(]?)([_a-zA-Z][_a-zA-Z0-9]*)(:(([eE][sS][cC][aA][pP][eE]|[eE]|[nN][oO][eE][sS][cC]|[nN]|[uU][nN][eE][sS][cC]|[uU])))?([\\)]?)");
    
    /**
     * Method that writes the token that corresponds to the variable and the name.
     * The token depends on the type (escape, unesv or noesc).
     * @param encoder The encoder that is wrinting the wbxml
     * @param matcher The matcher expresion containing the variable
     * @throws IOException Some error witing to the encoder
     */
    static public void writeToken(WbXmlEncoder encoder, Matcher matcher) throws IOException {
        String var = matcher.group(2);
        //System.err.println("variable = " + var);
        String type = matcher.group(3);
        //System.err.println("type = " + type);
        if (":e".equalsIgnoreCase(type)  || ":escape".equalsIgnoreCase(type)) {
            // escape variable is EXT_I_0 or EXT_T_0
            encoder.writeExtension((byte) 0, var);
        } else if (":u".equalsIgnoreCase(type)  || ":unesc".equalsIgnoreCase(type)) {
            // unscape  variable is EXT_I_1 or EXT_T_1
            encoder.writeExtension((byte) 1, var);
        } else {
            // no transformation is EXT_I_2 or EXT_T_2
            encoder.writeExtension((byte) 2, var);
        }
    }
    
    /**
     * Method that performs the variable localization and the conversion
     * for all the variables found into the proper EXTension.
     * @param encoder The encoder to use
     * @param value The string with possible variables
     * @throws IOException Some error using the encoder
     */
    static public void encode(WbXmlEncoder encoder, String value) throws IOException {
        // look for variables and encode them separately in tokens
        Matcher matcher = varPattern.matcher(value);
        int pos = 0;
        while (matcher.find()) {
            // write the text before the matcher
            if (matcher.start() > pos) {
                encoder.writeString(value.substring(pos, matcher.start()));
            }
            // now write the token that corresponds to the variable
            writeToken(encoder, matcher);
            // refresh pos
            pos = matcher.end();
        }
        // write the remaining 
        if (pos < value.length()) {
            encoder.writeString(value.substring(pos));
        }
    }
    
    /**
     * In this case the opaque plugin is very strange. The WML does not really
     * create an opaque but a list of extension for variables (EXT_I or EXT_T).
     * So the opaque is used in order to correctly write variables inside the
     * proper extensions.
     * 
     * @param encoder The encoder used in the encoding process
     * @param element The element which content or attribute is being encoded 
     * @param content The content to encode (compulsory to string if it is and attribute plugin)
     * @throws IOException Some error in the encoding process
     */
    @Override
    public void encode(WbXmlEncoder encoder, WbXmlElement element, WbXmlContent content) throws IOException {
        if (content.isString()) {
            encode(encoder, content.getString());
        } else {
            // normal encoding for this content
            encoder.encode(content);
        }
    }

    /**
     * Not really used cos the opaque plugin does not really creates an opaque.
     * This method should be never called (extensions are used instead).
     * 
     * @param parser The parser doing the parsing
     * @param data The data read from the opaque
     * @return The content representation of the opaque data
     * @throws IOException Some error in the parsing
     */
    @Override
    public WbXmlContent parse(WbXmlParser parser, byte[] data) throws IOException {
        // this is quite extrange, the WML opaque does not generate an Opaque
        // but some extensions (EXT_I or EXT_T). So it never goes over this method
        throw new IllegalStateException("It should not be called.");
    }
    
}
