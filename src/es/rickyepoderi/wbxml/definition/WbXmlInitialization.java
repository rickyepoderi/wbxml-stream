/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.definition;

import es.rickyepoderi.wbxml.document.OpaquePlugin;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author ricky
 */
public class WbXmlInitialization {
    
    protected static final Logger log = Logger.getLogger(WbXmlInitialization.class.getName());

    static public final String DEFAULT_RESOURCE_DIRECTORY = "es/rickyepoderi/wbxml/definition/defaults";
    static public final String PROPERTIES_SUFFIX = ".properties";
    static public final String PROPERTIES_PREFIX = "wbxml.";

    static public final String PROP_WBXML_NAME = "wbxml.name";
    static public final String PROP_WBXML_PUBLIC_ID = "wbxml.publicid";
    static public final String PROP_WBXML_XML_PUBLIC_IDENTIFIER =  "wbxml.xmlpublicidentifier";
    static public final String PROP_WBXML_XML_URI_REFERENCE =  "wbxml.xmlurireference";
    static public final String PROP_WBXML_ROOT_ELEMENT =  "wbxml.rootelement";
    static public final String PROP_WBXML_CLASS_ELEMENT =  "wbxml.class";
    static public final String PROP_WBXML_TAG_PREFIX = "wbxml.tag.";
    static public final String PROP_WBXML_NAMESPACE_PREFIX = "wbxml.namespaces.";
    static public final String PROP_WBXML_ATTR_PREFIX = "wbxml.attr.";
    static public final String PROP_WBXML_ATTR_VALUE_PREFIX = "wbxml.attrvalue.";
    static public final String PROP_WBXML_EXT_PREFIX = "wbxml.ext.";
    static public final String PROP_WBXML_OPAQUE_ATTR_PREFIX = "wbxml.opaque.attr.";
    static public final String PROP_WBXML_OPAQUE_TAG_PREFIX = "wbxml.opaque.tag.";
    static public final String PROP_WBXML_VALUE_SUFFIX = ".value";
    
    static private List<WbXmlDefinition> definitions = null;
    
    static private WbXmlTagDef getTagDefinition(String key, String value) {
        try {
            // the line is "wbxml.tag.<pageCode>.[<prefix>:]<name>=<token>"
            String[] keys = key.split(Pattern.quote("."));
            byte pageCode = Integer.decode(keys[2]).byteValue();
            String name = keys[3];
            String prefix = null;
            int idx = name.indexOf(':');
            if (idx > 0) {
                prefix = name.substring(0, idx);
                name = name.substring(idx + 1);
            }
            byte token = Integer.decode(value).byteValue();
            return new WbXmlTagDef(prefix, name, token, pageCode);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading attribute {0}={1}", new Object[]{key, value});
            log.log(Level.SEVERE, "Exception", e);
            return null;
        }
    }
    
    static private WbXmlNamespaceDef getNamespaceDefinition(String key, String value) {
        try {
            // the line is "wbxml.namespaces.<prefix>=<namespace>"
            String[] keys = key.split(Pattern.quote("."));
            String prefix = keys[2];
            String namespace = value;
            return new WbXmlNamespaceDef(prefix, namespace);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading attribute {0}={1}", new Object[]{key, value});
            log.log(Level.SEVERE, "Exception",  e);
            return null;
        }
    }
    
    static private WbXmlAttributeDef getAttrDefinition(Properties props, String key, String value) {
        try {
            // two possible lines
            // compulsory line: wbxml.attr.<pageCode>.[<prefix>:]<name>[.<optional>]=<token>
            // optional line  : <previous_key>.value=<value>
            String val = props.getProperty(key + PROP_WBXML_VALUE_SUFFIX);
            String[] keys = key.split(Pattern.quote("."));
            byte pageCode = Integer.decode(keys[2]).byteValue();
            String name = keys[3];
            String prefix = null;
            int idx = name.indexOf(':');
            if (idx > 0) {
                prefix = name.substring(0, idx);
                name = name.substring(idx + 1);
            }
            byte token = Integer.decode(value).byteValue();
            return new WbXmlAttributeDef(prefix, name, token, pageCode, val);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading attribute {0}={1}", new Object[]{key, value});
            log.log(Level.SEVERE, "Exception",  e);
            return null;
        }
    }
    
    static private WbXmlAttributeValueDef getAttrValueDefinition(Properties props, String key, String value) {
        try {
            // two possible lines
            // compulsory line: wbxml.attrvalue.<pageCode>[.<optional>]=<token>
            // optional line  : <previous_key>.value=<value>
            String[] keys = key.split(Pattern.quote("."));
            byte pageCode = Integer.decode(keys[2]).byteValue();
            byte token = Integer.decode(value).byteValue();
            String val = props.getProperty(key + PROP_WBXML_VALUE_SUFFIX);
            return new WbXmlAttributeValueDef(val, token, pageCode);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading attribute value {0}={1}", new Object[]{key, value});
            log.log(Level.SEVERE, "Exception",  e);
            return null;
        }
    }
    
