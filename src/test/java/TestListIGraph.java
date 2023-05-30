import com.wf.wfdeliverysystem.implementations.IGraph;
import com.wf.wfdeliverysystem.implementations.ListGraph;
import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.Pair;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestListIGraph {
    private IGraph<Integer> graphAdjacencyList;

    void newGraphSetup01() {
        /*
         * Properties
         * - Guided
         * - No multiple
         * - Do not allow loops
         */

        graphAdjacencyList = new ListGraph<>(true, false, false);
    }

    void newGraphSetup02() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        /*
         * Properties
         * - Guided
         * - No multiple
         * - Do not allow loops
         */

        graphAdjacencyList = new ListGraph<>(true, false, false);

        //Adding vertices
        for (int i = 0; i <= 7; i++) {
            graphAdjacencyList.addVertex(i);
        }
        //Adding edges
        graphAdjacencyList.addEdge(0, 1, "v1", 4);
        graphAdjacencyList.addEdge(0, 2, "v1", 3);
        graphAdjacencyList.addEdge(0, 4, "v1", 8);
        graphAdjacencyList.addEdge(1, 6, "v1", 4);
        graphAdjacencyList.addEdge(1, 5, "v1", 2);
        graphAdjacencyList.addEdge(2, 0, "v1", 3);
        graphAdjacencyList.addEdge(2, 5, "v1", 5);
        graphAdjacencyList.addEdge(3, 1, "v1", 9);
        graphAdjacencyList.addEdge(3, 7, "v1", 1);
        graphAdjacencyList.addEdge(5, 3, "v1", 3);
        graphAdjacencyList.addEdge(6, 2, "v1", 2);
        graphAdjacencyList.addEdge(6, 5, "v1", 2);
        graphAdjacencyList.addEdge(6, 7, "v1", 7);
    }

    void newGraphSetup03() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        /*
         * Properties
         * - No guided
         * - Multiple
         * - Allows loops
         */
        graphAdjacencyList = new ListGraph<>(false, true, true);

        //Adding vertices
        for (int i = 0; i <= 7; i++) {
            graphAdjacencyList.addVertex(i);
        }
        //Adding edges
        graphAdjacencyList.addEdge(0, 1, "v1", 5);
        graphAdjacencyList.addEdge(2, 6, "v1", 4);
        graphAdjacencyList.addEdge(4, 6, "v1", 8);
        graphAdjacencyList.addEdge(6, 7, "v1", 7);
        graphAdjacencyList.addEdge(3, 7, "v1", 6);
        graphAdjacencyList.addEdge(3, 5, "v1", 7);
        graphAdjacencyList.addEdge(0, 0, "v1", 1);
    }

    /* In most of the tests, the methods of searchVertex() and searchEdge() are used, so their correct
     * functionality is already proved.
     */

    //addVertex()

    @Test
    public void addVertexTest() {
        //Arrange
        newGraphSetup01();
        //Art
        try {
            graphAdjacencyList.addVertex(0);
            //Assert
        } catch (VertexAlreadyAddedException e) {
            fail();
        }
    }

    @Test
    public void addMultipleVertex() {
        //Arrange
        newGraphSetup01();
        //Art
        for (int i = 0; i <= 7; i++) {
            try {
                graphAdjacencyList.addVertex(i);
                //Assert
            } catch (VertexAlreadyAddedException e) {
                fail();
            }

        }
    }

    @Test
    public void addDuplicatedVertexException() throws VertexAlreadyAddedException {
        //Arrange
        newGraphSetup01();
        //Art
        graphAdjacencyList.addVertex(0);
        //Assert
        assertThrows(VertexAlreadyAddedException.class, () -> graphAdjacencyList.addVertex(0));
    }

    //addEdge

    @Test
    public void addEdgeTest() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup01();
        //Art
        graphAdjacencyList.addVertex(1);
        graphAdjacencyList.addVertex(2);
        graphAdjacencyList.addEdge(1, 2, "v1", 4);
        //Assert
        assertTrue(graphAdjacencyList.searchEdge(1, 2, "v1"));
    }

    @Test
    public void addMultiplesEdgesTest() throws LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException, VertexAlreadyAddedException {
        //Arrange
        newGraphSetup01();
        //Art
        //Adding vertices
        for (int i = 0; i <= 7; i++) {
            graphAdjacencyList.addVertex(i);
        }
        //Adding edges
        graphAdjacencyList.addEdge(0, 3, "v1", 5);
        graphAdjacencyList.addEdge(0, 4, "v1", 2);
        graphAdjacencyList.addEdge(1, 2, "v1", 7);
        graphAdjacencyList.addEdge(2, 0, "v1", 9);
        graphAdjacencyList.addEdge(3, 1, "v1", 4);
        graphAdjacencyList.addEdge(5, 7, "v1", 3);
        graphAdjacencyList.addEdge(6, 4, "v1", 8);
        graphAdjacencyList.addEdge(7, 6, "v1", 1);
        graphAdjacencyList.addEdge(4, 2, "v1", 6);

        //Assert
        assertTrue(graphAdjacencyList.searchEdge(0, 3, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(0, 4, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(1, 2, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(2, 0, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(3, 1, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(5, 7, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(6, 4, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(7, 6, "v1"));
        assertTrue(graphAdjacencyList.searchEdge(4, 2, "v1"));
    }

    @Test
    public void addEdgeNoMultipleException() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup01();
        //Art
        graphAdjacencyList.addVertex(1);
        graphAdjacencyList.addVertex(2);
        graphAdjacencyList.addEdge(1, 2, "v1", 4);
        //Assert
        assertThrows(MultipleEdgesNotAllowedException.class, () -> graphAdjacencyList.addEdge(1, 2, "v1", 4));
    }

    //deleteVertex()

    @Test
    public void deleteVertexTest() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Art
        graphAdjacencyList.deleteVertex(5);
        //Assert
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.searchEdge(1, 5, "v1"));
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.searchEdge(5, 3, "v1"));
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.searchEdge(2, 5, "v1"));
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.searchEdge(6, 5, "v1"));
    }

    @Test
    public void deleteMultipleVertexTest() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Art
        try {
            graphAdjacencyList.deleteVertex(0);
            graphAdjacencyList.deleteVertex(2);
        } catch (VertexNotFoundException e) {
            fail();
        }
    }

    @Test
    public void deleteVertexWithLoops() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup03();
        //Art
        graphAdjacencyList.deleteVertex(0);
        //Assert
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.searchEdge(0, 1, "v1"));
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.searchEdge(0, 0, "v1"));
    }

    //deleteEdge()

    @Test
    public void deleteEdgeTest() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException, EdgeNotFoundException {
        //Arrange
        newGraphSetup01();
        //Art
        graphAdjacencyList.addVertex(1);
        graphAdjacencyList.addVertex(2);
        graphAdjacencyList.addEdge(1, 2, "v1", 7);
        graphAdjacencyList.deleteEdge(1, 2, "v1");
        //Assert
        assertFalse(graphAdjacencyList.searchEdge(1, 2, "v1"));
    }

    @Test
    public void deleteManyEdgesOnMultipleGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException, EdgeNotFoundException {
        //Arrange
        newGraphSetup03();
        //Art
        graphAdjacencyList.addEdge(1, 0, "v2",1);
        graphAdjacencyList.deleteEdge(1, 0, "v1");
        assertFalse(graphAdjacencyList.searchEdge(1, 0, "v1"));
    }

    @Test
    public void deleteNonExistingEdgeException() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Assert
        assertThrows(EdgeNotFoundException.class, () -> graphAdjacencyList.deleteEdge(1, 0, "v1"));
    }

    //bfs()

    @Test
    public void calculateDistanceOnGuidedGraphUsingBfs() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException, VertexNotAchievableException {
        //Arrange
        newGraphSetup02();
        //Art
        int distance = graphAdjacencyList.calculateDistance(0, 6);
        //Assert
        assertEquals(distance, 2);
    }

    @Test
    public void calculateDistanceOfNonAchievableVerticesException() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Assert
        assertThrows(VertexNotAchievableException.class, () -> graphAdjacencyList.calculateDistance(4, 2));
    }

    @Test
    public void calculateDistanceOnNonGuidedGraphUsingBfs() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException, VertexNotAchievableException {
        //Arrange
        newGraphSetup03();
        //Art
        int distance = graphAdjacencyList.calculateDistance(3, 2);
        //Assert
        assertEquals(distance, 3);
    }

    //dijkstra()

    @Test
    public void dijkstraOnDirectedGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException, VertexNotAchievableException {
        //Arrange
        newGraphSetup02();
        //Art
        ArrayList<Pair<Integer, Integer>> result = graphAdjacencyList.dijkstra(6, 7);
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
    public void createMinimumSpanTree() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Art
        ArrayList<Pair<Integer, Integer>> mst = graphAdjacencyList.prim(0);
        //Assert
        assertEquals(7, mst.size());

        for (Pair<Integer, Integer> p : mst) {
            graphAdjacencyList.searchEdge(p.getKey(), p.getValue(), "v1");
            //System.out.println("Origin: " + p.getKey() + " Dest: " + p.getValue());
        }
    }
}