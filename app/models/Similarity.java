package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"one_id", "two_id"}))
public class Similarity extends Model {

    @ManyToOne(fetch= FetchType.EAGER)
    public User one;

    @ManyToOne(fetch=FetchType.EAGER)
    public User two;

    public double value;

    public static Map<User, Double> getSimilarities(User one) {
        List<Similarity> similarities = Similarity.find("byOne", one).fetch();

        Map<User, Double> result = new HashMap<User, Double>();
        for(Similarity sim: similarities){
            result.put(sim.two, sim.value);
        }

        if(result.isEmpty()){
            similarities = Similarity.find("byTwo", one).fetch();
            for(Similarity sim: similarities){
                result.put(sim.one, sim.value);
            }
        }

        return result;
    }
}
