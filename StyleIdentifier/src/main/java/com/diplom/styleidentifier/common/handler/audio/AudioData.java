package com.diplom.styleidentifier.common.handler.audio;

import com.diplom.styleidentifier.common.enums.EStyle;

public class AudioData {

    private EStyle style;

    private double[][] spectrogram;

    public AudioData(EStyle style, double[][] spectrogram) {
        this.style = style;
        this.spectrogram = spectrogram;
    }

    public EStyle getStyle() {
        return style;
    }

    public void setStyle(EStyle EStyle) {
        this.style = EStyle;
    }

    public double[][] getSpectrogram() {
        return spectrogram;
    }

    public void setSpectrogram(double[][] spectrogram) {
        this.spectrogram = spectrogram;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Style: " + style + "\n");
        return builder.toString();
    }
}
