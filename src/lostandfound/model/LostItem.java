package lostfound.model;

public class LostItem extends Item {

    private String lostDate;
    private String ownerName;

    public LostItem() {
        super();
        setType("LOST");
    }

    public LostItem(int id, String name, String description, String lostDate, String ownerName) {
        super(id, name, "LOST", description);
        this.lostDate = lostDate;
        this.ownerName = ownerName;
    }

    // Getters and Setters
    public String getLostDate() {
        return lostDate;
    }

    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "LostItem{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", lostDate='" + lostDate + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }
}