package cloudflight.integra.backend.model;

public class Proposal{
    private Long id;
    private String name;
    private Type type;
    private String status; //pending, approved, rejected
    public Proposal(Long id, String name, Type type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = "pending";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public cloudflight.integra.backend.model.Type getType() {
        return type;
    }

    public void setType(cloudflight.integra.backend.model.Type type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
