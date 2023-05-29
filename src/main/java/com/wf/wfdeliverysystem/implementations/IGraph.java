package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;
import java.util.ArrayList;

public interface IGraph<T> {
    //Adds a vertex in the graph
    void addVertex(T vertex) throws VertexAlreadyAddedException;
    //Adds an edge in the graph (always between two vertices)
    void addEdge(T start, T end, String id, int weight) throws VertexNotFoundException, LoopsNotAllowedException, MultipleEdgesNotAllowedException;
    //Returns true if an edge exists
    boolean searchEdge(T start, T end, String id) throws VertexNotFoundException;
    //Deletes a vertex from the graph
    void deleteVertex(T vertex) throws VertexNotFoundException;
    //Deletes an edge from the graph
    void deleteEdge(T start, T end, String id) throws EdgeNotFoundException, VertexNotFoundException;
    //Does the bfs algorithm from a given vertex
    void bfs(T value) throws VertexNotFoundException;
    //Does the Dijkstra algorithm
    ArrayList<Pair<T, T>> dijkstra(T startVertex, T endVertex) throws VertexNotFoundException, VertexNotAchievableException;
    //Calculates the minimum spread tree by using Floyd Warshall algorithm (Prim)
    ArrayList<Pair<T, T>> prim(T value) throws VertexNotFoundException;
}