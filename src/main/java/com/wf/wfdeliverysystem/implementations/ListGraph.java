package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.model.*;
import javafx.util.Pair;
import java.util.ArrayList;

public class ListGraph<T> implements IWFDelivery<T> {
    //Vertex list
    private final ArrayList<ListVertex<T>> list;
    //Empty constructor
    public ListGraph() {
        list = new ArrayList<>();
    }

    //Adding houses
    public void addVertex(T vertex) throws VertexAlreadyAddedException {
        if (searchVertexIndex(vertex) == -1) {
            list.add(new ListVertex<>(vertex));
        } else {
            throw new VertexAlreadyAddedException("Vertex found: " + vertex);
        }
    }

    //Adding edge between houses
    public void addEdge(T start, T end, String id, int weight) throws VertexNotFoundException {
        int startVertex = searchVertexIndex(start);
        int endVertex = searchVertexIndex(end);
        if (startVertex == -1 || endVertex == -1) {
            throw new VertexNotFoundException("Error. Vertex not found.");
        }
        list.get(startVertex).getEdges().add(new ListEdge<>(list.get(endVertex), list.get(startVertex), weight));
        list.get(endVertex).getEdges().add(new ListEdge<>(list.get(startVertex), list.get(endVertex), weight));
    }

    //Deleting houses
    public void deleteVertex(T vertexValue) throws VertexNotFoundException {
        int vertexIndex = searchVertexIndex(vertexValue);
        if (vertexIndex == -1) {
            throw new VertexNotFoundException("Error. Vertex not found: " + vertexValue);
        }
        for (ListVertex<T> tVertex : list) {
            for (int j = 0; j < tVertex.getEdges().size(); j++) {
                if (tVertex.getEdges().get(j).getLeftVertex() == list.get(vertexIndex) || tVertex.getEdges().get(j).getRightVertex() == list.get(vertexIndex)) {
                    tVertex.getEdges().remove(j);
                }
            }
        }
        list.remove(vertexIndex);
    }

    //Deleting edges between houses
    public void deleteEdge(T start, T end) throws EdgeNotFoundException, VertexNotFoundException {
        int startIndex = searchVertexIndex(start);
        int endIndex = searchVertexIndex(end);
        if (startIndex == -1 || endIndex == -1) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        if (!searchEdge(start, end)) {
            throw new EdgeNotFoundException("Error. Edge not found: " + start + " -> " + end);
        }
        list.get(startIndex).getEdges().remove(searchEdgeIndex(list.get(startIndex), list.get(endIndex)));
        list.get(endIndex).getEdges().remove(searchEdgeIndex(list.get(endIndex), list.get(startIndex)));
    }

    //Searcher returns vertex index
    private int searchVertexIndex(T vertex) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() == vertex) {
                return i;
            }
        }
        return -1;
    }

    //Searcher returns edge index
    private int searchEdgeIndex(ListVertex<T> start, ListVertex<T> end) {
        for (int i = 0; i < start.getEdges().size(); i++) {
            if (start.getEdges().get(i).getLeftVertex() == start && start.getEdges().get(i).getRightVertex() == end) {
                return i;
            }
        }
        return -1;
    }

    //Searcher returns if vertex exists
    public boolean searchVertex(T vertex) {
        for (ListVertex<T> tVertex : list) {
            if (tVertex.getValue() == vertex) {
                return true;
            }
        }
        return false;
    }

    //Searcher returns if edge exists
    public boolean searchEdge(T start, T end) throws VertexNotFoundException {
        if (!searchVertex(start) || !searchVertex(end)) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        int startIndex = searchVertexIndex(start);
        for (int i = 0; i < list.get(startIndex).getEdges().size(); i++) {
            ListEdge<T> edge = list.get(startIndex).getEdges().get(i);
            if (edge.getLeftVertex().getValue() == start && edge.getRightVertex().getValue() == end) {
                return true;
            }
        }
        return false;
    }

    //Returns all houses
    public ArrayList<T> getAllVertex() {
        ArrayList<T> vertices = new ArrayList<>();
        for (ListVertex<T> tVertex : list) {
            vertices.add(tVertex.getValue());
        }
        return vertices;
    }

    @Override
    public boolean checkPathBetweenHouses(House h1, House h2) {
        return false;
    }

    @Override
    public ArrayList<House> calculateMinimumPath(House h1, House h2) {
        return null;
    }

    @Override
    public ArrayList<Pair<House, House>> generateDeliveryTour(House h0) {
        return null;
    }
}
