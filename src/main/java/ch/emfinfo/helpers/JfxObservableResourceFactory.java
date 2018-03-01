package ch.emfinfo.helpers;

import java.util.ResourceBundle;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Fabrique à resources pour permettre le changement de langue des resources à la volée.
 *
 * @author jcstritt
 * @see <a href="https://stackoverflow.com/questions/32464974/javafx-change-application-language-on-the-run">Tutoriel</a>
 */
public class JfxObservableResourceFactory {
  private ObjectProperty<ResourceBundle> objProperty; // = new SimpleObjectProperty<>();

  /**
   * Constructeur.
   */
  public JfxObservableResourceFactory() {
    objProperty = new SimpleObjectProperty<>();
  }

  public ObjectProperty<ResourceBundle> resourcesProperty() {
    return objProperty;
  }

  /**
   * Retourne une référence sur le paquet de resources lié.
   * @return le paquet de resources lié
   */
  public final ResourceBundle getResourcesBundle() {
    return resourcesProperty().get();
  }

  /**
   * Définit le paquet de resources à utiliser,
   *
   * @param rb une référence sur le paquet de resources à utiliser
   */
  public final void setResourcesBundle(ResourceBundle rb) {
    resourcesProperty().set(rb);
  }

  /**
   * Retourne un objet de liaison pour une clé donnée.
   *
   * @param key la clé de recherche d'une resource
   * @return l'objet de liaison de type StringBinding
   */
  public StringBinding getStringBinding(String key) {
    return new StringBinding() {
      {
        bind(resourcesProperty());
      }

      @Override
      public String computeValue() {
        return getResourcesBundle().getString(key);
      }
    };
  }
}
