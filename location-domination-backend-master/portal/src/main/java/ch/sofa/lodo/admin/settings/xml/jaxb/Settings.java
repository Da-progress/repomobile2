
package ch.sofa.lodo.admin.settings.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="event-module" type="{}event-module"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eventModule"
})
@XmlRootElement(name = "settings")
public class Settings {

    @XmlElement(name = "event-module", required = true)
    protected EventModule eventModule;

    /**
     * Gets the value of the eventModule property.
     * 
     * @return
     *     possible object is
     *     {@link EventModule }
     *     
     */
    public EventModule getEventModule() {
        return eventModule;
    }

    /**
     * Sets the value of the eventModule property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventModule }
     *     
     */
    public void setEventModule(EventModule value) {
        this.eventModule = value;
    }

}
