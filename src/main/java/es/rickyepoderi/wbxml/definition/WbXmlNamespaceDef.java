/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.definition;

/**
 *
 * <p>WBXML says nothing about namespaces but the languages uses them a lot
 * (for example SyncML). The idea is any language can or cannot define
 * namespaces and if it uses them all the tags and attributes must be 
 * prefixed by the namespace prefix.</p>
 * 
 * <p>In the definition properties file a namespace is defined
 * in a single key:<p>/
 * 
 * <ul>
 * <li>wbxml.namespaces.{prefix}={namespaceURI}</li>
 * </ul>
 * 
 * <p>All the rest of elements that are namespace aware (tags and attributes)
 * should be prefixed if namespaces are used in the language. The definition
 * object is just a pair prefix-namespaceURI</p>
 * 
 * @author ricky
 */
public class WbXmlNamespaceDef {
    
    /**
     * The prefix of the namespece
     */
    private String prefix = null;
    
    /**
     * The namespaceURI
     */
    private String namespace = null;
    
    /**
     * Constructor via both properties: prefix and namespacveURI
     * @param prefix The prefix
     * @param namespace The namespace URI
     */
    protected WbXmlNamespaceDef(String prefix, String namespace) {
        this.prefix = prefix;
        this.namespace = namespace;
    }
    
    /**
     * Getter for the prefix
     * @return The prefix of the namespace definition
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Setter for the prefix
     * @param prefix The new prefix
     */
    protected void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Getter for the namespace URI
     * @return The namespace URI
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Setter for the namespace URI
     * @param namespace The new namespace URI
     */
    protected void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    /**
     * String representation
     * @return The string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(System.getProperty("line.separator"));
        sb.append("prefix: ");
        sb.append(prefix);
        sb.append(System.getProperty("line.separator"));
        sb.append("namespace: ");
        sb.append(namespace);
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
