package ch.emfinfo.helpers;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Chargement d'une vue (fichier fxml), de son contrôleur et du fichier de resources lié.
 *
 * @author jcstritt
 * @param <T> le type le contrôleur
 * @see <a href="http://xebia.developpez.com/tutoriels/java/concepts-javafx-illustration-exemple/">Tutoriel</a>
 */
public class JfxExtLoader<T> {
  private String exMessage;
  private String viewName;
  private Parent view;
  private T ctrl;
  private JfxExtResourceBundle extRb;

  /**
   * Constructeur,
   * @param viewName le nom de la vue (sans l'extension .fxml ou autre)
   */
  public JfxExtLoader (String viewName) {
    this.viewName = viewName;
    view = null;
    ctrl = null;
    extRb = null;
    loadFxml();
  }

  /**
   * Charge le fichier FXML avec son contrôleur.
   */
  private void loadFxml() {
    FXMLLoader loader;
    String fxmlFullName = "/app/ihm/" + viewName + ".fxml";
    try {
      String lang = Locale.getDefault().getLanguage();
      ResourceBundle rb = ResourceBundle.getBundle("resources/bundles/" + viewName + "_" + lang);
      loader = new FXMLLoader(getClass().getResource(fxmlFullName), rb);
      view = loader.load();
      ctrl = loader.getController();
      extRb = new JfxExtResourceBundle(rb);
      exMessage = "";
//    } catch (MissingResourceException | IllegalStateException | IOException ex) {
    } catch (IOException ex) {
      exMessage = ex.getLocalizedMessage();
    }
  }

  /**
   * Retourne vrai (true) si la dernière opération a provoqué une erreur.
   *
   * @return false ou true (si erreur)
   */
  public boolean hasError() {
    return !exMessage.isEmpty();
  }

  /**
   * Retourne une référence sur la vue.
   *
   * @return une référence sur la vue
   */
  public Parent getView() {
    return view;
  }

  /**
   * Retourne une référence sur le contrôleur.
   *
   * @return une référence sur le contrôleur
   */
  public T getCtrl() {
    return ctrl;
  }

  /**
   * Retourne une référence sur l'objet qui gère les resources actuelles.
   *
   * @return une référence sur l'objet qui gère les resources actuelles
   */
  public JfxExtResourceBundle getExtResourceBundle() {
    return extRb;
  }

  /**
   * Permet de définir quel est le paquet de resources à utiliser.
   *
   * @param rb une référence sur un paquet de resources
   */
  public void setExtResourceBundle(JfxExtResourceBundle rb) {
    this.extRb = rb;
  }

  public String getErrorTitle() {
    String errTitle = "Error";
    switch (Locale.getDefault().getLanguage()) {
      case "fr":
        errTitle = "Erreur";
        break;
      case "de":
        errTitle = "Fehler";
        break;
      case "it":
        errTitle = "Errore";
        break;
    }
    return errTitle;
  }

  /**
   * Retourne un message d'erreur en fr/de/it suivant la Locale.
   *
   * @return le message d'erreur de chargement
   */
  public String getErrorMessage() {
    String errMsg = "Laoding error with file";
    switch (Locale.getDefault().getLanguage()) {
      case "fr":
        errMsg = "Problème avec le chargement de la vue " + viewName + " !";
        break;
      case "de":
        errMsg = "Problem mit der Ansicht " + viewName + " Lade !";
        break;
      case "it":
        errMsg = "Problemi con la vista di carico " + viewName + " !";
        break;
    }
    return errMsg + "\n" + exMessage;
  }

}
