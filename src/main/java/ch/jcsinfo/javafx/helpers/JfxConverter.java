package ch.jcsinfo.javafx.helpers;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Depuis des valeurs String de paramètres de l'application (AppSettings),
 * retourne les objets correspondants.
 *
 * @author jcstritt
 */
public class JfxConverter {

  /**
   * Convertit une valeur String en entier.
   *
   * @param value une valeur de type String
   * @return une valeur de type entier
   */
  public static int getInt(String value) {
    int result;
    try {
      result = Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
      result = 0;
    }
    return result;
  }

  /**
   * Convertit une valeur String en boolean. Celle-ci doit contenir
   * "true" ou "TRUE" pour un retour d'une boolean TRUE.
   *
   * @param value une valeur de type String
   * @return un boolean TRUE ou FALSE
   */
  public static boolean getBoolean(String value) {
    return value.equalsIgnoreCase("true");
  }

  /**
   * Convertit une valeur de type String contenant une position de label
   * en une valeur d'énumération de type "JfxLabelPosEnum".
   *
   * @param value une valeur de position de type String (ex: "bottom_left");
   * @return un objet correspondant de type JfxLabelPosEnum
   */
  public static JfxLabelPosEnum getPos(String value) {
    JfxLabelPosEnum pos = JfxLabelPosEnum.MIDDLE_CENTER;
    if (!value.isEmpty()) {
      pos = JfxLabelPosEnum.getEnum(value);
    }
    return pos;
  }

  /**
   * Convertit une valeur de type String contenant une police
   * de caractères (font) en un objet correspondant de type "Font".
   *
   * @param value une valeur de police de type String (ex: "Comic Sans MS-ITALIC-20")
   * @return un objet de type Font
   */
  public static Font getFont(String value) {
    Font font = new Font("Arial", 12);
    if (!value.isEmpty()) {
      int size = 10;
      String[] t = value.split("-");
      String name = t[0].trim();
      FontPosture posture = FontPosture.REGULAR; // Font.PLAIN;
      FontWeight weight = FontWeight.NORMAL; // Font.PLAIN;

      if (t.length >= 2) {
        t[1] = t[1].trim();
        if (t[1].equalsIgnoreCase("italic")) {
          posture = FontPosture.ITALIC; // Font.ITALIC;
        } else if (t[1].equalsIgnoreCase("bold")) {
          weight = FontWeight.BOLD; // Font.BOLD;
        }
      }
      if (t.length >= 3) {
        size = Integer.parseInt(t[2].trim());
      }
      font = Font.font(name, weight, posture, size);
    }
    return font;
  }

  /**
   * Convertit une valeur de type String contenant une valeur 
   * de couleur en un objet correspondant de type "Color".
   *
   * @param value une valeur de couleur de type String (ex: "#fafad2")
   * @return un objet de type Color
   */  
  public static Color getColor(String value) {
    Color color = Color.AQUAMARINE;
    if (!value.isEmpty()) {
      color = Color.web(value); // Color.decode(s);
    }
    return color;
  }

}
