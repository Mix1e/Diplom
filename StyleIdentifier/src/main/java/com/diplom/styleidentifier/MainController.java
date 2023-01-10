package com.diplom.styleidentifier;

import com.diplom.styleidentifier.app.enums.EStyle;
import com.diplom.styleidentifier.app.handler.audio.AudioHelper;
import com.diplom.styleidentifier.app.handler.neuronet.NeuronetService;
import com.diplom.styleidentifier.app.neuronet.MultiLayerPerceptron;
import javafx.fxml.FXML;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class MainController {
    private NeuronetService neuronetService;
    @FXML
    protected void onLearnButtonClick() throws UnsupportedAudioFileException, IOException {
        neuronetService = new NeuronetService();
    }
}