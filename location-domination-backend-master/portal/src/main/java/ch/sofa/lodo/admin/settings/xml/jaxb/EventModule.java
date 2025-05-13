
package ch.sofa.lodo.admin.settings.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for event-module complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="event-module"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="new-event-price" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="new-event-currency" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "event-module", propOrder = {
    "newEventPrice",
    "newEventCurrency"
})
public class EventModule {

    @XmlElement(name = "new-event-price")
    protected double newEventPrice;
    @XmlElement(name = "new-event-currency", required = true)
    protected String newEventCurrency;

    /**
     * Gets the value of the newEventPrice property.
     * 
     */
    public double getNewEventPrice() {
        return newEventPrice;
    }

    /**
     * Sets the value of the newEventPrice property.
     * 
     */
    public void setNewEventPrice(double value) {
        this.newEventPrice = value;
    }

    /**
     * Gets the value of the newEventCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewEventCurrency() {
        return newEventCurrency;
    }

    /**
     * Sets the value of the newEventCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewEventCurrency(String value) {
        this.newEventCurrency = value;
    }

}
