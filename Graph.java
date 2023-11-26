import java.util.ArrayList;
import java.util.Iterator;

public class Graph{

    ArrayList<ArrayList<Edge>> adjacencyList;
    ArrayList<Vertex> vertices;

    public Graph(){
        adjacencyList = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    //Returns the number of vertices in the graph
    public int numVertices(){
        return vertices.size();
    }

    //Returns an iteration of all the vertices in the graph
    public Iterator<Vertex> vertices(){
        return vertices.iterator();
    }

    //Returns the number of edges in the graph
    public int numEdges(){
        int numEdges = 0;
        for(ArrayList<Edge> edges : adjacencyList){
            numEdges += edges.size();
        }

        return numEdges;
    }

    //Returns an iteration of all the edges in the graph
    public Iterator<Edge> edges(){
        ArrayList<Edge> edges = new ArrayList<>();
        for(ArrayList<Edge> edgeList : adjacencyList){
            for(Edge edge : edgeList){
                edges.add(edge);
            }
        }

        return edges.iterator();
    }

    //Returns the edge from u to v, or null if they are not adjacent
    public Edge getEdge(Vertex u, Vertex v){
        ArrayList<Edge> edges = adjacencyList.get(u.getVertexNumber());
        for(Edge edge : edges){
            if(edge.getdestinationVertex().equals(v)){
                return edge;
            }
        }

        return null;
    }

    //Returns an array containing the two endpoint vertices of edge e
    public Vertex[] endVertices(Edge e){
        Vertex[] endVertices = new Vertex[2];
        endVertices[0] = e.getdestinationVertex();
        endVertices[1] = vertices.get(e.getdestinationVertex().getVertexNumber());

        return endVertices;
    }

    //For edge e incident to vertex v, returns the other vertex of the edge; error occurs if e is not incident to v
    public Vertex opposite(Vertex v, Edge e){
        Vertex[] endVertices = endVertices(e);
        if(endVertices[0].equals(v)){
            return endVertices[1];
        }

        else if(endVertices[1].equals(v)){
            return endVertices[0];
        }

        else {
            throw new IllegalArgumentException("Vertex is not incident to this edge");
        }
    }

    //Returns the number of outgoing edges from vertex v
    public int outDegree(Vertex v){
        return adjacencyList.get(v.getVertexNumber()).size();
    }

    //Returns an iteration of all outgoing edges from vertex v
    public Iterator<Edge> outgoingEdges(Vertex v){
        return adjacencyList.get(v.getVertexNumber()).iterator();
    }

    //Creates and returns a new Vertex storing the station's name
    public Vertex insertVertex(int vertexNumber, String stationName){
        Vertex newVertex = new Vertex(vertexNumber, stationName);
        vertices.add(newVertex);
        adjacencyList.add(new ArrayList<Edge>());

        return newVertex;
    }

    //Creates and returns a new edge from vertex u to vertex v, storing the weight; an error occurs if there already exists an edge from u to v
    public Edge insertEdge(Vertex u, Vertex v, int weight){
        Edge newEdge = new Edge(u, v, weight);
        ArrayList<Edge> edges = adjacencyList.get(u.getVertexNumber());
        edges.add(newEdge);

        return newEdge;
    }

    //Removes vertex v and all its incident edges from the graph
    public void removeVertex(Vertex v){
        vertices.remove(v);
        adjacencyList.remove(v.getVertexNumber());
    }

    //Removes edge e from the graph
    public void removeEdge(Edge e){
        ArrayList<Edge> edges = adjacencyList.get(e.getdestinationVertex().getVertexNumber());
        edges.remove(e);
    }
}