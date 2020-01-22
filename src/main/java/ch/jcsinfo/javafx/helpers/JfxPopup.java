package ch.jcsinfo.javafx.helpers;

import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Affichage de popups.
 *
 * @author jcstritt
 */
public class JfxPopup {

  /**
   * Méthode privée. Prépare une boite de dialogue pour l'affichage.
   * Utilise du CSS dans "/resources/css/MyDialogs.css" pour personnaliser la chose.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param dlg une boite de dialogue préalablement créée
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param content normalement un message ou un autre panneau
   */
  private static void decorate(Window owner, Dialog<?> dlg, String title, String header, Object content) {
    String RES_PATH = "resources/css/MyDialogs.css";
    dlg.initOwner(owner);
    dlg.setTitle("- " + title);
    dlg.setHeaderText(header);
    
    // teste si c'est un simple texte à afficher ou un contenu plus consistent
    if (content instanceof Pane) {
      dlg.getDialogPane().setContent((Pane)content);
    } else {
      dlg.setContentText((String)content);
    }
    
    // ajuste la taille préférée et ajoute des classes de styles CSS
    Pane pane = dlg.getDialogPane();
    pane.setMinHeight(Region.USE_PREF_SIZE);
    URL url = JfxPopup.class.getClassLoader().getResource(RES_PATH);
    if (url != null) {
      pane.getStylesheets().add(url.toExternalForm());
      pane.getStyleClass().add("dialog-pane");
    }    
    
    // ajoute un logo à gauche du titre
    String iconPath = JfxSettings.getValue("LOGO");
    if (!iconPath.isEmpty()) {
      Stage stage = (Stage)pane.getScene().getWindow();
      stage.getIcons().add(new Image(iconPath));  
    }    
  }  

  /**
   * Affiche une popup de type "error" centrée sur son propriétaire.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   */
  public static void displayError(Window owner, String title, String header, String msg) {
    Alert alert = new Alert(AlertType.ERROR);
    decorate(owner, alert, title, header, msg);
    alert.showAndWait();
  }

  /**
   * Affiche une popup de type "error" centrée sur l'écran.
   *
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   */
  public static void displayError(String title, String header, String msg) {
    displayError(null, title, header, msg);
  }



  /**
   * Affiche une popup de type "information" centrée sur son propriétaire.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   */
  public static void displayInformation(Window owner, String title, String header, String msg) {
    Alert alert = new Alert(AlertType.INFORMATION);
    decorate(owner, alert, title, header, msg);
    alert.showAndWait();
  }
  
  public static void displayInformation(Window owner, String title, String header, VBox vbox) {
    Alert alert = new Alert(AlertType.INFORMATION);
    decorate(owner, alert, title, header, vbox);
    alert.showAndWait();
  }  

  /**
   * Affiche une popup de type "information" centrée sur l'écran.
   *
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   */
  public static void displayInformation(String title, String header, String msg) {
    displayInformation(null, title, header, msg);
  }


  
  /**
   * Affiche une popup de type "avertissement" centrée sur son propriétaire.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   */
  public static void displayWarning(Window owner, String title, String header, String msg) {
    Alert alert = new Alert(AlertType.WARNING);
    decorate(owner, alert, title, header, msg);
    alert.showAndWait();
  }

  /**
   * Affiche une popup de type "avertissement" centrée sur l'écran.
   *
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   */
  public static void displayWarning(String title, String header, String msg) {
    displayWarning(null, title, header, msg);
  }



  /**
   * Affiche une popup de type "confirmation" (avec OK ou Annuler)
   * centrée sur son propriétaire.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   *
   * @return true si OK a été sélectionné
   */
  public static boolean askConfirmation(Window owner, String title, String header, String msg) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    decorate(owner, alert, title, header, msg);
    Optional<ButtonType> rep = alert.showAndWait();
    return rep.get() == ButtonType.OK;
  }

  /**
   * Affiche une popup de type "confirmation" (avec OK ou Annuler)
   * centrée sur l'écran.
   *
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   *
   * @return true si OK a été sélectionné
   */
  public static boolean askConfirmation(String title, String header, String msg) {
    return askConfirmation(null, title, header, msg);
  }



  /**
   * Affiche une popup de type "confirmation" (avec OUI/NON)
   * centrée sur son propriétaire.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   * @param defaultNo true le bouton par défaut est le "non"
   *
   * @return true si OUI a été sélectionné
   */
  public static boolean askYesNo(Window owner, String title, String header, String msg, boolean defaultNo) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    decorate(owner, alert, title, header, msg);

    // messages dans les boutons suivant la "Locale" par défaut
    String msgYes = "Yes";
    String msgNo = "No";
    switch (Locale.getDefault().getLanguage()) {
      case "fr":
        msgYes = "Oui";
        msgNo = "No";
        break;
      case "de":
        msgYes = "Ja";
        msgNo = "Nein";
        break;
      case "it":
        msgYes = "Si";
        msgNo = "No";
        break;
    }

    // création des boutons
    ButtonType btnYes = new ButtonType(msgYes);
    ButtonType btnNo = new ButtonType(msgNo);
    alert.getButtonTypes().setAll(btnYes, btnNo);

    // si le bouton NON est choisi par défaut
    if (defaultNo) {
      // déactive le bouton OUI par défaut
      Button yesButton = (Button) alert.getDialogPane().lookupButton(btnYes);
      yesButton.setDefaultButton(false);

      // active le bouton NON par défaut
      Button noButton = (Button) alert.getDialogPane().lookupButton(btnNo);
      noButton.setDefaultButton(true);
    }

    // affiche la popup et attent la réponse
    Optional<ButtonType> rep = alert.showAndWait();
    return rep.get() == btnYes;
  }

  /**
   * Affiche une popup de type "confirmation" (avec OUI/NON)
   * centrée sur l'écran.
   *
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   * @param defaultNo true le bouton par défaut est le "non"
   *
   * @return true si OUI a été sélectionné
   */
  public static boolean askYesNo(String title, String header, String msg, boolean defaultNo) {
    return askYesNo(null, title, header, msg, defaultNo);
  }



  /**
   * Affiche une popup de type "demande d'info" centrée sur son propriétaire.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   *
   * @return une valeur de type string or null
   */
  public static String askInfo(Window owner, String title, String header, String msg) {
    TextInputDialog textInputDialog = new TextInputDialog();
    decorate(owner, textInputDialog, title, header, msg);
    Optional<String> text = textInputDialog.showAndWait();
    return text.isPresent() ? text.get() : null;
  }

  /**
   * Affiche une popup de type "demande d'info" centrée sur l'écran.
   *
   * @param title un titre pour la popup
   * @param header un entête (si null = pas d'entête)
   * @param msg normalement le message principal
   *
   * @return une valeur de type string or null
   */
  public static String askInfo(String title, String header, String msg) {
    return askInfo(null, title, header, msg);
  }

}
