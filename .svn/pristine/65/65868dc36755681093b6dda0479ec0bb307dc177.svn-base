package controllers;
import java.util.*;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class SessionController extends Controller{
	
    @Inject FormFactory formFactory;    

    private static ArrayNode listOfProject;

	public static Boolean connected(){
		return session().containsKey("connected");
	}

	public static Boolean isSuperAdministrator() {
        try {
            return session().get("member_running").equals("SUPERADMINISTRATOR");
        }catch (Exception e) {
            return false;
        }
    }

    public static Boolean isSupervisor() {
        try {
            return getMemberRunning().equals("SUPERVISOR");
        }catch (Exception e) {
            return false;
        }
    }


	public static int getMemberId(){
        return Json.parse(session().get("project"))
				.get(getProjectNumberRunning())
				.get("members")
				.get(getMemberNumberRunning())
				.get("member_id")
				.asInt();
	}

	public static int getProjectId(){
		return Json.parse(session().get("project")).get(Integer.parseInt(session().get("project_running"))).get("project_id").asInt();
	}

	public static int getProjectNumberRunning(){
		return Integer.parseInt(session().get("project_running"));
	}

	private static int getMemberNumberRunning(){
		return Integer.parseInt(session().get("member_running"));
	}

	public static Members getMember(){
        return Ebean.find(Members.class).where().eq("id",getMemberId()).findUnique();
	}

    public static Users getUsers(){
        return Users.find.where().eq("cip", session().get("cip")).findUnique();
    }

	public static Projects getProjectRunning(){
		return Ebean.find(Projects.class).where().eq("id",getProjectId()).findUnique();
	}
	
	public Result switchProject(Integer idProject){
		int index = 0;
		for(JsonNode j:Json.parse(session().get("project"))){
			if(j.get("project_id").asInt() == idProject){
                session().put("project_running", index+"");
                session().put("member_running", "0");
                return redirect(getPreviousURL());
            }
			index++;
		}
        return redirect(getPreviousURL());

	}
	
	public Result switchMember(Integer idMember) {
		int index = 0;
		for(JsonNode j:Json.parse(session().get("project")).get(getProjectNumberRunning()).get("members")){
			if(j.get("member_id").asInt() == idMember){
                session().put("member_running", index+"");
                return redirect(getPreviousURL());
			}
			index++;
		}
        return redirect(getPreviousURL());

	}

    private String getPreviousURL(){
        return request().getHeader("referer");
    }

    public static Boolean memberOnTheTask(SprintTasks task){
        try{
            return SessionController.getMember().getMemberTaskRunning() != null && SessionController.getMember().getMemberTaskRunning().getId() == task.getId();
        }catch (Exception e){
            return false;
        }
    }

	public static List<Map<String,String>> getProject(){
		List<Map<String,String>> projects = new ArrayList<>();
		for(JsonNode j:Json.parse(session().get("project"))){
			Map<String,String> map = new HashMap<>();
			map.put("name", j.get("project").asText());
			map.put("id", j.get("project_id").asText());
			projects.add(map);
		}
		projects.remove(getProjectNumberRunning());
		return projects;
	}

    public static List<Projects> getListProject(){
        List<Projects> projects = new ArrayList<>();
        for(JsonNode j:Json.parse(session().get("project"))){
            projects.add(Projects.find.byId(j.get("project_id").asInt()));
        }
        return projects;
    }

	public static List<Map<String,String>> getMembers(){
		List<Map<String,String>> members = new ArrayList<>();
		for(JsonNode j:Json.parse(session().get("project")).get(getProjectNumberRunning()).get("members")){
			Map<String,String> map = new HashMap<>();
			map.put("name", j.get("member").asText());
			map.put("id", j.get("member_id").asText());
			members.add(map);
		}
		members.remove(getMemberNumberRunning());
		return members;
	}
	
	public static String getProjectNameRunning(){
		return Json.parse(session().get("project"))
                   .get(getProjectNumberRunning())
                   .get("project").asText();
	}
	
	public static String getMemberRunning(){
		return Json.parse(session().get("project"))
                   .get(getProjectNumberRunning())
                   .get("members")
                   .get(getMemberNumberRunning())
                   .get("member").asText();
	}

    public static Boolean authorization(Members.EnumMem typeMember){
        try{
            return getMember().getMemberType() == typeMember;
        }catch (Exception e){
            return false;
        }
    }

	private Result login(String cip, String password){
        ConnectResult connectResult = Users.connect(cip, password);
        if (connectResult == ConnectResult.OK) {
            return createSession(cip);
        } else if (connectResult == ConnectResult.FIRST_LOGIN) {
            session("firstLogin", "true");
            session("cip", cip);
            return redirect(routes.ProfilController.firstLogin().url());
        } else {
            flash("connect", connectResult.getMessage());
            return redirect(routes.HomeController.index().url());
        }
	}

    public Result loginGet(String cip, String password){
        return login(cip, password);
    }

    public Result loginPost(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String cip = requestData.get("cip");
        String password = requestData.get("password");
        return login(cip, password);
    }

    private static Result createSession(String cip){
        Users user = Users.find.where().eq("cip", cip).findUnique();
        Boolean memberDisactivate=true;
        setSessionGlobal(user);
        listOfProject = Json.newArray();

        for(Associations association:user.getAssociations()){

            if(association.getAssociationType() == Associations.EnumAsso.ADMINISTRATOR){
                return setSessionAdministrator();
            }else if(association.getAssociationType() == Associations.EnumAsso.SUPERVISOR){
                setSessionSupervisor(association.getSupervisors());
                memberDisactivate = false;
            }else if(association.getAssociationType() == Associations.EnumAsso.MEMBER){
                memberDisactivate = memberDisactivate && !association.getMembers().getActive();
                if(association.getMembers().getActive()) {
                    setSessionMember(association.getMembers());
                }
            }
        }
        if(memberDisactivate)
            return clearSessionAndRedirect("The user is not active");
        return setSessionUser(listOfProject.toString());
    }

    private static Result clearSessionAndRedirect(String messageError){
        session().clear();
        flash("connect", messageError);
        return redirect(routes.HomeController.index());
    }

    private static void setSessionGlobal(Users user){
        session("cip", user.getCip());
        session("firstName", user.getFirstName());
        session("lastName", user.getLastName());
        session("nav", "open");

    }

    private static Result setSessionUser(String projects){
        session("connected", "true");
        session("project", projects);
        session("project_running", "0");
        session("member_running", "0");
        WarningController.resetWarningTimes();
        return redirect(routes.DashboardController.dashboard().url());
    }

    private static void setSessionSupervisor(Supervisors supervisors){
        for(Projects projects:supervisors.getProjects()) {
            setSessionProjectOfUser(getSupervisorItem(), projects);
        }
    }

    private static Result setSessionAdministrator(){
        session("member_running", "SUPERADMINISTRATOR");
        return redirect(routes.AdministratorController.superadministrators().url());
    }

    private static void setSessionMember(Members member){
        setSessionProjectOfUser(getMemberItem(member), member.getGroups().getProjects());
    }

    private static void setSessionProjectOfUser(ObjectNode member, Projects project){
        Boolean find = false;
        int index;
        for(index = 0; index < listOfProject.size() && !find; index++) {
            find = listOfProject.get(index).get("project_id").asInt() == project.getId();
        }
        index--;

        if (find) {
            ((ArrayNode) listOfProject.get(index).get("members")).add(member);
        } else {
            ObjectNode projectItem = getProjectItem(project);
            ArrayNode listOfMemberByProject = Json.newArray();
            listOfMemberByProject.add(member);
            projectItem.put("members", listOfMemberByProject);
            listOfProject.add(projectItem);
        }
    }


    private static ObjectNode getMemberItem(Members member){
        ObjectNode memberItem = Json.newObject();
        memberItem.put("member", member.getMemberType().toString());
        memberItem.put("member_id", member.getId());
        return memberItem;
    }

    private static ObjectNode getSupervisorItem(){
        ObjectNode memberItem = Json.newObject();
        memberItem.put("member", "SUPERVISOR");
        memberItem.put("member_id", "0");
        return memberItem;
    }

    private static ObjectNode getProjectItem(Projects projects){
        ObjectNode projectItem = Json.newObject();
        projectItem.put("project", projects.getName());
        projectItem.put("project_id", projects.getId());
        return projectItem;
    }

    public static Result refreshSession(){
        String projectRunning = getProjectNameRunning();
        String cip = session().get("cip");
        session().clear();
        Result result = createSession(cip);
        session("project_running", String.valueOf(getIndexOfProject(projectRunning)));
        session("member_running", String.valueOf(getIndexOfAdministrator()));

        return result;
    }

    private static int getIndexOfAdministrator(){
        int index = 0;
        for(JsonNode member :Json.parse(session().get("project")).get(getProjectNumberRunning()).get("members")){
            if(member.get("member").asText().equals("ADMINISTRATOR")){
                return index;
            }
            index++;
        }
        return 0;
    }

    private static int getIndexOfProject(String project){
        int index = 0;
        for(JsonNode projectNode :Json.parse(session().get("project"))){
            if(projectNode.get("project").asText().equals(project)){
                return index;
            }
            index++;
        }
        return 0;
    }


	public Result logout(){
		session().clear();
		return redirect(routes.HomeController.index().url());
	}
}