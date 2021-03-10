package com.samyx.indicadores.economicos;


import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {
  private String xml;
  
  private Element rootElement;
  
  public XmlParser(String data) throws SAXException, IOException, ParserConfigurationException {
    data = replaceChars(data);
    this.xml = data;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(new InputSource(new StringReader(this.xml)));
    this.rootElement = document.getDocumentElement();
  }
  
  public String getValue(String tag) {
    try {
      NodeList list = this.rootElement.getElementsByTagName(tag);
      if (list != null && list.getLength() > 0) {
        NodeList subList = list.item(0).getChildNodes();
        if (subList != null && subList.getLength() > 0)
          return subList.item(0).getNodeValue(); 
      } 
    } catch (Exception e) {
      return "0";
    } 
    return "0";
  }
  
  private String replaceChars(String string) {
    string = string.replace("&lt;", "<");
    string = string.replace("&gt;", ">");
    return string;
  }
}

