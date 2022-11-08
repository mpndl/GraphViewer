package com.example.task03;

import javafx.beans.property.*;

import java.util.ArrayList;

public class Node {
    public StringProperty labelProperty = new SimpleStringProperty();
    public DoubleProperty xProperty =  new SimpleDoubleProperty();
    public DoubleProperty yProperty = new SimpleDoubleProperty();
    public BooleanProperty isSelectedProperty = new SimpleBooleanProperty();

    public Node(double x, double y) {
        xProperty.setValue(x);
        yProperty.setValue(y);
    }
    public Node(String label) {
        this.labelProperty.setValue(label);
    }

    @Override
    public String toString() {
        return "Node{" +
                "labelProperty=" + labelProperty.getValue() +
                ", xProperty=" + xProperty.getValue() +
                ", yProperty=" + yProperty.getValue() +
                ", isSelectedProperty=" + isSelectedProperty.getValue() +
                '}';
    }
}
