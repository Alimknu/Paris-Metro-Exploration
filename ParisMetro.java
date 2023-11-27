import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

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
    public static void shortestPath(Graph metro, Vertex startStation, Vertex endStation){

    }
    
    public static void main(String[] args) {
        ParisMetro metro = new ParisMetro();
        metro.parisMetro = readMetro("metro.txt");
        System.out.println(metro.parisMetro.getVertex(0).getStationName());
        System.out.println("Space");
        System.out.println(metro.parisMetro.getVertex(56).getStationName());
        System.out.println("Space");
        ArrayList<Vertex> sameLine = sameLine(metro.parisMetro, metro.parisMetro.getVertex(0));
        System.out.println("sameline test:");
        for (Vertex v: sameLine){
            System.out.print(v.getVertexNumber() + " ");
        }
        
    }
}
