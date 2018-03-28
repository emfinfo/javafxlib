package ch.emfinfo.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe pour créer la scène principale (première vue) avec une image de fond.
 * Si on redimensionne la vue, celle-ci s'adapte au ratio largeur/hauteur de l'image de fond.
 *
 * @author jcstritt
 */
public class JfxMainScene extends Scene {

  private JfxExtLoader<?> loader;
  private Stage stage;

  private double imageRatio;
  private Timer ti = null;
  private boolean lockedWidth = false;
  private boolean lockedHeight = false;

  private Label labels[] = new Label[9];
  private int labelSizes[] = new int[9];

  private double origWidth = 0;

  private List<File> bgImages = new ArrayList<>();


  /**
   * Constructeur.
   *
   * @param stage  l'estrade, soit l'équivalent de la fenêtre principale
   * @param loader l'objet "extended loader" qui a permis de charger la vue.
   */
  public JfxMainScene(Stage stage, JfxExtLoader<?> loader) {
    super(loader.getView());

    // mémorise certains objets pour une utilisation tardive
    this.stage = stage;
    this.loader = loader;

    // remplit un tableau avec les images trouvées dans le dossier prévu à cet effet
    String bgImagePath = SettingsHelper.getValue("BG_IMAGE_PATH");
    Path dir = Paths.get(bgImagePath);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{jpg,jpeg,png}")) {
      for (Path entry : stream) {
        bgImages.add(entry.toFile());
      }
    } catch (IOException x) {
    }

    // charge une image de fond d'après l'index mémorisé dans les préférences
    Image bgImage = loadIndexedImage(SettingsHelper.getInt("BG_IMAGE_IDX", 0));

    // initialise la vue
    init(bgImage);
  }

  /**
   * Méthode privée pour charger une image.
   *
   * @param imageUrl l'URL d'une image à charger
   * @return un objet Image
   */
  private Image loadImage(String imageUrl) {
    Image bgImage = null;
    try {
      bgImage = new Image(imageUrl);
    } catch (Exception e) {
    }
    return bgImage;
  }

  /**
   * Méthode privée pour charger une image d'après son index dans la liste
   * des images trouvées dans un dossier particulier.
   *
   * @param bgImageIdx l'index de l'image à charger
   * @return une image chargée (ou pas = null)
   */
  private Image loadIndexedImage(int bgImageIdx) {

    // initialisations
    Image bgImage = null;
    imageRatio = 1.0;

    // corrige l'index s'il est trop grand et le mémorise dans les préférences
    if (bgImageIdx > (bgImages.size()-1)) {
      bgImageIdx = 0;
    }
    SettingsHelper.setInt("BG_IMAGE_IDX", bgImageIdx);

    // s'il y a des images, prend celle qui est pointée par bgImageIdx
    if (bgImages.size() > 0) {
      String bgImageUrl = "file:" + bgImages.get(bgImageIdx);
      bgImage = loadImage(bgImageUrl);
    }

    // en cas de problème, charge une image de fond par défaut depuis les resources
    if (bgImage == null || bgImage.getWidth() == 0) {
      String resourceBgImageUrl = loader.getExtResourceBundle().getTextProperty("app.background");
      bgImage = loadImage(resourceBgImageUrl);
    }

    // si l'image a été chargée
    if (bgImage != null) {
      double w = bgImage.getWidth();
      double h = bgImage.getHeight();
      imageRatio = (h > 0) ? w / h : 1.0;

      // remplit la scène avec comme décor l'image chargée
      ImagePattern pattern = new ImagePattern(bgImage);
      this.setFill(pattern);

      // correction de la largeur de la fenêtre
      // stage.setWidth(stage.getHeight() * imageRatio);

    }

    return bgImage;
  }

  /**
   * Méthode privée pour intialiser la vue principale avec l'image de fond et ses 9 labels.
   *
   * @param bgImage un objet Image pour l'image de fond
   */
  private void init(Image bgImage) {

    // si l'image a été chargée
    if (bgImage != null) {

      // ajout d'un écouteur sur les double-clics pour changer d'image
      this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
          int nb = event.getClickCount();
          if (nb == 2) {
            loadIndexedImage(SettingsHelper.getInt("BG_IMAGE_IDX", 0) + 1);
          }
          event.consume();
        }
      });

    }

    // ajoute un écouteur sur le redimensionnement de la largeur ou de la hauteur de scène
    MyNumberChangeListener widthListener = new MyNumberChangeListener(0);
    MyNumberChangeListener heightListener = new MyNumberChangeListener(1);
    this.widthProperty().addListener(widthListener);
    this.heightProperty().addListener(heightListener);

    // référence sur le layout principal
    VBox rootPane = (VBox) this.getRoot();
    rootPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.0);");

    // référence sur le gridPane en dessous du menu
    GridPane gridPane = (GridPane) rootPane.getChildren().get(1);

    // ajout de labels
    labels = new Label[9];
    int i = 0;
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        labels[i] = new Label("");
        labels[i].setAlignment(Pos.CENTER);
