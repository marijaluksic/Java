package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * sluzi za biljezenje informacija o nazivima/imenima
 */
public class NamedEntity implements Serializable {
    /**
     * varijabla u koju se sprema id
     */
    private Long id;
    /**
     * varijabla u koju se sprema naziv
     */
    private String name;


    /**
     * konstruktor klase koji prima naziv/ime
     * @param name unos naziva/imena
     */
    /**
     * konstruktor klase koji prima id i naziv/ime
     * @param id unos id
     * @param name unos naziva/imena
     */
    public NamedEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public NamedEntity(String name) {
        this.name = name;
    }

    public NamedEntity(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        hr.java.production.model.NamedEntity that = (hr.java.production.model.NamedEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
