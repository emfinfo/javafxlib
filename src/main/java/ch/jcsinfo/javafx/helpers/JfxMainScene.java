package ch.jcsinfo.javafx.helpers;

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
    String bgImagePath = JfxSettings.getValue("BG_IMAGE_PATH");
    Path dir = Paths.get(bgImagePath);
    Image bgImage;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{jpg,jpeg,png,JPG,JPEG,PNG}")) {
      for (Path entry : stream) {
        bgImages.add(entry.toFile());
      }
      bgImage = loadIndexedImage(JfxSettings.getInt("BG_IMAGE_IDX"));
      init(bgImage, true);
    } catch (IOException ex) {
      bgImage = loadIndexedImage(0);
      init(bgImage, false);
    }
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
    JfxSettings.setInt("BG_IMAGE_IDX", bgImageIdx);

    // s'il y a des images, prend celle qui est pointée par bgImageIdx
    if (bgImages.size() > 0) {
      String bgImageUrl = "file:" + bgImages.get(bgImageIdx);
      bgImage = loadImage(bgImageUrl);
    }

    // en cas de problème, charge une image de fond par défaut depuis les resources
    if (bgImage == null || bgImage.getWidth() == 0) {
      String resourceBgImageUrl = JfxSettings.getValue("BG_IMAGE_RESOURCE");
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
  private void init(Image bgImage,boolean addListener) {

    // si l'image a été chargée
    if (bgImage != null && addListener) {

      // ajout d'un écouteur sur les double-clics pour changer d'image
      this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

        // gère le double-clic
        @Override
        public void handle(MouseEvent event) {
          int nb = event.getClickCount();
          if (nb == 2) {
            loadIndexedImage(JfxSettings.getInt("BG_IMAGE_IDX") + 1);
          }
          event.consume();
        }
      });
    }

    // ajoute un écouteur sur le redimensionnement de la scène
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
//  public void setExtResourceBundle(JfxExtResourceBundle rb) {
//    loader.setExtResourceBundle(rb);
//  }
  
  
  
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
   * Méthode privée pour afficher un message de label dans l'une 
   * des 9 positions possibles.
   *
   * @param lblText  le texte à afficher
   * @param lblPos   la position où afficher le texte
   * @param lblFont  la police pour l'affichage
   * @param lblColor la couleur pour l'affichage
   */
  private void setLabel(String lblText, JfxLabelPosEnum lblPos, Font lblFont, Color lblColor) {
    int i = lblPos.getIdx();
    labels[i].setText(lblText);
    labels[i].setFont(lblFont);
    labels[i].setTextFill(lblColor);
    labels[i].getStyleClass().remove("label");
    labelSizes[i] = (int) labels[i].getFont().getSize();

    // alignement par défaut pour les colonnes
    switch (lblPos.getCol()) {
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
    switch (lblPos.getRow()) {
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
   * Ajuste un message de label dans l'une des 9 positions prévues. Le message 
   * doit être identifié par son index dans la clé "BG_MSGx_TEXT" (x=1,2 ou 3).
   * 
   * @param idx l'index du message à récupérer (1 à 3 normalement)
   * @param text le texte à afficher 
   * @param options éventuellement un boolean TRUE pour recorriger la taille du label
   */
  public void setLabel(int idx, String text, boolean... options) {
    JfxLabelPosEnum position = JfxSettings.getPosition(idx);
    Font font = JfxSettings.getFont(idx);
    Color color = JfxSettings.getColor(idx);
    setLabel(text, position, font, color);
    if ((options.length > 0) && (options[0] == true)) {
      correctLabelFont();
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
