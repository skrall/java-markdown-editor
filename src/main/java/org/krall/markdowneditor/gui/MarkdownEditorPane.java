package org.krall.markdowneditor.gui;

import javafx.collections.ListChangeListener;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownEditorPane extends StackPane {

    private static final Logger logger = LoggerFactory.getLogger(MarkdownEditorPane.class);

    private final SplitPane splitPane = new SplitPane();
    private final TextArea textArea = new TextArea();
    private final WebView webView = new WebView();
    private final PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.HARDWRAPS | Extensions.AUTOLINKS |
                                                                           Extensions.FENCED_CODE_BLOCKS);

    public MarkdownEditorPane() {
        init();
    }

    private void init() {

        splitPane.getItems().addAll(textArea, webView);

        textArea.getParagraphs().addListener(new ListChangeListener<CharSequence>() {

            public void onChanged(Change<? extends CharSequence> change) {
                StringBuilder markDownText = new StringBuilder();
                for(CharSequence charSequence : change.getList()) {
                    markDownText.append(charSequence);
                    markDownText.append("\n");
                }
                logger.info("String: {}", markDownText);
                String html = pegDownProcessor.markdownToHtml(markDownText.toString().toCharArray());
                logger.info("Html {}", html);
                webView.getEngine().loadContent(html);
            }
        });
        getChildren().add(splitPane);
    }

    public void loadFile(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        textArea.setText(new String(bytes));
    }

    public void saveFile(Path path) {

    }

}
