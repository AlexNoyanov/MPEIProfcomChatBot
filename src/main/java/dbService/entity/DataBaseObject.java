package dbService.entity;

public abstract class DataBaseObject {
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}