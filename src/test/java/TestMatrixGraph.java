import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.implementations.IGraph;
import com.wf.wfdeliverysystem.implementations.MatrixGraph;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class TestMatrixGraph {
    private IGraph<String> matrixGraph;
    private IGraph<Integer> matrixGraphInt;

    void setUpGraph() {
        matrixGraph = new MatrixGraph<>(false, 5);
    }

    void addFiveVertexes() throws VertexAlreadyAddedException {
        setUpGraph();
        matrixGraph.addVertex("a");
        matrixGraph.addVertex("b");
        matrixGraph.addVertex("c");
        matrixGraph.addVertex("d");
        matrixGraph.addVertex("e");
    }

    void intGraphSetup1() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        matrixGraphInt = new MatrixGraph<>(true, 8);
        //Adding vertices
        for (int i = 0; i <= 7; i++) {
            matrixGraphInt.addVertex(i);
        }
        //Adding edges
        matrixGraphInt.addEdge(0, 1, "v1", 4);
        matrixGraphInt.addEdge(0, 2, "v1", 3);
        matrixGraphInt.addEdge(0, 4, "v1", 8);
        matrixGraphInt.addEdge(1, 6, "v1", 4);
        matrixGraphInt.addEdge(1, 5, "v1", 2);
        matrixGraphInt.addEdge(2, 0, "v1", 3);
        matrixGraphInt.addEdge(2, 5, "v1", 5);
        matrixGraphInt.addEdge(3, 1, "v1", 9);
        matrixGraphInt.addEdge(3, 7, "v1", 1);
        matrixGraphInt.addEdge(5, 3, "v1", 3);
        matrixGraphInt.addEdge(6, 2, "v1", 2);
        matrixGraphInt.addEdge(6, 5, "v1", 2);
        matrixGraphInt.addEdge(6, 7, "v1", 7);
    }

    @Test
    public void addMultipleEdges() {
        try {
            addFiveVertexes();
            matrixGraph.addEdge("a", "c", "1", 10);
            matrixGraph.addEdge("a", "b", "1", 10);
            matrixGraph.addEdge("b", "d", "1", 10);
            matrixGraph.addEdge("d", "e", "1", 10);

            assertTrue(matrixGraph.searchEdge("a", "c", "1"));
            assertTrue(matrixGraph.searchEdge("b", "d", "1"));
            assertFalse(matrixGraph.searchEdge("d", "a", "1"));
        } catch (VertexAlreadyAddedException | VertexNotFoundException | LoopsNotAllowedException |
                 MultipleEdgesNotAllowedException e) {
            fail();
        }
    }

    @Test
    public void deleteVertexMultipleConnections() {
        try {
            addFiveVertexes();
            matrixGraph.addEdge("a", "c", "1", 10);
            matrixGraph.addEdge("a", "b", "1", 10);
            matrixGraph.addEdge("b", "c", "1", 10);

            matrixGraph.deleteVertex("a");

            assertFalse(matrixGraph.searchEdge("b", "a", "1"));
            fail();
            assertFalse(matrixGraph.searchEdge("a", "c", "1"));
            fail();
            assertFalse(matrixGraph.searchEdge("a", "b", "1"));
            fail();
            assertFalse(matrixGraph.searchEdge("c", "a", "1"));
            fail();

        } catch (VertexAlreadyAddedException | LoopsNotAllowedException | MultipleEdgesNotAllowedException e){
            fail();
        } catch (VertexNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteMultipleEdges() {
        try {
            addFiveVertexes();
            matrixGraph.addEdge("a", "c", "1", 10);
            matrixGraph.addEdge("a", "b", "1", 10);
            matrixGraph.addEdge("b", "d", "1", 10);
            matrixGraph.addEdge("d", "e", "1", 10);

            matrixGraph.deleteEdge("a", "b", "1");
            matrixGraph.deleteEdge("b", "d", "1");

            assertFalse(matrixGraph.searchEdge("b", "d", "1"));
            assertFalse(matrixGraph.searchEdge("a", "b", "1"));
        } catch (VertexAlreadyAddedException | VertexNotFoundException | LoopsNotAllowedException |
                 MultipleEdgesNotAllowedException | EdgeNotFoundException e) {
            fail();
        }
    }

    //bfs()

    @Test
    public void calculateDistanceOnGuidedGraphUsingBfs() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException, VertexNotAchievableException {
        //Arrange
        intGraphSetup1();
        //Art
        int distance = matrixGraphInt.calculateDistance(0, 6);
        //Assert
        assertEquals(distance, 2);
    }

    @Test
    public void calculateDistanceOfNonAchievableVerticesException() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        intGraphSetup1();
        //Assert
        assertThrows(VertexNotAchievableException.class, () -> matrixGraphInt.calculateDistance(4, 2));
    }

    //dijkstra()

    @Test
    public void dijkstraOnDirectedGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException, VertexNotAchievableException {
        //Arrange
        intGraphSetup1();
        //Art
        ArrayList<Pair<Integer, Integer>> result = matrixGraphInt.dijkstra(6, 7);
        int[] validation = {6, 5, 5, 3, 3, 7};
        //Assert
        int index = 0;
        for (Pair<Integer, Integer> integerIntegerPair : result) {
            assertEquals((int) integerIntegerPair.getKey(), validation[index]);
            assertEquals((int) integerIntegerPair.getValue(), validation[index + 1]);
            index += 2;
        }
    }

    //prim()

    @Test
    public void createMinimumSpreadTree() {
        try {
            addFiveVertexes();
            matrixGraph.addEdge("a", "c", "1", 10);
            matrixGraph.addEdge("a", "b", "1", 5);
            matrixGraph.addEdge("c", "b", "1", 3);
            matrixGraph.addEdge("c", "e", "1", 3);
            matrixGraph.addEdge("b", "d", "1", 2);
            matrixGraph.addEdge("e", "a", "1", 1);
            matrixGraph.addEdge("d", "e", "1", 6);

            ArrayList<Pair<String, String>> mst = matrixGraph.prim("c");

            assertEquals(4, mst.size());

            for (Pair<String, String> p : mst) {
                assertTrue(matrixGraph.searchEdge(p.getKey(), p.getValue(), ""));
                //System.out.println("Origin: " + p.getKey() + " Dest: " + p.getValue());
            }
        } catch (LoopsNotAllowedException | VertexAlreadyAddedException | MultipleEdgesNotAllowedException |
                 VertexNotFoundException e) {
            fail();
        }
    }
}
