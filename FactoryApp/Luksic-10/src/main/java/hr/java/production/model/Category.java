package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * sluzi za stvaranje objekta tipa Category
 */
public class Category extends NamedEntity implements Serializable {
    /**
     * varijabla u koju se sprema id
     */
    private Long id;
    /**
     * sprema naziv kategorije
     */
    private String name;
    /**
     * sprema opis kategorije
     */
    private String description;

    /**
     * konstruktor objekta tipa Category
     * @param name unos naziva kategorije
     * @param description unos opisa kategorije
     */
    public Category(Long id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public Category(String name, String description) {
        super(name);
        this.description = description;
    }

    public Category(String name) {
        super(name);
    }

    public Category(Long categoryId) {
        super(categoryId);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        hr.java.production.model.Category category = (hr.java.production.model.Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description);
    }

    @Override
    public String toString() {
        return "ID kategorije: " + this.getId() +
                "\tNaziv kategorije: " + this.getName() +
                "\tOpis kategorije: " + description;
    }
}
