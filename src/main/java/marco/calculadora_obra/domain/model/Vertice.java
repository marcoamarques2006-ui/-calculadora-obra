package marco.calculadora_obra.domain.model;

/**
 * Representa um vértice do grafo G=(V,A).
 * Vértice = encontro de paredes que receberá um pilar estrutural.
 * Princípio S: responsável apenas pelos dados de um vértice.
 */
public class Vertice {

    private String id;
    private double x;
    private double y;

    public Vertice() {}

    public Vertice(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
}
