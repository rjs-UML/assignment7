package edu.rseymour.advancedjava.model.database;

import javax.persistence.*;

@Entity
@Table(name = "person_stocks", schema = "stocks")
public class PersonStocksDAO implements DatabasesAccessObject {
    private int id;
    private PersonDAO personDAO;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonStocksDAO that = (PersonStocksDAO) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    public PersonDAO getPersonDAO() { return personDAO; }

    public void setPersonDAO(PersonDAO personByPersonId) {
        this.personDAO = personByPersonId;
    }
}
