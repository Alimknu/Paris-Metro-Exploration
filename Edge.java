public class Edge {
    Vertex originVertex;
    Vertex destinationVertex;
    int weight;
    
    public Edge(Vertex originVertex, Vertex destinationVertex, int weight){
        this.originVertex = originVertex;
        this.destinationVertex = destinationVertex;
        this.weight = weight;
    }

    public Vertex getOriginVertex(){
        return originVertex;
    }
    
    public Vertex getdestinationVertex(){
        return destinationVertex;
    }

    public int getWeight(){
        return weight;
    }
}
