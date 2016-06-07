package org.familysearch.paas.schema;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Animal {

  private String name;

  public Animal() {
  }

  public Animal(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
