package ch.emfinfo.helpers;

import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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


  /**
   * Constructeur 1.
   *
   * @param view        la vue préchargée
   * @param bgImagePath le nom de l'image de fond avec son chemin
   * @param stage       l'environnement JavaFx (l'estrade)
   */
  public JfxMainScene(Parent view, String bgImagePath, Stage stage) {
    super(view);
    this.stage = stage;

    // charge l'image et définit son ratio
    Image myImage = new Image(bgImagePath);
    double w = myImage.getWidth();
    double h = myImage.getHeight();
    imageRatio = (h > 0) ? w / h : 1.0;
//    System.out.println("image: w=" + w + ", h=" + h + ", r=" + imageRatio);

    // ajoute l'image à la scène principale
    ImagePattern pattern = new ImagePattern(myImage);
    this.setFill(pattern);

    // ajoute un écouteur sur le redimensionnement de la largeur ou de la hauteur de scène
    MyNumberChangeListener widthListener = new MyNumberChangeListener(0);
    MyNumberChangeListener heightListener = new MyNumberChangeListener(1);
    this.widthProperty().addListener(widthListener);
    this.heightProperty().addListener(heightListener);

    // référence sur le layout principal
    VBox rootPane = (VBox) this.getRoot();
    rootPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.0);");

    // référence sur le gridPane en dessous du menu
    GridPane gridPane = (GridPane)rootPane.getChildren().get(1);

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
   * Constructeur 2.
   *
   * @param loader l'objet "extended loader" qui a permis de charger la vue.
   * @param stage  l'environnement JavaFx (l'estrade)
   */
  public JfxMainScene(JfxExtLoader<?> loader, Stage stage) {
    this(loader.getView(), loader.getExtResourceBundle().getTextProperty("app.background"), stage);
    this.loader = loader;
  }

  /**
   * Permet de remettre à jour le ResourceBundle après un changement de langue par exemple.
   *
   * @param rb un objet JfxExtResourceBundle
   */
  public void setExtResourceBundle(JfxExtResourceBundle rb) {
    loader.setExtResourceBundle(rb);;
  }

  /**
   * Permet d'afficher un message de label dans une des 9 positions possibles.
   *
   * @param text le texte à afficher
   * @param position la position où afficher le texte
   * @param textFont la police pour l'affichage
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
   * @param text le texte à afficher
   * @param key la clé qui identifie les propriétés de ce texte à afficher
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
   * @param key la clé qui identifie le message à afficher depuis le fichier de resources
   * @param correctSize indique s'il faut corriger la hauteur du message
   */
  public void setLabel(String key, boolean correctSize) {
    JfxExtResourceBundle rb = loader.getExtResourceBundle();
    String msg = rb.getTextProperty(key + ".text");
    setLabel(msg, key, correctSize);
  }

  /**
   * Corrige les labels d'après la largeur originelle de la fenêtre
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

    private void correctSize(double value) {
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
//      System.out.println("id: "+id+", oldValue: "+oldValue.doubleValue()+", newValue: " + newValue.doubleValue() + ", lockedHeight: "+lockedHeight+", lockedWidth: "+lockedWidth);
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
