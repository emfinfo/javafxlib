package ch.jcsinfo.javafx.models;

import java.net.URL;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

/**
 * Extension de la classe FlowPane pour ajouter du CSS automatiquement
 * par le fichier "MyDialogs.css".
 * 
 * @author jcstritt
 */
public class ExtFlowPane extends FlowPane {
  final static String RES_PATH = "resources/css/MyDialogs.css";

  public ExtFlowPane(Node... nodes) {
    super(nodes);
    URL url = this.getClass().getClassLoader().getResource(RES_PATH);
    if (url != null) {
      this.getStylesheets().add(url.toExternalForm());
      this.getStyleClass().add("dialog-pane");
    }    
  }

  
}
