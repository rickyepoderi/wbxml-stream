/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document;

/**
 * 
 * <p>Java representation of the content of an element in the WBXML format.
 * Following the specification the content is the following:</p>
 * 
 * <pre> 
 * content = element | string | extension | entity | pi | opaque
 * 
 * string = inline | tableref
 * inline = STR_I termstr
 * tableref = STR_T index
 * 
 * extension = [switchPage] (( EXT_I termstr ) | ( EXT_T index ) | EXT)
 * 
 * entity = ENTITY entcode
 * 
 * opaque = OPAQUE length *byte
 * 
 * pi = PI attrStart *attrValue END
 * </pre>
 * 
 * <p>So this class is just a typical C union, in which the value of the content
 * can be a String (it represents the STR_I, STR_T, extension and entity), 
 * another element (element) or PI attribute (remember PI is just like an 
 * attribute). Opaque data is managed using the plugins. Obviously only
 * one can be not null.</p>
 * 
 * @author ricky
 */
public class WbXmlContent {
    
    /**
     * element of the content.
     */
    private WbXmlElement element = null;
    
    /**
     * String value that can be a normal STR_T, STR_I, entity or extension.
     */
    private String string = null;
    
    /**
     * PI part of the union.
     */
    private WbXmlAttribute pi = null;
    
    /**
     * Empty constructor.
     */
    protected WbXmlContent() {
        this.element = null;
        this.pi = null;
        this.string = null;
    }
    
    /**
     * Constructor for the element.
     * @param element The element of the content
     */
    public WbXmlContent(WbXmlElement element) {
        this();
        this.setElement(element);
    }
    
    /**
     * Constructor for string content.
     * @param string The string
     */
    public WbXmlContent(String string) {
        this();
        this.setString(string);
    }
    
    /**
     * Constructor for the PI.
     * @param pi The pi that is the content
     */
    public WbXmlContent(WbXmlAttribute pi) {
        this();
        this.setPi(pi);
    }

    /**
     * Setter for the element. Sets the element and set two null or the
     * other possibilities.
     * @param element The element to set
     * @return The same content to chain calls
     */
    final public WbXmlContent setElement(WbXmlElement element) {
        this.string = null;
        this.pi = null;
        this.element = element;
        return this;
    }

    /**
     * Setter for the string. All the rest of possibilities are set to null.
     * @param string The string that compounds the content
     * @return The content to chain calls
     */
    final public WbXmlContent setString(String string) {
        this.string = string;
        this.pi = null;
        this.element = null;
        return this;
    }

    /**
     * Setter for the Pi part. All the other two possibilities are set to null.
     * @param pi The pi that is the content
     * @return The same content to chain calls
     */
    final public WbXmlContent setPi(WbXmlAttribute pi) {
        this.string = null;
        this.pi = pi;
        this.element = null;
        return this;
    }
    
    /**
     * Getter for the string.
     * @return The string value
     */
    public String getString() {
        return string;
    }
    
    /**
     * Checks if the content is a string content.
     * @return true if string is not null, false otherwise
     */
    public boolean isString() {
        return string != null;
    }
    
    /**
     * Checks if the content is an entity. It should be an string an
     * be in the form "&#{number};".
     * @return true if it is a entity content, false otherwise
     */
    public boolean isEntity() {
        return isString() && string.startsWith("&#") && string.endsWith(";");
    }
    
    public long getEntityNumber() {
        if (isEntity()) {
            String number = string.substring(2, string.length() - 1);
            return Long.parseLong(number);
        } else {
            throw new IllegalStateException(String.format("The value is not an entity: %s", string));
        }
    }
    
    /**
     * Checks if the content is a PI content. it returns true if the pi
     * property is not null.
     * @return true if pi is not null, false otherwise
     */
    public boolean isPi() {
        return pi != null;
    }
    
    /**
     * Checks if the content is a element content. It is if it just have an element.
     * @return true if element property is not null, false otherwise
     */
    public boolean isElement() {
        return element != null;
    }
    
    /**
     * Getter for the element
     * @return the element
     */
    public WbXmlElement getElement() {
        return element;
    }
    
    /**
     * Getter for the PI.
     * @return The pi
     */
    public WbXmlAttribute getPi() {
        return pi;
    }
    
    /**
     * Method that returns if the content is empty (no child has been 
     * assigned, neither string, nor pi nor element.
     * @return true if all properties (pi, element and string) are null, false otherwise
     */
    public boolean isEmpty() {
        return string == null && pi == null && element == null;
    }

    /**
     * String representation with indentation.
     * @param ident The indentation to use
     * @return String representation
     */
    public String toString(int ident) {
        String spaces = WbXmlLiterals.identString(ident);
        StringBuilder sb = new StringBuilder(spaces);
        sb.append(this.getClass().getSimpleName());
        sb.append(": ");
        if (string != null) {
            sb.append("str->");
            sb.append(string);
            sb.append(System.getProperty("line.separator"));
        } else if (pi != null) {
            sb.append("pi->");
            sb.append(pi.toString(0));
            sb.append(System.getProperty("line.separator"));
        } else if (element != null) {
            sb.append(System.getProperty("line.separator"));
            sb.append(element.toString(++ident));
        }
        return sb.toString();
    }
    
    /**
     * String representation of the content.
     * @return String representation
     */
    @Override
    public String toString() {
        return toString(0);
    }
}
