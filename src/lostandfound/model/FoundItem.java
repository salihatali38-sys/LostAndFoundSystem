package lostfound.model;

public class FoundItem extends Item {

    private String foundDate;
    private String finderName;

    public FoundItem() {
        super();
        setType("FOUND");
    }

    public FoundItem(int id, String name, String description, String foundDate, String finderName) {
        super(id, name, "FOUND", description);
        this.foundDate = foundDate;
        this.finderName = finderName;
    }

    // Getters and Setters
    public String getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(String foundDate) {
        this.foundDate = foundDate;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    @Override
    public String toString() {
        return "FoundItem{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", foundDate='" + foundDate + '\'' +
                ", finderName='" + finderName + '\'' +
                '}';
    }
}