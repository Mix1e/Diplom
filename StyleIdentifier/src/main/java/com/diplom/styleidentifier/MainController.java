package com.diplom.styleidentifier;

import com.diplom.styleidentifier.common.handler.audio.AudioData;
import com.diplom.styleidentifier.common.handler.audio.AudioHelper;
import com.diplom.styleidentifier.common.neuronet.MultiLayerPerceptron;
import com.diplom.styleidentifier.common.services.NeuronetService;
import com.diplom.styleidentifier.common.threads.LearnNeuronetThread;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;

public class MainController {
    private NeuronetService neuronetService = new NeuronetService();

    @FXML
    private ImageView datasetChosenImage;
    @FXML
    private TextArea logsTextArea;
    @FXML
    private TextField learnRateTextField;
    @FXML
    private TextField hiddenLayerNeuronsTestField;
    @FXML
    private TextField epochsCountTextField;
    @FXML
    private Toggle learnUntilToggle;
    @FXML
    private TextField errorSumValueTextField;


    private void showAlert(Alert.AlertType type, String title, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait().ifPresent(rs -> {
        });
    }

    //Tab1
    @FXML
    protected void onCreateNeuronetButtonClick() {
        try {
            double learnRate = Double.parseDouble(learnRateTextField.getText());
            int hiddenLayerNeurons = Integer.parseInt(hiddenLayerNeuronsTestField.getText());
            this.neuronetService.createNeuronet(learnRate, hiddenLayerNeurons);
        }
        catch (Exception ex) {
            this.showAlert(
                    Alert.AlertType.ERROR,
                    ex.getMessage(),
                    "Проверьте входные данные"
            );
        }
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

    //TAB2
    @FXML
    protected void onLearnButtonClick() {
        if(this.neuronetService.getMultiLayerPerceptron() != null) {
            try {
                Thread learnNeuronetThread = null;
                if(learnUntilToggle.isSelected()) {
                    double errorPercent = Double.parseDouble(errorSumValueTextField.getText())/100;
                    learnNeuronetThread = new LearnNeuronetThread(neuronetService, this.neuronetService.getDatasetPath(), logsTextArea, errorPercent);
                } else {
                    int epochsCount = Integer.parseInt(epochsCountTextField.getText());
                    learnNeuronetThread = new LearnNeuronetThread(neuronetService, this.neuronetService.getDatasetPath(), logsTextArea, epochsCount);
                }
                learnNeuronetThread.start();
            }
            catch (Exception ex) {
                this.showAlert(
                        Alert.AlertType.ERROR,
                        "Ошибка",
                        ex.getMessage()
                );
            }
        } else {
            this.showAlert(
                    Alert.AlertType.ERROR,
                    "Не найдена нейронная сеть!",
                    "Необходимо создать или загрузить уже существующаю сеть."
            );
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

        if(loadingFile != null) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(loadingFile.getPath()));
                this.neuronetService.loadSavedDataset((List<AudioData>) inputStream.readObject());
                inputStream.close();
            }
            catch (Exception ex) {
                this.showAlert(
                        Alert.AlertType.ERROR,
                        "Ошибка",
                        ex.getMessage()
                );
            }
        }
    }
    @FXML
    protected void onSaveDatasetClick() throws IOException, UnsupportedAudioFileException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Сохранение датасета");
        fileChooser.setInitialDirectory(new File(
                Paths.get("calculated-dataset")
                        .toAbsolutePath()
                        .toUri()
        ));
        File savingFile = fileChooser.showSaveDialog(null);

        if(savingFile != null) {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(savingFile.getPath()));
            this.neuronetService.loadDataset(NeuronetService.DEFAULT_DATASET_PATH);
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
            this.neuronetService.setDatasetPath(selectedDirectory.getPath());
            checkPathSelection();
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
                this.showAlert(
                        Alert.AlertType.ERROR,
                        "Ошибка",
                        ex.getMessage()
                );
            }
        }
    }

    @FXML
    protected void onLearnUntilToggleClick() {
        errorSumValueTextField.setDisable(!learnUntilToggle.isSelected());
        epochsCountTextField.setDisable(learnUntilToggle.isSelected());
    }

    //TAB3
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

                System.out.println(audioFile.getPath());
                this.neuronetService.classifyAudioFile(
                        audioHelper.calculateUserAudio(audioFile.getPath())
                );
            }
            catch (Exception ex) {
                this.showAlert(
                        Alert.AlertType.ERROR,
                        "Ошибка",
                        ex.getMessage()
                );
            }
        }
    }

    private void checkPathSelection() {
        if(this.datasetChosenImage != null) {
            this.datasetChosenImage.setVisible(this.neuronetService.isDatasetPathSpecified());
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