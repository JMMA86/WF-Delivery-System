package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;

import java.util.*;

public class MatrixGraph<T> implements IGraph<T> {
    private final MatrixVertex<T>[] vertices;
    private final int[][] matrix;
    private final boolean isDirected;

    /**
     * This constructor is used to create a graph that is not pondered
     *
     * @param isDirected Determines if the graph is directed
     * @param vertices   Determines the amount of vertices in the graph
     */
    public MatrixGraph(boolean isDirected, int vertices) {
        this.isDirected = isDirected;
        this.vertices = new MatrixVertex[vertices];
        this.matrix = new int[vertices][vertices];
    }

    /**
     * This constructor is used to create a graph that is pondered
     *
     * @param isDirected Determines if the graph is directed
     * @param isPondered Determines if the graph have weights in its connections
     * @param vertices   Determines the amount of vertices in the graph
     */
    public MatrixGraph(boolean isDirected, int vertices, boolean isPondered) {
        this.isDirected = isDirected;
        this.vertices = new MatrixVertex[vertices];
        this.matrix = new int[vertices][vertices];
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
        for (int i = 0; i < vertices.length && !stop; i++) {
            if (vertices[i] == null) {
                vertices[i] = new MatrixVertex<>(value);
                stop = true;
            }
        }
    }

