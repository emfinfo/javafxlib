package ch.jcsinfo.javafx.helpers;

/**
 * Création d'un type énumération qui permet de retrouver facilement
 * le n° de ligne ou de colonne d'un texte placé sur l'une des 9 positions
 * suivantes :
 *
 * 0 1 2
 * 3 4 5
 * 6 7 8
 *
 * @author jcstritt
 */
public enum JfxLabelPosEnum {
  TOP_LEFT(0),
  TOP_CENTER(1),
  TOP_RIGHT(2),
  MIDDLE_LEFT(3),
  MIDDLE_CENTER(4),
  MIDDLE_RIGHT(5),
  BOTTOM_LEFT(6),
  BOTTOM_CENTER(7),
  BOTTOM_RIGHT(8);
  
  protected int idx;

  JfxLabelPosEnum(int idx) {
    this.idx = idx;
  }
  
  @Override
  public String toString() {
    return this.name();
  }  

  public int getIdx() {
    return this.idx;
  }

  public int getRow() {
    return this.idx / 3;
  }

  public int getCol() {
    return this.idx % 3;
  }

  public String getName() {
    return this.name();
  }
  
  public static JfxLabelPosEnum getEnum(String name) {
    String s = name.toUpperCase();
    for (JfxLabelPosEnum e : values()) {
      if (e.getName().equals(s)) {
        return e;
      }
    }
    return null;
  }

  public static JfxLabelPosEnum getEnum(int idx) {
    for (JfxLabelPosEnum e : values()) {
      if (e.idx == idx) {
        return e;
      }
    }
    return null;
  }  

}
