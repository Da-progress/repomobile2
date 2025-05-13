package ch.sofa.xml;

import java.io.File;
import java.io.Writer;
import java.util.function.Function;

import javax.xml.bind.JAXBElement;

public class XmlJaxbElementWriter<T> {

  private XmlWriter<Object> writer;
  private Function<T, JAXBElement<T>> tToJaxbElement;

  @SuppressWarnings("unchecked")
  public XmlJaxbElementWriter(Class<T> xmlRootElementClass, Function<T, JAXBElement<T>> tToJaxbElement) {
    this.tToJaxbElement = tToJaxbElement;
    writer = new XmlWriter(xmlRootElementClass);
  }

  @SuppressWarnings("unchecked")
  public XmlJaxbElementWriter(Class<T> xmlRootElementClass, String encoding, Function<T, JAXBElement<T>> tToJaxbElement) {
    this.tToJaxbElement = tToJaxbElement;
    writer = new XmlWriter(xmlRootElementClass, encoding);
  }

  public File write(File xmlFile, T xmlRootElement) {
    return writer.write(xmlFile, tToJaxbElement.apply(xmlRootElement));
  }

  public String write(T xmlRootElement) {
    return writer.write(tToJaxbElement.apply(xmlRootElement));
  }

  public Writer write(Writer writer, T xmlRootElement) {
    return this.writer.write(writer, tToJaxbElement.apply(xmlRootElement));
  }

  /**
   * Static method to write XML file. Encoding of the output-file is UTF-8.
   * 
   * @param xmlFile
   * @param xmlRootElement
   * @param tToJaxbElement Function that converts T into a JAXBElement<T>
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> File writeFile(File xmlFile, T xmlRootElement, Function<T, JAXBElement<T>> tToJaxbElement) {
    return new XmlJaxbElementWriter(xmlRootElement.getClass(), tToJaxbElement).write(xmlFile, xmlRootElement);
  }

  /**
   * Static method to return XML content in a String.
   * 
   * @param xmlRootElement
   * @param tToJaxbElement Function that converts T into a JAXBElement<T>
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> String writeString(T xmlRootElement, Function<T, JAXBElement<T>> tToJaxbElement) {
    return new XmlJaxbElementWriter(xmlRootElement.getClass(), tToJaxbElement).write(xmlRootElement);
  }

}
