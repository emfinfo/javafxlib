package ch.jcsinfo.javafx.helpers;

import java.util.ResourceBundle;

/**
 * Permet l'extraction facilitée de propriétés dans un ResourceBundle,
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
  public void setResourceBundle(ResourceBundle rb) {
    this.rb = rb;
  }


  /**
   * Grâce à sa clé, récupère une propriété de type "texte" (String).
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
        String s2 = rb.getString(key2);
        s = s.replace("${" + key2 + "}", s2);
        b = s.contains("${");
      }
      return s.isEmpty() ? def : s;
    } catch (Exception e) {
      return def;
    }
  }

  /**
   * Grâce à sa clé, récupère une propriété de type "texte" (String).
   * 
   * @param key nom de la clé de recherche de la propriété
   * @return la propriété recherchée
   */
  public String getTextProperty(String key) {
    return getTextProperty(key, "");
  }
  
  /**
   * Grâce à un préfixe de clé, récupère un tableau de messages dans les propriétés.
   * 
   * @param prefixKey une préfixe de clé, par exemple "popup.about.label"
   * @param lowIdx l'index le plus bas à récupérer
   * @param highIdx l'index le plus haut à récupérer
   * @return  un tableau de propriétés récupérées dans le ResourceBundle enregistré
   */
  public String[] getTextProperties(String prefixKey, int lowIdx, int highIdx) {
    String properties[] = new String[highIdx-lowIdx+1];
    int j = 0;
    for (int i = lowIdx; i <= highIdx; i++) {
      String p = (highIdx >= 10 && i < 10) ? "0"+i : ""+i;      
      properties[j] = getTextProperty(prefixKey + p);
      j++;
    }
    return properties;
  }

}
