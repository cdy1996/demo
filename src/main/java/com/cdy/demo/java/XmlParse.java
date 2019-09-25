package com.cdy.demo.java;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlParse {

    public static void main(String[] args) throws Exception {
        Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("d:/dataservice.xml"));


        System.out.println(parse.getElementsByTagName("result").item(0).getAttributes().getNamedItem("code").getNodeValue());
        System.out.println(parse.getElementsByTagName("CONTENT").item(0).getTextContent());

    }
}
