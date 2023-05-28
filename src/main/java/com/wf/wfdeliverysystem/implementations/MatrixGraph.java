package com.wf.wfdeliverysystem.implementations;

import javafx.util.Pair;

import java.util.ArrayList;

public class MatrixGraph<T> {
    private final MatrixVertex<T>[] vertexes;
    private final int[][] matrix;
    private final boolean isDirected;
    private boolean isPondered;

    /**
     * This constructor is used to create a graph that is not pondered
     *
     * @param isDirected Determines if the graph is directed
     * @param vertexes Determines the amount of vertexes in the graph
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
     * @param vertexes Determines the amount of vertexes in the graph
     */
    public MatrixGraph(boolean isDirected, int vertexes, boolean isPondered) {
        this.isDirected = isDirected;
        this.vertexes = new MatrixVertex[vertexes];
        this.matrix = new int[vertexes][vertexes];
        this.isPondered = isPondered;
    }

    /**
     * This function adds a vertex to the graph, if the graph is full it will return
     * false
     *
     * @param value The value of the new vertex
     * @return A boolean indicating if the vertex was added
     */
    public boolean addVertex(T value) {
        for (int i = 0; i < vertexes.length; i++) {
            if (vertexes[i] == null) {
                vertexes[i] = new MatrixVertex<>(value);
                return true;
            }
        }

        return false;
    }

    /**
     * This function adds an edge between to vertexes, for this
     * both vertexes must exists
     *
     * @param value1 The vertex 1
     * @param value2 The vertex 2
     * @return A boolean indicating if the edge was added successfully
     */
    public boolean addEdge(T value1, T value2) {
        int vertex1 = searchVertex(value1);
        int vertex2 = searchVertex(value2);

        if (vertex1 != -1 && vertex2 != -1) {
            if (isDirected) {
                matrix[vertex1][vertex2] = 1;
            } else {
                matrix[vertex1][vertex2] = 1;
                matrix[vertex2][vertex1] = 1;
            }

            return true;
        }

        return false;
    }

    /**
     * This function adds an edge and a weight between to vertexes, if one of the
     * two vertexes doesn't exists or the graph is not pounded, it will return
     * a  false, indicating that the edge was not added
     *
     * @param value1 The value 1
     * @param value2 The value 2
     * @param weight The weight of the edge
     * @return A boolean indicating if the edge was created successfully
     */
    public boolean addEdge(T value1, T value2, int weight) {
        int vertex1 = searchVertex(value1);
        int vertex2 = searchVertex(value2);

        if (vertex1 != -1 && vertex2 != -1 && isPondered) {
            if (isDirected) {
                matrix[vertex1][vertex2] = weight;
            } else {
                matrix[vertex1][vertex2] = weight;
                matrix[vertex2][vertex1] = weight;
            }
            return true;
        }

        return false;
    }

    /**
     * This function removes a vertex and its connections from the graph
     *
     * @param value The value of the vertex to be removed
     */
    public void deleteVertex(T value) {
        int oldVerPos = searchVertex(value);
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
     * @param value1 The first vertex
     * @param value2 The second vertex
     */
    public void deleteEdge(T value1, T value2) {
        int posVal1 = searchVertex(value1);
        int posVal2 = searchVertex(value2);

        matrix[posVal1][posVal2] = 0;
        matrix[posVal2][posVal1] = 0;
    }

    /**
     * This method provides a private searcher to search the value of a vertex
     *
     * @param value The value to search
     * @return The position where the vertex is located
     */
    private int searchVertex(T value) {
        for (int i = 0; i < matrix.length; i++) {
            if (vertexes[i].getValue().equals(value)) {
                return i;
            }
        }

        return -1;
    }

    public MatrixVertex<T> getVertex(T value) {
        if (searchVertex(value) == -1) return null;
        return vertexes[searchVertex(value)];
    }

    public MatrixVertex<T>[] getEdge(T value1, T value2) {
        int vValue1 = searchVertex(value1);
        int vValue2 = searchVertex(value2);

        if (vValue1 != -1 && vValue2 != -1) {
            if (isDirected) {
                if (matrix[vValue1][vValue2] != 0) {
                    return new MatrixVertex[]{vertexes[vValue1], vertexes[vValue2]};
                } else {
                    return null;
                }
            } else {
                if (matrix[vValue1][vValue2] != 0 && matrix[vValue2][vValue1] != 0) {
                    return new MatrixVertex[]{vertexes[vValue1], vertexes[vValue2]};
                } else {
                    return null;
                }
            }
        }
    }
}
