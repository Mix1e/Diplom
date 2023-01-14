package com.diplom.styleidentifier;

import com.diplom.styleidentifier.common.services.NeuronetService;
import javafx.fxml.FXML;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class MainController {
    private NeuronetService neuronetService;
    @FXML
    protected void onLearnButtonClick() throws UnsupportedAudioFileException, IOException {
        neuronetService = new NeuronetService();
    }
}