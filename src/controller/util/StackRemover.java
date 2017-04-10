package controller.util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class StackRemover {

    private Pane mPane;

    public StackRemover() {

    }

    public void setPane(final Pane mnPane) {
        mPane = mnPane;
    }

    public boolean remove(final Node node) {
        if (mPane == null)
            return false;
        return mPane.getChildren().remove(node);
    }

}
