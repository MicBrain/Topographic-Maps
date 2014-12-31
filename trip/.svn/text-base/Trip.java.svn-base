package trip;

import graph.DirectedGraph;
import graph.LabeledGraph;
import graph.SimpleShortestPaths;

import java.io.FileReader;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.io.FileNotFoundException;

import static trip.Main.error;

/** Encapsulates a map containing sites, positions, and road distances between
 *  them.
 *  @author Rafayel Mkrtchyan
 */
class Trip {

    /** Read map file named NAME into out map graph. */
    void readMap(String name) {
        int n;
        n = 0;
        try {
            Scanner inp = new Scanner(new FileReader(name));
            while (inp.hasNext()) {
                n += 1;
                switch (inp.next()) {
                case "L":
                    addLocation(inp.next(), inp.nextDouble(), inp.nextDouble());
                    break;
                case "R":
                    addRoad(inp.next(), inp.next(), inp.nextDouble(),
                            Direction.parse(inp.next()), inp.next());
                    break;
                default:
                    error("map entry #%d: unknown type", n);
                    break;
                }
            }
        } catch (FileNotFoundException excp) {
            error(excp.getMessage());
        } catch (InputMismatchException excp) {
            error("bad entry #%d", n);
        } catch (NoSuchElementException excp) {
            error("entry incomplete at end of file");
        }
    }

    /** Produce a report on the standard output of a shortest journey from
     *  DESTS[0], then DESTS[1], .... */
    void makeTrip(String[] dests) {
        if (dests.length < 2) {
            error("must have at least two locations for a trip");
        }

        System.out.printf("From %s:%n%n", dests[0]);
        int step;

        step = 1;
        for (int i = 1; i < dests.length; i += 1) {
            Integer
                from = _sites.get(dests[i - 1]),
                to = _sites.get(dests[i]);
            if (from == null) {
                error("No location named %s", dests[i - 1]);
            } else if (to == null) {
                error("No location named %s", dests[i]);
            }
            TripPlan plan = new TripPlan(from, to);
            plan.setPaths();
            List<Integer> segment = plan.pathTo(to);
            step = reportSegment(step, from, segment);
        }
    }

    /** Print out a written description of the location sequence SEGMENT,
     *  starting at FROM, and numbering the lines of the description starting
     *  at SEQ.  That is, FROM and each item in SEGMENT are the
     *  numbers of vertices representing locations.  Together, they
     *  specify the starting point and vertices along a path where
     *  each vertex is joined to the next by an edge.  Returns the
     *  next sequence number.  The format is as described in the
     *  project specification.  That is, each line but the last in the
     *  segment is formated like this example:
     *      1. Take University_Ave west for 0.1 miles.
     *  and the last like this:
     *      5. Take I-80 west for 8.4 miles to San_Francisco.
     *  Adjacent roads with the same name and direction are combined.
     *  */
    int reportSegment(int seq, int from, List<Integer> segment) {
        int indexOfSeq = segment.indexOf((Integer) from);
        String finalRoadName = "";
        String finalreadDir = "";
        double finalpostLength = 0;
        String dest = "";
        boolean checker = false;
        while (indexOfSeq < segment.size() - 2) {
            Road currentRoad = _map.getLabel(segment.get(indexOfSeq),
                segment.get(indexOfSeq + 1));
            String roadName = currentRoad.toString();
            String roadDirection = currentRoad.direction().fullName();
            double roadLength = currentRoad.length();
            finalpostLength += roadLength;
            Road nextRoad = _map.getLabel(segment.get(indexOfSeq + 1),
                segment.get(indexOfSeq + 2));
            double nextRoadlength = nextRoad.length();
            if (indexOfSeq < segment.size() - 3) {
                if (roadName.equals(nextRoad.toString())
                    && roadDirection.equals(nextRoad.direction().fullName())) {
                    finalRoadName = nextRoad.toString();
                    finalreadDir = nextRoad.direction().fullName();
                } else {
                    finalRoadName = roadName;
                    finalreadDir = roadDirection;
                    System.out.printf("%d. Take %s %s for %.1f miles.%n",
                        seq, finalRoadName, finalreadDir, finalpostLength);
                    seq++;
                    finalpostLength = 0;
                }
            } else {
                Road last = _map.getLabel(segment.get(indexOfSeq + 1),
                    segment.get(indexOfSeq + 2));
                dest = _map.getLabel(segment.get(segment.size() - 1)).
                    toString();
                finalRoadName = last.toString();
                finalreadDir = last.direction().fullName();
                if (roadName.equals(last.toString())
                    && roadDirection.equals(last.direction().fullName())) {
                    checker = true;
                    finalpostLength += last.length();
                } else {
                    System.out.printf("%d. Take %s %s for %.1f miles.%n",
                        seq, roadName, roadDirection, finalpostLength);
                    finalpostLength = last.length();
                    seq++;
                }
            }
            indexOfSeq++;
        }
        if (checker) {
            System.out.printf("%d. Take %s %s for %.1f miles to %s.%n",
                seq, finalRoadName, finalreadDir, finalpostLength, dest);
        } else {
            System.out.printf("%d. Take %s %s for %.1f miles to %s.%n",
                seq, finalRoadName, finalreadDir, finalpostLength, dest);
        }
        seq++;
        return seq;
    }

    /** Add a new location named NAME at (X, Y). */
    private void addLocation(String name, double x, double y) {
        if (_sites.containsKey(name)) {
            error("multiple entries for %s", name);
        }
        int v = _map.add(new Location(name, x, y));
        _sites.put(name, v);
    }

    /** Add a stretch of road named NAME from the Location named FROM
     *  to the location named TO, running in direction DIR, and
     *  LENGTH miles long.  Add a reverse segment going back from TO
     *  to FROM. */
    private void addRoad(String from, String name, double length,
                         Direction dir, String to) {
        Integer v0 = _sites.get(from),
            v1 = _sites.get(to);

        if (v0 == null) {
            error("location %s not defined", from);
        } else if (v1 == null) {
            error("location %s not defined", to);
        }
        Road currentRoad = new Road(name, dir, length);
        Road currentOppositeRoad = new Road(name, dir.reverse(), length);
        _map.add(v0, v1, currentRoad);
        _map.add(v1, v0, currentOppositeRoad);
    }

    /** Represents the network of Locations and Roads. */
    private RoadMap _map = new RoadMap();
    /** Mapping of Location names to corresponding map vertices. */
    private HashMap<String, Integer> _sites = new HashMap<>();

    /** A labeled directed graph of Locations whose edges are labeled by
     *  Roads. */
    private static class RoadMap extends LabeledGraph<Location, Road> {
        /** An empty RoadMap. */
        RoadMap() {
            super(new DirectedGraph());
        }
    }

    /** Paths in _map from a given location. */
    private class TripPlan extends SimpleShortestPaths {
        /** A plan for travel from START to DEST according to _map. */
        TripPlan(int start, int dest) {
            super(_map, start, dest);
            _finalLocation = _map.getLabel(dest);
        }

        @Override
        protected double getWeight(int u, int v) {
            return _map.getLabel(u, v).length();
        }

        @Override
        protected double estimatedDistance(int v) {
            return _finalLocation.dist(_map.getLabel(v));
        }

        /** Location of the destination. */
        private final Location _finalLocation;

    }

}
