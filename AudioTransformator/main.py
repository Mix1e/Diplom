import numpy as np
import matplotlib.pyplot as plt
import json
from sys import argv
import librosa
import librosa.feature
from sklearn import preprocessing

blues_file = "../StyleIdentifier/dataset/genres_original/blues/blues.00000.wav"
metal_file = "../StyleIdentifier/dataset/genres_original/metal/metal.00000.wav"
classical_file = "../StyleIdentifier/dataset/genres_original/classical/classical.00000.wav"

FRAME_SIZE = 1024
HOP_LENGTH = 512
MAX_BPM = 300


def amplitude_envelope(signal, frame_size, hop_length):
    amplitudes = []

    for i in range(0, len(signal), hop_length):
        current_amplitude_envelope = max(signal[i:i + frame_size])
        amplitudes.append(current_amplitude_envelope)

    return np.array(amplitudes)


def calculate_difference(ae_audio):
    return ae_audio.max() - ae_audio.min()


def calculate_average(ae_audio):
    return ae_audio.sum() / ae_audio.size


def calculate_rms(audio):
    return librosa.feature.rms(y=audio, frame_length=FRAME_SIZE, hop_length=HOP_LENGTH)[0]


def calculate_zcr(audio):
    return librosa.feature.zero_crossing_rate(y=audio, frame_length=FRAME_SIZE, hop_length=HOP_LENGTH)[0]


def calculate_bandwidth(audio, sr):
    return preprocessing.normalize(
        [librosa.feature.spectral_bandwidth(y=audio, sr=sr, n_fft=FRAME_SIZE, hop_length=HOP_LENGTH)[0]])[0]


def calculate_bpm(audio, sr):
    tempo, _ = librosa.beat.beat_track(y=audio, sr=sr)
    return tempo


class AudioParameter:
    def __init__(self, style, bpm, amplitude_difference, amplitude_average, rms_difference, rms_average, zcr_difference,
                 zcr_average, bandwidth_difference):
        self.style = style
        self.bpm = bpm
        self.amplitude_difference = amplitude_difference
        self.amplitude_average = amplitude_average
        self.rms_difference = rms_difference
        self.rms_average = rms_average
        self.zcr_difference = zcr_difference
        self.zcr_average = zcr_average
        self.bandwidth_difference = bandwidth_difference


def style_by_path(path):
    mas = path.split("\\")
    return mas[len(mas) - 2].upper()


def get_data(file_path):
    audio, sr = librosa.load(file_path)
    ae_audio = amplitude_envelope(audio, FRAME_SIZE, HOP_LENGTH)
    rms_audio = calculate_rms(audio)
    zcr_audio = calculate_zcr(audio)
    bandwidth_audio = calculate_bandwidth(audio, sr)

    return AudioParameter(
        style_by_path(file_path),
        float(calculate_bpm(audio, sr)/MAX_BPM),
        float(calculate_difference(ae_audio)),
        float(calculate_average(ae_audio)),
        float(calculate_difference(rms_audio)),
        float(calculate_average(rms_audio)),
        float(calculate_difference(zcr_audio)),
        float(calculate_average(zcr_audio)),
        float(calculate_difference(bandwidth_audio)),
    )


def graph_two_waves(audio, mas):
    frames = range(0, mas.size)
    t = librosa.frames_to_time(frames, hop_length=HOP_LENGTH)

    plt.figure(figsize=(15, 17))

    plt.subplot(3, 1, 1)
    librosa.display.waveshow(audio)
    plt.plot(t, mas)
    plt.title("title")

    plt.show()


if __name__ == '__main__':
    if len(argv) > 1:
        _, path = argv
        params = get_data(path)
        print(json.dumps(vars(params)))
