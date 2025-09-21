package com.example.LMS.dto.response;

import java.util.List;

public class ChartData {
    private List<String> labels;
    private List<Dataset> datasets;

    public ChartData() {}

    public List<String> getLabels() { return labels; }
    public void setLabels(List<String> labels) { this.labels = labels; }

    public List<Dataset> getDatasets() { return datasets; }
    public void setDatasets(List<Dataset> datasets) { this.datasets = datasets; }

    public static class Dataset {
        private String label;
        private List<Number> data;
        private List<String> backgroundColor;
        private String borderColor;
        private Integer borderWidth;

        public Dataset() {}

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public List<Number> getData() { return data; }
        public void setData(List<Number> data) { this.data = data; }

        public List<String> getBackgroundColor() { return backgroundColor; }
        public void setBackgroundColor(List<String> backgroundColor) { this.backgroundColor = backgroundColor; }

        public String getBorderColor() { return borderColor; }
        public void setBorderColor(String borderColor) { this.borderColor = borderColor; }

        public Integer getBorderWidth() { return borderWidth; }
        public void setBorderWidth(Integer borderWidth) { this.borderWidth = borderWidth; }
    }
}