//        labels[i].setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        labels[i].setFont(new Font("Arial", 20));
        labels[i].setTextFill(Color.WHITE);
        GridPane.setHalignment(labels[i], HPos.CENTER);
        gridPane.add(labels[i], col, row);
        labelSizes[i] = (int) labels[i].getFont().getSize();
        i++;
      }
    }

  }

  /**
   * Remet à jour le ResourceBundle (après un changement de langue par exemple).
   *
   * @param rb un objet JfxExtResourceBundle
   */
  public void setExtResourceBundle(JfxExtResourceBundle rb) {
    loader.setExtResourceBundle(rb);
  }

  /**
   * Méthode privée pour afficher un message de label dans une des 9 positions possibles.
   *
   * @param text      le texte à afficher
   * @param position  la position où afficher le texte
   * @param textFont  la police pour l'affichage
   * @param textColor la couleur pour l'affichage
   */
  private void setLabel(String text, JfxLabelPosEnum position, Font textFont, Color textColor) {
    int i = position.getValue();
    labels[i].setText(text);
    labels[i].setFont(textFont);
    labels[i].setTextFill(textColor);
    labels[i].getStyleClass().remove("label");
    labelSizes[i] = (int) labels[i].getFont().getSize();

    // alignement par défaut pour les colonnes
    switch (position.getCol()) {
      case 1:
        GridPane.setHalignment(labels[i], HPos.CENTER);
        break;
      case 2:
        GridPane.setHalignment(labels[i], HPos.RIGHT);
        break;
      default:
        GridPane.setHalignment(labels[i], HPos.LEFT);
    }

    // alignement par défaut pour les lignes
    switch (position.getRow()) {
      case 1:
        GridPane.setValignment(labels[i], VPos.CENTER);
        break;
      case 2:
        GridPane.setValignment(labels[i], VPos.BOTTOM);
        break;
      default:
        GridPane.setValignment(labels[i], VPos.TOP);
    }

  }

  /**
   * Permet d'afficher un message déjà connu en utilisant les autres paramètres définis
   * pour ce message.
   *
   * @param text        le texte à afficher
   * @param key         la clé qui identifie les propriétés de ce texte à afficher
   * @param correctSize indique s'il faut corriger la hauteur du message
   */
  public void setLabel(String text, String key, boolean correctSize) {
    JfxExtResourceBundle rb = loader.getExtResourceBundle();
    Color color = rb.getColorProperty(key + ".color");
    Font font = rb.getFontProperty(key + ".font");
    JfxLabelPosEnum positiom = rb.getLabelPosProperty(key + ".position");
    setLabel(text, positiom, font, color);
    if (correctSize) {
      correctLabelFont();
    }
  }

  /**
   * Permet d'afficher un message identifié par une clé dans une des 9 positions possibles.
   *
   * @param key         la clé qui identifie le message à afficher depuis le fichier de resources
   * @param correctSize indique s'il faut corriger la hauteur du message
   */
  public void setLabel(String key, boolean correctSize) {
    JfxExtResourceBundle rb = loader.getExtResourceBundle();
    String msg = rb.getTextProperty(key + ".text");
    setLabel(msg, key, correctSize);
  }

  /**
   * Méthode privée pour corriger les labels d'après la largeur originelle de la fenêtre
   */
  private void correctLabelFont() {
    double r = stage.getWidth() / origWidth;
    for (int i = 0; i < labels.length; i++) {
      Font f = labels[i].getFont();
      int newSize = (int) (labelSizes[i] * r);
      labels[i].setFont(new Font(f.getName(), newSize));
    }
  }



  /**
   * Classe pour écouter les changements sur une propriété de type Number.
   */
  private class MyNumberChangeListener implements ChangeListener<Number> {

    private int id;

    public MyNumberChangeListener(int id) {
      this.id = id;
    }

    private synchronized void correctSize(double value) {
      if (ti != null) {
        ti.cancel();
      }
      ti = new Timer(false);
      ti.schedule(new TimerTask() {
        @Override
        public void run() {
          if (id == 0) {
            stage.setHeight(value);
          } else {
            stage.setWidth(value);
          }
          Timer ti2 = new Timer();
          ti2.schedule(new UnlockTask(), 500);
        }
      }, 500);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
      if (origWidth == 0 && id == 0) {
        origWidth = newValue.doubleValue();
      }
      if (oldValue.doubleValue() > 0d) {
        if (id == 0) {
          if (!lockedHeight) {
            lockedWidth = true;
            correctSize(newValue.doubleValue() / imageRatio);
          }
        } else {
          if (!lockedWidth) {
            lockedHeight = true;
            correctSize(newValue.doubleValue() * imageRatio);
          }
        }
        correctLabelFont();
      }
    }

  }



  /**
   * Classe de type TimerTask pour déverrouiller le blocage sur le redimensionnement.
   */
  private class UnlockTask extends TimerTask {

    @Override
    public void run() {
      lockedWidth = false;
      lockedHeight = false;
    }
  }

}
