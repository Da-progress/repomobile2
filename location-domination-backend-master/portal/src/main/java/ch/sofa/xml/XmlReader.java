package ch.sofa.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class XmlReader<T> {

  protected Unmarshaller unmarshaller;
  protected XMLInputFactory xmlInputFactory;

  public XmlReader(Class<T> xmlRootElementClass) {
    this.unmarshaller = createUnmarshaller(xmlRootElementClass);
    this.xmlInputFactory = createXMLInputFactory();
  }

  protected Unmarshaller createUnmarshaller(Class<T> xmlRootElementClass) {
    try {
      JAXBContext jc = JAXBContext.newInstance(xmlRootElementClass.getPackage()
                                                                  .getName());
      return jc.createUnmarshaller();
    } catch (JAXBException e) {
      throw new XmlReadException("An error occured while creating xml reader.", e);
    }
  }

  protected XMLInputFactory createXMLInputFactory() {
    XMLInputFactory xif = XMLInputFactory.newInstance();
    xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    return xif;
  }
  
  public static <T> T fromFile(File xmlFile, Class<T> xmlRootElementClass) {
    return new XmlReader<T>(xmlRootElementClass).read(xmlFile, xmlRootElementClass);
  }
  
  public static <T> T fromStream(InputStream stream, Class<T> xmlRootElementClass) {
    return new XmlReader<T>(xmlRootElementClass).read(stream, xmlRootElementClass);
  }
  
  public static <T> T fromReader(Reader reader, Class<T> xmlRootElementClass) {
    return new XmlReader<T>(xmlRootElementClass).read(reader, xmlRootElementClass);
  }
  
  public static <T> T fromString(String xmlcontent, Class<T> xmlRootElementClass) {
    return new XmlReader<T>(xmlRootElementClass).read(xmlcontent, xmlRootElementClass);
  }

  /**
   * Reads the given XML file and creates POJOs out of it. Throws an unchecked
   * {@link XmlReadException} if any exception occurs.
   * 
   * @param xmlFile
   * @param xmlRootElementClass
   * @return
   */
  public T read(File xmlFile, Class<T> xmlRootElementClass) {
    try (FileInputStream fis = new FileInputStream(xmlFile)) {
      return read(fis, xmlRootElementClass);
    } catch (Throwable e) {
      throw new XmlReadException("An error occured while opening and reading xml file '" + xmlFile.getName() + "'.", e);
    }
  }
  
  /**
   * Reads a XML from the given String and creates POJOs out of it. Throws an unchecked
   * {@link XmlReadException} if any exception occurs.
   * 
   * @param xmlcontent
   * @param xmlRootElementClass
   * @return
   */
  public T read(String xmlcontent, Class<T> xmlRootElementClass) {
    try (StringReader reader = new StringReader(xmlcontent)) {
      return read(reader, xmlRootElementClass);
    }
  }
  
  /**
   * Reads a XML from the given reader and creates POJOs out of it. Throws an unchecked
   * {@link XmlReadException} if any exception occurs.
   * 
   * @param reader
   * @param xmlRootElementClass
   * @return
   */
  public T read(Reader reader, Class<T> xmlRootElementClass) {
    try {
      XMLStreamReader xsr = xmlInputFactory.createXMLStreamReader(reader);
      return xmlRootElementClass.cast(unmarshaller.unmarshal(xsr));
    } catch (Throwable e) {
      throw new XmlReadException("An error occured while reading XML.", e);
    }
  }
  
  /**
   * Reads a XML from the given input-stream and creates POJOs out of it. Throws an unchecked
   * {@link XmlReadException} if any exception occurs.
   * 
   * @param stream
   * @param xmlRootElementClass
   * @return
   */
  public T read(InputStream stream, Class<T> xmlRootElementClass) {
    try {
      XMLStreamReader xsr = xmlInputFactory.createXMLStreamReader(stream);
      return xmlRootElementClass.cast(unmarshaller.unmarshal(xsr));
    } catch (Throwable e) {
      throw new XmlReadException("An error occured while reading XML from stream.", e);
    }
  }

}
