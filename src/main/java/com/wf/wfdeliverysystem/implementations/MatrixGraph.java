package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MatrixGraph<T> implements IGraph<T> {
    private final MatrixVertex<T>[] vertexes;
    private final int[][] matrix;
    private final boolean isDirected;

    /**
     * This constructor is used to create a graph that is not pondered
     *
     * @param isDirected Determines if the graph is directed
     * @param vertexes   Determines the amount of vertexes in the graph
     */
    public MatrixGraph(boolean isDirected, int vertexes) {
        this.isDirected = isDirected;
        this.vertexes = new MatrixVertex[vertexes];
        this.matrix = new int[vertexes][vertexes];
    }

    /**
     * This constructor is used to create a graph that is pondered
     *
     * @param isDirected Determines if the graph is directed
     * @param isPondered Determines if the graph have weights in its connections
     * @param vertexes   Determines the amount of vertexes in the graph
     */
    public MatrixGraph(boolean isDirected, int vertexes, boolean isPondered) {
        this.isDirected = isDirected;
        this.vertexes = new MatrixVertex[vertexes];
        this.matrix = new int[vertexes][vertexes];
    }

    /**
     * This function adds a vertex to the graph, if the graph is full it will return
     * false
     *
     * @param value The value of the new vertex
     */
    public void addVertex(T value) throws VertexAlreadyAddedException {
        boolean stop = false;
        if (searchVertexIndex(value) != -1) throw new VertexAlreadyAddedException("There is a vertex with the same value");
        for (int i = 0; i < vertexes.length && !stop; i++) {
            if (vertexes[i] == null) {
                vertexes[i] = new MatrixVertex<>(value);
                stop = true;
            }
        }
    }

    /**
     * This function adds an edge between to vertexes, for this
     * both vertexes must exists
     *
     * @param start  The vertex 1
     * @param end    The vertex 2
     * @param weight The weight between the two vertex
     */
    @Override
    public void addEdge(T start, T end, String id, int weight) throws VertexNotFoundException, LoopsNotAllowedException, MultipleEdgesNotAllowedException {
        int vertex1 = searchVertexIndex(start);
        int vertex2 = searchVertexIndex(end);

        if (vertex1 == vertex2) throw new LoopsNotAllowedException("Loops are not allowed");

        if (vertex1 != -1 && vertex2 != -1) {
            if (isDirected) {
                matrix[vertex1][vertex2] = 1;
            } else {
                matrix[vertex1][vertex2] = 1;
                matrix[vertex2][vertex1] = 1;
            }
        } else {
            String vError = vertex1 == -1 ? "vertex1" : "vertex2";
            throw new VertexNotFoundException("Cannot find the vertex " + vError);
        }
    }

    /**
     * This function removes a vertex and its connections from the graph
     *
     * @param value The value of the vertex to be removed
     */
    @Override
    public void deleteVertex(T value) throws VertexNotFoundException {
        int oldVerPos = searchVertexIndex(value);
        if (oldVerPos == -1) throw new VertexNotFoundException("There's no such vertex in the graph");
        for (int i = 0; i < vertexes.length; i++) {
            if (vertexes[i].getValue().equals(value)) {
                vertexes[i] = null;
                break;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            matrix[oldVerPos][i] = 0;
            matrix[i][oldVerPos] = 0;
        }
    }

    /**
     * This function deletes an edge connection from the adjacency matrix
     *
     * @param start The first vertex
     * @param end   The second vertex
     */
    @Override
    public void deleteEdge(T start, T end, String id) throws EdgeNotFoundException, VertexNotFoundException {
        int posVal1 = searchVertexIndex(start);
        int posVal2 = searchVertexIndex(end);

        boolean hasEdge = searchEdge(start, end, id);

        if (hasEdge) {
            matrix[posVal1][posVal2] = 0;
            matrix[posVal2][posVal1] = 0;
        } else {
            throw new EdgeNotFoundException("The edge was not found");
        }
    }

    /**
     * This method provides a private searcher to search the value of a vertex
     *
     * @param value The value to search
     * @return The position where the vertex is located
     */
    private int searchVertexIndex(T value) {
        for (int i = 0; i < matrix.length; i++) {
            if (vertexes[i] != null && vertexes[i].getValue().equals(value)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean searchEdge(T start, T end, String id) throws VertexNotFoundException {
        int vertex1 = searchVertexIndex(start);
        int vertex2 = searchVertexIndex(end);
        boolean result = false;

        String error = vertex1 == -1 ? "start" : "end";
        if (vertex1 == -1 || vertex2 == -1) throw new VertexNotFoundException("The " + error + " vertex was not found");

        if (isDirected) {
            if (matrix[vertex1][vertex2] != 0) {
                result = true;
            }
        } else {
            if (matrix[vertex1][vertex2] != 0 && matrix[vertex2][vertex1] != 0) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public void bfs(T value) throws VertexNotFoundException {

    }

    @Override
    public ArrayList<T> dijkstra(T startVertex, T endVertex) throws VertexNotFoundException, VertexNotAchievableException {
        return null;
    }

    @Override
    public ArrayList<Pair<T, T>> prim(T value) throws VertexNotFoundException {
        int originPos = searchVertexIndex(value);
        if (originPos == -1) throw new VertexNotFoundException("The vertex was not found");
        ArrayList<Pair<T, T>> predecessors = new ArrayList<>();
        PriorityQueue<MatrixVertex<T>> toVisit = new PriorityQueue<>(Comparator.comparingInt(MatrixVertex::getDistance));
        toVisit.add(vertexes[originPos]);
        for (MatrixVertex<T> vertex : vertexes) {
            if (vertex != null && vertex != vertexes[originPos]) {
                vertex.setDistance(Integer.MAX_VALUE);
                vertex.setColor(Color.WHITE);
                toVisit.add(vertex);
            }
        }

        vertexes[originPos].setDistance(0);
        toVisit.add(vertexes[originPos]);

        while (!toVisit.isEmpty()) {
            int temp = searchVertexIndex(toVisit.poll().getValue());
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[temp][i] != 0) {
                    if (vertexes[i].getColor().equals(Color.WHITE) && matrix[temp][i] < vertexes[i].getDistance()) {
                        toVisit.remove(vertexes[i]);
                        vertexes[i].setDistance(matrix[temp][i]);
                        toVisit.add(vertexes[i]);
                        predecessors.add(new Pair<>(vertexes[temp].getValue(), vertexes[i].getValue()));
                    }
                }
            }
            vertexes[temp].setColor(Color.BLACK);
        }

        return predecessors;
    }
}
