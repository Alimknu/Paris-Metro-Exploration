public class Edge {
    Vertex destinationVertex;
    int weight;
    
    public Edge(Vertex destinationVertex, int weight){
        this.destinationVertex = destinationVertex;
        this.weight = weight;
    }

    public Vertex getdestinationVertex(){
        return destinationVertex;
    }

    public int getWeight(){
        return weight;
    }
}