    static private WbXmlExtensionDef getExtDefinition(Properties props, String key, String value) {
        try {
            // two possible lines
            // compulsory line: wbxml.ext.<key_differenciator>=<token>
            // optional line  : <previous_key>.value=<value>
            byte token = Integer.decode(value).byteValue();
            String val = props.getProperty(key + PROP_WBXML_VALUE_SUFFIX);
            return new WbXmlExtensionDef(val, token);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading extension {0}={1}", new Object[] {key, value});
            log.log(Level.SEVERE, "Exception",  e);
            return null;
        }
    }
    
    static private void addOpaqueAttrPlugin(WbXmlDefinition def, Properties props, String key, String value) {
        try {
            // the line is: webxml.opaque.attr.<pageCode>.<name>=<class>
            String[] keys = key.split(Pattern.quote("."));
            byte pageCode = Integer.decode(keys[3]).byteValue();
            String name = keys[4];
            String tagProp = new StringBuilder(PROP_WBXML_ATTR_PREFIX)
                    .append(pageCode)
                    .append(".")
                    .append(name).toString();
            WbXmlAttributeDef attrDef = getAttrDefinition(props, tagProp, props.getProperty(tagProp));
            Class clazz = Class.forName(value);
            OpaquePlugin plugin = (OpaquePlugin) clazz.newInstance();
            def.addOpaqueAttr(attrDef, plugin);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading plugin {0}={1}", new Object[] {key, value});
            log.log(Level.SEVERE, "Exception",  e);
        }
    }
    
    static private void addOpaqueTagPlugin(WbXmlDefinition def, Properties props, String key, String value) {
        try {
            // the line is: webxml.opaque.tag.<pageCode>.<name>=<class>
            String[] keys = key.split(Pattern.quote("."));
            byte pageCode = Integer.decode(keys[3]).byteValue();
            String name = keys[4];
            String tagProp = new StringBuilder(PROP_WBXML_TAG_PREFIX)
                    .append(pageCode)
                    .append(".")
                    .append(name).toString();
            WbXmlTagDef tagDef = getTagDefinition(tagProp, props.getProperty(tagProp));
            Class clazz = Class.forName(value);
            OpaquePlugin plugin = (OpaquePlugin) clazz.newInstance();
            def.addOpaqueTag(tagDef, plugin);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading plugin {0}={1}", new Object[] {key, value});
            log.log(Level.SEVERE, "Exception",  e);
        }
    }
    
    static private void loadDefinition(Properties props) {
        // read the main parameters for the definition
        String name = props.getProperty(PROP_WBXML_NAME);
        long publicId = Long.decode(props.getProperty(PROP_WBXML_PUBLIC_ID));
        String xmlPublicId = props.getProperty(PROP_WBXML_XML_PUBLIC_IDENTIFIER);
        String xmlUriRef = props.getProperty(PROP_WBXML_XML_URI_REFERENCE);
        String clazz = props.getProperty(PROP_WBXML_CLASS_ELEMENT);
        WbXmlDefinition def = new WbXmlDefinition(name, publicId, xmlPublicId, xmlUriRef, clazz);
        // read all the tags
        for (String key: props.stringPropertyNames()) {
            if (key.startsWith(PROP_WBXML_TAG_PREFIX)) {
                WbXmlTagDef tag = getTagDefinition(key, props.getProperty(key));
                if (tag != null) {
                    def.addTag(tag);
                }
            } else if (key.startsWith(PROP_WBXML_NAMESPACE_PREFIX)) {
                WbXmlNamespaceDef ns = getNamespaceDefinition(key, props.getProperty(key));
                if (ns != null) {
                    def.addNamespace(ns);
                }
            } else if (key.startsWith(PROP_WBXML_ATTR_PREFIX) &&
                    !key.endsWith(PROP_WBXML_VALUE_SUFFIX)) {
                WbXmlAttributeDef attr = getAttrDefinition(props, key, props.getProperty(key));
                if (attr != null) {
                    def.addAttr(attr);
                }
            } else if (key.startsWith(PROP_WBXML_ATTR_VALUE_PREFIX) &&
                    !key.endsWith(PROP_WBXML_VALUE_SUFFIX)) {
                WbXmlAttributeValueDef attrVal = getAttrValueDefinition(props, key, props.getProperty(key));
                if (attrVal != null) {
                    def.addAttrValue(attrVal);
                }
            } else if (key.startsWith(PROP_WBXML_EXT_PREFIX) &&
                    !key.endsWith(PROP_WBXML_VALUE_SUFFIX)) {
                WbXmlExtensionDef ext = getExtDefinition(props, key, props.getProperty(key));
                if (ext != null) {
                    def.addExtension(ext);
                }
            } else if (key.startsWith(PROP_WBXML_OPAQUE_ATTR_PREFIX)) {
                addOpaqueAttrPlugin(def, props, key, props.getProperty(key));
            } else if (key.startsWith(PROP_WBXML_OPAQUE_TAG_PREFIX)) {
                addOpaqueTagPlugin(def, props, key, props.getProperty(key));
            }
        }
        String root = props.getProperty(PROP_WBXML_ROOT_ELEMENT);
        def.setRoot(root);
        definitions.add(def);
    }
    
