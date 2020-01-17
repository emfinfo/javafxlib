package ch.jcsinfo.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.prefs.Preferences;
import javafx.geometry.Rectangle2D;

/**
 * Cette classe de méthodes statiques permet de stocker ou retrouver une préférence (paramètre) de l'application.
 * Si les méthodes sont statiques, c'est pour pouvoir lire ou mémoriser des paramètres de l'application très vite
 * après le démarrage d'une application, lorsque contrôleurs et workers ne sont pas encore disponibles.<br>
 * <br>
 * Sur Windows, cela va écrire des clés dans la base de registre sous :<br>
 * - HKEY_CURRENT_USER/Software/JavaSoft/Prefs/nom_application<br>
 * <br>
 * Sur MacOS, cela va écrire dans un fichier "com.apple.java.util.prefs.plist" sous :<br>
 * - /Users/nom_user/Library/Preferences<br>
 *
 * @author Jean-Claude Stritt
 * @version 1.0
 */
public class JfxSettingsHelper {
  private static Preferences prefs = Preferences.userRoot().node("JavaFX");



  /**
   * Récupère le nom du noeud des préférences de l'utilisateur.
   *
   * @return le nom du noeud
   */
  public static String getUserNodeName() {
    return prefs.name();
  }

  /**
   * Permet de spécifier le nom identifiant le noeud dans l'arbre des préférences de l'utilisateur.
   * Le nom abrégé de l'application est souvent utilisé pour cela.
   *
   * @param userNodeName le nom identifiant le noeud pour l'utilisateur courant
   */
  public static void setUserNodeName(String userNodeName) {
    JfxSettingsHelper.prefs = Preferences.userRoot().node(userNodeName);
  }



  /**
   * Récupère une valeur de préférence. Une valeur par défaut peut être spécifiée.
   *
   * @param pref une préférence de type String ou Enum
   * @param defValue la valeur par défaut si la préférence n'est pas trouvée
   * @return la valeur de la préférence sous la forme d'un String
   */
  public static String getValue(Object pref, String defValue) {
    return prefs.get(pref.toString(), defValue).trim();
  }

  /**
   * Récupère une valeur de préférence.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de la préférence sous la forme d'un String
   */
  public static String getValue(Object pref) {
    return prefs.get(pref.toString(), "").trim();
  }

  /**
   * Mémorise une valeur de préférence.
   *
   * @param pref  une préférence de type String ou Enum
   * @param value une valeur String à mettre à jour pour la clé donnée
   */
  public static void setValue(Object pref, String value) {
    prefs.put(pref.toString(), value.trim());
  }



  /**
   * Récupère une valeur de préférence de type "boolean".
   *
   * @param pref une préférence de type String ou Enum
   * @param defValue la valeur par défaut si la préférence n'est pas trouvée
   * @return true si la valeur de cette clé est "true"
   */
  public static boolean getBoolean(Object pref, boolean defValue) {
    String s = getValue(pref.toString(), "" + defValue);
    return s.equalsIgnoreCase("true");
  }

  /**
   * Récupère une valeur de préférence de type "boolean".
   *
   * @param pref une préférence de type String ou Enum
   * @return true si la valeur de cette clé est "true"
   */
  public static boolean getBoolean(Object pref) {
    String s = getValue(pref.toString());
    return s.equalsIgnoreCase("true");
  }

  /**
   * Mémorise une valeur de préférence de type "boolean".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur booléenne à mémoriser
   */
  public static void setBoolean(Object pref, boolean value) {
    setValue(pref, value ? "true" : "false");
  }



  /**
   * Récupère une valeur de préférence de type "int" (Integer).
   * Si la préférence ne peut être lue, la valeur par défaut spécifiée est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @param defValue une valeur par défaut
   * @return la valeur de cette préférence
   */
  public static int getInt(Object pref, int defValue) {
    return prefs.getInt(pref.toString(), defValue);
  }

  /**
   * Récupère une valeur de préférence de type "int" (Integer).
   * Si la préférence ne peut être lue, 0 est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence
   */
  public static int getInt(Object pref) {
    return prefs.getInt(pref.toString(), 0);
  }

  /**
   * Mémorise une valeur de type int (Integer).
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type Integer à mémoriser
   */
  public static void setInt(Object pref, int value) {
    setValue(pref, "" + value);
  }



  /**
   * Récupère une valeur de préférence de type "long" (Long Integer).
   * Si la préférence ne peut être lue, la valeur par défaut spécifiée est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @param defValue une valeur par défaut
   * @return la valeur de cette préférence
   */
  public static long getLong(Object pref, long defValue) {
    return prefs.getLong(pref.toString(), defValue);
  }

  /**
   * Récupère une valeur de préférence de type "long" (Long Integer).
   * Si la préférence ne peut être lue, 0 est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence
   */
  public static long getLong(Object pref) {
    return prefs.getLong(pref.toString(), 0);
  }

  /**
   * Mémorise une valeur de type long (Long Integer).
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type Long à mémoriser
   */
  public static void setLong(Object pref, long value) {
    setValue(pref, "" + value);
  }



  /**
   * Prépare un string d'une certaine longueur avec un caractère spécifié.
   * Cela permet de préparer un format genre "#.##" avec un certain nb de décimales pour
   * les méthodes setFloat et setDouble.
   *
   * @param len la longueur à obtenir
   * @param ch le caractère qui remplira la chaine
   * @return le String avec les caractères demandés
   */
  private static String fillString(int len, char ch) {
    char[] array = new char[len];
    Arrays.fill(array, ch);
    return new String(array);
  }

