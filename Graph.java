import java.util.ArrayList;

// Implementation of the graph ADT using adjacency list

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

    //Returns a list of all the vertices in the graph
    public ArrayList<Vertex> vertices(){
        return new ArrayList<>(vertices);
    }

    //Returns the number of edges in the graph
    public int numEdges(){
        int numEdges = 0;
        for(ArrayList<Edge> edges : adjacencyList){
            numEdges += edges.size();
        }

        return numEdges;
    }

    //Returns a list of all the edges in the graph
    public ArrayList<Edge> edges(){
        ArrayList<Edge> edges = new ArrayList<>();
        for(ArrayList<Edge> edgeList : adjacencyList){
            edges.addAll(edgeList);
        }

        return edges;
    }

    //Returns the edge from u to v, or null if they are not adjacent
    public Edge getEdge(Vertex u, Vertex v){
        for (Edge edge : adjacencyList.get(u.getVertexNumber())) {
            if (edge.getdestinationVertex().equals(v)) {
                return edge;
            }
        }

        return null;
    }

    //Returns the vertex with the given number, or null if it doesn't exist
    public Vertex getVertex(int vertexNumber){
        for(Vertex vertex : vertices){
            if(vertex.getVertexNumber() == vertexNumber){
                return vertex;
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

    //Returns a list of all outgoing edges from vertex v
    public ArrayList<Edge> outgoingEdges(Vertex v){
        return adjacencyList.get(v.getVertexNumber());
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
        if(getEdge(u, v) != null){
            throw new IllegalArgumentException("Edge already exists");
        }
        Edge newEdge = new Edge(u, v, weight);
        adjacencyList.get(u.getVertexNumber()).add(newEdge);

        return newEdge;
    }

    //Removes vertex v and all its incident edges from the graph
    public void removeVertex(Vertex v){
        if(!vertices.contains(v)){
            throw new IllegalArgumentException("Vertex does not exist");
        }

        vertices.remove(v);
        adjacencyList.remove(v.getVertexNumber());
    }

    //Removes edge e from the graph
    public void removeEdge(Edge e){

        Vertex u = e.getOriginVertex();

        if(!adjacencyList.get(u.getVertexNumber()).contains(e)){
            throw new IllegalArgumentException("Edge does not exist");
        }

        adjacencyList.get(u.getVertexNumber()).remove(e);
    }
}