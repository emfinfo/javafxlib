package ch.emfinfo.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe correspondante.
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsHelperTest {

  @Test
  public void test01_setUserNodeName() {
    System.out.println("setUserNodename");
    String userNodeName = "JAVAFX-TEST";
    SettingsHelper.setUserNodeName(userNodeName);
    String name = SettingsHelper.getUserNodeName();
    assertTrue(name.equals(userNodeName));
  }

  @Test
  public void test02_setValue() {
    System.out.println("setValue");
    Object pref = "STRING_VALUE";
    String expResult = "Hello";
    SettingsHelper.setValue(pref, expResult);
    String result = SettingsHelper.getValue(pref);
    assertEquals(expResult, result);
  }

  @Test
  public void test03_setBoolean() {
    System.out.println("setBoolean");
    Object pref = "BOOLEAN_VALUE";
    boolean expResult = true;
    SettingsHelper.setBoolean(pref, expResult);
    boolean result = SettingsHelper.getBoolean(pref);
    assertEquals(expResult, result);
  }

  @Test
  public void test04_setInt() {
    System.out.println("setInt");
    Object pref = "INT_VALUE";
    int expResult = -10;
    SettingsHelper.setInt(pref, expResult);
    int result = SettingsHelper.getInt(pref);
    assertEquals(expResult, result);
  }

  @Test
  public void test05_setLong() {
    System.out.println("setLong");
    Object pref = "LONG_VALUE";
    long expResult = 20L;
    SettingsHelper.setLong(pref, expResult);
    long result = SettingsHelper.getLong(pref);
    assertEquals(expResult, result);
  }

  @Test
  public void test06_setFloat() {
    System.out.println("setFloat");
    Object pref = "FLOAT_VALUE";
    float value = 0.7140f;
    float expResult = 0.7140f;
    SettingsHelper.setFloat(pref, value, 4);
    float result = SettingsHelper.getFloat(pref);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void test07_setDouble() {
    System.out.println("setDouble");
    Object pref = "DOUBLE_VALUE";
    double value = Math.PI;
    double expResult = 3.1416d;
    SettingsHelper.setDouble(pref, value, 4);
    double result = SettingsHelper.getDouble(pref);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void test08_setObject() {
    System.out.println("setObject");
    Object pref = "OBJECT_VALUE";
    Classe expResult = new Classe("1i1", "Informatique");
    SettingsHelper.setObject(pref, expResult);
    Classe result = (Classe) SettingsHelper.getObject(pref);
    assertEquals(expResult.toString(), result.toString());
  }

}
