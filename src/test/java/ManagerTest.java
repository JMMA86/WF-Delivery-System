import com.wf.wfdeliverysystem.implementations.IGraph;
import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.implementations.ListGraph;
import com.wf.wfdeliverysystem.implementations.MatrixGraph;
import javafx.util.Pair;

import java.util.ArrayList;
import com.wf.wfdeliverysystem.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    private Manager manager;
    private ArrayList<House> houses;

    @BeforeEach
    void initEmptyGraphs() {
        manager = new Manager(128);
        houses = new ArrayList<>();
        houses.add(null);
    }

    void initHouses() {
        House newHouse;
        for(int i=1; i<=14; i++) {
            // fields related to gui are set null
            newHouse = new House(1, null, null, null, i+"");
                houses.add(newHouse);
        }
    }

    void sampleGraphSetup() {
        initHouses();
        for(int i=0; i<houses.size()-1; i++) {
            try {
                manager.addHouse(houses.get(i+1));
            } catch (VertexAlreadyAddedException e) {
                throw new RuntimeException(e);
            }
        }

        // Vertices 1 to 10 are connected, 11 to 14 too.
        try {
            // tree 1
            manager.addEdge(houses.get(1), houses.get(2), "", 25 );
            manager.addEdge(houses.get(1), houses.get(10), "", 5 );
            manager.addEdge(houses.get(1), houses.get(4), "", 20 );
            manager.addEdge(houses.get(1), houses.get(5), "", 10 );
            manager.addEdge(houses.get(1), houses.get(3), "", 80 );
            manager.addEdge(houses.get(6), houses.get(2), "", 15 );
            manager.addEdge(houses.get(6), houses.get(3), "", 100 );
            manager.addEdge(houses.get(6), houses.get(7), "", 8 );
            manager.addEdge(houses.get(7), houses.get(9), "", 9 );
            manager.addEdge(houses.get(8), houses.get(3), "", 20 );
            manager.addEdge(houses.get(8), houses.get(5), "", 9 );
            // tree 2
            manager.addEdge(houses.get(12), houses.get(11), "", 15 );
            manager.addEdge(houses.get(12), houses.get(14), "", 25 );
            manager.addEdge(houses.get(12), houses.get(13), "", 30);
            manager.addEdge(houses.get(13), houses.get(14), "", 10);

        } catch (LoopsNotAllowedException | MultipleEdgesNotAllowedException | VertexNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    // FR1 - House creation
    @Test
    void addHousesEmptyGraph() {
        initHouses();
        for(int i=1; i<=10; i++) {
            int j = i;
            assertDoesNotThrow( () -> manager.addHouse(houses.get(j)) );
        }
    }

    @Test
    void addNewHouseExistingGraph() {
        sampleGraphSetup();
        assertDoesNotThrow( () -> manager.addHouse( new House(1, null, null, null, 15+"") ) );
    }


    @Test
    void checkHouseLimit() throws VertexAlreadyAddedException {
        House newHouse;
        for(int i=1; i<=128; i++) {
            // fields related to gui are set null
            newHouse = new House(1, null, null, null, i+"");
            houses.add(newHouse);
            House h = newHouse;
            assertDoesNotThrow(() -> manager.addHouse(h));
        }
    }



}