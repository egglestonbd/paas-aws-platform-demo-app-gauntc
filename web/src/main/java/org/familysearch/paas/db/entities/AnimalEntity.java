package org.familysearch.paas.db.entities;

import javax.persistence.*;

@Entity(name = "animal")
@Table
public class AnimalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", unique = true)
  private String name;

  public AnimalEntity() {
  }

  public AnimalEntity(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
