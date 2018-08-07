package models;

import java.util.List;

import javax.persistence.*;
import com.avaje.ebean.Model;

import play.data.validation.Constraints;

@Entity
public class Supervisors extends Model{

    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Associations associations;

    @ManyToMany(mappedBy = "supervisors")
    private List<Projects> projects;


    public Supervisors(Associations associations){
        this.associations = associations;
    }


    public Supervisors(Associations associations, List<Projects> projects) {
        this.associations = associations;
        this.projects = projects;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Associations getAssociations() {
        return associations;
    }

    public void setAssociations(Associations associations) {
        this.associations = associations;
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> projects) {
        this.projects = projects;
    }

    public static Finder<Integer, Supervisors> find = new Finder<Integer,Supervisors>(Supervisors.class);
}
