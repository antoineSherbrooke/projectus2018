package controllers;

import models.*;
import play.Application;
import play.mvc.Http;

import java.util.*;
import static play.test.Helpers.*;

public class ConstantsTest {
    public final static int OK = 200;
    public final static int REDIRECTION = 303;
    public final static int UNAUTHORIZED = 401;

    public static Application fakeApp() {
        Map<String, String> options = new HashMap<>();
        options.put("MODE", "PostgreSQL");
        options.put("DB_CLOSE_DELAY", "-1");
        options.put("DATABASE_TO_UPPER", "FALSE");
        return fakeApplication(inMemoryDatabase("default", options));
    }

    public static Http.Session createSession(String cip, String password){
        Map<String, String> post = new HashMap<>();

        post.put("cip", cip);
        post.put("password", password);

        Http.RequestBuilder requestBuilder = new Http.RequestBuilder().method("POST")
                .bodyForm(post)
                .uri(routes.SessionController.loginPost().url());
        return route(requestBuilder).session();
    }

    public static void createUser(String cip, List<Members.EnumMem> enumMemList){
        Projects projects = new Projects("Vide", "TestPRO", true);
        projects.save();

        Groups group = new Groups("", "", projects);
        group.save();

        projects.setGroups(group);
        Users user = new Users(cip, "Test", "Test", "foobar");
        user.save();
        for (Members.EnumMem enumMem : enumMemList) {
            Associations associations = new Associations(user, Associations.EnumAsso.MEMBER);
            associations.save();
            Members membersScrum = new Members(associations, group, enumMem);
            associations.setMembers(membersScrum);
            membersScrum.save();
        }
    }
    public static void createUser(String cip){
        createUser(cip, Arrays.asList(Members.EnumMem.values()));
    }

    public static void createUser(String cip,Projects project, Members.EnumMem enumMem){
        Users user = new Users(cip, "Test", "Test", "foobar");
        user.save();
        Associations associations = new Associations(user, Associations.EnumAsso.MEMBER);
        associations.save();
        Members membersScrum = new Members(associations, project.getGroups(), enumMem);
        associations.setMembers(membersScrum);
        membersScrum.save();

    }
    public static void createSuperadmin(String cip){
        Users user = new Users(cip, "Test", "Test", "foobar");
        user.save();
        Associations associations = new Associations(user, Associations.EnumAsso.ADMINISTRATOR);
        associations.save();
    }

    public static Http.RequestBuilder getRequest(String method, Http.Session session, String uri, Map setting){
        return getRequest(method, session, uri).bodyForm(setting);
    }

    public static Http.RequestBuilder getRequest(String method, Http.Session session, String uri){
            Http.RequestBuilder request = new Http.RequestBuilder().method(method)
            .session(session)
            .uri(uri);
        return request;
    }

    public static void setHttpContext(Http.RequestBuilder request) {
        Http.Context context = new Http.Context(request);
        Http.Context.current.set(context);
    }
}
