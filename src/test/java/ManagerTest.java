import com.wf.wfdeliverysystem.exceptions.*;
import javafx.util.*;
import java.util.*;
import com.wf.wfdeliverysystem.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    // FR2 - Create House connections
    @Test
    void addHouseConnection() {
        sampleGraphSetup();
        assertDoesNotThrow( () -> manager.addEdge(houses.get(3), houses.get(7), "", 30) );
        assertTrue(manager.checkPathBetweenHouses(houses.get(3), houses.get(7)));
    }

    @Test
    void addExistingConnection() {
        sampleGraphSetup();
        assertThrows( MultipleEdgesNotAllowedException.class, () -> manager.addEdge( houses.get(3),houses.get(6), "", 100) );
    }
    @Test
    void addEdgesNonExistentHouses() {
        initHouses();
        assertThrows( VertexNotFoundException.class, () -> manager.addEdge( houses.get(3),houses.get(6), "", 100) );
    }

    // FR3 - Verify path between houses
    @Test
    void checkNonExistentPath() {
        sampleGraphSetup();
        assertFalse( manager.checkPathBetweenHouses(houses.get(10), houses.get(14)) );
    }

    @Test
    void checkExistentLongPath() {
        sampleGraphSetup();
        assertTrue( manager.checkPathBetweenHouses(houses.get(10), houses.get(9)) );
    }

    @Test
    void checkEdgeAsPath() {
        sampleGraphSetup();
        assertTrue( manager.checkPathBetweenHouses(houses.get(1), houses.get(3)) );
    }

    // FR4 - Calculate minimum Path
    @Test
    void minimumPathIsLonger() throws VertexNotAchievableException, VertexNotFoundException {
        sampleGraphSetup();
        ArrayList<Pair<House, House>> path = manager.calculateMinimumPath(houses.get(1), houses.get(3));
        assertEquals(3, path.size());
    }

    @Test
    void minimumPathIsEdge() throws VertexNotAchievableException, VertexNotFoundException {
        sampleGraphSetup();
        ArrayList<Pair<House, House>> path = manager.calculateMinimumPath(houses.get(1), houses.get(10));
        assertEquals(1, path.size());
    }
    @Test
    void minimumPathDoesNotExist() {
        sampleGraphSetup();
        assertThrows( VertexNotAchievableException.class, () -> manager.calculateMinimumPath(houses.get(1), houses.get(13)));
    }

    // FR5 - Calculate tour
    @Test
    void unconnectedHouseTour() throws VertexNotFoundException {
        sampleGraphSetup();
        House newHouse = new House(1, null, null, null, 15+"");
        houses.add(newHouse);
        try {
            manager.addHouse(newHouse);
        } catch (VertexAlreadyAddedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Pair<House, House>> path = manager.generateDeliveryTour(houses.get(15));
        boolean hasEdges = false;
        for(Pair<House, House> p : path) {
            if( p.getKey().equals(houses.get(15)) ) hasEdges = true;
        }
        assertFalse(hasEdges);

    }

    @Test
    void calculateTourLinear() throws VertexNotFoundException {
        sampleGraphSetup();
        ArrayList<Pair<House, House>> path = manager.generateDeliveryTour(houses.get(11));
        boolean visited = false;
        for(Pair<House, House> p : path) {
            if( p.getValue().equals(houses.get(14)) ) {
                visited = true;
                assertEquals(houses.get(12), p.getKey()); // 14 is reachable with the minimum path when going to 12 first
            }
        }
        assertTrue(visited);
    }

    @Test
    void calculateTourCheckVisited() throws VertexNotFoundException {
        sampleGraphSetup();
        ArrayList<Pair<House, House>> path = manager.generateDeliveryTour(houses.get(3));
        HashMap<House, Boolean> visited = new HashMap<>();
        for(Pair<House, House> p : path) {
            visited.put(p.getValue(), true);
        }
        for(int i=1; i<=10; i++) {
            if(i != 3) {
                assertTrue( visited.get(houses.get(i)) );
            }
        }
    }

}