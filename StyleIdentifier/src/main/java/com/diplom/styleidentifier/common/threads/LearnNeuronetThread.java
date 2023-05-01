package com.diplom.styleidentifier.common.threads;

import com.diplom.styleidentifier.common.services.NeuronetService;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LearnNeuronetThread extends Thread {
    private NeuronetService neuronetService;
    private String datasetPath;
    private TextArea logsTextArea;
    private int epochsCount;
    private double errorPercent;
    private boolean isEpochs;

    public LearnNeuronetThread(NeuronetService neuronetService, String datasetPath, TextArea logsTextArea, int epochsCount) {
        this.neuronetService = neuronetService;
        this.datasetPath = datasetPath;
        this.logsTextArea = logsTextArea;
        this.epochsCount = epochsCount;
        this.isEpochs = true;
    }

    public LearnNeuronetThread(NeuronetService neuronetService, String datasetPath, TextArea logsTextArea, double errorPercent) {
        this.neuronetService = neuronetService;
        this.datasetPath = datasetPath;
        this.logsTextArea = logsTextArea;
        this.errorPercent = errorPercent;
        this.isEpochs = false;
    }

    @Override
    public void run() {
        try {
            //this.neuronetService.loadDataset(datasetPath == null ? StorageService.DEFAULT_DATASET_PATH : datasetPath);
            if (isEpochs) {
                this.neuronetService.learnNeuronet(epochsCount);
            } else {
                this.neuronetService.learnNeuronet(errorPercent);
            }
            String newNote = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + "Сеть успешно обучена" + "\n";
            this.logsTextArea.setText(this.logsTextArea.getText() + newNote);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait().ifPresent(rs -> {

            });
        }
    }
}
