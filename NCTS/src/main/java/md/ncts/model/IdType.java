package md.ncts.model;

public class IdType {
    private int code;
    private String description;
    private boolean active;

    public IdType(int code, String description, boolean active) {
        this.code = code;
        this.description = description;
        this.active = active;
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }

    public void setCode(int code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(boolean active) { this.active = active; }
}
