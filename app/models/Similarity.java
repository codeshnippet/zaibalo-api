package models;

import play.db.jpa.Model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"one_id", "two_id"}))
public class Similarity extends Model {

    @ManyToOne(fetch= FetchType.EAGER)
    public User one;

    @ManyToOne(fetch=FetchType.EAGER)
    public User two;

    public double value;

}
