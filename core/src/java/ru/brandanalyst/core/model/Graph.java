package ru.brandanalyst.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dmitry Batkovich
 * Date: 10/16/11
 * Time: 7:18 PM
 * general model of graph
 */
public class Graph {
    private List<SingleDot> graph;
    private String ticker;

    public Graph(List<SingleDot> graph) {
        this.graph = graph;
        this.ticker = "no ticker";
    }

    public Graph(String ticker) {
        graph = new ArrayList<SingleDot>();
        this.ticker = ticker;
    }

    public List<SingleDot> getGraph() {
        return graph;
    }

    public String getTicker() {
        return ticker;
    }

    public void addPoint(SingleDot dot) {
        graph.add(dot);
    }
}
