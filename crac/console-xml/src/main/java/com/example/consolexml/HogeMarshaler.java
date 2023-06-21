package com.example.consolexml;

import java.io.StringReader;

import javax.xml.bind.JAXB;

public class HogeMarshaler {
  public void marshal() {
    var hoge = new Hoge(0, "hoge0");
    JAXB.marshal(hoge, System.out);
  }

  public Hoge unmarshal() {
    String xml = "<?xml version=\"1.0\"?>"
               + "<hoge><id>1</id><value>hoge1</value></hoge>";
    return JAXB.unmarshal(new StringReader(xml), Hoge.class);
  }
}
