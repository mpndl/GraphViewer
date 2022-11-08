package com.example.task03;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class EdgeView extends Line {
    public Edge edge;
    public DoubleProperty x1Property = new SimpleDoubleProperty();
    public DoubleProperty y1Property = new SimpleDoubleProperty();
    public DoubleProperty x2Property = new SimpleDoubleProperty();
    public DoubleProperty y2Property = new SimpleDoubleProperty();
    public EdgeView(Pane pane, Edge edge, ArrayList<EdgeView> edgeViews) {
        super();
        this.edge = edge;
        edgeViews.add(this);

        setStroke(Color.BLACK);
        DropShadow shortShadow = new DropShadow();
        shortShadow.setColor(new Color(0, 0,0, .25));
        shortShadow.setRadius(5);
        shortShadow.setOffsetX(5);
        shortShadow.setOffsetY(5);
        setEffect(shortShadow);

        x1Property.bindBidirectional(edge.source.xProperty);
        y1Property.bindBidirectional(edge.source.yProperty);
        x2Property.bindBidirectional(edge.target.xProperty);
        y2Property.bindBidirectional(edge.target.yProperty);

        startXProperty().bindBidirectional(x1Property);
        startYProperty().bindBidirectional(y1Property);
        endXProperty().bindBidirectional(x2Property);
        endYProperty().bindBidirectional(y2Property);

        pane.getChildren().add(this);
        this.toBack();
    }

    public EdgeView(Edge edge) {
        super();
        this.edge = edge;

        setStroke(Color.BLACK);
        DropShadow shortShadow = new DropShadow();
        shortShadow.setColor(new Color(0, 0,0, .25));
        shortShadow.setRadius(5);
        shortShadow.setOffsetX(5);
        shortShadow.setOffsetY(5);
        setEffect(shortShadow);

        x1Property.bindBidirectional(edge.source.xProperty);
        y1Property.bindBidirectional(edge.source.yProperty);
        x2Property.bindBidirectional(edge.target.xProperty);
        y2Property.bindBidirectional(edge.target.yProperty);

        startXProperty().bindBidirectional(x1Property);
        startYProperty().bindBidirectional(y1Property);
        endXProperty().bindBidirectional(x2Property);
        endYProperty().bindBidirectional(y2Property);
    }
}
