package ch.sofa.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

public class XmlWriter<T> {

  protected Marshaller marshaller;
  protected String encoding = StandardCharsets.UTF_8.name();

  public XmlWriter(Class<T> xmlRootElementClass) {
    this(xmlRootElementClass, StandardCharsets.UTF_8.name());
  }

  public XmlWriter(Class<T> xmlRootElementClass, String encoding) {
    this.encoding = encoding;
    marshaller = createMarshaller(xmlRootElementClass);
  }

  /**
   * Static method to write XML file. Encoding of the output-file is UTF-8.
   * 
   * @param xmlFile
   * @param xmlRootElement
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static File writeFile(File xmlFile, Object xmlRootElement) {
    return new XmlWriter(xmlRootElement.getClass()).write(xmlFile, xmlRootElement);
  }

  /**
   * Static method to return XML content in a String.
   * 
   * @param xmlRootElement
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static String writeString(Object xmlRootElement) {
    return new XmlWriter(xmlRootElement.getClass()).write(xmlRootElement);
  }

  public File write(File xmlFile, T xmlRootElement) {
    try {
      if (!xmlFile.exists() && xmlFile.getParentFile() != null) {
        xmlFile.getParentFile()
               .mkdirs();
      }
      marshaller.marshal(xmlRootElement, xmlFile);
      return xmlFile;
    } catch (JAXBException e) {
      throw new XmlWriteException("An error occured while writing xml file '" + xmlFile.getName() + "'.", e);
    }
  }
  
  public String write(T xmlRootElement) {
    try (StringWriter writer = new StringWriter()) {
      write(writer, xmlRootElement);
      return writer.toString();
    } catch (IOException e) {
      throw new XmlWriteException("An error occured while writing '" + xmlRootElement.getClass().getSimpleName() + "' XML content to String.", e);
    }
  }
  
  public Writer write(Writer writer, T xmlRootElement) {
    try {
      marshaller.marshal(xmlRootElement, writer);
      writer.flush();
      return writer;
    } catch (IOException | JAXBException e) {
      throw new XmlWriteException("An error occured while writing '" + xmlRootElement.getClass().getSimpleName() + "' XML content to String.", e);
    }
  }

  protected Marshaller createMarshaller(Class<T> xmlRootElementClass) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(xmlRootElementClass.getPackage()
                                                                           .getName());

      Marshaller marshaller = jaxbContext.createMarshaller();
      configMarshaller(marshaller);
      return marshaller;
    } catch (JAXBException e) {
      throw new XmlWriteException("An error occured while creating the marshaller for '" + xmlRootElementClass.getSimpleName() + "'.", e);
    }
  }

  protected void configMarshaller(Marshaller marshaller) throws PropertyException {
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
  }

}
