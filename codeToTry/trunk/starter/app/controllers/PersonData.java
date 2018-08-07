package controllers;


import play.data.validation.Constraints;

public class PersonData {
    @Constraints.Required
    private String name;
    private String mdp;

    public PersonData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

}
