package org.example.routeapp.model;

import java.text.SimpleDateFormat;

import java.util.*;

import static org.example.routeapp.model.TransportationType.BUS;

public class Graph {
    //the graph will be represented as an adjacency list.
    //map to store each node (vertex) and its list of adjacent nodes (edges).
    private Map<Location, List<Transportation>> adjacencyList;

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
    public void addEdge(Transportation transportation) {
        if (!adjacencyList.containsKey(transportation.getOriginLocation())) {
            addVertex(transportation.getOriginLocation());
        }
        if (!adjacencyList.containsKey(transportation.getDestinationLocation())) {
            addVertex(transportation.getDestinationLocation());
        }

        adjacencyList.get(transportation.getOriginLocation()).add(transportation);
    }

    //finds routes with depth-first search & initializes lists
    public List<List<Transportation>> findAllRoutes(Location start, Location end, String date) {
        List<List<Transportation>> allPaths = new ArrayList<>();
        Set<Location> visited = new HashSet<>();
        List<Transportation> currentPath = new ArrayList<>();
        List<List<Integer>> currentOperatingDays = new ArrayList<>();
        dfs(start, end, visited, currentPath, allPaths, date);

        return allPaths;
    }

    //depth-first search, saves found paths & transportation types as well
    private void dfs(Location currentNode, Location destinationNode, Set<Location> visited,
                     List<Transportation> currentPath,
                     List<List<Transportation>> allPaths, String date) {

        //if reached the destination, record the path
        if (currentNode == destinationNode) {
            if (isValidPath(currentPath, date)) {
                allPaths.add(new ArrayList<>(currentPath));
            }
              // backtrack
            return;
        }

        visited.add(currentNode);

        //explores all neighbors of currentNode
        if (adjacencyList.containsKey(currentNode)) {
            for (Transportation transportation : adjacencyList.get(currentNode)) {
                currentPath.add(transportation);
                Location neighbor = transportation.getDestinationLocation();
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, destinationNode, visited, currentPath,
                            allPaths, date);
                }
                currentPath.remove(currentPath.size() - 1);
            }
        }
        //backtrack - removes currentNode from the path and unmarks it as visited
        visited.remove(currentNode);
        /*if(currentOperatingDays.size() >= 1){
            currentPath.remove(currentPath.size() - 1);
        }*/
    }

    //checks if current path is valid
    private boolean isValidPath(List<Transportation> currentPath, String date) {

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

        if(currentPath.size() == 1 &&
                currentPath.get(0).getTransportationType() != TransportationType.FLIGHT){
                    return false;
        }

        for (Transportation transportation : currentPath) {
            if(!(transportation.getOperatingDays().contains(Integer.parseInt(dayNumberOfWeek)))) {
                return false;
            }
            switch (transportation.getTransportationType()) {   //checks transportation types of path whether it is valid or not
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
        }


        return true;
    }

    //displays the graph
    public void displayGraph() {
        for (Location vertex : adjacencyList.keySet()) {
            System.out.print(vertex + ": ");
            for (Transportation adjacentVertex : adjacencyList.get(vertex)) {
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