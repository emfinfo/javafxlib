package ch.emfinfo.helpers;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

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
    this.view = null;
    this.ctrl = null;
    this.extRb = null;
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
   * Retourne le nom de la vue.
   *
   * @return le nom de la vue
   */
  public String getViewName() {
    return viewName;
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
      default :
        errTitle = "Error";
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
      default :
        errMsg = "Problem when loading the view " + viewName + " !";
    }
    return errMsg + "\n" + exMessage;
  }


  /**
   * Permet d'afficher une sous-vue déjà chargée vers un stage (fenêtre) "enfant". Le premier conteneur
   * doit être un BorderPane. La méthode recherche dans le paquet de resources les clés
   * "login" et "logo" pour les attribuer à la barre de titre.
   *
   * @param owner le propriétaire de la sous-vue.
   * @param myFunc une fonction à utiliser lorsque l'on quitte la fenêtre
   */
  public void displayView(Window owner, Callable<Void> myFunc) {

    // nom transformé en majuscules
    String uViewName = getViewName().toUpperCase();

    // teste si la vue a été chargée
    if (hasError()) {
      JfxPopup.displayError(owner, getErrorTitle(), null, getErrorMessage());
    } else {
      BorderPane childRootpane = (BorderPane) getView();
      Scene newScene = new Scene(childRootpane);

      // définir une nouvelle fenêtre enfant de la première
      Stage childStage = new Stage();
      childStage.initOwner(owner);
      childStage.initStyle(StageStyle.UTILITY);
      childStage.initModality(Modality.WINDOW_MODAL);
      childStage.setScene(newScene);

      // appliquer les dimensions minimales
      childStage.setMinWidth(SettingsHelper.getDouble(uViewName+"_MIN_WIDTH"));
      childStage.setMinHeight(SettingsHelper.getDouble(uViewName+"_MIN_HEIGHT"));

      // choisir un titre pour la fenêtre
      childStage.setTitle(extRb.getTextProperty("title"));

      // rajouter une icône dans la barre de titre
      String logoPath = extRb.getTextProperty("logo").trim();

      if (!logoPath.isEmpty()) {
        File file = new File(logoPath);
        if (file.exists()) {
          childStage.getIcons().add(new Image(extRb.getTextProperty("logo")));
        }
      }

      // repositionne et redimentionne la fenêtre si les données ont été mémorisées
      Rectangle2D mainRect = new Rectangle2D(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());
      Rectangle2D childRect = SettingsHelper.getRectangle(uViewName);
      if (childRect.getWidth() > 0d && childRect.getHeight() > 0d) {
        childStage.setX(mainRect.getMinX() + mainRect.getWidth() / 2 - childRect.getWidth() / 2);
        childStage.setY(mainRect.getMinY() + mainRect.getHeight() / 2 - childRect.getHeight() / 2);
        childStage.setWidth(childRect.getWidth());
        childStage.setHeight(childRect.getHeight());
      }

      // ajouter un écouteur pour contrôler la sortie
      childStage.setOnCloseRequest(e -> {
        e.consume();
        try {
          myFunc.call();
        } catch (Exception ex) {
        }
      });

      // ajouter un autre écouteur pour une sortie avec la touche ESC
      childStage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
        if (e.getCode() == KeyCode.ESCAPE) {
          e.consume();
          try {
            myFunc.call();
          } catch (Exception ex) {
          }
        }
      });

      // afficher cette nouvelle vue "enfant"
      childStage.show();
    }

  }

}