  /**
   * Force le séparateur "." et pas de séparateur de milliers pour les stockages
   * dans les préférences de valeurs "float" ou "double".
   *
   * @return les symboles
   */
  private static DecimalFormatSymbols getDefSymbols() {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setDecimalSeparator('.');
//    unusualSymbols.setGroupingSeparator('\'');
    return symbols;
  }



  /**
   * Récupère une valeur de préférence de type "float".
   * Si la préférence ne peut être lue, la valeur par défaut spécifiée est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @param defValue une valeur par défaut
   * @return la valeur de cette préférence
   */
  public static float getFloat(Object pref, float defValue) {
    return prefs.getFloat(pref.toString(), defValue);
  }

  /**
   * Récupère une valeur de préférence de type "float".
   * Si la préférence ne peut être lue, 0f est retourné.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette clé (nombre réel de type float)
   */
  public static float getFloat(Object pref) {
    return prefs.getFloat(pref.toString(), 0f);
  }

  /**
   * Mémorise une valeur de type "float".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type "float" à mémoriser
   * @param nbOfDecs nombre de decimales à mémoriser
   */
  public static void setFloat(Object pref, float value, int nbOfDecs) {
    String fmt = (nbOfDecs > 0) ? "0." + fillString(nbOfDecs, '0') : "0";
    setValue(pref, new DecimalFormat(fmt, getDefSymbols()).format(value));
  }



  /**
   * Récupère une valeur de préférence de type "double".
   * Si la préférence ne peut être lue, la valeur par défaut spécifiée est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @param defValue une valeur par défaut
   * @return la valeur de cette préférence
   */
  public static double getDouble(Object pref, double defValue) {
    return prefs.getDouble(pref.toString(), defValue);
  }

  /**
   * Récupère une valeur de préférence de type "double".
   * Si la préférence ne peut être lue, 0d est retourné.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence
   */
  public static double getDouble(Object pref) {
    return prefs.getDouble(pref.toString(), 0d);
  }

  /**
   * Mémorise une valeur de type "double".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type "double" à mémoriser
   * @param nbOfDecs nombre de decimales à mémoriser
   */
  public static void setDouble(Object pref, double value, int nbOfDecs) {
    String fmt = (nbOfDecs > 0) ? "0." + fillString(nbOfDecs, '0') : "0";
    setValue(pref, new DecimalFormat(fmt, getDefSymbols()).format(value));
  }



  /**
   * Récupère un objet sérialisé dans une préférence.
   *
   * @param pref une préférence de type String ou Enum
   * @return l'objet désérialisé ou null;
   */
  public static Object getObject(Object pref) {
    Object obj = null;
    byte[] bytes = prefs.getByteArray(pref.toString(), new byte[0]);
    if (bytes.length > 0) {
      try {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        obj = in.readObject();
        in.close();
      } catch (ClassNotFoundException | IOException ex) {
      }
    }
    return obj;
  }


  /**
   * Mémorise un objet quelconque dans un tableau d'octets stockés dans les préférences de l'application.
   *
   * @param pref une préférence de type String ou Enum
   * @param value un objet de type quelconque, mais qui implémente la classe Serializable
   */
  public static void setObject(Object pref, Object value) {
    setValue(pref, "");
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ObjectOutput out;
    try {
      out = new ObjectOutputStream(os);
      out.writeObject(value);
      out.flush();
      byte[] bytes = os.toByteArray();
      prefs.putByteArray(pref.toString(), bytes);
    } catch (IOException ex) {
    } finally {
      try {
        os.close();
      } catch (IOException ex) {
      }
    }
  }



  /**
   * Récupère un objet Rectangle.
   *
   * @param prefId identifie le début de la préférence
   * @return un objet Rectangle;
   */
  public static Rectangle2D getRectangle(String prefId) {
    double x = getDouble(prefId + "_X", 0d);
    double y = getDouble(prefId + "_Y", 0d);
    double w = getDouble(prefId + "_WIDTH", 0d);
    double h = getDouble(prefId + "_HEIGHT", 0d);
    return new Rectangle2D(x, y, w, h);
  }

  /**
   * Mémoriser un objet de type Rectangle.
   *
   * @param prefId identifie le début de la préférence
   * @param x la position x du rectangle
   * @param y la position y du rectangle
   * @param width la largeur du rectangle
   * @param height la hauteur du rectangle
   */
  public static void setRectangle(String prefId, double x, double y, double width, double height) {
    setDouble(prefId + "_X", x, 2);
    setDouble(prefId + "_Y", y, 2);
    setDouble(prefId + "_WIDTH", width, 2);
    setDouble(prefId + "_HEIGHT", height, 2);
  }

  /**
   * Mémoriser un objet de type Rectangle.
   *
   * @param prefId identifie le début de la préférence
   * @param rect un objet Rectangle2D
   */
  public static void setRectangle(String prefId, Rectangle2D rect) {
    setDouble(prefId + "_X", rect.getMinX(), 2);
    setDouble(prefId + "_Y", rect.getMinY(), 2);
    setDouble(prefId + "_WIDTH", rect.getWidth(), 2);
    setDouble(prefId + "_HEIGHT", rect.getHeight(), 2);
  }

}
