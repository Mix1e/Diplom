package com.diplom.styleidentifier.common.handler.audio;

import com.diplom.styleidentifier.common.enums.EStyle;

import java.io.Serializable;

public class AudioData implements Serializable {
    public static int INPUT_NEURON_COUNT = 8;
    private EStyle style;
    private double bpm;
    private double amplitudeDifference;
    private double amplitudeAverage;
    private double rsmDifference;
    private double rsmAverage;
    private double zrcDifference;
    private double zrcAverage;
    private double bandwidthDifference;

    public AudioData(double bpm, double amplitudeDifference, double amplitudeAverage, double rsmDifference, double rsmAverage, double zrcDifference, double zrcAverage, double bandwidthDifference) {
        this.bpm = bpm;
        this.amplitudeDifference = amplitudeDifference;
        this.amplitudeAverage = amplitudeAverage;
        this.rsmDifference = rsmDifference;
        this.rsmAverage = rsmAverage;
        this.zrcDifference = zrcDifference;
        this.zrcAverage = zrcAverage;
        this.bandwidthDifference = bandwidthDifference;
    }


    public AudioData(EStyle style, double bpm, double amplitudeDifference, double amplitudeAverage, double rsmDifference, double rsmAverage, double zrcDifference, double zrcAverage, double bandwidthDifference) {
        this.style = style;
        this.bpm = bpm;
        this.amplitudeDifference = amplitudeDifference;
        this.amplitudeAverage = amplitudeAverage;
        this.rsmDifference = rsmDifference;
        this.rsmAverage = rsmAverage;
        this.zrcDifference = zrcDifference;
        this.zrcAverage = zrcAverage;
        this.bandwidthDifference = bandwidthDifference;
    }

    public EStyle getStyle() {
        return style;
    }

    public void setStyle(EStyle style) {
        this.style = style;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public double getAmplitudeDifference() {
        return amplitudeDifference;
    }

    public void setAmplitudeDifference(double amplitudeDifference) {
        this.amplitudeDifference = amplitudeDifference;
    }

    public double getAmplitudeAverage() {
        return amplitudeAverage;
    }

    public void setAmplitudeAverage(double amplitudeAverage) {
        this.amplitudeAverage = amplitudeAverage;
    }

    public double getRsmDifference() {
        return rsmDifference;
    }

    public void setRsmDifference(double rsmDifference) {
        this.rsmDifference = rsmDifference;
    }

    public double getRsmAverage() {
        return rsmAverage;
    }

    public void setRsmAverage(double rsmAverage) {
        this.rsmAverage = rsmAverage;
    }

    public double getZrcDifference() {
        return zrcDifference;
    }

    public void setZrcDifference(double zrcDifference) {
        this.zrcDifference = zrcDifference;
    }

    public double getZrcAverage() {
        return zrcAverage;
    }

    public void setZrcAverage(double zrcAverage) {
        this.zrcAverage = zrcAverage;
    }

    public double getBandwidthDifference() {
        return bandwidthDifference;
    }

    public void setBandwidthDifference(double bandwidthDifference) {
        this.bandwidthDifference = bandwidthDifference;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Style: " + style + "\n");
        builder.append("bpm: " + bpm + "\n");
        builder.append("amplitudeDifference: " + amplitudeDifference + "\n");
        builder.append("amplitudeAverage: " + amplitudeAverage + "\n");
        builder.append("rsmDifference: " + rsmDifference + "\n");
        builder.append("rsmAverage: " + rsmAverage + "\n");
        builder.append("zrcDifference: " + zrcDifference + "\n");
        builder.append("zrcAverage: " + zrcAverage + "\n");
        builder.append("bandwidthDifference: " + bandwidthDifference + "\n");
        return builder.toString();
    }
}
