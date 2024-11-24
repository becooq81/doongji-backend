package com.find.doongji.apt.util;

import org.json.JSONArray;
import org.json.JSONObject;
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

public class ParseUtils {

    public static List<Map<String, String>> parseXML(String path, String responseBody, String... keys) throws Exception {
        if (path == null || path.isEmpty()) {
            throw new Exception("XPath Expression cannot be empty.");
        }
        List<Map<String, String>> result = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(responseBody)));
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList items = (NodeList) xPath.evaluate(path, doc, XPathConstants.NODESET);

            for (int i = 0; i < items.getLength(); i++) {
                result.add(new HashMap<>());

                Element item = (Element) items.item(i);
                for (String key : keys) {
                    if (item.getElementsByTagName(key).item(0) != null) {
                        result.get(i).put(key, item.getElementsByTagName(key).item(0).getTextContent());
                    } else {
                        result.get(i).put(key, "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Map<String, String>> parseJson(String responseBody, String... keys) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Extract resultdata array from the JSON
            JSONArray resultDataArray = jsonResponse.getJSONObject("result").getJSONArray("resultdata");

            for (int i = 0; i < resultDataArray.length(); i++) {
                JSONObject item = resultDataArray.getJSONObject(i);
                Map<String, String> itemMap = new HashMap<>();

                for (String key : keys) {
                    itemMap.put(key, item.getString(key));
                }

                result.add(itemMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
