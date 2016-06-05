/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.test;

import es.rickyepoderi.wbxml.bind.si.Indication;
import es.rickyepoderi.wbxml.bind.si.Info;
import es.rickyepoderi.wbxml.bind.si.Item;
import es.rickyepoderi.wbxml.bind.si.Si;
import es.rickyepoderi.wbxml.definition.IanaCharset;
import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.definition.WbXmlInitialization;
import es.rickyepoderi.wbxml.document.WbXmlAttribute;
import es.rickyepoderi.wbxml.document.WbXmlBody;
import es.rickyepoderi.wbxml.document.WbXmlContent;
import es.rickyepoderi.wbxml.document.WbXmlDocument;
import es.rickyepoderi.wbxml.document.WbXmlElement;
import es.rickyepoderi.wbxml.document.WbXmlEncoder;
import es.rickyepoderi.wbxml.document.WbXmlVersion;
import es.rickyepoderi.wbxml.stream.WbXmlOutputFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class NumericCharacterEntityTest extends GenericDirectoryTester {

    public NumericCharacterEntityTest() {
        super("", WbXmlInitialization.getDefinitionByName("SI 1.0"));
    }
    
    private Si createSi() {
        Indication indication = new Indication();
        indication.setCreated("1999-06-25T15:23:15Z");
        indication.setHref("http://www.xyz.com/email/123/abc.wml");
        indication.setSiExpires("1999-06-30T00:00:00Z");
        indication.setvalue("You have 4 new emails");
        Si si = new Si();
        si.setIndication(indication);
        Info info = new Info();
        Item item = new Item();
        item.setClazz("test1");
        info.getItem().add(item);
        si.setInfo(info);
        return si;
    }
    
    private byte[] si2wbxmlRaw(Si si, String entity) throws XMLStreamException, IOException {
        WbXmlDocument doc = new WbXmlDocument(
                WbXmlVersion.VERSION_1_3,
                this.getDefinition(),
                IanaCharset.UTF_8,
                new WbXmlBody(
                        new WbXmlElement(
                                "si",
                                new WbXmlElement[]{
                                    new WbXmlElement(
                                            "indication",
                                            new WbXmlAttribute[]{
                                                new WbXmlAttribute("href", si.getIndication().getHref()),
                                                new WbXmlAttribute("created", si.getIndication().getCreated()),
                                                new WbXmlAttribute("si-expires", si.getIndication().getSiExpires())
                                            },
                                            new WbXmlContent[] {
                                                new WbXmlContent(entity),
                                                new WbXmlContent(si.getIndication().getvalue())
                                            }
                                    ),
                                    new WbXmlElement(
                                            "info",
                                            new WbXmlElement(
                                                    "item",
                                                    new WbXmlAttribute("class", 
                                                            new String[] {
                                                                entity,
                                                                si.getInfo().getItem().get(0).getClazz()
                                                            })
                                            )
                                    ),
                                }
                        )
                )
        );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WbXmlEncoder encoder = new WbXmlEncoder(bos, doc);
        encoder.encode();
        return bos.toByteArray();
    }
    
    private byte[] si2wbxmlByStax(Si si, String entity) throws XMLStreamException {
        XMLOutputFactory fact = new WbXmlOutputFactory();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter xmlStreamWriter = fact.createXMLStreamWriter(bos);
        xmlStreamWriter.writeStartDocument();
        xmlStreamWriter.writeStartElement("si");
        xmlStreamWriter.writeStartElement("indication");
        xmlStreamWriter.writeAttribute("created", si.getIndication().getCreated());
        xmlStreamWriter.writeAttribute("href", si.getIndication().getHref());
        xmlStreamWriter.writeAttribute("si-expires", si.getIndication().getSiExpires());
        xmlStreamWriter.writeCharacters(si.getIndication().getvalue());
        xmlStreamWriter.writeEntityRef(entity);
        xmlStreamWriter.writeEndElement(); // indication end
        xmlStreamWriter.writeStartElement("info");
        xmlStreamWriter.writeStartElement("item");
        xmlStreamWriter.writeAttribute("class", si.getInfo().getItem().get(0).getClazz());
        xmlStreamWriter.writeEndElement(); // item
        xmlStreamWriter.writeEndElement(); // info
        xmlStreamWriter.writeEndElement(); // si
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.close();
        return bos.toByteArray();
    }
    
    @Test(groups = {"wbxml", "entity"})
    public void testTagEntityDecimal() throws Exception {
        WbXmlDefinition def = this.getDefinition();
        Si si = createSi();
        byte[] wbxml = si2wbxmlByStax(si, "#241");
        FileOutputStream fos = new FileOutputStream("/home/ricky/lala.wbxml");
        fos.write(wbxml);
        fos.close();
        Si si2 = (Si) this.wbxml2Object(wbxml, def, false);
        // assert the value is the same plus the &#241; (ñ)
        Assert.assertEquals(si2.getIndication().getvalue(), si.getIndication().getvalue() + "ñ");
    }
    
    @Test(groups = {"wbxml", "entity"})
    public void testTagEntityHexa() throws Exception {
        WbXmlDefinition def = this.getDefinition();
        Si si = createSi();
        byte[] wbxml = si2wbxmlByStax(si, "#xF1");
        Si si2 = (Si) this.wbxml2Object(wbxml, def, false);
        // assert the value is the same plus the &#F1; (ñ)
        Assert.assertEquals(si2.getIndication().getvalue(), si.getIndication().getvalue() + "ñ");
    }
    
    @Test(groups = {"wbxml", "entity"})
    public void testTagEntityHexa2() throws Exception {
        WbXmlDefinition def = this.getDefinition();
        Si si = createSi();
        byte[] wbxml = si2wbxmlByStax(si, "#xf1");
        Si si2 = (Si) this.wbxml2Object(wbxml, def, false);
        // assert the value is the same plus the &#f1; (ñ)
        Assert.assertEquals(si2.getIndication().getvalue(), si.getIndication().getvalue() + "ñ");
    }
    
    @Test(groups = {"wbxml", "entity"})
    public void testAttrEntityDecimal() throws Exception {
        Si si = createSi();
        byte[] wbxml = si2wbxmlRaw(si, "&#241;");
        Si si2 = (Si) this.wbxml2Object(wbxml, this.getDefinition(), false);
        // assert the value is the same plus the &#f1; (ñ)
        Assert.assertEquals(si2.getIndication().getvalue(), "ñ" + si.getIndication().getvalue());
        Assert.assertEquals(si2.getInfo().getItem().get(0).getClazz(), 
                "ñ" + si.getInfo().getItem().get(0).getClazz());
    }
    
    @Test(groups = {"wbxml", "entity"})
    public void testAttrEntityHexa() throws Exception {
        Si si = createSi();
        byte[] wbxml = si2wbxmlRaw(si, "&#xF1;");
        Si si2 = (Si) this.wbxml2Object(wbxml, this.getDefinition(), false);
        // assert the value is the same plus the &#f1; (ñ)
        Assert.assertEquals(si2.getIndication().getvalue(), "ñ" + si.getIndication().getvalue());
        Assert.assertEquals(si2.getInfo().getItem().get(0).getClazz(), 
                "ñ" + si.getInfo().getItem().get(0).getClazz());
    }
    
    @Test(groups = {"wbxml", "entity"})
    public void testAttrEntityHexa2() throws Exception {
        Si si = createSi();
        byte[] wbxml = si2wbxmlRaw(si, "&#xf1;");
        Si si2 = (Si) this.wbxml2Object(wbxml, this.getDefinition(), false);
        // assert the value is the same plus the &#f1; (ñ)
        Assert.assertEquals(si2.getIndication().getvalue(), "ñ" + si.getIndication().getvalue());
        Assert.assertEquals(si2.getInfo().getItem().get(0).getClazz(), 
                "ñ" + si.getInfo().getItem().get(0).getClazz());
    }
    
}
