import com.wf.wfdeliverysystem.implementations.ListGraph;
import com.wf.wfdeliverysystem.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestListGraph {
    private ListGraph<Integer> graphAdjacencyList;

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

    void newGraphSetup04() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        /*
         * Properties
         * - No guided
         * - Multiple
         * - Do not allow loops
         */

        graphAdjacencyList = new ListGraph<>(false, true, false);

        //Add Vertices
        for (int i = 0; i <= 7; i++) {
            graphAdjacencyList.addVertex(i);
        }

        //Add Edges
        graphAdjacencyList.addEdge(0, 3, "v1", 5);
        graphAdjacencyList.addEdge(0, 4, "v1", 2);
        graphAdjacencyList.addEdge(0, 4, "v2", 8);
        graphAdjacencyList.addEdge(0, 1, "v1", 9);
        graphAdjacencyList.addEdge(0, 2, "v1", 4);
        graphAdjacencyList.addEdge(1, 3, "v1", 1);
        graphAdjacencyList.addEdge(1, 3, "v2", 7);
        graphAdjacencyList.addEdge(1, 3, "v3", 3);
        graphAdjacencyList.addEdge(1, 6, "v1", 6);
        graphAdjacencyList.addEdge(2, 5, "v1", 5);
        graphAdjacencyList.addEdge(2, 6, "v1", 3);
        graphAdjacencyList.addEdge(2, 6, "v2", 2);
        graphAdjacencyList.addEdge(3, 5, "v1", 8);
        graphAdjacencyList.addEdge(6, 5, "v1", 7);
        graphAdjacencyList.addEdge(7, 5, "v1", 1);
        graphAdjacencyList.addEdge(7, 5, "v2", 4);
        graphAdjacencyList.addEdge(7, 5, "v3", 9);
        graphAdjacencyList.addEdge(7, 5, "v4", 6);
        graphAdjacencyList.addEdge(7, 6, "v1", 2);
        graphAdjacencyList.addEdge(7, 4, "v1", 3);
        graphAdjacencyList.addEdge(7, 3, "v1", 5);
    }

    /* In most of the tests, the methods of searchVertex() and searchEdge() are used, so their correct
     * functionality is already proved.
     */

    //addVertex()

    @Test
    public void addVertexTest() throws VertexAlreadyAddedException {
        //Arrange
        newGraphSetup01();
        //Art
        graphAdjacencyList.addVertex(0);
        //Assert
        assertTrue(graphAdjacencyList.searchVertex(0));
    }

    @Test
    public void addMultipleVertex() throws VertexAlreadyAddedException {
        //Arrange
        newGraphSetup01();
        //Art
        for (int i = 0; i <= 7; i++) {
            graphAdjacencyList.addVertex(i);
            //Assert
            assertTrue(graphAdjacencyList.searchVertex(i));
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
            assertTrue(graphAdjacencyList.searchVertex(i));
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
        assertEquals(graphAdjacencyList.getAllVertex().size(), 7);
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
        graphAdjacencyList.deleteVertex(0);
        graphAdjacencyList.deleteVertex(2);
        //Assert
        assertFalse(graphAdjacencyList.searchVertex(0));
        assertFalse(graphAdjacencyList.searchVertex(2));
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
        //Assert
        assertEquals(graphAdjacencyList.getAllNeighbors(0).size(), 1);
    }

    @Test
    public void deleteNonExistingEdgeException() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Assert
        assertThrows(EdgeNotFoundException.class, () -> graphAdjacencyList.deleteEdge(1, 0, "v1"));
    }

    //getAllVertex()

    @Test
    public void getAllVertexFromConnectedGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Art
        int vertexAmount = graphAdjacencyList.getAllVertex().size();
        //Assert
        assertEquals(vertexAmount, 8);
    }

    @Test
    public void getAllVertexFromNonConnectedGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup03();
        //Art
        int vertexAmount = graphAdjacencyList.getAllVertex().size();
        //Assert
        assertEquals(vertexAmount, 8);
    }

    @Test
    public void getAllVertexAfterDeletingSome() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Art
        graphAdjacencyList.deleteVertex(1);
        graphAdjacencyList.deleteVertex(2);
        graphAdjacencyList.deleteVertex(4);
        //Assert
        assertEquals(graphAdjacencyList.getAllVertex().size(), 5);
    }

    //getAllNeighbors()

    @Test
    public void getAllNeighborsFromNonExistingVertex() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup03();
        //Assert
        assertThrows(VertexNotFoundException.class, () -> graphAdjacencyList.getAllNeighbors(8));
    }

    @Test
    public void getAllNeighborsFromMultipleGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup04();
        //Art
        int neighborsAmount = graphAdjacencyList.getAllNeighbors(1).size();
        //Assert
        assertEquals(neighborsAmount, 3);
    }

    @Test
    public void getAllNeighborsFromGuidedGraph() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        //Arrange
        newGraphSetup02();
        //Art
        int neighborsAmount = graphAdjacencyList.getAllNeighbors(4).size();
        //Assert
        assertEquals(neighborsAmount, 0);
    }

    //bfs()

    @Test
    public void testCheckPathBetweenConnectedHouses() {

    }

}