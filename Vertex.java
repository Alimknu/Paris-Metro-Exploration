public class Vertex {
    int vertexNumber;
    String stationName;

    public Vertex(int vertexNumber, String stationName){
        this.vertexNumber = vertexNumber;
        this.stationName = stationName;
    }

    public int getVertexNumber(){
        return vertexNumber;
    }

    public String getStationName(){
        return stationName;
    }
}
