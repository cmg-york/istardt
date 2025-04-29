package ca.yorku.cmg.istardt.xmlparser.objects;

/**
 * Implementation of the Atom class with required methods
 */
public class Atom extends Formula {
    private String id;
    private String titleText;
    private String titleHTMLText;
    private String description;

    /**
     * Get the ID of this atom
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID of this atom
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the title text of this atom
     */
    public String getTitleText() {
        return titleText;
    }

    /**
     * Set the title text of this atom
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    /**
     * Get the HTML title text of this atom
     */
    public String getTitleHTMLText() {
        return titleHTMLText;
    }

    /**
     * Set the HTML title text of this atom
     */
    public void setTitleHTMLText(String titleHTMLText) {
        this.titleHTMLText = titleHTMLText;
    }

    /**
     * Get the description of this atom (from XML description attribute)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this atom (from XML description attribute)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getFormula() {
        return id != null ? id : "";
    }

    /**
     * Get the string representation of this atom
     */
    public String getAtomRepresentation() {
        return  "Atom{" +
                "id='" + id + '\'' +
                ", titleText='" + titleText + '\'' +
                ", titleHTMLText='" + titleHTMLText + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Atom{" +
                "id='" + id + '\'' +
                ", titleText='" + titleText + '\'' +
                ", titleHTMLText='" + titleHTMLText + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}