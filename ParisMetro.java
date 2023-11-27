import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ParisMetro {
    Graph parisMetro;

    public ParisMetro(){
        this.parisMetro = new Graph();
    }

    //Reads the metro data from the text file and creates the graph to be returned
    public static Graph readMetro(String fileName){
        boolean creatingEdges = false; //Determines whether we are creating vertices or edges
        Graph metroBuild = new Graph();
        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            scanner.nextLine(); //Skip the first line since it is just the amount of vertexes and edges (DOUBLE CHECK THIS)
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.equals("$")){ //Line to determine when we begin creating edges
                    creatingEdges = true;
                    continue;
                }

                if(!creatingEdges){ //If we aren't creating edges
                    String[] lineSplit = line.split(" ", 2); //Split the line into two parts, the first part is the vertex number and the second part is the station name
                    int vertexNumber = Integer.parseInt(lineSplit[0]);
                    String stationName = lineSplit[1];
                    metroBuild.insertVertex(vertexNumber, stationName);
                }

                else{ //If we are making edges
                    String[] lineSplit = line.split(" "); //Don't need to set a split limit here since we are working with vertice numbers, not station names
                    int vertexNumber = Integer.parseInt(lineSplit[0]);
                    int destinationVertexNumber = Integer.parseInt(lineSplit[1]);
                    int weight = Integer.parseInt(lineSplit[2]);
                    Vertex currentVertex = metroBuild.getVertex(vertexNumber);
                    Vertex destinationVertex = metroBuild.getVertex(destinationVertexNumber);
                    metroBuild.insertEdge(currentVertex, destinationVertex, weight);
                }
            }

            scanner.close();

        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be found");
            e.printStackTrace();
        }

        return metroBuild;
    }

    //Identify all the stations belonging to the same line of a given station. Done with DFS
    public static ArrayList<Vertex> sameLine(Graph metro, Vertex station){
        ArrayList<Vertex> stationsOnSameLine = new ArrayList<>();
        boolean[] visited = new boolean[metro.numVertices()];
        dfs(metro, station, stationsOnSameLine, visited);
        return stationsOnSameLine;
    }

    //DFS implementation
    private static void dfs(Graph metro, Vertex station, ArrayList<Vertex> stationsOnSameLine, boolean[] visited){
        visited[station.getVertexNumber()] = true;
        stationsOnSameLine.add(station);

        ArrayList<Edge> edges = metro.outgoingEdges(station);
        for (Edge e: edges){
            if (e.getWeight() == -1){
                continue; //Skips edges with weight of -1 (edges that aren't on the same line)
            }
            Vertex destination = e.getdestinationVertex();
            if (!visited[destination.getVertexNumber()]){
                dfs(metro, destination, stationsOnSameLine, visited);
            }
        }

    }

    //Find the shortest path between any two stations, i.e. the path taking the minimum total time. Print all the stations of the path in order, and print the total travel time.
    //This will be done with Dijkstra's algorithm
    public static Pair<ArrayList<Vertex>, Integer> shortestPath(Graph metro, Vertex originStation, Vertex destinationStation){
        int[] distance = new int[metro.numVertices()]; //Array to store the distance from the origin station to each station
        ArrayList<Vertex> previous = new ArrayList<>(Collections.nCopies(metro.numVertices(), null)); //Array to store the previous station in the shortest path to each station, setting each value to null in order to have each spot filled with a value later
        PriorityQueue<Vertex> pq = new PriorityQueue<>((v1,v2) -> Integer.compare(distance[v1.getVertexNumber()], distance[v2.getVertexNumber()]));

        Arrays.fill(distance, Integer.MAX_VALUE); //Set all distances to infinity
        distance[originStation.getVertexNumber()] = 0; //Set the distance from the origin station to itself to 0
        pq.add(originStation);

        while (!pq.isEmpty()){
            Vertex currentStation = pq.poll();
            for (Edge e : metro.outgoingEdges(currentStation)){
                Vertex nearbyStation = e.getdestinationVertex();
                int edgeWeight = e.getWeight() < 0 ? 90 : e.getWeight(); //Sets the weight to 90 on edges that aren't on the same line
                int newDistance = distance[currentStation.getVertexNumber()] + edgeWeight;
                if (newDistance < distance[nearbyStation.getVertexNumber()]){
                    distance[nearbyStation.getVertexNumber()] = newDistance;
                    previous.set(nearbyStation.getVertexNumber(), currentStation);
                    pq.add(nearbyStation);
                }
            }
        }

        ArrayList<Vertex> shortestPath = new ArrayList<>();
        for (Vertex v = destinationStation; v != null; v = previous.get(v.getVertexNumber())){
            shortestPath.add(0, v); //Adds to the beginning of the arrayList so that the path is in order
        }
    
        return new Pair<>(shortestPath, distance[destinationStation.getVertexNumber()]);

    }

    //Method to print out each vertice in the graph and its outgoing edges
    public static void printGraph(Graph metro) {
        for (Vertex v : metro.vertices()) {
            System.out.print(v.getVertexNumber() + ": ");
            for (Edge e : metro.outgoingEdges(v)) {
                System.out.print(e.getdestinationVertex().getVertexNumber() + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        ParisMetro metro = new ParisMetro();
        metro.parisMetro = readMetro("metro.txt");
        System.out.println(metro.parisMetro.getVertex(0).getStationName());
        System.out.println("Space");
        System.out.println(metro.parisMetro.getVertex(28).getStationName());
        System.out.println("Space");
        ArrayList<Vertex> sameLine = sameLine(metro.parisMetro, metro.parisMetro.getVertex(0));
        System.out.println("sameline test:");
        //for (Vertex v: sameLine){
        //    System.out.print(v.getVertexNumber() + " ");
        //}

        System.out.println("Space");
        Pair<ArrayList<Vertex>, Integer> shortestPath = shortestPath(metro.parisMetro, metro.parisMetro.getVertex(0), metro.parisMetro.getVertex(42));
        for (Vertex v : shortestPath.getKey()){
            System.out.print(v.getVertexNumber() + " ");
        }
        System.out.println();
        System.out.println("total travel time: " + shortestPath.getValue());

        //printGraph(metro.parisMetro);

    
    }
}
