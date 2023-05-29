import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.implementations.IGraph;
import com.wf.wfdeliverysystem.implementations.MatrixGraph;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestMatrixGraph {
    private IGraph<String> matrixGraph;

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

    @Test
    public void createMinimumSpreadTree() {
        try {
            addFiveVertexes();
            matrixGraph.addEdge("a", "c", "1", 10);
            matrixGraph.addEdge("a", "b", "1", 5);
            matrixGraph.addEdge("c", "b", "1", 3);
            matrixGraph.addEdge("c", "e", "1", 3);
            matrixGraph.addEdge("b", "d", "1", 2);
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
