package ch.emfinfo.helpers;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * Méthodes statiques pour récupérer les informations d'un écran.
 *
 * @author jcstritt
 */
public class JfxScreenInfo {

  /**
   * Retourne le nombre d'écrans valides.
   * 
   * @return le nombre d'écrans valides
   */
  public static int getNbrOfScreens() {
    return Screen.getScreens().size();
  }

  /**
   * Récupère le numéro de l'écran par rapport à une fenêtre affichée. Attention
   * le numéro 0 n'est pas forcément l'écran principal qui est celui dont la valeur
   * x commence à 0.
   *
   * @param w une fenêtre à tester
   * @return le numéro 0, 1, ... (ID) de l'écran
   */
  public static int getScreenID(Window w) {
    int scrID = 0;
    ObservableList<Screen> screens = Screen.getScreens();
    if (screens.size() > 0) {
      Rectangle2D rect = new Rectangle2D(w.getX(), w.getY(), w.getWidth(), w.getHeight());
      for (int i = 0; i < screens.size(); i++) {
        if (screens.get(i).getVisualBounds().contains(rect)) {
          scrID = i;
          break;
        }
      }
    }
    return scrID;
  }

  /**
   * Retourne un tableau avec la position et la taille des écrans.
   *
   * @return un tableau de Rectangle2D
   */
  public static Rectangle2D[] getScreensBounds() {
    ObservableList<Screen> screens = Screen.getScreens();
    Rectangle2D rect[] = new Rectangle2D[screens.size()];
    int i = 0;
    for (Screen screen : screens) {
      rect[i] = screen.getVisualBounds();
      i++;
    }
    return rect;
  }




  /**
   * Corrige la position et/ou la largeur/hauteur d'une fenêtre proposée en paramètre
   * d'après l'écran où elle se trouve par défaut.
   *
   * @param w uzne fenêtre de type Window
   */
  public static void correctWindowBounds(Window w) {
    int scrID = getScreenID(w);
    ObservableList<Screen> screens = Screen.getScreens();
    Rectangle2D windowBounds = new Rectangle2D(w.getX(), w.getY(), w.getWidth(), w.getHeight());
    Rectangle2D screenBounds = screens.get(scrID).getVisualBounds();
    if (windowBounds.getMinX() < screenBounds.getMinX()) {
      w.setX(screenBounds.getMinX());
    }
    if (windowBounds.getMinY() < screenBounds.getMinY()) {
      w.setY(screenBounds.getMinY());
    }
    if (windowBounds.getHeight() > screenBounds.getHeight()) {
      w.setHeight(screenBounds.getHeight());
    }
    if (windowBounds.getWidth() > screenBounds.getWidth()) {
      w.setWidth(screenBounds.getWidth());
    }
  }


  /**
   * Corrige la position d'une fenêtre proposée en paramètre
   * pour qu'elle se retrouve centrée sur l'écran proposée.
   *
   * @param w uzne fenêtre de type Window
   * @param scrID le no de l'écran où centrer la fenêtre
   */
  public static void correctWindowBounds(Window w, int scrID) {
    ObservableList<Screen> screens = Screen.getScreens();
    if (scrID < screens.size()) {
      Rectangle2D screenBounds = screens.get(scrID).getVisualBounds();
      w.setX(screenBounds.getMinX() + (screenBounds.getWidth() - w.getWidth()) / 2);
      w.setY(screenBounds.getMinY() + (screenBounds.getHeight() - w.getHeight()) / 2);
    }
  }

}
