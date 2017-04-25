package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "categories")
public class Tag extends Model {

    @NotNull
    @Column(name = "name", unique = true)
    public String name;
}
