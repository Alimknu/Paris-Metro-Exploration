import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
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

    //Identify all the stations belonging to the same line of a given station. This will be implemented using BFS
    public static void sameLine(Graph metro, Vertex station){
        Iterator<Edge> edgeIterator = metro.outgoingEdges(station);
        while(edgeIterator.hasNext()){
            Edge edge = edgeIterator.next();
            Vertex destinationVertex = edge.getdestinationVertex();
            System.out.println(destinationVertex.getStationName());
        }
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
    }
}
