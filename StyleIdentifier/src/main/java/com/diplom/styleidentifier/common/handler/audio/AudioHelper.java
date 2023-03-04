package com.diplom.styleidentifier.common.handler.audio;
import com.diplom.styleidentifier.common.enums.EStyle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.quifft.QuiFFT;
import org.quifft.output.FFTResult;
import org.quifft.params.WindowFunction;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AudioHelper {

    private List<AudioData> data;
    private final String SCRIPT_START = "C:\\diplom\\AudioTransformator\\venv\\Scripts\\python.exe C:\\diplom\\AudioTransformator\\main.py ";

    private static final int WINDOW_SIZE = 1024;
    private static final int DATA_SIZE = 640;
    private static int powerDensityCount;
    private static int frequencyCount;

    public AudioHelper() {
    }

    public AudioHelper(String datasetPath) throws UnsupportedAudioFileException, IOException {
        data = new LinkedList<AudioData>();
        getDataSet(new File(datasetPath), null);
        Collections.shuffle(this.data);
    }

    public List<AudioData> getData() {
        return data;
    }
    public void setData(List<AudioData> data) {
        this.data = data;
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

    public AudioData calculateAudioData(String path) throws IOException {
        Process p = Runtime.getRuntime().exec(SCRIPT_START + path);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String s, out = "";
        while ((s = stdInput.readLine()) != null) {
            out += s;
        }
        stdInput.close();
        p.destroy();

        System.out.println(out);

        JsonObject jsonResponse = new Gson().fromJson(out, JsonObject.class);
        AudioData audioData = new AudioData(
                EStyle.valueOf(jsonResponse.get("style").getAsString()),
                jsonResponse.get("bpm").getAsDouble(),
                jsonResponse.get("amplitude_difference").getAsDouble(),
                jsonResponse.get("amplitude_average").getAsDouble(),
                jsonResponse.get("rms_difference").getAsDouble(),
                jsonResponse.get("rms_average").getAsDouble(),
                jsonResponse.get("zcr_difference").getAsDouble(),
                jsonResponse.get("zcr_average").getAsDouble(),
                jsonResponse.get("bandwidth_difference").getAsDouble()
        );
        return audioData;
    }


    public static int calculate(int sampleRate, short[] audioData) {
        int numSamples = audioData.length;
        int numCrossing = 0;
        for (int p = 0; p < numSamples-1; p++)
        {
            if ((audioData[p] > 0 && audioData[p + 1] <= 0) ||
                    (audioData[p] < 0 && audioData[p + 1] >= 0))
            {
                numCrossing++;
            }
        }
        float numSecondsRecorded = (float)numSamples/(float)sampleRate;
        float numCycles = numCrossing/2;
        float frequency = numCycles/numSecondsRecorded;
        return (int)frequency;
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
