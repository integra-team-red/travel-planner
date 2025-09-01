package cloudflight.integra.backend.model;

public class Proposal{
    private Long id;
    private String name;
    private Type type;
    private Status status;

    public Proposal(Long id, String name, Type type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = Status.PENDING;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
