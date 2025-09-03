package cloudflight.integra.backend.model;

public class City {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public City setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }
}
