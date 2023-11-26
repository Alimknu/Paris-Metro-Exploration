import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParisMetro {
    Graph parisMetro;

    public ParisMetro(){
        this.parisMetro = new Graph();
    }

    //Reads the metro data from the text file and creates the graph
    public static Graph readMetro(String fileName){
        boolean creatingEdges = false;
        Graph metroBuild = new Graph();
        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.equals("$")){
                    creatingEdges = true;
                    continue;
                }

                if(!creatingEdges){
                    String[] lineSplit = line.split(" ");
                    int vertexNumber = Integer.parseInt(lineSplit[0]);
                    String stationName = lineSplit[1];
                    metroBuild.insertVertex(vertexNumber, stationName);
                }

                else{
                    String[] lineSplit = line.split(" ");
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
}
