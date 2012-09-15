/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.definition;

/**
 * <p>Tags in WBXML are traduced by a byte-lengthed identifier. Those 
 * ids should be defined in the language specification. Tag tokens inside WBXML
 * are a single u_int8 and how to parse/encode them is explained in chapter
 * <em>5.8.2. Tag Code Space</em> of the specification:</p>
 * 
 * <ul>
 * <li>7 (most significant) Indicates whether attributes follow the tag code. If 
 * this bit is zero, the tag contains no attributes. If this bit is one, the 
 * tag is followed immediately by one or more attributes. The attribute list is 
 * terminated by an END token.</li>
 * <li>6 Indicates whether this tag begins an element containing content. If 
 * this bit is zero, the tag contains no content and no end tag. If this bit 
 * is one, the tag is followed by any content it contains and is terminated by 
 * an END token.</li>
 * <li>5- 0 Indicates the tag identity.</li>
 * </ul>
 * 
 * <p>For example:</p>
 * <ul>
 * <li>Tag value 0xC6: indicates tag six (6), with both attributes and 
 * content following the tag, e.g.,&lt;TAG arg="1"&gt;foo&lt;/TAG&gt;</li>
 * <li>Tag value 0x46: indicates tag six (6), with content following the start 
 * tag. This element contains no attributes, e.g., &lt;TAG&gt;test&lt;/TAG&gt;</li>
 * <li>Tag value 0x06: indicates tag six (6). This element contains no content 
 * and has no attributes, e.g., &lt;TAG/&gt;</li>
 * </ul>
 * 
 * <p>The globally unique codes LITERAL, LITERAL_A, LITERAL_C, and LITERAL_AC 
 * represent unknown tag names. (Note that the tags LITERAL_A, LITERAL_C, and 
 * LITERAL_AC are the LITERAL tag with the appropriate combinations of bits 6 
 * and 7 set.) An XML tokeniser should avoid the use of the literal or string
 * representations of a tag when a more compact form is available. Tags 
 * containing both attributes and content always encode the attributes before 
 * the content.</p>
 * 
 * <p>In the properties file each token is just a key with the format:</p>
 * 
 * <ul>
 * <li>wbxml.tag.{pageCode}.[{prefix}:]{name}={token}</li>
 * </ul>
 *
 * <p>Attributes and tags can be prefixed if the element is defined inside a 
 * namespace. The prefix should be the one specified in the namespace key.</p>
 * 
 * @author ricky
 */
public class WbXmlTagDef {
    
    /**
     * The prefix of the tag
     */
    private String prefix = null;
    
    /**
     * The tag name
     */
    private String name = null;
    
    /**
     * The token (page code and tag)
     */
    private WbXmlToken token = null;

    /**
     * Constructor using name and token
     * @param name The name tag
     * @param token The token (page code and tag token)
     */
    protected WbXmlTagDef(String name, WbXmlToken token) {
        this(null, name, token);
    }
    
    /**
     * Constructor using name, tag token and page code.
     * @param name The name tag
     * @param token The tag token byte
     * @param pageCode The page code
     */
    protected WbXmlTagDef(String name, byte token, byte pageCode) {
        this(null, name, new WbXmlToken(pageCode, token));
    }
    
    /**
     * Constructor using the prefix, name, token and page code.
     * @param prefix The prefig of the tag
     * @param name The name tag
     * @param token The token tag byte
     * @param pageCode The page code
     */
    protected WbXmlTagDef(String prefix, String name, byte token, byte pageCode) {
        this(prefix, name, new WbXmlToken(pageCode, token));
    }
    
    /**
     * Constructor using the prefix, the name and the token.
     * @param prefix The prefix tag
     * @param name The name tag
     * @param token The token (page code and tag byte)
     */
    protected WbXmlTagDef(String prefix, String name, WbXmlToken token) {
        this.prefix = prefix;
        this.token = token;
        this.name = name;
    }
    
    /**
     * Getter for the prefixed name "prefix":"name" if prefix exists, if not
     * normal name is returned.
     * @return The prefixed name
     */
    public String getNameWithPrefix() {
        if (prefix != null) {
            return new StringBuilder(prefix).append(":").append(name).toString();
        } else {
            return name;
        }
    }
    
    /**
     * Getter for the tag name
     * @return The tag name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the tag name
     * @param name The new name
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the token
     * @return The token (page code and tag byte)
     */
    public WbXmlToken getToken() {
        return token;
    }

    /**
     * Setter for the token
     * @param token The new token
     */
    protected void setToken(WbXmlToken token) {
        this.token = token;
    }

    /**
     * Getter for the prefix
     * @return The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Setter for the prefix
     * @param prefix Thew new prefix
     */
    protected void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    /**
     * String representation for the tag definition
     * @return The string representation
     */
    @Override
    public String toString() {
        StringBuilder sb=  new StringBuilder(this.getClass().getSimpleName())
                .append(": ");
        if (prefix != null) {
            sb.append(prefix);
            sb.append(":");
        }
        sb.append(name)
                .append("->")
                .append(token)
                .append(System.getProperty("line.separator"));
        return sb.toString();
    }
    
}
