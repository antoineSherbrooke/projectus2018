package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import Security.Administrator;
import Security.Secure;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.team;
import views.html.edit_member;
import views.html.add_member;
import views.html.team_give_role;
import views.html.team_solo;

import javax.inject.Inject;

@With(Secure.class)
public class TeamController extends Controller {

    @Inject
    FormFactory formFactory;

	public Result index(){
        return ok(team.render(SessionController.getProjectRunning().getGroups().getUsers(),SessionController.getProjectRunning()));
	}

    public Result team(){
        return ok(team_solo.render(SessionController.getProjectRunning().getGroups().getUsers(),SessionController.getProjectRunning()));
    }

	@With(Administrator.class)
	public Result deactivateMember(Integer idMember){
		Members member = Members.find.byId(idMember);
		member.setActive(false);
        System.out.println("Desactivate member : " + member.getAssociations().getAssociationType());
		member.update();
        History.disableMember(member);
        SessionController.refreshSession();
        return team();
	}

	@With(Administrator.class)
	public Result activateMember(Integer idMember){
		Members member = Members.find.byId(idMember);
		member.setActive(true);
        System.out.println("Activate member : " + member.getAssociations().getAssociationType());
		member.update();
        History.activateMember(member);
        SessionController.refreshSession();
        return team();
	}

	public Result infoMember(String cip){
		Users user = Ebean.find(Users.class).where().eq("cip",cip).findUnique();
        List<Members> members = user.getMembersTypeInProject(SessionController.getProjectRunning());
        return ok(edit_member.render(user, members));
	}

    public Result getDatasUser(String cip){
        ObjectNode result = Json.newObject();
        Users user = Ebean.find(Users.class).where().eq("cip",cip).findUnique();
        Members member = user.getDev(SessionController.getProjectRunning());
        if(member != null) {
            result.put("datasDay", getDatasDay(member));
            result.put("datasTasks", getDatasTasks(member));
        }
        return ok(result);
    }

    @With(Administrator.class)
    public Result dialAddMember(){
        List<Users> users = new ArrayList<>();
        Projects runningProject = Projects.find.byId(SessionController.getProjectId());
        Groups group = runningProject.getGroups();
        List<Members> members = group.getMembers();
        for(Members member : members){
            if(!users.contains(member.getAssociations().getUsers())){
                users.add(member.getAssociations().getUsers());
            }
        }
        return ok(add_member.render(users));
    }

    @With(Administrator.class)
    public Result addMember(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String cip = requestData.get("memberCip");
        String firstName = requestData.get("memberFirstName");
        String lastName = requestData.get("memberLastName");
        Projects runningProject = Projects.find.byId(SessionController.getProjectId());
        Groups group = runningProject.getGroups();
        try {
            Users user = Users.newUser(cip,firstName,lastName,runningProject);
            Associations asso = new Associations(user, Associations.EnumAsso.MEMBER);
            asso.save();
            Members member = new Members(asso, group, Members.EnumMem.DEVELOPER);
            member.save();
            asso.setMembers(member);
            asso.save();
            History.newMember(member);
            SessionController.refreshSession();
        }catch (Exception e){
            return badRequest(e.getMessage());
        }
        //return ok(team_solo.render(group.getUsers(),runningProject));
        return team();
    }

    @With(Administrator.class)
    public Result resetUser(String cip){
        Users user = Ebean.find(Users.class).where().eq("cip",cip).findUnique();
        user.setHash("");
        user.save();
        return ok(user.getFirstName()+" "+user.getLastName()+" have been reset !");
    }

    public ObjectNode getDatasDay(Members member){
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");

        ObjectNode obj = Json.newObject();

        List<MembersTasksReview> membersTasksR = member.getMembersTasksReviews();
        List<MembersTasksDoing> membersTasksD = member.getMembersTaskDoings();
        for(MembersTasksReview membersTaskR : membersTasksR){
            String date = formater.format(membersTaskR.getDay());
            if(!obj.has(date)){
                Integer time = membersTaskR.getTimeSpent();
                obj.put(date, time);
            }else {
                Integer time = obj.findValue(date).asInt() + membersTaskR.getTimeSpent();
                obj.remove(date);
                obj.put(date, time);
            }
        }

        for(MembersTasksDoing membersTaskD : membersTasksD){
            String date2 = formater.format(membersTaskD.getDay());
            if(!obj.has(date2)){
                Integer time = membersTaskD.getTimeSpent();
                obj.put(date2, time);
            }else {
                Integer time = obj.findValue(date2).asInt() + membersTaskD.getTimeSpent();
                obj.remove(date2);
                obj.put(date2, time);
            }

        }
        return obj;
    }

    public ObjectNode getDatasTasks(Members member){
        ObjectNode obj = Json.newObject();

        List<MembersTasksReview> membersTasksR = member.getMembersTasksReviews();
        List<MembersTasksDoing> membersTasksD = member.getMembersTaskDoings();
        obj.put("Executed", membersTasksD.size());
        obj.put("Reviewed", membersTasksR.size());
        return obj;
    }

    public Result modalGiveRole(Integer memberId){
        Members mem = Members.find.byId(memberId);
        Users userSession = SessionController.getUsers();
        Projects projects = SessionController.getProjectRunning();
        List<Members> members = projects.getGroups().getMembers();
        List<Users> users = new ArrayList<>();
        for (Members member : members) {
            if (!users.contains(member.getAssociations().getUsers()) && !member.getAssociations().getUsers().equals(mem.getAssociations().getUsers())) {
                if(userSession.isAdminInProject(projects) && !mem.getAssociations().getUsers().equals(userSession)){
                    users.add(member.getAssociations().getUsers());
                }else if(!member.getAssociations().getUsers().equals(userSession)){
                    users.add(member.getAssociations().getUsers());
                }
            }
        }
        return ok(team_give_role.render(users, mem));
    }

    public Result giveRole(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Integer memberDonatorId = Integer.parseInt(requestData.get("memberDonatorId"));
        String userReceiverId = requestData.get("userReceiverId");
        Members memDonator = Members.find.byId(memberDonatorId);
        Users userDonator = memDonator.getAssociations().getUsers();
        Users userReceiver = Ebean.find(Users.class).where().eq("cip",userReceiverId).findUnique();
        Associations asso = memDonator.getAssociations();
        if(!userDonator.equals(SessionController.getUsers()) && !SessionController.authorization(Members.EnumMem.ADMINISTRATOR)){
            return badRequest("You can't give this role !");
        }
        if(memDonator.getMemberType() == Members.EnumMem.DEVELOPER || memDonator.getMemberType() == Members.EnumMem.ADMINISTRATOR){
            return badRequest("You can't give this role !");
        }
        asso.setUsers(userReceiver);
        asso.update();
        if(!memDonator.getActive()){
            memDonator.setActive(true);
            memDonator.update();
        }
        memDonator.update();
        SessionController.getMember().getAssociations().getUsers().refresh();
        SessionController.refreshSession();

        return team();
    }




}

