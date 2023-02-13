package com.diplom.styleidentifier.common.threads;

import com.diplom.styleidentifier.common.services.NeuronetService;
import com.diplom.styleidentifier.common.services.StorageService;
import javafx.scene.control.TextArea;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class LearnNeuronetThread extends Thread {
    private NeuronetService neuronetService;
    private String datasetPath;
    private TextArea logsTextArea;

    public LearnNeuronetThread(NeuronetService neuronetService, String datasetPath, TextArea logsTextArea) {
        this.neuronetService = neuronetService;
        this.datasetPath = datasetPath;
        this.logsTextArea = logsTextArea;
    }

    @Override
    public void run() {
        try {
            this.neuronetService.loadDataset(datasetPath == null ? StorageService.DEFAULT_DATASET_PATH : datasetPath);
            this.neuronetService.learnNeuronet(1);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
