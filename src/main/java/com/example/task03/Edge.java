package com.example.task03;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Edge {
    public Node source;
    public Node target;
    public BooleanProperty isSelectedProperty = new SimpleBooleanProperty();

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source.labelProperty.getValue() +
                ", target=" + target.labelProperty.getValue() +
                '}';
    }
}
