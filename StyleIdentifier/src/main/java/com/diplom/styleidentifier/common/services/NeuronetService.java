package com.diplom.styleidentifier.common.services;

import com.diplom.styleidentifier.common.enums.EStyle;
import com.diplom.styleidentifier.common.handler.audio.AudioData;
import com.diplom.styleidentifier.common.handler.audio.AudioHelper;
import com.diplom.styleidentifier.common.neuronet.MultiLayerPerceptron;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class NeuronetService {
    private AudioHelper audioHelper;
    private MultiLayerPerceptron multiLayerPerceptron;
    private UnaryOperator<Double> sigmoid = x -> 1/ (1+Math.exp(-x));
    private UnaryOperator<Double> dsigmoid = y -> y * (1-y);

    public UnaryOperator<Double> getSigmoid() {
        return sigmoid;
    }

    public UnaryOperator<Double> getDsigmoid() {
        return dsigmoid;
    }

    public NeuronetService() {
    }

    public AudioHelper getAudioHelper() {
        return audioHelper;
    }

    public MultiLayerPerceptron getMultiLayerPerceptron() {
        return multiLayerPerceptron;
    }

    public void setMultiLayerPerceptron(MultiLayerPerceptron multiLayerPerceptron) {
        this.multiLayerPerceptron = multiLayerPerceptron;
    }

    public void loadDataset(String datasetPath) throws UnsupportedAudioFileException, IOException {
        audioHelper = new AudioHelper(datasetPath);
    }

    //TODO: сжать количество входных данных
    public void createNeuronet(double learnRate) {
        multiLayerPerceptron = new MultiLayerPerceptron(learnRate, sigmoid, dsigmoid, 40960, 640, 10);
    }

    public void learnNeuronet(int epochs) {
        multiLayerPerceptron.learn(audioHelper.getData(), epochs);
    }

    public void classifyAudioFile(AudioData audioData) {
        this.multiLayerPerceptron.classify(audioData);
    }
}