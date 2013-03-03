package org.krall.markdowneditor.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.krall.markdowneditor.gui.MarkdownEditorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkdownEditorMain extends Application {

    private static Logger logger = LoggerFactory.getLogger(MarkdownEditorMain.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Markdown Editor");

        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("Untitled");
        tab.setContent(new MarkdownEditorPane());
        tabPane.getTabs().add(tab);

        stage.setScene(new Scene(tabPane, 800, 600));
        stage.show();
    }
}
