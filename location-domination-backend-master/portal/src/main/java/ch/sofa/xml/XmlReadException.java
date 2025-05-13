package ch.sofa.xml;

public class XmlReadException extends RuntimeException {

  private static final long serialVersionUID = 5188957072935932629L;

  public XmlReadException() {
    super();
  }

  public XmlReadException(String message, Throwable cause) {
    super(message, cause);
  }

  public XmlReadException(String message) {
    super(message);
  }

  public XmlReadException(Throwable cause) {
    super(cause);
  }

}
