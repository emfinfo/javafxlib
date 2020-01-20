package ch.jcsinfo.javafx.helpers;

import ch.jcsinfo.javafx.models.ExtFlowPane;
import javafx.application.HostServices;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Classe de méthodes statiques qui permettent de créer des bouts de panneau
 * dynamiquwes avec des liens.
 *
 * @author jcstritt
 */
public class JfxPopupHelper {
  private final Stage stage;
  private final int labelWidth;

  public JfxPopupHelper(Stage stage, int labelWidth) {
    this.stage = stage;
    this.labelWidth = labelWidth;
  }
  
  private Hyperlink createHyperlink(String linkText) {
    
    // crée le lien hypertexte
    Hyperlink link = new Hyperlink(linkText);
    link.setPadding(new Insets(0, 0, 0, 0));
    
    // gère le clic sur le lien HyperLink
    link.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          HostServices hostServices = (HostServices) stage.getProperties().get("hostServices");
          String url = linkText;
          if (linkText.contains("@")) {
            url = "mailto:" + linkText;
          }
          hostServices.showDocument(url);
        } catch (Throwable e) {
//          e.printStackTrace();
        }
      }
    });
    return link;
  }

  private String rightPadding(String str) {
    return String.format("%1$-" + labelWidth + "s", str);
  }
  

  
  /**
   * Génère un panneau horizonal (FlowPane) avec un seul label.
   * Cela est utile pour afficher un texte d'une classe css "description".
   *
   * @param text le texte d'un label à afficher
   * @return un panneau de type FlowPane
   */
  public FlowPane buildOneLabelPane(String text) {
    Label label = new Label(text);
    label.pseudoClassStateChanged(PseudoClass.getPseudoClass("description"), true);
    return new ExtFlowPane(label);
  }

  /**
   * Génère un panneau horizonal (FlowPane) avec deux objets Label.
   * Le premier sera un label à gauche d'une information à droite.
   *
   * @param txtLabel le texte d'un label à afficher à gauche
   * @param txtInfo le texte de l'informatiion à afficher à droite
   * @return un panneau de type FlowPane
   */
  public FlowPane buildTwoLabelsPane(String txtLabel, String txtInfo) {
    Label label1 = new Label(rightPadding(txtLabel + ":") + "\t");
    label1.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighting"), true);
    Label label2 = new Label(txtInfo);
    return new ExtFlowPane(label1, label2);    
  }

  /**
   * Génère un panneau horizonal (FlowPane) avec un objet Label à gauche
   * et un lien HyperLink à droite.
   *
   * @param txtLabel le texte du label à afficher é gauche
   * @param txtURL l'URL texte qui pewrmettra l'ouverture d'un navigateur ou d'un gestionnaire de mails
   * @return un panneau de type FlowPane
   */
  public FlowPane buildLabelAndLinkPane(String txtLabel, String txtURL) {
    Label label = new Label(rightPadding(txtLabel + ":") + "\t");
    label.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighting"), true);
    Hyperlink link = createHyperlink(txtURL);
    return new ExtFlowPane(label, link);    
  }
  
  /**
   * Crée un panneau horizontal bidon juste pour
   * faire de l'espace entre 2 panneaux.
   * 
   * @param pxBefore le nb de px à afficher avant
   * @param pxAfter le nombre de pixels à afficher après
   * @return un panneau de type FlowPane
   */
  public FlowPane buildBlankPane(int pxBefore, int pxAfter) {
    Label label = new Label("");
    label.setPadding(new Insets(pxBefore, 0, pxAfter, 0));
    return new FlowPane(label);    
  }

}
