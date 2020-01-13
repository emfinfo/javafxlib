package ch.emfinfo.helpers;

import java.net.URL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Classe pour afficher une popup avec une ProgressBar et un Label
 * affichant un message de comptage.
 *
 * @author J.-C. Stritt
 */
public class JfxProgressPopup extends Stage {
  private final ProgressBar progressbar;
  private final Label lblCount;

  /**
   * Constructeur.
   *
   * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
   * @param title un titre pour la popup
   */
  public JfxProgressPopup(Window owner, String title) {
    super();
    initOwner(owner);
    setTitle(title);
    initStyle(StageStyle.DECORATED);
    setResizable(true);
    initModality(Modality.APPLICATION_MODAL);

    // widgets
    progressbar = new ColoredProgressBar("black-bar", 0);
    progressbar.setProgress(-1f);
    progressbar.setMaxWidth(Double.MAX_VALUE);
    lblCount = new Label();

    // dans une vbox
    final VBox layout = new VBox();
    layout.setPadding(new Insets(5, 5, 5, 5));
    layout.setSpacing(5);
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(progressbar, lblCount);

    // si css trouvé
    URL url = getClass().getClassLoader().getResource("resources/css/ProgressBar.css");
    if (url != null) {
      layout.getStylesheets().add(url.toExternalForm());
      layout.getStyleClass().add("background");
    }

    // on ajoute la vbox dans un panneau simple
    StackPane root = new StackPane();
    root.getChildren().add(layout);

    // on crée une scène par l'afficher dans la fenêtre
    Scene scene = new Scene(root, 420, 80);
    setScene(scene);

    // si la fenêtre propriétaire est connue, on peut positionneer la popup par rapport à cette fenêtre
    if (owner != null) {
      Rectangle2D rect = new Rectangle2D((int)owner.getX(), (int)owner.getY(), (int)owner.getWidth(), (int)owner.getHeight());
      this.setX(rect.getMinX() + rect.getWidth() / 2 - scene.getWidth() / 2);
      this.setY(rect.getMinY() + rect.getHeight() / 2 - scene.getHeight() / 2);
    }
    show();
  }

  /**
   * Retourne une instance sur le composant "ProgressBar".
   *
   * @return l'instance du composant
   */
  public ProgressBar getProgressBar() {
    return progressbar;
  }

  /**
   * Retourne une instance sur le composant "Label".
   *
   * @return l'instance du composant
   */
  public Label getLblCount() {
    return lblCount;
  }


  /**
   * Classe privée pour disposer de ProgressBar en couleur en
   * conjonction avec du css dans "ProgressBars.css".
   */
  private class ColoredProgressBar extends ProgressBar {
    ColoredProgressBar(String styleClass, double progress) {
      super(progress);
      getStyleClass().add(styleClass);
    }
  }

}
