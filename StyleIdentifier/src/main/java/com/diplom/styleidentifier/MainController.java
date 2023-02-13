package com.diplom.styleidentifier;

import com.diplom.styleidentifier.common.neuronet.MultiLayerPerceptron;
import com.diplom.styleidentifier.common.services.NeuronetService;
import com.diplom.styleidentifier.common.services.StorageService;
import com.diplom.styleidentifier.common.threads.LearnNeuronetThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Paths;

public class MainController {
    private NeuronetService neuronetService = new NeuronetService();

    private StorageService storageService = new StorageService();

    @FXML
    private ImageView datasetChosenImage;
    @FXML
    private TextArea logsTextArea;
    @FXML
    protected void onLearnButtonClick() {
//        if(this.storageService.isDatasetPathSpecified()) {
            Thread learnNeuronetTread = new LearnNeuronetThread(neuronetService, this.storageService.getDatasetPath(), logsTextArea);
            learnNeuronetTread.start();
        /*} else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не найден путь до датасета!");
            alert.setHeaderText("Необходимо выбрать папку с датасетом для обучения. \nВ директории программы есть стандартный датасет содержащий 10 жанров");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }*/
    }

    @FXML
    protected void onChoseDatasetButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Выберите путь к датасету");
        directoryChooser.setInitialDirectory(new File(
                Paths.get("dataset")
                        .toAbsolutePath()
                        .toUri()
        ));
        File selectedDirectory = directoryChooser.showDialog(null);

        if(selectedDirectory != null) {
            selectedDirectory.getPath();
            this.storageService.setDatasetPath(selectedDirectory.getPath());
            checkPathSelection();
        }
    }

    @FXML
    protected void onCreateNeuronetButtonClick() {
        this.neuronetService.createNeuronet(0.01);
    }

    @FXML
    protected void onSaveNetworkButtonClick() throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Сохранение сети");
        fileChooser.setInitialDirectory(new File(
                Paths.get("trained-neural-network")
                    .toAbsolutePath()
                    .toUri()
        ));
        File savingFile = fileChooser.showSaveDialog(null);

        if(savingFile != null) {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(savingFile.getPath()));
            outputStream.writeObject(this.neuronetService.getMultiLayerPerceptron());
            outputStream.close();
        }
    }

    @FXML
    protected void onLoadNeuronetButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите сеть");

        fileChooser.setInitialDirectory(new File(
                Paths.get("trained-neural-network")
                        .toAbsolutePath()
                        .toUri()
        ));
        File loadingFile = fileChooser.showOpenDialog(null);

        if(loadingFile != null) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(loadingFile.getPath()));
                this.neuronetService.setMultiLayerPerceptron((MultiLayerPerceptron) inputStream.readObject());
                inputStream.close();
            }
            catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    private void checkPathSelection() {
        if(this.datasetChosenImage != null) {
            this.datasetChosenImage.setVisible(this.storageService.isDatasetPathSpecified());
        }
    }

    public void onNetworkTabChanged() {
        checkPathSelection();
    }
    public void onLearnNetworkTabChanged() {
        checkPathSelection();
    }
    public void onUseNetworkTabChanged() {
        checkPathSelection();
    }
}