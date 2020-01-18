package ch.jcsinfo.javafx.helpers;


import java.io.Serializable;

public class Classe implements Serializable{
  private static final long serialVersionUID = 1L;

  private String nom;
  private String section;

  public Classe(String nom, String section) {
    this.nom = nom;
    this.section = section;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getSection() {
    return section;
  }

  public void setSection(String section) {
    this.section = section;
  }

  @Override
  public String toString() {
    return nom + " ("+section + ')';
  }

}
