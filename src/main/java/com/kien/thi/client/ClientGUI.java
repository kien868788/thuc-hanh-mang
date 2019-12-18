package com.kien.thi.client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;

@Data
public class ClientGUI extends Application {
    private SendFileHandler sendFile;
    private static int index = 0;
    FileChooser fileChooser;
    DirectoryChooser directoryChooser;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("/home/kien"));
    }

    @Override
    public void start(Stage rootStage) throws Exception {
        FlowPane rootNode = new FlowPane(10, 10);
        rootNode.setAlignment(Pos.CENTER);
        Scene rootScene = new Scene(rootNode,500 ,500);
        rootStage.setTitle("Phần mềm xếp phòng thi cho cán bộ");
        rootStage.setScene(rootScene);

        Button uploadBtn = new Button("Upload");
        uploadBtn.setPrefSize(150, 150);

        Button downloadBtn = new Button("Download");
        downloadBtn.setPrefSize(150, 150);
        downloadBtn.setVisible(false);

        Label label = new Label("Dowloaded");
        label.setAlignment(Pos.BOTTOM_CENTER);
        label.setVisible(false);

        uploadBtn.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(rootStage);
            if (file != null) {
                System.out.println("Sending file");
                sendFile = new SendFileHandler();
                sendFile.sendFile(file);
                downloadBtn.setVisible(true);
                label.setVisible(false);
            }
        });

        downloadBtn.setOnAction(event -> {
            File file = directoryChooser.showDialog(rootStage);
            String path = file.getPath() + "/result-" + ++index + ".xlsx";
            rootNode.setCursor(Cursor.WAIT);
            rootNode.setDisable(true);
            sendFile.downloadFile(path);
            rootNode.setDisable(false);
            rootNode.setCursor(Cursor.DEFAULT);
            label.setVisible(true);
            downloadBtn.setVisible(false);
            System.out.println("File received");
        });

        rootNode.getChildren().addAll(uploadBtn, downloadBtn, label);
        rootStage.show();
    }

}
