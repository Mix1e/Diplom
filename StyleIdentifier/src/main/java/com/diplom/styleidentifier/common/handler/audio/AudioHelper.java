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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    public List<AudioData> getRandomAudioData(String path, int count) throws IOException {
        List<AudioData> data = new LinkedList<AudioData>();

        for (int i = 0; i < count; i++) {
            File[] folderEntries = new File(path).listFiles();
            if (folderEntries!=null) {
                File randomFolder = folderEntries[
                        ThreadLocalRandom.current().nextInt(0, folderEntries.length)
                        ];
                File[] audioFiles = randomFolder.listFiles();
                if(audioFiles != null) {
                    File randomFile = audioFiles[
                            ThreadLocalRandom.current().nextInt(0, audioFiles.length)
                            ];
                    data.add(calculateAudioData(randomFile.getPath()));
                }
            }
        }

        return data;
    }

    public List<AudioData> getRandomAudioEveryStyle(String path) throws IOException {
        List<AudioData> data = new LinkedList<AudioData>();

        File[] folderEntries = new File(path).listFiles();
        if (folderEntries==null) {
            return new LinkedList<>();
        }

        for(File styleFolder : folderEntries) {
            File[] audioFiles = styleFolder.listFiles();
            if(audioFiles != null) {
                File randomFile = audioFiles[
                        ThreadLocalRandom.current().nextInt(0, audioFiles.length)
                        ];
                data.add(calculateAudioData(randomFile.getPath()));
            }
        }

        return data;
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
