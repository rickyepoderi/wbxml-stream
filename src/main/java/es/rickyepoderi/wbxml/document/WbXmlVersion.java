/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.document;

/**
 *
 * <p>The version of the WBXML document is defined in the specification in
 * the following chapter <em>5.4. Version Number</em></p>
 * 
 * <pre>
 * version = u_int8 // WBXML version number
 * </pre>
 * 
 * <p>The version is just the major and minor number. Although all versions are
 * defined only 1.3 is used (parser and encoder do not differentiate between
 * versions. Here a enumeration is used.</p>
 * 
 * @author ricky
 */
public enum WbXmlVersion {
    
    /**
     * Version 1.0
     */
    VERSION_1_0((byte)1, (byte)0),
    
    
    /**
     * Version 1.1
     */
    VERSION_1_1((byte)1, (byte)0),
    
    /**
     * Version 1.2
     */
    VERSION_1_2((byte)1, (byte)2),
    
    /**
     * Version 1.3
     */
    VERSION_1_3((byte)1, (byte)3);
    
    /**
     * major version number
     */
    private byte major;
    
    /**
     * minor version number
     */
    private byte minor;
    
    /**
     * Private constructor for the enumeration.
     * @param major The major version number
     * @param minor The minor version number
     */
    private WbXmlVersion(byte major, byte minor) {
        this.major = major;
        this.minor = minor;
    }

    /**
     * Getter for the major number
     * @return The major number
     */
    public byte getMajor() {
        return major;
    }

    /**
     * Getter for the minor number
     * @return The minor number
     */
    public byte getMinor() {
        return minor;
    }

    /**
     * Searches over the list of versions to get the one that corresponds to
     * this major and minor.
     * @param major The major version number of the version to search
     * @param minor The minor version number of the version to search
     * @return The version or null
     */
    static public WbXmlVersion locateVersion(byte major, byte minor) {
        for (WbXmlVersion v : WbXmlVersion.values()) {
            if (v.getMajor() == major && v.getMinor() == minor) {
                return v;
            }
        }
        return null;
    }

    /**
     * String representation with indentation
     * @param ident The indentation to use
     * @return The string representation
     */
    public String toString(int ident) {
        return new StringBuilder(WbXmlLiterals.identString(ident))
                .append("version: ")
                .append(major)
                .append(".")
                .append(minor)
                .toString();
    }
    
    /**
     * String representation
     * @return The strong representation
     */
    @Override
    public String toString() {
        return toString(0);
    }
}
