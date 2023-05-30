package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;

import java.util.*;

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
     *
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
     *
     * @param start  origin vertex
     * @param end    destination vertex
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
        if (searchEdgeIndex(list.get(startVertex), list.get(endVertex), id) != -1 && !isMultiple) {
            throw new MultipleEdgesNotAllowedException("Error. Multiple edges between vertex not allowed.");
        }
        if (!isGuided) {
            list.get(endVertex).getEdges().add(new ListEdge<>(list.get(endVertex), list.get(startVertex), id, weight));
        }
        list.get(startVertex).getEdges().add(new ListEdge<>(list.get(startVertex), list.get(endVertex), id, weight));
    }

    @Override
    public boolean searchEdge(T start, T end, String id) throws VertexNotFoundException {
        if (searchVertexIndex(start) == -1 || searchVertexIndex(end) == -1) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        int startIndex = searchVertexIndex(start);
        for (int i = 0; i < list.get(startIndex).getEdges().size(); i++) {
            ListEdge<T> edge = list.get(startIndex).getEdges().get(i);
            if (edge.getRightVertex().getValue() == end && edge.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a vertex including all associated edges
     *
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
     *
     * @param start origin vertex
     * @param end   destination vertex
     * @throws EdgeNotFoundException   if edge doesn't exist
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
    @Override
    public void deleteEdge(T start, T end, String id) throws EdgeNotFoundException, VertexNotFoundException {
        int startIndex = searchVertexIndex(start);
        int endIndex = searchVertexIndex(end);
        if (startIndex == -1 || endIndex == -1) {
            throw new VertexNotFoundException("Error. One vertex not found.");
        }
        if (searchEdgeIndex(list.get(startIndex), list.get(endIndex), id) == -1) {
            throw new EdgeNotFoundException("Error. Edge not found: " + start + " -> " + end + " (" + id + ")");
        }
        if (!isGuided) {
            list.get(endIndex).getEdges().remove(searchEdgeIndex(list.get(endIndex), list.get(startIndex), id));
        }
        list.get(startIndex).getEdges().remove(searchEdgeIndex(list.get(startIndex), list.get(endIndex), id));
    }

    /**
     * Used for other methods, returns index from list graph of any vertex
     *
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
     *
     * @param start origin vertex
     * @param end   destination vertex
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

    @Override
    public int calculateDistance(T start, T end) throws VertexNotFoundException, VertexNotAchievableException {
        if (searchVertexIndex(start) == -1 || searchVertexIndex(end) == -1) throw new VertexNotFoundException("Error. One vertex not found.");
        bfs(start);
        if (list.get(searchVertexIndex(end)).getColor() == Color.WHITE) {
            throw new VertexNotAchievableException("Error. Vertex not achievable.");
        } else {
            return list.get(searchVertexIndex(end)).getDistance();
        }
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
                if (u.getEdges().get(i).getRightVertex().getColor() == Color.WHITE) {
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
    public ArrayList<Pair<T, T>> dijkstra(T startVertex, T endVertex) throws VertexNotFoundException, VertexNotAchievableException {
        //Validations
        if (searchVertexIndex(startVertex) == -1 || searchVertexIndex(endVertex) == -1) {
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
        PriorityQueue<ListVertex<T>> q = new PriorityQueue<>(Comparator.comparingInt(ListVertex::getDistance));
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
        ArrayList<Pair<T, T>> values = new ArrayList<>();
        for (ListEdge<T> tListEdge : chain) {
            values.add(new Pair<>(tListEdge.getLeftVertex().getValue(), tListEdge.getRightVertex().getValue()));
        }
        return values;
    }

    @Override
    public ArrayList<Pair<T, T>> prim(T value) throws VertexNotFoundException {
        int originPos = searchVertexIndex(value);
        if (originPos == -1) throw new VertexNotFoundException("The vertex was not found");
        ArrayList<Pair<T, T>> predecessors = new ArrayList<>();
        PriorityQueue<ListVertex<T>> toVisit = new PriorityQueue<>(Comparator.comparingInt(ListVertex::getDistance));
        for (ListVertex<T> lv : list) {
            if (lv != list.get(originPos)) {
                lv.setDistance(Integer.MAX_VALUE);
                lv.setColor(Color.WHITE);
                toVisit.add(lv);
            }
        }

        list.get(originPos).setDistance(0);
        toVisit.add(list.get(originPos));

        while (!toVisit.isEmpty()) {
            ListVertex<T> temp = toVisit.poll();

            for (ListEdge<T> ad : temp.getEdges()) {
                if (ad.getRightVertex().getColor().equals(Color.WHITE) && ad.getWeight() < ad.getRightVertex().getDistance()) {
                    toVisit.remove(ad.getRightVertex());
                    ad.getRightVertex().setDistance(ad.getWeight());
                    toVisit.add(ad.getRightVertex());
                    predecessors.add(new Pair<>(temp.getValue(), ad.getRightVertex().getValue()));
                    ad.getRightVertex().setColor(Color.BLACK);
                }
            }

            temp.setColor(Color.BLACK);
        }

        return predecessors;
    }
}
