package ch.emfinfo.helpers;

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
  TOP_LEFT(0, "top_left"),
  TOP_CENTER(1, "top_center"),
  TOP_RIGHT(2, "top_right"),
  MIDDLE_LEFT(3, "middle_left"),
  MIDDLE_CENTER(4, "middle_center"),
  MIDDLE_RIGHT(5, "middle_right"),
  BOTTOM_LEFT(6, "bottom_left"),
  BOTTOM_CENTER(7, "bottom_center"),
  BOTTOM_RIGHT(8, "bottom_right");
  protected int value;
  protected String name;

  JfxLabelPosEnum(int aValue, String aName) {
    this.value = aValue;
    this.name = aName;
  }

  public int getValue() {
    return this.value;
  }

  public int getRow() {
    return this.value / 3;
  }

  public int getCol() {
    return this.value % 3;
  }

  public String getName() {
    return this.name;
  }

  public static JfxLabelPosEnum getEnum(String searchStr) {
    String s = searchStr.toLowerCase();
    if (s.equals(TOP_CENTER.getName())) {
      return TOP_CENTER;
    } else if (s.equals(TOP_RIGHT.getName())) {
      return TOP_RIGHT;
    } else if (s.equals(MIDDLE_LEFT.getName())) {
      return MIDDLE_LEFT;
    } else if (s.equals(MIDDLE_CENTER.getName())) {
      return MIDDLE_CENTER;
    } else if (s.equals(MIDDLE_RIGHT.getName())) {
      return MIDDLE_RIGHT;
    } else if (s.equals(BOTTOM_LEFT.getName())) {
      return BOTTOM_LEFT;
    } else if (s.equals(BOTTOM_CENTER.getName())) {
      return BOTTOM_CENTER;
    } else if (s.equals(BOTTOM_RIGHT.getName())) {
      return BOTTOM_RIGHT;
    } else {
      return TOP_LEFT;
    }
  }

  public static JfxLabelPosEnum getEnum(int idx) {
    switch (idx) {
      case 1:
        return TOP_CENTER;
      case 2:
        return TOP_RIGHT;
      case 3:
        return MIDDLE_LEFT;
      case 4:
        return MIDDLE_CENTER;
      case 5:
        return MIDDLE_RIGHT;
      case 6:
        return BOTTOM_LEFT;
      case 7:
        return BOTTOM_CENTER;
      case 8:
        return BOTTOM_RIGHT;
    }
    return TOP_LEFT;
  }



}
