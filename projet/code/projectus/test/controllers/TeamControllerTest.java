package controllers;


import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class TeamControllerTest extends WithApplication {

    private Http.Session session;
    private Http.RequestBuilder request;
    private Members member = new Members(true, Members.EnumMem.DEVELOPER);
    private List<MembersTasksDoing> membersTaskDoingsList = new ArrayList();
    private List<MembersTasksReview> membersTasksReviewsList = new ArrayList();


    @Before
    public void before() throws Exception {
        start(ConstantsTest.fakeApp());
        new InitController().initTest();
        ConstantsTest.createUser("toto", Ebean.find(Projects.class).where().eq("id",1).findUnique(), Members.EnumMem.ADMINISTRATOR);
        session = ConstantsTest.createSession("toto","foobar");
    }

    @Test
    public void resetUser(){
        String cip = "toto";
        new TeamController().resetUser(cip);
        Users user = Ebean.find(Users.class).where().eq("cip",cip).findUnique();
        assertEquals("Resetting the password does not working.","",user.getHash());
        request = ConstantsTest.getRequest("POST",session,routes.TeamController.resetUser(cip).url());
        Result result = route(request);
        assertEquals("This is not the page reset user","/projectus/reset/"+cip, request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void getDatasTasks(){
        for(int i = 0;i<1001;i++) {
            Random random = new Random();
            random.setSeed(29847893);
            int rand = random.nextInt(12);
            membersTaskDoingsList = addMemberTaskDoing(i);
            membersTasksReviewsList = addMemberTaskReview(rand);
            member.setMembersTaskDoings(membersTaskDoingsList);
            member.setMembersTasksReviews(membersTasksReviewsList);
            ObjectNode obj = Json.newObject();
            obj = new TeamController().getDatasTasks(member);
            assertEquals("The function getDatsTasks exectued failed",i,obj.get("Executed").asInt());
            assertEquals("The function getDatsTasks reviewed failed",rand,obj.get("Reviewed").asInt());

        }
    }

    @Test
    public void infoMember(){
        request = ConstantsTest.getRequest("GET",session,routes.TeamController.infoMember("toto").url());
        Result result = route(request);
        assertEquals("This is not the page info member","/projectus/info/member/toto", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void activateMember(){
        int id = 1;
        request = ConstantsTest.getRequest("GET",session,routes.TeamController.activateMember(id).url());
        Result result = route(request);
        assertEquals("This is not the page activate member","/projectus/activatemember/"+id, request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void deactivateMember(){
        int id = 1;
        request = ConstantsTest.getRequest("GET",session,routes.TeamController.deactivateMember(id).url());
        Result result = route(request);
        assertEquals("This is not the page activate member","/projectus/deactivatemember/"+id, request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    /* ERROR 400 Bad Request */
    @Test
    public void addMember(){
        request = ConstantsTest.getRequest("POST",session,routes.TeamController.addMember().url());
        Result result = route(request);
        assertEquals("This is not the page dial add member","/projectus/add/member", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void DialAddMember(){
        request = ConstantsTest.getRequest("GET",session,routes.TeamController.dialAddMember().url());
        Result result = route(request);
        assertEquals("This is not the page dial add member","/projectus/dial-add-member", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void getDatasDay() {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            membersTaskDoingsList = addMemberTaskDoing(1001);
            member.setMembersTaskDoings(membersTaskDoingsList);
            ObjectNode obj = Json.newObject();
            obj = new TeamController().getDatasDay(member);
            int sum = 0;
            String date = formater.format(membersTaskDoingsList.get(0).getDay());
            for (MembersTasksDoing tasksDoing : membersTaskDoingsList) {
                if(formater.format(tasksDoing.getDay()).equals(date)) {
                    sum += tasksDoing.getTimeSpent();
                }
            }
            assertEquals("The function getDatasDay exectued failed "+obj.get(date).asInt(),sum,obj.get(date).asInt());
    }


    private List<MembersTasksDoing> addMemberTaskDoing(int nbTasks){
        List<MembersTasksDoing> mtd = new ArrayList();
        for(int i = 0;i<nbTasks;i++){
            Date date = new Date((i+1)*(1000*60*60*24));
            mtd.add(new MembersTasksDoing(i,date));

        }
        return mtd;
    }
    private List<MembersTasksReview> addMemberTaskReview(int nbTasks){
        List<MembersTasksReview> mtr = new ArrayList();
        for(int i = 0;i<nbTasks;i++){
            mtr.add(new MembersTasksReview(nbTasks,new Date(i*1000*60*60*24)));
        }
        return mtr;
    }



}