    static private void loadPropertiesJar(URL resource, String path) {
        JarURLConnection jarConn;
        JarFile jar = null;
        try {
            jarConn = (JarURLConnection) resource.openConnection();
            jar = jarConn.getJarFile();
            Enumeration<JarEntry> e = jar.entries();
            while (e.hasMoreElements()) {
                JarEntry entry = e.nextElement();
                if (entry.getName().startsWith(path + "/" + PROPERTIES_PREFIX)
                        && entry.getName().endsWith(PROPERTIES_SUFFIX)) {
                    Properties props = new Properties();
                    props.load(jar.getInputStream(entry));
                    loadDefinition(props);
                }
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "loadPropertiesJar(): Error loading definition {0}", resource);
            log.log(Level.SEVERE, "loadPropertiesJar(): Error loading definition...", e);
        } finally {
            try {
                jar.close();
            } catch (IOException e) {
            }
        }
    }

    static private void loadPropertiesFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile() && f.getName().endsWith(PROPERTIES_SUFFIX)
                        && f.getName().startsWith(PROPERTIES_PREFIX)) {
                    FileReader reader = null;
                    try {
                        Properties props = new Properties();
                        reader = new FileReader(f);
                        props.load(reader);
                        loadDefinition(props);
                    } catch (IOException e) {
                        log.log(Level.SEVERE, "loadPropertiesFile(): Error loading definition {0}", file.getAbsoluteFile());
                        log.log(Level.SEVERE, "loadPropertiesFile(): Error loading definition...", e);
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException ex) {
                            }
                        }
                    }
                }
            }
        }
    }

    static private void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(DEFAULT_RESOURCE_DIRECTORY);
        //System.err.println(resource);
        if (resource.getProtocol().equals("jar")) {
            loadPropertiesJar(resource, DEFAULT_RESOURCE_DIRECTORY);
        } else if (resource.getProtocol().equals("file")) {
            loadPropertiesFile(new File(resource.getFile()));
        } else {
            //System.err.println("Unknown protocol connection: " + resource);
        }
    }
    
    static synchronized public WbXmlDefinition[] getDefinitions() {
        return definitions.toArray(new WbXmlDefinition[0]);
    }
    
    static synchronized public WbXmlDefinition getDefinitionByFPI(String fpi) {
        for (WbXmlDefinition def: definitions) {
            if (def.getXmlPublicId() != null && def.getXmlPublicId().equals(fpi)) {
                return def;
            }
        }
        return null;
    }
    
    static synchronized public WbXmlDefinition getDefinitionByPublicId(long id) {
        for (WbXmlDefinition def: definitions) {
            if (def.getPublicId() == id) {
                return def;
            }
        }
        return null;
    }
    
    static synchronized public WbXmlDefinition getDefinitionByName(String fpi) {
        for (WbXmlDefinition def: definitions) {
            if (def.getName() != null && def.getName().equals(fpi)) {
                return def;
            }
        }
        return null;
    }
    
    static synchronized public WbXmlDefinition getDefinitionByRoot(String root, String namespaceUri) {
        for (WbXmlDefinition def : definitions) {
            if (namespaceUri != null && !namespaceUri.isEmpty()) {
                // get the prefix for this namespace
                String prefix = def.getPrefix(namespaceUri);
                if (prefix != null) {
                    root = new StringBuilder(prefix).append(":").append(root).toString();
                }
            }
            if (def.getRoot() != null && def.getRoot().getNameWithPrefix().equals(root)) {
                return def;
            }
        }
        return null;
    }
    
    static synchronized public void reload() {
        definitions.clear();
        init();
    }

    static public void main(String[] args) throws Exception {
        System.err.println(getDefinitions().length);
        for (WbXmlDefinition def: getDefinitions()) {
            System.err.println(def);
        }
    }
    
    static {
        definitions = new ArrayList<WbXmlDefinition>();
        init();
    }
}
