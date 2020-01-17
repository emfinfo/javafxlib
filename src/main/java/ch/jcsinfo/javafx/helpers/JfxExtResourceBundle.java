package ch.jcsinfo.javafx.helpers;

import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Permet l'extraction de propriétés plus sophistiquées que la classe standard,
 * tels que des propriétés Font ou Color.
 *
 * @author jcstritt
 */
public class JfxExtResourceBundle {
  ResourceBundle rb;


  /**
   * Constructeur.
   *
   * @param rb le resource bundle original de JavaFX
   */
  public JfxExtResourceBundle(ResourceBundle rb) {
    this.rb = rb;
  }


  /**
   * Récupère une référence sur l'objet ResourceBundle passé initialement au constructeur.
   *
   * @return l'objet ResourceBundle de base
   */
  public ResourceBundle getResourceBundle() {
    return rb;
  }

  /**
   * Permet de remettre à jour le ResourceBundle.
   *
   * @param rb un objet ResourceBundle
   */
  public void setRb(ResourceBundle rb) {
    this.rb = rb;
  }


  /**
   * Récupère une propriété de type "texte" (String).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def texte par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public String getTextProperty(String key, String def) {
    try {
      String s = rb.getString(key);
      boolean b = s.contains("${");
      while (b) {
        int p1 = s.indexOf("${");
        int p2 = s.indexOf("}");
        String key2 = s.substring(p1 + 2, p2);
//          System.out.println("key:"+key2);
        String s2 = rb.getString(key2);
        s = s.replace("${" + key2 + "}", s2);
        b = s.contains("${");
      }
//        System.out.println("getTextProperty: key="+(propPrefix + key)+", s="+s);
      return s.isEmpty() ? def : s;
    } catch (Exception e) {
      return def;
    }
  }

  public String getTextProperty(String key) {
    return getTextProperty(key, "");
  }
  
  public String[] getTextProperties(String prefixKey, int lowIdx, int highIdx) {
    String properties[] = new String[highIdx-lowIdx+1];
    int j = 0;
    for (int i = lowIdx; i <= highIdx; i++) {
      properties[j] = getTextProperty(prefixKey + i);
      j++;
    }
    return properties;
  }


  /**
   * Récupère une propriété de type "entier" (Integer).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def entier par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public int getIntProperty(String key, int def) {
    String s = getTextProperty(key);
    try {
      return Integer.parseInt(s.trim());
    } catch (NumberFormatException e) {
      return def;
    }
  }

  public int getIntProperty(String key) {
    return getIntProperty(key, 0);
  }


  /**
   * Récupère une propriété de type "boolean".
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def entier par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public boolean getBooleanProperty(String key, boolean def) {
    String s = getTextProperty(key);
    return s.equalsIgnoreCase("true");
  }

  public boolean getBooleanProperty(String key) {
    return getBooleanProperty(key, false);
  }


  /**
   * Récupère une propriété de type "police" (Font).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def police par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public Font getFontProperty(String key, Font def) {
    String s = getTextProperty(key, "");
    if (s.isEmpty()) {
      return def;
    } else {
      int size = 10;
      String[] t = s.split("-");
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
      return Font.font(name, weight, posture, size);
    }
  }

  public Font getFontProperty(String key) {
    return getFontProperty(key, new Font("Arial", 12));
  }


  /**
   * Récupère une propriété de type "couleur" (Color).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def texte par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public Color getColorProperty(String key, Color def) {
    String s = getTextProperty(key, "");
    if (s.isEmpty()) {
      return def;
    } else {
      return Color.web(s); // Color.decode(s);
    }
  }

  public Color getColorProperty(String key) {
    return getColorProperty(key, Color.AQUAMARINE);
  }


  /**
   * Récupère la position d'un label.
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def position par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public JfxLabelPosEnum getLabelPosProperty(String key, JfxLabelPosEnum def) {
    String s = getTextProperty(key, "");
    if (s.isEmpty()) {
      return def;
    } else {
      return JfxLabelPosEnum.getEnum(s);
    }
  }

  public JfxLabelPosEnum getLabelPosProperty(String key) {
    return getLabelPosProperty(key, JfxLabelPosEnum.MIDDLE_CENTER);
  }


  /**
   * Retourne une liste de propriétés prévues pour une base données
   * gérée avec JPA (utile pour supplanter les informations présentes
   * dans le fichier interne persistence.xml). Normalement le fichier
   * propriétés supplémentaires s'appellera "db.properties". Cette liste
   * ne peut être nulle, mais peut être vide.
   *
   * @return une liste de propriétés de DB
   */
  public Properties getDbProperties() {
    final String PREFIX_KEY = "javax.persistence.jdbc."; // JPA 2.0
    final String URL_KEY = "url";
    final String USER_KEY = "user";
    final String PSW_KEY = "psw";
    Properties dbProps = new Properties();

    // url
    String url = getTextProperty(URL_KEY);
    if (!url.isEmpty()) {
      dbProps.put(PREFIX_KEY + URL_KEY, url);
    }

    // user
    String user = getTextProperty(USER_KEY);
    if (!user.isEmpty()) {
      dbProps.put(PREFIX_KEY + USER_KEY, user);
    }

    // psw
    String psw = getTextProperty(PSW_KEY);
    if (!psw.isEmpty()) {
      dbProps.put(PREFIX_KEY + PSW_KEY, psw);
    }

    return dbProps;
  }

}
