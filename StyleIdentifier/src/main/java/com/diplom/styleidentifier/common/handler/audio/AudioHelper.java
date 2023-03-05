package com.diplom.styleidentifier.common.handler.audio;
import com.diplom.styleidentifier.common.enums.EStyle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AudioHelper {
    public static final int STYLES_COUNT = EStyle.values().length;
    private List<AudioData> data;
    private final String SCRIPT_START = "C:\\diplom\\AudioTransformator\\venv\\Scripts\\python.exe C:\\diplom\\AudioTransformator\\main.py ";

    public AudioHelper() {
    }

    public AudioHelper(List<AudioData> data) {
        this.data = data;
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

    void getDataSet(File folder, List<File> fileList) throws IOException {
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

    private String getJsonFromScript(String path) throws IOException {
        Process p = Runtime.getRuntime().exec(SCRIPT_START + path);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String s, out = "";
        while ((s = stdInput.readLine()) != null) {
            out += s;
        }
        stdInput.close();
        p.destroy();

        return out;
    }

    public AudioData calculateAudioData(String path) throws IOException {
        JsonObject jsonResponse = new Gson().fromJson(getJsonFromScript(path), JsonObject.class);
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
        System.out.println(audioData.getStyle() + ", Bpm: " + audioData.getBpm());
        return audioData;
    }

    public AudioData calculateUserAudio(String path) throws IOException {
        JsonObject jsonResponse = new Gson().fromJson(getJsonFromScript(path), JsonObject.class);
        AudioData audioData = new AudioData(
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
}
