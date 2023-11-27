import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    // Find the shortest path between two stations when we have the information that one given line is not functioning (the line is identified by one of its endpoints). The same type of information will be printed as in ii).
    // This will be done with modifications to Dijkstra's algorithm from ii)
    public static Pair<ArrayList<Vertex>, Integer> shortestPathWithBrokenLine(Graph metro, Vertex originStation, Vertex destinationStation, Vertex brokenStation){
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
                if (e.getdestinationVertex() == brokenStation){
                    edgeWeight = Integer.MAX_VALUE; //Sets the weight to infinity on edges that are on the broken line so we don't use it
                }
                long newDistance = (long) distance[currentStation.getVertexNumber()] + edgeWeight; //Had to use long here to avoid overflow
                if (newDistance < distance[nearbyStation.getVertexNumber()]){
                    distance[nearbyStation.getVertexNumber()] = (int) newDistance; //Cast back to int
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

    //Method to print out each vertice in the graph and its outgoing edges, used for troubleshooting
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
        
        if (args.length < 1 || args.length > 3){
            System.out.println("Invalid number of arguments. Please provide 1 to 3 station numbers in the following format: N1 N2 N3. You may omit N2 or N3.");
            return;
        }

        int N1 = Integer.parseInt(args[0]);
        Vertex stationN1 = metro.parisMetro.getVertex(N1);

        if (args.length == 1){ //Answer question 2 i)
            ArrayList<Vertex> stationsOnSameLine = sameLine(metro.parisMetro, metro.parisMetro.getVertex(N1));
            System.out.print("Stations on the same line as" + N1 + ": ");
            for (Vertex v : stationsOnSameLine){
                System.out.print(v.getVertexNumber() + " ");
            }
        }

        else if (args.length == 2){ //Answer question 2 ii)
            int N2 = Integer.parseInt(args[1]);
            Vertex stationN2 = metro.parisMetro.getVertex(N2);
            Pair<ArrayList<Vertex>, Integer> shortestPath = shortestPath(metro.parisMetro, stationN1, stationN2);
            System.out.print("Shortest path from " + N1 + " to " + N2 + ": ");
            for (Vertex v: shortestPath.getKey()){
                System.out.print(v.getVertexNumber() + " ");
            }
            System.out.println();
            System.out.println("Total travel time: " + shortestPath.getValue());
        }

        else { //Answer question 2 iii), can use else since I won't be implementing the bonus
            int N2 = Integer.parseInt(args[1]);
            int N3 = Integer.parseInt(args[2]);
            Vertex stationN2 = metro.parisMetro.getVertex(N2);
            Vertex stationN3 = metro.parisMetro.getVertex(N3);
            Pair<ArrayList<Vertex>, Integer> shortestPathBrokenLine = shortestPathWithBrokenLine(metro.parisMetro, stationN1, stationN2, stationN3);
            System.out.print("Shortest path from " + N1 + " to " + N2 + " with broken line " + N3 + ": ");
            for (Vertex v : shortestPathBrokenLine.getKey()){
                System.out.print(v.getVertexNumber() + " ");
            }
            System.out.println();
            System.out.println("Total travel time: " + shortestPathBrokenLine.getValue());
            
        }

    
    }
}
