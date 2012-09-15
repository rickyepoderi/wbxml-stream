/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream;

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * <p>Interface for read only XML Namespace context processing. </p>
 * 
 * <p>An XML Namespace has the properties:</p>
 * 
 * Namespace URI: Namespace name expressed as a URI to which the prefix is bound
 * prefix: syntactically, this is the part of the attribute name following the 
 * XMLConstants.XMLNS_ATTRIBUTE ("xmlns") in the Namespace declaration
 * example: &lt;element xmlns:prefix="http://Namespace-name-URI"&gt;
 * 
 * <p>All get*(*) methods operate in the current scope for Namespace URI and prefix
 * resolution.</p>
 * 
 * <p>Note that a Namespace URI can be bound to multiple prefixes in the current scope.
 * This can occur when multiple XMLConstants.XMLNS_ATTRIBUTE ("xmlns") Namespace 
 * declarations occur in the same Start-Tag and refer to the same Namespace URI. e.g.</p>
 * 
 * <pre>
 * &lt;element xmlns:prefix1="http://Namespace-name-URI"
 *           xmlns:prefix2="http://Namespace-name-URI"&lt;
 * </pre>
 * 
 * <p>This can also occur when the same Namespace URI is used in multiple 
 * XMLConstants.XMLNS_ATTRIBUTE ("xmlns") Namespace declarations in the logical 
 * parent element hierarchy. e.g.</p>
 * 
 * <pre>
 * &lt;parent xmlns:prefix1="http://Namespace-name-URI"&gt;
 *   &lt;child xmlns:prefix2="http://Namespace-name-URI"&gt;
 *     ...
 *   &lt;/child&gt;
 * &lt;/parent&gt;
 * </pre>
 * 
 * <P>A prefix can only be bound to a single Namespace URI in the current scope.</p>
 * 
 * @author ricky
 */
public class WbXmlNamespaceContext implements NamespaceContext {

    /**
     * logger for the class.
     */
    protected static final Logger log = Logger.getLogger(WbXmlNamespaceContext.class.getName());
    
    /**
     * Map for prefix to namespaces.
     */
    private Map<String, String> prefixToNamespace = null;
    
    /**
     * Map for namespace to prefix (the same namespace can have several prefixes).
     */
    private Map<String, Set<String>> namespaceToPrefix = null;
    
    /**
     * The default namespace (no prefix).
     */
    private String defaultNamespace = null;
    
    /**
     * Empty connstructor.
     */
    public WbXmlNamespaceContext() {
        this(null);
    }
    
    /**
     * Constructor setting the default namespace.
     * @param defaultNamespace The default namespace.
     */
    public WbXmlNamespaceContext(String defaultNamespace) {
        prefixToNamespace = new HashMap<String, String>();
        namespaceToPrefix = new HashMap<String, Set<String>>();
        this.defaultNamespace = defaultNamespace;
    }

    /**
     * Check if the default namespace has been set.
     * @return true if set, false otherwise
     */
    public boolean isDefaultNamespaceDefined() {
        return this.defaultNamespace != null;
    }
    
    /**
     * Setter for the default namespace.
     * @param defaultNamespace The default namespace
     */
    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }
    
    /**
     * Add a new prefix/namespace to the context.
     * @param prefix The prefirx for the namespace
     * @param namespaceURI The namespace URI
     */
    public void addPrefix(String prefix, String namespaceURI) {
        log.log(Level.FINE, "addPrefix({0}, {1})", new Object[] {prefix, namespaceURI});
        if (prefix == null || namespaceURI == null) {
            throw new IllegalArgumentException("Prefix or namespaceURI cannot be null!");
        }
        prefixToNamespace.put(prefix, namespaceURI);
        Set<String> prefixes = namespaceToPrefix.get(namespaceURI);
        if (prefixes == null) {
            prefixes = new HashSet<String>();
        }
        prefixes.add(prefix);
        namespaceToPrefix.put(namespaceURI, prefixes);
    }
    
    /**
     * Get Namespace URI bound to a prefix in the current scope.
     * @param prefix The prefix to get the URI from
     * @return The namespaceURI or null
     */
    @Override
    public String getNamespaceURI(String prefix) {
        log.log(Level.FINE, "getNamespaceURI({0})", prefix);
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null!");
        } else if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            // check if the namespace is the default one
            if (defaultNamespace == null) {
                return XMLConstants.NULL_NS_URI;
            } else {
                return defaultNamespace;
            }
        } else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        } else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else {
            String namespace = prefixToNamespace.get(prefix);
            if (namespace != null) {
                return prefixToNamespace.get(prefix);
            } else {
                return XMLConstants.NULL_NS_URI;
            }
        }
    }

    /**
     * Get prefix bound to Namespace URI in the current scope.
     * To get all prefixes bound to a Namespace URI in the current scope, 
     * use getPrefixes(String namespaceURI).
     * @param namespaceURI URI of Namespace to lookup
     * @return prefix bound to Namespace URI in current context
     */
    @Override
    public String getPrefix(String namespaceURI) {
        log.log(Level.FINE, "getPrefix({0})", namespaceURI);
        Iterator<String> i = getPrefixes(namespaceURI);
        if (i.hasNext()) {
            return i.next();
        } else {
            return null;
        }
    }

    /**
     * Get all prefixes bound to a Namespace URI in the current scope.
     * @param namespaceURI URI of Namespace to lookup
     * @return Iterator for all prefixes bound to the Namespace URI in the current scope
     */
    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        log.log(Level.FINE, "getPrefix({0})", namespaceURI);
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI cannot be null!");
        } else if (namespaceURI.equals(defaultNamespace)) {
            return  Arrays.asList(new String[]{XMLConstants.DEFAULT_NS_PREFIX}).iterator();
        } else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return Arrays.asList(new String[]{XMLConstants.XML_NS_PREFIX}).iterator();
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return Arrays.asList(XMLConstants.XMLNS_ATTRIBUTE).iterator();
        } else {
            Set<String> prefixes = namespaceToPrefix.get(namespaceURI);
            if (prefixes != null && !prefixes.isEmpty()) {
                return prefixes.iterator();
            } else {
                return new ArrayList<String>().iterator();
            }
        }
    }
    
}
