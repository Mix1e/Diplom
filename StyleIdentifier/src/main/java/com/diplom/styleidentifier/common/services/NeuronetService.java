package com.diplom.styleidentifier.common.services;

import com.diplom.styleidentifier.common.enums.EStyle;
import com.diplom.styleidentifier.common.handler.audio.AudioData;
import com.diplom.styleidentifier.common.handler.audio.AudioHelper;
import com.diplom.styleidentifier.common.models.NeuronetLearnResult;
import com.diplom.styleidentifier.common.neuronet.MultiLayerPerceptron;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.UnaryOperator;

public class NeuronetService {
    public static final String DEFAULT_DATASET_PATH = "dataset\\genres_original";
    private AudioHelper audioHelper;
    private MultiLayerPerceptron multiLayerPerceptron;
    private String datasetPath;
    private UnaryOperator<Double> sigmoid = x -> 1/ (1+Math.exp(-x));
    private UnaryOperator<Double> dsigmoid = y -> y * (1-y);


    public NeuronetService() {
    }


    public UnaryOperator<Double> getSigmoid() {
        return sigmoid;
    }

    public UnaryOperator<Double> getDsigmoid() {
        return dsigmoid;
    }

    public String getDatasetPath() {
        return datasetPath;
    }

    public void setDatasetPath(String datasetPath) {
        this.datasetPath = datasetPath;
    }

    public boolean isDatasetPathSpecified () {
        return datasetPath != null;
    }

    public MultiLayerPerceptron getMultiLayerPerceptron() {
        return multiLayerPerceptron;
    }

    public void setMultiLayerPerceptron(MultiLayerPerceptron multiLayerPerceptron) {
        this.multiLayerPerceptron = multiLayerPerceptron;
    }

    public List<AudioData> getDataset() {
        return audioHelper.getData();
    }

    public void loadSavedDataset(List<AudioData> data) {
        audioHelper = new AudioHelper(data);
    }

    public void loadDataset(String datasetPath) throws UnsupportedAudioFileException, IOException {
        audioHelper = new AudioHelper(datasetPath);
    }

    public void createNeuronet(double learnRate, int hiddenLayerNeurons) {
        multiLayerPerceptron = new MultiLayerPerceptron(learnRate, sigmoid, dsigmoid, AudioData.INPUT_NEURON_COUNT, hiddenLayerNeurons, AudioHelper.STYLES_COUNT);
    }

    public void learnNeuronet(int epochs) {
        learnByEpochs(audioHelper.getData(), epochs);
    }

    public void learnNeuronet(double errorPercent) {
        learnUntilError(audioHelper.getData(), errorPercent);
    }

    public void classifyAudioFile(AudioData audioData) {
        classify(audioData);
    }

    public void testByRandomAudio(int count) throws IOException {
        audioHelper = new AudioHelper();
        audioHelper.getRandomAudioData(DEFAULT_DATASET_PATH, count).stream().forEach(
                data -> classify(data)
        );
    }

    public void classifyRandomAudioEveryStyle() throws IOException {
        audioHelper = new AudioHelper();
        audioHelper.getRandomAudioEveryStyle(DEFAULT_DATASET_PATH).stream().forEach(
                data -> classify(data)
        );
    }

    public void showClassifyResult(double[] outputs, EStyle right) {
        Stage stage = new Stage();

        stage.setTitle("Гистограмма распознанных жанров");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);

        bc.setTitle(right == null ? "Исходный жанр не задан" : ("Исходный жанр: " + right));
        xAxis.setLabel("Жанры");
        yAxis.setLabel("Процент");

        XYChart.Series series = new XYChart.Series();
        series.setName("Процент распознавания");
        for (int i = 0; i < AudioHelper.STYLES_COUNT; i++) {
            series.getData().add(new XYChart.Data(EStyle.values()[i].toString(), outputs[i] * 100));
        }

        Scene scene  = new Scene(bc,800,600);
        bc.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }

    public void classify(AudioData audio) {
        double[] inputs = MultiLayerPerceptron.inputsFromAudioData(audio);

        double[] outputs = multiLayerPerceptron.feedForward(inputs);
        int maxAt = 0;

        for(int i = 0; i < AudioHelper.STYLES_COUNT; i++) {
            System.out.println(EStyle.values()[i] +" = " + outputs[i]);
            maxAt = outputs[i] > outputs[maxAt] ? i : maxAt;
        }

        showClassifyResult(outputs, audio.getStyle());
        System.out.println("Guess it: " + EStyle.values()[maxAt]);
    }

    public void learnByEpochs(List<AudioData> data, int epochs) {
        for (int i = 0; i < epochs; i++) {
            NeuronetLearnResult learnResult = multiLayerPerceptron.learn(data);
            visualizeLearningResults(i, learnResult);
        }
    }

    public void learnUntilError(List<AudioData> data, double errorPercent) {
        int epochs = 0;

        double currentError = 1;
        while (errorPercent < currentError) {
            NeuronetLearnResult learnResult = this.multiLayerPerceptron.learn(data);
            currentError = (1 - Double.valueOf(learnResult.getRight())/Double.valueOf(data.size()));

            visualizeLearningResults(++epochs, learnResult);
        }

        String result = MessageFormat.format(
                "Заданный коээфициент в {0}% получен, для этого потребовалось {1} эпох обучения.",
                Double.valueOf(errorPercent * 100),
                epochs
        );
        System.out.println(result);
    }

    private void visualizeLearningResults(int number, NeuronetLearnResult learnResult) {
        System.out.println("epoch: " + (number + 1) + ". correct: " + learnResult.getRight() + ". error: " + learnResult.getErrorSum());
    }
}