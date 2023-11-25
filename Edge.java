public class Edge {
    int destinationVertex;
    int weight;
    
    public Edge(int destinationVertex, int weight){
        this.destinationVertex = destinationVertex;
        this.weight = weight;
    }

    public int getdestinationVertex(){
        return destinationVertex;
    }

    public int getWeight(){
        return weight;
    }
}
