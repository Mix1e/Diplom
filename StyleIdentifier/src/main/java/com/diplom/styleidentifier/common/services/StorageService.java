package com.diplom.styleidentifier.common.services;

public class StorageService {

    public static final String DEFAULT_DATASET_PATH = "dataset\\genres_original";
    private String datasetPath;
    public StorageService() {
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
}
