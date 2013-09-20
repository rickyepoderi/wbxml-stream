/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.definition;

import es.rickyepoderi.wbxml.document.WbXmlLiterals;

/**
 *
 * <p>This class just represents a pair of bytes to define page code and
 * a byte token. In WBXML a single byte is used to define tags and attributes,
 * to let more possible values a page code concept is introduced. The way
 * the page code is used is explained in the chapter <em>5.8.1. Parser State Machine</em>
 * of the specification. In simple words the the page byte is reused if some tags
 * belongs to same page (there is a concept of switch page, to change the
 * current page of tokens).</p>
 * 
 * <p>This class just joins the byte id and the page the element belongs to. 
 * Pages only affect tags and attributes (attributes and their values).</p>
 * 
 * @author ricky
 */
public class WbXmlToken {
    
    /**
     * The id byte token
     */
    private byte token = 0;
    
    /**
     * The page code
     */
    private byte pageCode = 0;
    
    /**
     * Constructor using both bytes
     * @param pageCode The page code
     * @param token The byte token
     */
    public WbXmlToken(byte pageCode, byte token) {
        this.pageCode = pageCode;
        this.token = token;
    }

    /**
     * getter for the token byte
     * @return The token byte
     */
    public byte getToken() {
        return token;
    }

    /**
     * getter for the page byte
     * @return The page byte
     */
    public byte getPageCode() {
        return pageCode;
    }

    /**
     * hashcode based in the two bytes
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.token;
        hash = 97 * hash + this.pageCode;
        return hash;
    }

    /**
     * Equals based in the two bytes
     * @param obj The obj to compare
     * @return true if both object are equals, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WbXmlToken other = (WbXmlToken) obj;
        if (this.token != other.token) {
            return false;
        }
        if (this.pageCode != other.pageCode) {
            return false;
        }
        return true;
    }
    
    /**
     * String representation of the token.
     * @return The string representation
     */
    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName())
                .append(": ")
                .append(WbXmlLiterals.formatUInt8(pageCode))
                .append("|")
                .append(WbXmlLiterals.formatUInt8(token))
                .toString();
    }
}
