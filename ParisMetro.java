import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
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
                    Vertex currentVertex = metroBuild.vertices.get(vertexNumber);
                    Vertex destinationVertex = metroBuild.vertices.get(destinationVertexNumber);
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

    //Identify all the stations belonging to the same line of a given station.
    public static void sameLine(Graph metro, Vertex station){
        Iterator<Edge> edgeIterator = metro.outgoingEdges(station);
        while(edgeIterator.hasNext()){
            Edge edge = edgeIterator.next();
            Vertex destinationVertex = edge.getdestinationVertex();
            System.out.println(destinationVertex.getStationName());
        }
    }

    //Find the shortest path between any two stations, i.e. the path taking the minimum total time. Print all the stations of the path in order, and print the total travel time.
    //This will be done with Dijkstra's algorithm
    public static ArrayList<ArrayList<Vertex>> shortestPath(Graph metro, Vertex beginningStation, Vertex endingStation){
        ArrayList<Vertex> cloud = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        ArrayList<ArrayList<Vertex>> path = new ArrayList<>();
        PriorityQueue<Vertex> q = new PriorityQueue<>();

        for (Vertex station : metro.vertices) {
            cloud.add(station);
            distances.add(station.equals(beginningStation) ? 0 : Integer.MAX_VALUE);
            path.add(station.equals(beginningStation) ? new ArrayList<>() : null);
            q.add(station);
        }

        while (!q.isEmpty()){
            Vertex u = q.poll();
            Iterator<Edge> edgeIterator = metro.outgoingEdges(u);
            while (edgeIterator.hasNext()){
                Edge edge = edgeIterator.next();
                Vertex z = metro.opposite(u, edge);
                if (q.contains(z)){
                    int newDistance = distances.get(cloud.indexOf(u)) + edge.getWeight();
                    if (newDistance < distances.get(cloud.indexOf(z))) {
                        distances.set(cloud.indexOf(z), newDistance);
                        path.set(cloud.indexOf(z), path.get(cloud.indexOf(u)));
                        path.get(cloud.indexOf(z)).add(z);
                        q.remove(z);
                        q.add(z);
                    }
                }
            }
        }

        return path;
    }
    
    public static void main(String[] args) {
        ParisMetro metro = new ParisMetro();
        metro.parisMetro = readMetro("metro.txt");
        /* System.out.println(metro.parisMetro.numVertices());
        System.out.println("Space");
        System.out.println(metro.parisMetro.numEdges());
        System.out.println("Space");

        Iterator<Edge> edgeIterator = metro.parisMetro.edges();
        while (edgeIterator.hasNext()) {
            Edge edge = edgeIterator.next();
            System.out.println(edge.getdestinationVertex().getStationName());
            System.out.println("Space");
        }
        */

        sameLine(metro.parisMetro, metro.parisMetro.vertices.get(0));
        System.out.println("Space");
        System.out.println(shortestPath(metro.parisMetro, metro.parisMetro.vertices.get(0), metro.parisMetro.vertices.get(56)));
    }
}
