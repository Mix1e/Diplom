package com.diplom.styleidentifier.app.neuronet;

import com.diplom.styleidentifier.app.enums.EStyle;
import com.diplom.styleidentifier.app.handler.audio.AudioData;

import java.util.List;
import java.util.function.UnaryOperator;

public class MultiLayerPerceptron {

    private double learningRate;
    private Layer[] layers;
    private UnaryOperator<Double> activation;
    private UnaryOperator<Double> derivative;

    public MultiLayerPerceptron(double learningRate, UnaryOperator<Double> activation, UnaryOperator<Double> derivative, int... sizes) {
        this.learningRate = learningRate;
        this.activation = activation;
        this.derivative = derivative;
        layers = new Layer[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            int nextSize = 0;
            if(i < sizes.length - 1) nextSize = sizes[i + 1];
            layers[i] = new Layer(sizes[i], nextSize);
            for (int j = 0; j < sizes[i]; j++) {
                layers[i].biases[j] = Math.random();
                for (int k = 0; k < nextSize; k++) {
                    layers[i].weights[j][k] = Math.random();
                }
            }
        }
    }

    public double[] feedForward(double[] inputs) {
        System.arraycopy(inputs, 0, layers[0].neurons, 0, inputs.length);
        for (int i = 1; i < layers.length; i++)  {
            Layer l = layers[i - 1];
            Layer l1 = layers[i];
            for (int j = 0; j < l1.size; j++) {
                l1.neurons[j] = 0;
                for (int k = 0; k < l.size; k++) {
                    l1.neurons[j] += l.neurons[k] * l.weights[k][j];
                }
                l1.neurons[j] += l1.biases[j];
                l1.neurons[j] = activation.apply(l1.neurons[j]);
            }
        }
        return layers[layers.length - 1].neurons;
    }

    public void backpropagation(double[] targets) {
        double[] errors = new double[layers[layers.length - 1].size];
        for (int i = 0; i < layers[layers.length - 1].size; i++) {
            errors[i] = targets[i] - layers[layers.length - 1].neurons[i];
        }
        for (int k = layers.length - 2; k >= 0; k--) {
            Layer l = layers[k];
            Layer l1 = layers[k + 1];
            double[] errorsNext = new double[l.size];
            double[] gradients = new double[l1.size];
            for (int i = 0; i < l1.size; i++) {
                gradients[i] = errors[i] * derivative.apply(layers[k + 1].neurons[i]);
                gradients[i] *= learningRate;
            }
            double[][] deltas = new double[l1.size][l.size];
            for (int i = 0; i < l1.size; i++) {
                for (int j = 0; j < l.size; j++) {
                    deltas[i][j] = gradients[i] * l.neurons[j];
                }
            }
            for (int i = 0; i < l.size; i++) {
                errorsNext[i] = 0;
                for (int j = 0; j < l1.size; j++) {
                    errorsNext[i] += l.weights[i][j] * errors[j];
                }
            }
            errors = new double[l.size];
            System.arraycopy(errorsNext, 0, errors, 0, l.size);
            double[][] weightsNew = new double[l.weights.length][l.weights[0].length];
            for (int i = 0; i < l1.size; i++) {
                for (int j = 0; j < l.size; j++) {
                    weightsNew[j][i] = l.weights[j][i] + deltas[i][j];
                }
            }
            l.weights = weightsNew;
            for (int i = 0; i < l1.size; i++) {
                l1.biases[i] += gradients[i];
            }
        }
    }

    public void learn(List<AudioData> data, int epochs) {
        for (int i = 1; i < epochs; i++) {
            int right = 0;
            double errorSum = 0;
            for (int j = 0; j < data.size(); j++) {
                AudioData audio = data.get(j);
                double[] targets = new double[EStyle.values().length];
                targets[audio.getStyle().ordinal()] = 1;

                double[][] spec = audio.getSpectrogram();
                double[] inputs = new double[spec.length*spec[0].length/4];

                int m = 0;
                for (int k = 0; k < spec.length/4; k++) {
                    for(double n : spec[k]) {
                        inputs[m++] = n;
                    }
                }
                System.out.println(audio.getStyle());

                double[] outputs = feedForward(inputs);

                int maxDigit = 0;
                double maxDigitWeight = -1;
                for (int k = 0; k < 10; k++) {
                    if(outputs[k] > maxDigitWeight) {
                        maxDigitWeight = outputs[k];
                        maxDigit = k;
                    }
                }
                if(audio.getStyle().ordinal() == maxDigit) right++;
                for (int k = 0; k < 10; k++) {
                    errorSum += (targets[k] - outputs[k]) * (targets[k] - outputs[k]);
                }
                backpropagation(targets);
            }
            System.out.println("epoch: " + i + ". correct: " + right + ". error: " + errorSum);
        }

    }
}
