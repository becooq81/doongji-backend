package com.find.doongji.apt.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static List<Map<String, String>> parseXML(String responseBody, String... keys) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(responseBody)));
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList items = (NodeList) xPath.evaluate("/response/body/items/item", doc, XPathConstants.NODESET);

            for (int i = 0; i < items.getLength(); i++) {
                result.add(new HashMap<>());

                Element item = (Element) items.item(i);
                for (String key : keys) {
                    result.get(i).put(key, item.getElementsByTagName(key).item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
