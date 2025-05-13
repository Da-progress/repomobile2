package ch.sofa.xml;

public class XmlWriteException extends RuntimeException {

  private static final long serialVersionUID = 5188957072935932629L;

  public XmlWriteException() {
    super();
  }

  public XmlWriteException(String message, Throwable cause) {
    super(message, cause);
  }

  public XmlWriteException(String message) {
    super(message);
  }

  public XmlWriteException(Throwable cause) {
    super(cause);
  }

}
