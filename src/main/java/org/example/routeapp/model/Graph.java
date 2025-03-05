package org.example.routeapp.model;

import java.text.SimpleDateFormat;

import java.util.*;

public class Graph {
    //the graph will be represented as an adjacency list.
    //map to store each node (vertex) and its list of adjacent nodes (edges).
    private Map<Location, List<Edge>> adjacencyList;

    //initializes the adjacency list
    public Graph() {
        adjacencyList = new HashMap<>();
    }


    //adds vertex to the graph
    public void addVertex(Location vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    //adds edge to the graph
    public void addEdge(Location vertex1, Location vertex2, TransportationType transportationType,
                        List<Integer> operatingDays) {
        if (!adjacencyList.containsKey(vertex1)) {
            addVertex(vertex1);
        }
        if (!adjacencyList.containsKey(vertex2)) {
            addVertex(vertex2);
        }

        adjacencyList.get(vertex1).add(new Edge(vertex2, transportationType, operatingDays));
    }

    //finds routes with depth-first search & initializes lists
    public List<List<Location>> findAllRoutes(Location start, Location end, String date,
                                              List<List<TransportationType>> allTransportationTypes) {
        List<List<Location>> allPaths = new ArrayList<>();
        Set<Location> visited = new HashSet<>();
        List<Location> currentPath = new ArrayList<>();
        List<TransportationType> currentTransportationTypes = new ArrayList<>();
        dfs(start, end, visited, currentPath, currentTransportationTypes,allPaths, allTransportationTypes, date);
        return allPaths;
    }

    //depth-first search, saves found paths & transportation types as well
    private void dfs(Location currentNode, Location destinationNode, Set<Location> visited,
                     List<Location> currentPath, List<TransportationType> currentTransportationTypes,
                     List<List<Location>> allPaths, List<List<TransportationType>> allTransportationTypes, String date) {

        //if reached the destination, record the path
        if (currentNode == destinationNode) {
            currentPath.add(currentNode);

            if (isValidPath(currentPath, currentTransportationTypes, date)) {
                allPaths.add(new ArrayList<>(currentPath));
                allTransportationTypes.add(new ArrayList<>(currentTransportationTypes));
                currentTransportationTypes.clear();
            }
            currentPath.remove(currentPath.size() - 1);  // backtrack
            return;
        }
        currentTransportationTypes.clear();

        visited.add(currentNode);
        currentPath.add(currentNode);

        //explores all neighbors of currentNode
        if (adjacencyList.containsKey(currentNode)) {
            for (Edge edge : adjacencyList.get(currentNode)) {
                Location neighbor = edge.getLocation();

                if (!visited.contains(neighbor)) {
                    dfs(neighbor, destinationNode, visited, currentPath, currentTransportationTypes,
                            allPaths, allTransportationTypes, date);
                }
            }
        }

        //backtrack - removes currentNode from the path and unmarks it as visited
        visited.remove(currentNode);
        currentPath.remove(currentPath.size() - 1);
    }

    //checks if current path is valid
    private boolean isValidPath(List<Location> currentPath, List<TransportationType> currentTransportationTypes,
                                String date) {

        String dayNumberOfWeek;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //formats date
        try{
            Date formattedDate = sdf.parse(date);
            sdf.applyPattern("u");
            dayNumberOfWeek = sdf.format(formattedDate);  //finds number of the day
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int flightCount = 0;
        int pathCount = 0;


        for (int i = 0; i < currentPath.size()-1; i++) {
            List<Edge> edgeList = adjacencyList.get(currentPath.get(i));
            for (Edge edge : edgeList) {
                if(edge.getLocation().equals(currentPath.get(i+1))) {
                    if(!(edge.getOperatingDays().contains(Integer.parseInt(dayNumberOfWeek)))){  //if transportation does not operate that day
                        return false;
                    }
                    switch (edge.getTransportationType()) {   //checks transportation types of path whether it is valid or not
                        case BUS, UBER, SUBWAY:
                            pathCount++;
                            if (pathCount ==2 && flightCount == 0) {
                                return false;
                            }
                            else if(pathCount > 2){
                                return false;
                            }
                            break;
                        case FLIGHT:
                            flightCount++;
                            if(flightCount > 1) {
                                return false;
                            }
                            break;
                    }
                    currentTransportationTypes.add(edge.getTransportationType());
                }
            }
        }
        return true;
    }

    //displays the graph
    public void displayGraph() {
        for (Location vertex : adjacencyList.keySet()) {
            System.out.print(vertex + ": ");
            for (Edge adjacentVertex : adjacencyList.get(vertex)) {
                System.out.print(adjacentVertex + " ");
            }
            System.out.println();
        }
    }

    //class to hold destination, transportation type and operating days
    class Edge {
        private Location location;
        private TransportationType transportationType;
        private List<Integer> operatingDays;

        public Edge(Location location, TransportationType transportationType, List<Integer> operatingDays) {
            this.location = location;
            this.transportationType = transportationType;
            this.operatingDays = operatingDays;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public TransportationType getTransportationType() {
            return transportationType;
        }

        public void setTransportationType(TransportationType transportationType) {
            this.transportationType = transportationType;
        }

        public List<Integer> getOperatingDays() {
            return operatingDays;
        }

        public void setOperatingDays(List<Integer> operatingDays) {
            this.operatingDays = operatingDays;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "location=" + location +
                    ", transportationType=" + transportationType +
                    ", operatingDays='" + operatingDays + '\'' +
                    '}';
        }
    }
}