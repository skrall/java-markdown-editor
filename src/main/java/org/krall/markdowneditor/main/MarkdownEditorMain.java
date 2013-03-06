package org.krall.markdowneditor.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.krall.markdowneditor.gui.MarkdownEditorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MarkdownEditorMain extends Application {

    private static Logger logger = LoggerFactory.getLogger(MarkdownEditorMain.class);

    MarkdownEditorPane currentEditorPane = null;

    public static void main(String[] args) {
        launch(args);
    }

    public void setCurrentEditorPane(MarkdownEditorPane currentEditorPane) {
        this.currentEditorPane = currentEditorPane;
    }

    public MarkdownEditorPane getCurrentEditorPane() {
        return currentEditorPane;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Markdown Editor");

        final TabPane tabPane = new TabPane();

        ClassLoader cl = getClass().getClassLoader();

        Menu fileMenu = new Menu("File");
        // TODO - Why isn't the new MenuItem("New", "images/page_add.png"); not working...
        MenuItem newMenuItem = new MenuItem("New",
                                            new ImageView(new Image(cl.getResourceAsStream("images/page_add.png"))));
        newMenuItem.setAccelerator(KeyCombination.keyCombination("ShortCut+N"));
        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Tab newTab = new Tab();
                newTab.setText("Untitled");
                newTab.setContent(new MarkdownEditorPane());
                tabPane.getTabs().add(newTab);
            }
        });

        MenuItem openMenuItem = new MenuItem("Open",
                                             new ImageView(new Image(cl.getResourceAsStream("images/folder.png"))));
        openMenuItem.setAccelerator(KeyCombination.keyCombination("ShortCut+O"));
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                File theFile = fileChooser.showOpenDialog(stage.getOwner());
                if (theFile != null) {
                    logger.info("Opening file: {}", theFile.getName());
                    MarkdownEditorMain.this.getCurrentEditorPane().loadFile(theFile.toPath());
                }
            }
        });

        MenuItem saveMenuItem = new MenuItem("Save",
                                             new ImageView(new Image(cl.getResourceAsStream("images/page_save.png"))));
        saveMenuItem.setAccelerator(KeyCombination.keyCombination("ShortCut+S"));
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MarkdownEditorMain.this.getCurrentEditorPane().save();
            }
        });

        MenuItem saveAsMenuItem = new MenuItem("Save As",
                                               new ImageView(new Image(cl.getResourceAsStream("images/page_go.png"))));
        saveAsMenuItem.setAccelerator(KeyCombination.keyCombination("Shift+ShortCut+S"));

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem exitMenuItem = new MenuItem("Exit",
                                             new ImageView(new Image(cl.getResourceAsStream("images/door_out.png"))));
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("ShortCut+Q"));
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.trace("Exit called...");
                Platform.exit();
            }
        });

        fileMenu.getItems()
                .addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, separatorMenuItem, exitMenuItem);

        Menu editMenu = new Menu("Edit");
        MenuItem cutMenuItem = new MenuItem("Cut", new ImageView(new Image(cl.getResourceAsStream("images/cut.png"))));
        MenuItem copyMenuItem = new MenuItem("Copy",
                                             new ImageView(new Image(cl.getResourceAsStream("images/page_copy.png"))));
        MenuItem pasteMenuItem = new MenuItem("Paste", new ImageView(
                new Image(cl.getResourceAsStream("images/page_paste.png"))));
        MenuItem selectAllMenuItem = new MenuItem("Select All");
        editMenu.getItems().addAll(cutMenuItem, copyMenuItem, pasteMenuItem, selectAllMenuItem);

        Menu optionsMenu = new Menu("Options");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu);

        tabPane.getTabs().addListener(new ListChangeListener<Tab>() {
            @Override
            public void onChanged(Change<? extends Tab> change) {
                if (change.getList().size() == 1) {
                    Tab tab = change.getList().get(0);
                    tab.setClosable(false);
                    MarkdownEditorMain.this.setCurrentEditorPane((MarkdownEditorPane) tab.getContent());
                } else {
                    for (Tab tab : change.getList()) {
                        if (tab.isSelected()) {
                            MarkdownEditorMain.this.setCurrentEditorPane((MarkdownEditorPane) tab.getContent());
                        }
                        tab.setClosable(true);
                    }
                }
            }
        });
        Tab tab = new Tab();
        tab.setText("Untitled");
        tab.setContent(new MarkdownEditorPane());
        tabPane.getTabs().add(tab);

        VBox vbox = new VBox();
        vbox.getChildren().add(menuBar);
        vbox.getChildren().add(tabPane);
        stage.setScene(new Scene(vbox, 800, 600));
        stage.show();
    }
}
