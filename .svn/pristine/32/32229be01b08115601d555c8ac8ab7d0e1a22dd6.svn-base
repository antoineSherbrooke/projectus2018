package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import utils.PasswordStorage;

@Entity
public class Users extends Model{

    @Id
    @Constraints.Pattern(value =  "^([A-Z]{4}|[A-Z]{8})[0-9]{4}$", message = "Cip format is not valid")
    private String cip;

    private byte[] image;

    @Constraints.Required
    private String firstName;
    private String lastName;
    private String hash;

    private Boolean active;

    @OneToMany
    private List<Associations> associations;


    public Users(String cip, String firstName, String lastName){
        this.cip = cip;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hash = "";
        this.active = true;
    }
    public Users(String cip, String firstName, String lastName, String password){
        this(cip, firstName, lastName);
        try {
            this.setPassword(password);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
    }

    public Users(String cip, String firstName, String lastName, String password, List<Associations> associations){
        this(cip, firstName, lastName, password);
        this.associations = associations;
    }

    public Users(String cip, String firstName, String lastName, String password, List<Associations> associations, Boolean b){
        this(cip, firstName, lastName, password, associations);
        this.active = b;
    }


    public List<Members> getMembersTypeInProject(Projects project){
        List<Members> members = new ArrayList<>();
        for(Associations asso : associations){
            if (asso.getAssociationType().equals(Associations.EnumAsso.MEMBER)) {
                if (asso.getMembers().getGroups().getProjects().equals(project)) {
                    members.add(asso.getMembers());
                }
            }
        }
        Collections.sort(members, (member1, member2) -> Members.compare(member1, member2));
        return members;
    }

    public List<Members> getActivesMembersInProject(Projects project){
        List<Members> members = new ArrayList<>();
        for(Associations asso : associations){
            if(asso.getAssociationType() == Associations.EnumAsso.MEMBER){
                if(asso.getMembers().getGroups().getProjects().equals(project)){
                    if (asso.getMembers().getActive()) {
                        members.add(asso.getMembers());
                    }
                }
            }
        }
        Collections.sort(members, (m1, m2) -> Members.compare(m1, m2));
        return members;
    }

    public Members getDev(Projects project){
        Members member = null;
        for(Associations asso : associations){
            if(asso.getAssociationType() == Associations.EnumAsso.MEMBER){
                if(asso.getMembers().getGroups().getProjects().equals(project)){
                    if(asso.getMembers().getMemberType() == Members.EnumMem.DEVELOPER){
                        member = asso.getMembers();
                    }
                }
            }
        }
        return member;
    }

    public Boolean isAdminInProject(Projects project){
        for(Associations asso : associations){
            if(asso.getMembers().getGroups().getProjects().equals(project)) {
                if (asso.getAssociationType() == Associations.EnumAsso.MEMBER && asso.getMembers().getMemberType() == Members.EnumMem.ADMINISTRATOR) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isScrumInProject(Projects project){
        for(Associations asso : associations){
            if(asso.getMembers().getGroups().getProjects().equals(project)) {
                if (asso.getAssociationType() == Associations.EnumAsso.MEMBER && asso.getMembers().getMemberType() == Members.EnumMem.SCRUM_MASTER) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isOwnerInProject(Projects project){
        for(Associations asso : associations){
            if(asso.getMembers().getGroups().getProjects().equals(project)) {
                if (asso.getAssociationType().equals(Associations.EnumAsso.MEMBER) && asso.getMembers().getMemberType().equals(Members.EnumMem.PRODUCT_OWNER)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isSupervisorInProject(Projects project){
        Boolean isSupervisor = false;
        for(Associations asso : associations){
            for (Projects projectsLoop : asso.getSupervisors().getProjects()) {
                if (projectsLoop.equals(project)) {
                    if (asso.getAssociationType().equals(Associations.EnumAsso.SUPERVISOR)) {
                        isSupervisor = true;
                    }
                }
            }
        }
        return isSupervisor;
    }

    public static ConnectResult connect(String cip, String password){
        Users userToConnect = Users.find.where().eq("cip", cip).setMaxRows(1).findUnique();
        Boolean isCipOk = (userToConnect != null);

        if (isCipOk) {
            if (userToConnect.getActive()) {
                String hash = userToConnect.getHash();
                if ("".equals(hash)) {
                    return ConnectResult.FIRST_LOGIN;
                } else {
                    try {
                        if (PasswordStorage.verifyPassword(password, hash)) {
                            return ConnectResult.OK;
                        } else {
                            return ConnectResult.E_PASSWORD;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConnectResult.E_HASH;
                    }
                }
            } else {
                return ConnectResult.E_INACTIVE;
            }
        } else {
            return ConnectResult.E_CIP;
        }
    }

    public static Users newUser(String cip, String firstName, String lastName, Projects runningProject) throws Exception {
        Users user;
        Users userExist = Ebean.find(Users.class).where().eq("cip",cip).findUnique();
        List<Members> membersList = runningProject.getGroups().getMembers();
        List<Users> usersList = new ArrayList<>();
        if (membersList != null && membersList.size() != 0) {
            for (Members members : membersList) {
                usersList.add(members.getAssociations().getUsers());
            }
        }
        if(userExist==null) {
            user = new Users(cip, firstName, lastName);
            user.save();
        }else if(usersList.contains(userExist)){
            throw new Exception ("The cip already exist in this project");
        }else if(!userExist.getFirstName().equals(firstName)){
            throw new Exception ("The cip already exist in an other project but the first name is wrong");
        }else if(!userExist.getLastName().equals(lastName)){
            throw new Exception ("The cip already exist in an other project but the last name is wrong");
        }else{
            user = userExist;
        }
        return user;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getCip() {
        return cip;
    }
    public void setCip(String cip) {
        this.cip = cip;
    }
    public List<Associations> getAssociations() {
        return associations;
    }
    public void setAssociations(List<Associations> associations) {
        this.associations = associations;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setPassword(String password) throws PasswordStorage.CannotPerformOperationException {
        this.hash = PasswordStorage.createHash(password);
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public static Finder<Integer, Users> find = new Finder<>(Users.class);

}