    /**
     * This function adds an edge between to vertices, for this
     * both vertices must exist
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
        if (matrix[vertex1][vertex2] != 0) throw new MultipleEdgesNotAllowedException("Multiples edges are not allowed");

        if (vertex1 != -1 && vertex2 != -1) {
            if (isDirected) {
                matrix[vertex1][vertex2] = weight;
            } else {
                matrix[vertex1][vertex2] = weight;
                matrix[vertex2][vertex1] = weight;
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
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].getValue().equals(value)) {
                vertices[i] = null;
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
            if (vertices[i] != null && vertices[i].getValue().equals(value)) {
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
    public int calculateDistance(T start, T end) throws VertexNotFoundException, VertexNotAchievableException {
        int startIndex = searchVertexIndex(start);
        int endIndex = searchVertexIndex(end);
        if (startIndex == -1 || endIndex == -1) throw new VertexNotFoundException("Error. One vertex not found.");
        bfs(start);
        if (vertices[endIndex].getColor() == Color.WHITE) {
            throw new VertexNotAchievableException("Error. Vertex not achievable.");
        } else {
            return vertices[endIndex].getDistance();
        }
    }

    @Override
    public void bfs(T value) throws VertexNotFoundException {
        //Validation
        int vertexIndex = searchVertexIndex(value);
        if (vertexIndex == -1) throw new VertexNotFoundException("The vertex was not found");
        //Execution
        for (MatrixVertex<T> vertex : vertices) {
            vertex.setColor(Color.WHITE);
            vertex.setDistance(Integer.MAX_VALUE);
            vertex.setFather(null);
        }
        vertices[vertexIndex].setColor(Color.GRAY);
        vertices[vertexIndex].setDistance(0);
        vertices[vertexIndex].setFather(null);
        Queue<MatrixVertex<T>> queue = new LinkedList<>();
        queue.add(vertices[vertexIndex]);
        while (!queue.isEmpty()) {
            MatrixVertex<T> u = queue.poll();
            int uIndex = searchVertexIndex(u.getValue());
            for (int j = 0; j < vertices.length; j++) {
                if (matrix[uIndex][j] != 0 && j != uIndex) {
                    if (vertices[j].getColor() == Color.WHITE) {
                        vertices[j].setColor(Color.GRAY);
                        vertices[j].setDistance(u.getDistance() + 1);
                        vertices[j].setFather(u);
                        queue.add(vertices[j]);
                    }
                }
            }
            u.setColor(Color.BLACK);
        }
    }

    @Override
    public ArrayList<Pair<T, T>> dijkstra(T startVertex, T endVertex) throws VertexNotFoundException, VertexNotAchievableException {
        //Validations
        if (searchVertexIndex(startVertex) == -1 || searchVertexIndex(endVertex) == -1) throw new VertexNotFoundException("Error. One vertex not found.");
        bfs(startVertex);
        int startVertexIndex = searchVertexIndex(startVertex);
        int endVertexIndex = searchVertexIndex(endVertex);
        if (vertices[endVertexIndex].getDistance() == Integer.MAX_VALUE) throw new VertexNotAchievableException("Error. Vertex not achievable.");
        //Start algorithm
        ArrayList<Pair<T, T>> chain = new ArrayList<>();
        PriorityQueue<MatrixVertex<T>> q = new PriorityQueue<>(Comparator.comparingInt(MatrixVertex::getDistance));
        for (int i = 0; i < vertices.length; i++) {
            if (i != startVertexIndex) {
                vertices[i].setDistance(Integer.MAX_VALUE);
            } else {
                vertices[i].setDistance(0);
            }
            vertices[i].setFather(null);
            q.add(vertices[i]);
        }
        //Obtain edges
        while (!q.isEmpty() && q.peek() != vertices[endVertexIndex]) {
            MatrixVertex<T> u = q.poll();
            int uIndex = searchVertexIndex(u.getValue());
            for (int i = 0; i < vertices.length; i++) {
                if (matrix[uIndex][i] != 0 && i != uIndex) {
                    int alt = u.getDistance() + matrix[uIndex][i];
                    if (alt < vertices[i].getDistance()) {
                        vertices[i].setDistance(alt);
                        vertices[i].setFather(u);
                        //Update priority
                        q.remove(vertices[i]);
                        q.add(vertices[i]);
                    }
                }
            }
        }
        //Update chain
        MatrixVertex<T> vertexObj = vertices[endVertexIndex];
        while (vertexObj.getFather() != null) {
            MatrixVertex<T> vertexFather = vertexObj.getFather();
            int fatherIndex = searchVertexIndex(vertexFather.getValue());
            for (int i = 0; i < vertices.length; i++) {
                if (matrix[fatherIndex][i] != 0 && i != fatherIndex) {
                    if (vertices[i] == vertexObj) {
                        chain.add(0, new Pair<>(vertices[fatherIndex].getValue(), vertexObj.getValue()));
                        break;
                    }
                }
            }
            //Restart
            vertexObj = vertexFather;
        }
        return chain;
    }

    @Override
    public ArrayList<Pair<T, T>> prim(T value) throws VertexNotFoundException {
        int originPos = searchVertexIndex(value);
        if (originPos == -1) throw new VertexNotFoundException("The vertex was not found");
        ArrayList<Pair<T, T>> predecessors = new ArrayList<>();
        PriorityQueue<MatrixVertex<T>> toVisit = new PriorityQueue<>(Comparator.comparingInt(MatrixVertex::getDistance));
        toVisit.add(vertices[originPos]);
        for (MatrixVertex<T> vertex : vertices) {
            if (vertex != null && vertex != vertices[originPos]) {
                vertex.setDistance(Integer.MAX_VALUE);
                vertex.setColor(Color.WHITE);
                toVisit.add(vertex);
            }
        }

        vertices[originPos].setDistance(0);
        toVisit.add(vertices[originPos]);

        while (!toVisit.isEmpty()) {
            int temp = searchVertexIndex(toVisit.poll().getValue());
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[temp][i] != 0) {
                    if (vertices[i].getColor().equals(Color.WHITE) && matrix[temp][i] < vertices[i].getDistance()) {
                        toVisit.remove(vertices[i]);
                        vertices[i].setDistance(matrix[temp][i]);
                        toVisit.add(vertices[i]);
                        predecessors.add(new Pair<>(vertices[temp].getValue(), vertices[i].getValue()));
                        vertices[i].setColor(Color.BLACK);
                    }
                }
            }
            vertices[temp].setColor(Color.BLACK);
        }

        return predecessors;
    }


    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        int limit = 8;
        ans.append(String.format("%-" + limit +  "s", ""));
        for(MatrixVertex<T> v : vertices) {
            ans.append(String.format("%-" + limit +  "s", v.getValue().toString()));
        }
        for(int i=0; i<matrix.length; i++ ) {
            ans.append("\n").append( String.format("%-" + limit +  "s", vertices[i].getValue().toString()));
            for(int element : matrix[i]) {
                String elementStr = String.format("[ %2d ]", element);
                ans.append(String.format("%-" + limit +  "s", elementStr));
            }
        }

        return ans.toString();
    }
}
