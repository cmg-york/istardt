package ca.yorku.cmg.istardt.xmlparser.objects;

public class Atom extends Formula {
    private String id;
    private String titleText;
    private String titleHTMLText;
    private String description;
    private Element element;

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
     * Get the description of this atom
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this atom
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getFormula() {
        return titleText;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Get the string representation of this atom
     */
    public String getAtomRepresentation() {
        return "Atom{" +
                "id='" + id + '\'' +
                ", titleText='" + titleText + '\'' +
                ", titleHTMLText='" + titleHTMLText + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Atom{id=" + id +
                ", titleText=" + titleText +
                ", titleHTMLText=" + titleHTMLText +
                ", description=" + description + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atom atom)) return false;

        if (getTitleText() != null ? !getTitleText().equals(atom.getTitleText()) : atom.getTitleText() != null)
            return false;
        if (getTitleHTMLText() != null ? !getTitleHTMLText().equals(atom.getTitleHTMLText()) : atom.getTitleHTMLText() != null)
            return false;
        return getDescription() != null ? getDescription().equals(atom.getDescription()) : atom.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitleText() != null ? getTitleText().hashCode() : 0;
        result = 31 * result + (getTitleHTMLText() != null ? getTitleHTMLText().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}