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

    /**
     * Adds a vertex to the list graph
     * @param vertex Value to ve inserted
     * @throws VertexAlreadyAddedException If vertex is already added
     */
    public void addVertex(T vertex) throws VertexAlreadyAddedException {
        if (searchVertexIndex(vertex) == -1) {
            list.add(new ListVertex<>(vertex));
        } else {
            throw new VertexAlreadyAddedException("Vertex found: " + vertex);
        }
    }

    /**
     * Adds an edge between two vertices
     * @param start origin vertex
     * @param end destination vertex
     * @param weight connection weight
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
    public void addEdge(T start, T end, int weight) throws VertexNotFoundException {
        int startVertex = searchVertexIndex(start);
        int endVertex = searchVertexIndex(end);
        if (startVertex == -1 || endVertex == -1) {
            throw new VertexNotFoundException("Error. Vertex not found.");
        }
        list.get(startVertex).getEdges().add(new ListEdge<>(list.get(endVertex), list.get(startVertex), weight));
        list.get(endVertex).getEdges().add(new ListEdge<>(list.get(startVertex), list.get(endVertex), weight));
    }

    /**
     * Deletes a vertex including all associated edges
     * @param vertexValue vertex to be deleted
     * @throws VertexNotFoundException if the vertex doesn't exist
     */
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

    /**
     * Deletes an edge between two vertices
     * @param start origin vertex
     * @param end destination vertex
     * @throws EdgeNotFoundException if edge doesn't exist
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
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

    /**
     * Used for other methods, returns index from list graph of any vertex
     * @param vertex vertex to be searched
     * @return index in the graph list
     */
    private int searchVertexIndex(T vertex) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() == vertex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Used for other methods, returns index from edge list of a determinant vertex
     * @param start origin vertex
     * @param end destination vertex
     * @return index in the origin vertex edge list
     */
    private int searchEdgeIndex(ListVertex<T> start, ListVertex<T> end) {
        for (int i = 0; i < start.getEdges().size(); i++) {
            if (start.getEdges().get(i).getLeftVertex() == start && start.getEdges().get(i).getRightVertex() == end) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns if a vertex exist
     * @param vertex vertex to be searched
     * @return true if it exists and false otherwise
     */
    public boolean searchVertex(T vertex) {
        for (ListVertex<T> tVertex : list) {
            if (tVertex.getValue() == vertex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if an edge exist
     * @param start origin vertex
     * @param end destination vertex
     * @return true if it exists and false otherwise
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
    public boolean searchEdge(T start, T end) throws VertexNotFoundException {
        if (!searchVertex(start) || !searchVertex(end)) {
            throw new VertexNotFoundException("Error. One or both vertex not found.");
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

    /**
     * Returns all vertex in the graph
     * @return a list with all of them
     */
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