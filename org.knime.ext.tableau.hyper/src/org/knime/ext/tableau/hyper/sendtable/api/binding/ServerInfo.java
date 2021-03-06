//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.07 at 05:07:04 PM CET 
//


package org.knime.ext.tableau.hyper.sendtable.api.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serverInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serverInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productVersion" type="{http://tableau.com/api}productVersion"/>
 *         &lt;element name="restApiVersion" type="{http://tableau.com/api}restApiVersion"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serverInfo", propOrder = {
    "productVersion",
    "restApiVersion"
})
public class ServerInfo {

    @XmlElement(required = true)
    protected ProductVersion productVersion;
    @XmlElement(required = true)
    protected String restApiVersion;

    /**
     * Gets the value of the productVersion property.
     * 
     * @return
     *     possible object is
     *     {@link ProductVersion }
     *     
     */
    public ProductVersion getProductVersion() {
        return productVersion;
    }

    /**
     * Sets the value of the productVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductVersion }
     *     
     */
    public void setProductVersion(ProductVersion value) {
        this.productVersion = value;
    }

    /**
     * Gets the value of the restApiVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestApiVersion() {
        return restApiVersion;
    }

    /**
     * Sets the value of the restApiVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestApiVersion(String value) {
        this.restApiVersion = value;
    }

}
