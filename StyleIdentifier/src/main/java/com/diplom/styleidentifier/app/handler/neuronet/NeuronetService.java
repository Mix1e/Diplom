package com.diplom.styleidentifier.app.handler.neuronet;

import com.diplom.styleidentifier.app.handler.audio.AudioHelper;
import com.diplom.styleidentifier.app.neuronet.MultiLayerPerceptron;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class NeuronetService {
    private static final String DEFAULT_DATASET_PATH = "dataset\\genres_original";
    private static final double DEFAULT_LEARN_RATE = 0.1;
    private AudioHelper audioHelper;
    private MultiLayerPerceptron multiLayerPerceptron;
    private UnaryOperator<Double> sigmoid = x -> 1/ (1+Math.exp(-x));
    private UnaryOperator<Double> dsigmoid = y -> y * (1-y);

    public NeuronetService() {
    }

    public void loadDataset() throws UnsupportedAudioFileException, IOException {
        audioHelper = new AudioHelper(DEFAULT_DATASET_PATH);
    }

    public void loadDataset(String datasetPath) throws UnsupportedAudioFileException, IOException {
        audioHelper = new AudioHelper(datasetPath);
    }

    public void createNeuronet(double learnRate) {
        multiLayerPerceptron = new MultiLayerPerceptron(learnRate, sigmoid, dsigmoid, 40960, 2560, 640, 40, 10);
    }

    public void learnNeuronet(int epochs) {
        multiLayerPerceptron.learn(audioHelper.getData(), epochs);
    }
}
