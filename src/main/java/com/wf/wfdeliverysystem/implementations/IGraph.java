package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;
import java.util.ArrayList;

public interface IGraph<T> {
    /**
     * Adds a vertex in the graph
     *
     * @param vertex Value to ve inserted
     * @throws VertexAlreadyAddedException If vertex is already added
     */
    void addVertex(T vertex) throws VertexAlreadyAddedException;

    /**
     * Adds an edge in the graph (always between two vertices)
     *
     * @param start  origin vertex
     * @param end    destination vertex
     * @param weight connection weight
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
    void addEdge(T start, T end, String id, int weight) throws VertexNotFoundException, LoopsNotAllowedException, MultipleEdgesNotAllowedException;

    /**
     * Validates if an edge exist
     *
     * @param start origin vertex
     * @param end destination vertex
     * @param id id of vertex (in case of multiple graphs)
     * @return True if exists or false otherwise
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
    boolean searchEdge(T start, T end, String id) throws VertexNotFoundException;

    /**
     * Deletes a vertex including all associated edges
     *
     * @param vertex vertex to be deleted
     * @throws VertexNotFoundException if the vertex doesn't exist
     */
    void deleteVertex(T vertex) throws VertexNotFoundException;

    /**
     * Deletes an edge between two vertices
     *
     * @param start origin vertex
     * @param end   destination vertex
     * @throws EdgeNotFoundException   if edge doesn't exist
     * @throws VertexNotFoundException if one of the vertices doesn't exist
     */
    void deleteEdge(T start, T end, String id) throws EdgeNotFoundException, VertexNotFoundException;

    /**
     * Calculates minimum number of edges between two vertices
     *
     * @param start origin vertex
     * @param end destination vertex
     * @return number of vertices
     * @throws VertexNotFoundException if one vertex doesn't exist
     * @throws VertexNotAchievableException if both vertices are not related
     */
    int calculateDistance(T start, T end) throws VertexNotFoundException, VertexNotAchievableException;

    /**
     * Calculates distances from an origin vertex to the rest of vertices of the graph
     *
     * @param value origin vertex
     * @throws VertexNotFoundException if origin vertex doesn't exist
     */
    void bfs(T value) throws VertexNotFoundException;

    /**
     * Calculates the minimum path between two related vertices in a graph
     *
     * @param startVertex initial vertex
     * @param endVertex destination vertex
     * @return pairs of vertices (edges) that builds the path
     * @throws VertexNotFoundException if the initial of destination vertices don't exist
     * @throws VertexNotAchievableException if both vertices are not related
     */
    ArrayList<Pair<T, T>> dijkstra(T startVertex, T endVertex) throws VertexNotFoundException, VertexNotAchievableException;

    /**
     * Calculates the MST of a graph from an origin vertex
     *
     * @param value origin vertex
     * @return returns pairs of vertices (edges) that builds the MST
     * @throws VertexNotFoundException if the origin vertex doesn't exist
     * @throws LoopsNotAllowedException if the graph contains loops
     */
    ArrayList<Pair<T, T>> prim(T value) throws VertexNotFoundException, LoopsNotAllowedException;
}