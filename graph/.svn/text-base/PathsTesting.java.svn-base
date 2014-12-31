package graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.HashMap;

/** Unit tests for the ShortestPath class.
 *  @author Rafayel Mkrtchyan
 */

public class PathsTesting {

    @Test
    public void testSimpleShortPath() {
        DirectedGraph ug = new DirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.remove(1);
        ug.add(2, 3);
        ug.add(4, 2);
        ug.add(4, 3);
        ug.add(4, 5);
        ug.add(5, 6);
        ug.add(5, 3);
        ug.add(6, 7);
        VideoGraphPaths path = new VideoGraphPaths(ug, 4, 3);
        path.setDistance(4, 102.0);
        path.setDistance(2, 4.0);
        path.setDistance(5, 5.1);
        path.setDistance(6, 40.0);
        path.setDistance(3, 0.0);
        path.setDistance(7, 4000.5);
        path.setWeight(4, 2, 12.2);
        path.setWeight(2, 3, 6.5);
        path.setWeight(4, 5, 11.2);
        path.setWeight(4, 3, 102.0);
        path.setWeight(5, 6, 30.0);
        path.setWeight(5, 3, 9.1);
        path.setWeight(6, 7, 3244.2);
        path.setPaths();
        List<Integer> shortPath = path.pathTo();
        int mySource = path.getSource();
        int myDestination = path.getDest();
        assertEquals(4, mySource);
        assertEquals(3, myDestination);
        assertArrayEquals(shortPath.toArray(), new Integer[]{4, 2, 3});
        boolean firstCond = path.getPredecessor(5) == 4;
        boolean secondCond = path.getPredecessor(7) == 0;
        assertTrue(firstCond);
        assertTrue(path.getChecker());
        assertTrue(secondCond);
    }

    private class VideoGraphPaths extends SimpleShortestPaths {

        public VideoGraphPaths(Graph G, int source, int dest) {
            super(G, source, dest);
            weightData = new HashMap<Integer, Double>();
            distanceData = new HashMap<Integer, Double>();
        }

        public void setWeight(int u, int v, double weight) {
            int id = _G.edgeId(u, v);
            weightData.put(id, weight);
        }

        @Override
        protected double estimatedDistance(int v) {
            checker = true;
            return distanceData.get(v);
        }

        public void setDistance(int v, double distance) {
            distanceData.put(v, distance);
        }

        @Override
        public double getWeight(int u, int v) {
            int id = _G.edgeId(u, v);
            return weightData.get(id);
        }

        public boolean getChecker() {
            return checker;
        }

        private HashMap<Integer, Double> weightData;
        private HashMap<Integer, Double> distanceData;
        boolean checker = false;
    }
}
