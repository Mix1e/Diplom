package com.diplom.styleidentifier.common.handler.audio;
import com.diplom.styleidentifier.common.enums.EStyle;
import org.quifft.QuiFFT;
import org.quifft.output.FFTResult;
import org.quifft.params.WindowFunction;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AudioHelper {

    private List<AudioData> data;
    private static final int WINDOW_SIZE = 1024;
    private static final int DATA_SIZE = 640;
    private static int powerDensityCount;
    private static int frequencyCount;

    public AudioHelper(String datasetPath) throws UnsupportedAudioFileException, IOException {
        data = new LinkedList<AudioData>();
        getDataSet(new File(datasetPath), null);
    }

    public List<AudioData> getData() {
        return data;
    }

    public void setData(List<AudioData> data) {
        this.data = data;
    }

    public static int getFrequencyCount() {
        return frequencyCount;
    }

    public static int getDataSize() {
        return DATA_SIZE;
    }

    void getDataSet(File folder, List<File> fileList) throws UnsupportedAudioFileException, IOException {
        if (folder.isDirectory()) {
            File[] folderEntries = folder.listFiles();
            if (folderEntries!=null) {
                for (File file : folderEntries) {
                    if (file.isDirectory()) getDataSet(file, fileList);
                    else data.add(calculateAudioData(file.getPath()));
                }
            }
        }
    }

    private AudioData calculateAudioData(String path) throws UnsupportedAudioFileException, IOException {
        FFTResult fft = new QuiFFT(path)
                .windowFunction(WindowFunction.HAMMING)
                .windowSize(WINDOW_SIZE)
                .fullFFT();
        String fName = fft.fileName;
        powerDensityCount = (int)(fft.fileDurationMs/fft.windowDurationMs);
        frequencyCount = fft.fftFrames[0].bins.length/2;
        double[][] specDensity = new double[powerDensityCount][frequencyCount];
        int x = specDensity.length;
        for (int i = 0; i < x; i++) {
            for(int j = 0; j< frequencyCount; j++) {
                specDensity[i][frequencyCount-j-1] = Math.pow(10, fft.fftFrames[i*2].bins[j*2].amplitude/10);
            }
        }
        //saveSpectrogram(specDensity, fName);
        return new AudioData(EStyle.valueOf(fName.substring(0, fName.indexOf('.')).toUpperCase()), makeDataUnified(specDensity));
    }

    private double[][] makeDataUnified(double[][] specDensity) {
        return Arrays.copyOfRange(specDensity, 0, DATA_SIZE);
    }


    private double[][] normalize(double[][] specDensity) {
        double[] maxes = new double[powerDensityCount];
        double[] mins = new double[powerDensityCount];
        for (int i = 0; i < specDensity.length; i++) {
            maxes[i] = Arrays.stream(specDensity[i]).max().getAsDouble();
            mins[i] = Arrays.stream(specDensity[i]).min().getAsDouble();
        }
        double minD = Arrays.stream(mins).min().getAsDouble();
        double maxD = Arrays.stream(maxes).max().getAsDouble();
        for (int i = 0; i < powerDensityCount; i++){
            for (int j = 0; j < frequencyCount; j++){
                specDensity[i][j] = specDensity[i][j]/maxD;
            }
        }
        return specDensity;
    }

    private void saveSpectrogram(double[][] specDensity, String name) throws IOException {

        BufferedImage theImage = new BufferedImage(powerDensityCount, frequencyCount, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i< powerDensityCount; i++){
            for(int j = 0; j< frequencyCount; j++){
                theImage.setRGB(i, j, getColor(specDensity[i][j]).getRGB());
            }
        }
        File outputFile = new File("spec/" + name +".png");
        ImageIO.write(theImage, "png", outputFile);
    }

    private static Color getColor(double num) {
        double h = (1-num) * 90;
        return Color.getHSBColor((float)h, 1.0f, 1.0f);
    }
}
