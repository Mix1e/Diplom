package com.diplom.styleidentifier;

import com.diplom.styleidentifier.common.enums.EStyle;
import com.diplom.styleidentifier.common.handler.audio.AudioData;
import com.diplom.styleidentifier.common.handler.audio.AudioHelper;
import com.diplom.styleidentifier.common.neuronet.MultiLayerPerceptron;
import com.diplom.styleidentifier.common.services.NeuronetService;
import com.diplom.styleidentifier.common.services.StorageService;
import com.diplom.styleidentifier.common.threads.LearnNeuronetThread;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class MainController {
    private NeuronetService neuronetService = new NeuronetService();

    private StorageService storageService = new StorageService();

    @FXML
    private ImageView datasetChosenImage;
    @FXML
    private TextArea logsTextArea;
    @FXML
    protected void onLearnButtonClick() {
        if(this.neuronetService.getMultiLayerPerceptron() != null) {
            Thread learnNeuronetTread = new LearnNeuronetThread(neuronetService, this.storageService.getDatasetPath(), logsTextArea);
            learnNeuronetTread.start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не найдена нейронная сеть!");
            alert.setHeaderText("Необходимо создать или загрузить уже существующаю сеть.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    @FXML
    protected void onLoadSavedDatasetClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите датасет");

        fileChooser.setInitialDirectory(new File(
                Paths.get("calculated-dataset")
                        .toAbsolutePath()
                        .toUri()
        ));
        File loadingFile = fileChooser.showOpenDialog(null);

        this.neuronetService.setAudioHelper(new AudioHelper());
        if(loadingFile != null) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(loadingFile.getPath()));
                this.neuronetService.setDataset((List<AudioData>) inputStream.readObject());
                inputStream.close();

                System.out.println(this.neuronetService.getDataset().get(0).toString());
            }
            catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }
    @FXML
    protected void testClick() throws IOException, UnsupportedAudioFileException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Сохранение сети");
        fileChooser.setInitialDirectory(new File(
                Paths.get("calculated-dataset")
                        .toAbsolutePath()
                        .toUri()
        ));
        File savingFile = fileChooser.showSaveDialog(null);

        if(savingFile != null) {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(savingFile.getPath()));
            this.neuronetService.loadDataset(StorageService.DEFAULT_DATASET_PATH);
            outputStream.writeObject(this.neuronetService.getDataset());
            outputStream.close();
        }
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
        this.neuronetService.createNeuronet(0.065);
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

                this.neuronetService.getMultiLayerPerceptron().setActivation(this.neuronetService.getSigmoid());
                this.neuronetService.getMultiLayerPerceptron().setDerivative(this.neuronetService.getDsigmoid());
            }
            catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    @FXML
    protected void onRecognizeButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите запись");

        fileChooser.setInitialDirectory(new File(
                Paths.get("dataset/genres_original")
                        .toAbsolutePath()
                        .toUri()
        ));
        File audioFile = fileChooser.showOpenDialog(null);

        if(audioFile != null) {
            try {
                AudioHelper audioHelper = new AudioHelper();

                this.neuronetService.classifyAudioFile(
                        audioHelper.calculateAudioData(audioFile.getPath())
                );
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