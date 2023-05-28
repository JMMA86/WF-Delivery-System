package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ListGraph<T> implements IGraph<T> {
    //Initial conditions
    private final boolean isGuided;
    private final boolean isMultiple;
    private final boolean allowsLoop;
    //Structure
    private final ArrayList<ListVertex<T>> list;
    //Empty constructor
    public ListGraph(boolean isGuided, boolean isMultiple, boolean allowsLoop) {
        list = new ArrayList<>();
        this.isGuided = isGuided;
        this.isMultiple = isMultiple;
        this.allowsLoop = allowsLoop;
    }

    /**
     * Adds a vertex to the list graph
     * @param vertex Value to ve inserted
     * @throws VertexAlreadyAddedException If vertex is already added
     */
    @Override
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
    @Override
    public void addEdge(T start, T end, String id, int weight) throws VertexNotFoundException, LoopsNotAllowedException, MultipleEdgesNotAllowedException {
        int startVertex = searchVertexIndex(start);
        int endVertex = searchVertexIndex(end);
        if (startVertex == -1 || endVertex == -1) {
            throw new VertexNotFoundException("Error. Vertex not found.");
        }
        if (startVertex == endVertex && !allowsLoop) {
            throw new LoopsNotAllowedException("Error. Loops not allowed.");
        }
        if (searchEdge(start, end, id) && !isMultiple) {
            throw new MultipleEdgesNotAllowedException("Error. Multiple edges between vertex not allowed.");
        }
        if (!isGuided) {
            list.get(endVertex).getEdges().add(new ListEdge<>(list.get(endVertex), list.get(startVertex), id, weight));
        }
        list.get(startVertex).getEdges().add(new ListEdge<>(list.get(startVertex), list.get(endVertex), id, weight));
    }

    /**
     * Deletes a vertex including all associated edges
     * @param vertexValue vertex to be deleted
     * @throws VertexNotFoundException if the vertex doesn't exist
     */
    @Override
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
    @Override
    public void deleteEdge(T start, T end, String id) throws EdgeNotFoundException, VertexNotFoundException {
        int startIndex = searchVertexIndex(start);
        int endIndex = searchVertexIndex(end);
        if (startIndex == -1 || endIndex == -1) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        if (!searchEdge(start, end, id)) {
            throw new EdgeNotFoundException("Error. Edge not found: " + start + " -> " + end + " (" + id + ")");
        }
        if (!isGuided) {
            list.get(endIndex).getEdges().remove(searchEdgeIndex(list.get(endIndex), list.get(startIndex), id));
        }
        list.get(startIndex).getEdges().remove(searchEdgeIndex(list.get(startIndex), list.get(endIndex), id));
    }

    /**
     * Returns all vertex in the graph
     * @return a list with all of them
     */
    @Override
    public ArrayList<T> getAllVertex() {
        ArrayList<T> vertices = new ArrayList<>();
        for (ListVertex<T> tVertex : list) {
            vertices.add(tVertex.getValue());
        }
        return vertices;
    }

    /**
     * Returns all neighbors from a vertex
     * @param vertex Vertex to analyze
     * @return list of neighbors (vertices)
     * @throws VertexNotFoundException if the vertex doesn't exist
     */
    @Override
    public ArrayList<T> getAllNeighbors(T vertex) throws VertexNotFoundException {
        int vertexIndex = searchVertexIndex(vertex);
        ArrayList<T> neighbors = new ArrayList<>();
        if (vertexIndex == -1) {
            throw new VertexNotFoundException("Vertex not found: " + vertex);
        }
        for (int i = 0; i < list.get(vertexIndex).getEdges().size(); i++) {
            if (!validateNeighborsArrayList(neighbors, list.get(vertexIndex).getEdges().get(i).getRightVertex().getValue()) && list.get(vertexIndex).getEdges().get(i).getRightVertex().getValue() != vertex) {
                neighbors.add(list.get(vertexIndex).getEdges().get(i).getRightVertex().getValue());
            }
        }
        return neighbors;
    }

    /**
     * Validates if a neighbor is already added
     * @param arrayList list of current neighbors
     * @param value vertex to compare
     * @return true if it exists and false otherwise
     */
    private boolean validateNeighborsArrayList(ArrayList<T> arrayList, T value) {
        for (T t : arrayList) {
            if (t == value) {
                return true;
            }
        }
        return false;
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
    private int searchEdgeIndex(ListVertex<T> start, ListVertex<T> end, String id) {
        for (int i = 0; i < start.getEdges().size(); i++) {
            if (start.getEdges().get(i).getLeftVertex() == start && start.getEdges().get(i).getRightVertex() == end && start.getEdges().get(i).getId().equals(id)) {
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
    @Override
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
    @Override
    public boolean searchEdge(T start, T end, String id) throws VertexNotFoundException {
        if (!searchVertex(start) || !searchVertex(end)) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        int startIndex = searchVertexIndex(start);
        for (int i = 0; i < list.get(startIndex).getEdges().size(); i++) {
            ListEdge<T> edge = list.get(startIndex).getEdges().get(i);
            if (edge.getLeftVertex().getValue() == start && edge.getRightVertex().getValue() == end && edge.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void bfs(T value) throws VertexNotFoundException {
        ListVertex<T> root;
        try {
            root = list.get(searchVertexIndex(value));
        } catch (IndexOutOfBoundsException e) {
            throw new VertexNotFoundException("Vertex not found: " + value);
        }
        for (ListVertex<T> tVertex : list) {
            tVertex.setColor(Color.WHITE);
            tVertex.setDistance(Integer.MAX_VALUE);
            tVertex.setFather(null);
        }
        root.setColor(Color.GRAY);
        root.setDistance(0);
        root.setFather(null);
        Queue<ListVertex<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            ListVertex<T> u = queue.poll();
            for (int i = 0; i < u.getEdges().size(); i++) {
                if (u.getEdges().get(i).getRightVertex().getColor() == Color.WHITE){
                    u.getEdges().get(i).getRightVertex().setColor(Color.GRAY);
                    u.getEdges().get(i).getRightVertex().setDistance(u.getDistance() + 1);
                    u.getEdges().get(i).getRightVertex().setFather(u);
                    queue.add(u.getEdges().get(i).getRightVertex());
                }
            }
            u.setColor(Color.BLACK);
        }
    }

    @Override
    public ArrayList<T> dijkstra(T startVertex, T endVertex) throws VertexNotFoundException, VertexNotAchievableException {
        //Validations
        if (!searchVertex(startVertex) || !searchVertex(endVertex)) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        bfs(startVertex);
        int startVertexIndex = searchVertexIndex(startVertex);
        int endVertexIndex = searchVertexIndex(endVertex);
        if (list.get(endVertexIndex).getDistance() == Integer.MAX_VALUE) {
            throw new VertexNotAchievableException("Error. Vertex not achievable.");
        }
        //Start algorithm
        ArrayList<ListEdge<T>> chain = new ArrayList<>(); //Fathers (return)
        PriorityQueue<ListVertex<T>> q = new PriorityQueue<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != startVertexIndex) {
                list.get(i).setDistance(Integer.MAX_VALUE);
            } else {
                list.get(i).setDistance(0);
            }
            list.get(i).setFather(null);
            q.add(list.get(i));
        }
        //Obtain edges
        while (!q.isEmpty() && q.peek() != list.get(endVertexIndex)) {
            ListVertex<T> u = q.poll();
            for (int i = 0; i < u.getEdges().size(); i++) {
                int alt = u.getDistance() + u.getEdges().get(i).getWeight();
                if (alt < u.getEdges().get(i).getRightVertex().getDistance()) {
                    u.getEdges().get(i).getRightVertex().setDistance(alt);
                    u.getEdges().get(i).getRightVertex().setFather(u);
                    //Update priority
                    q.remove(u.getEdges().get(i).getRightVertex());
                    q.add(u.getEdges().get(i).getRightVertex());
                }
            }
        }
        //Update chain
        ListVertex<T> vertexObj = list.get(endVertexIndex);
        ListEdge<T> minEdge = null;
        while (vertexObj.getFather() != null) {
            ListVertex<T> vertexFather = vertexObj.getFather();
            for (int i = 0; i < vertexFather.getEdges().size(); i++) {
                if (vertexFather.getEdges().get(i).getRightVertex() == vertexObj) {
                    if (minEdge == null) {
                        minEdge = vertexFather.getEdges().get(i);
                    } else if (minEdge.getWeight() > vertexFather.getEdges().get(i).getWeight()) {
                        minEdge = vertexFather.getEdges().get(i);
                    }
                }
            }
            chain.add(0, minEdge);
            //Restart
            minEdge = null;
            vertexObj = vertexFather;
        }
        ArrayList<T> values = new ArrayList<>();
        values.add(list.get(startVertexIndex).getValue());
        for (ListEdge<T> tListEdge : chain) {
            values.add(tListEdge.getRightVertex().getValue());
        }
        return values;
    }

    @Override
    public ArrayList<Pair<T, T>> prim(T value) throws VertexNotFoundException, VertexNotAchievableException {
        return null;
    }
}
