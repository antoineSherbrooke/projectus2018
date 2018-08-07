package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import models.*;
import models.Associations.EnumAsso;
import models.BacklogEntries.EnumPriority;
import models.BacklogEntries.EnumFirstEstimate;
import models.Members.EnumMem;
import models.SprintTasks.EnumState;
import org.apache.commons.lang3.time.DateUtils;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import utils.CyclicIterator;
import utils.PasswordStorage;


public class InitController extends Controller {

    public int releasesPerProject = 6;
    public int idReleaseRunning = 3;
    public int releaseDuration = 21;
    public int featuresPerProject = 10;
    public int sprintsPerRelease = 3;
    public int targetIndexWarn = 8;
    public int indexSprintToStart = -1;
    public int tasksPerFeature = 4;

    private ToSave toSave;

    /** unique initialization for production.
     * a superadmin is set
     */
    public Result initProd() {
        createSuperAdmin();

        return redirect(routes.HomeController.index().url());
    }

    public void createSuperAdmin() {
        toSave = new ToSave();
        if (Users.find.where().eq("cip", "superadmin").setMaxRows(1).findUnique() == null) {
            ArrayList<Users> users = new ArrayList<>();
            Users user = new Users("superadmin", "Ruben", "Gonzalez-Rubio");
            users.add(user);
            toSave.users = users;
            toSave.associations.add(new Associations(user,EnumAsso.ADMINISTRATOR));
            toSave.save();
        } else {
            flash("connect", "There is already a superadmin");
        }
    }

    public Result initEmptyTestProjects() {
        createEmptyTestProjects();
        toSave.save();

        return ok("SNAFU");
    }

    private void createEmptyTestProjects() {
        toSave = new ToSave();

        toSave.users = createTestUsers();

        Projects project = new Projects("Le projet idéal pour tous vos tests", "ProtectUS", true);
        Groups group = new Groups(project, "Dream Team");
        project.setGroups(group);
        toSave.projects.add(project);
        List<Integer> whoIsDev = new ArrayList<>(Arrays.asList(1,2,3,4));
        List<Integer> whoIsAdmin = new ArrayList<>(Arrays.asList(1));
        List<Integer> whoIsPO = new ArrayList<>(Arrays.asList(1));
        List<Integer> whoIsScrum = new ArrayList<>(Arrays.asList(0,1));
        toSave.createAssocAndMembers(group, whoIsDev, whoIsAdmin, whoIsPO, whoIsScrum);

        project = new Projects("Voila un autre projet", "ScrumBadass", true);
        group = new Groups(project, "Team tout seul");
        project.setGroups(group);
        toSave.projects.add(project);
        whoIsDev = new ArrayList<>(Arrays.asList(1,2));
        whoIsAdmin = new ArrayList<>(Arrays.asList(1));
        whoIsPO = new ArrayList<>(Arrays.asList(1));
        whoIsScrum = new ArrayList<>(Arrays.asList(1));
        toSave.createAssocAndMembers(group, whoIsDev, whoIsAdmin, whoIsPO, whoIsScrum);
    }

    public Result initTest() {
        return initTestCase(0);
    }
    public Result initTestCase(int testCase) {
        if (!Play.isProd()) {
            createSuperAdmin();
            createEmptyTestProjects();

            Boolean exactDates = false;
            if (testCase > 0) {
                exactDates = true;
            }
            Date startDate = createDateToWarnCase(testCase, sprintsPerRelease, releasesPerProject, releaseDuration, targetIndexWarn);

            generateReleases(toSave.projects.get(0), releasesPerProject, releaseDuration, startDate,
                    idReleaseRunning, exactDates);
            generateFeaturesInSprints(toSave.projects.get(0), featuresPerProject, sprintsPerRelease);
            generateReleases(toSave.projects.get(1), releasesPerProject, releaseDuration, startDate,
                    idReleaseRunning, exactDates);
            generateFeaturesInSprints(toSave.projects.get(1), featuresPerProject, sprintsPerRelease);

            if (sprintsPerRelease > 0) {
                setSprintsDates(startDate);
                startSprint(indexSprintToStart);
            }

            for (BacklogEntries feature : toSave.features) {
                generateTasks(feature, tasksPerFeature);
            }

            toSave.save();

            return ok("Damm ça en fait des données");
        } else {
            flash("connect", "This route is inaccessible in production");
            return redirect(routes.HomeController.index().url());
        }
    }

    /** return a init date to be in a warning testCase on a specified sprint or release.
     * when sprintPerRelease is 0, target is a release*/
    private Date createDateToWarnCase(int testCase, int sprintsPerRelease, int releasesPerProject,
                                      int releaseDuration, int targetIndex) {
        int minute = 60000;
        int day = 24*60*minute;
        Date now = new Date();
        Date date;
        if (sprintsPerRelease > 0) {
            int sprintLength = releaseDuration / sprintsPerRelease;
            date = DateUtils.addDays(now, -(targetIndex + 1) * sprintLength);
        } else if (targetIndex < releasesPerProject) {
            date = DateUtils.addDays(now, -(targetIndex + 1) * releaseDuration);
        } else {
            date = now;
        }

        if (testCase == 0) {
            return DateUtils.addMilliseconds(date, 4*day);
        }   else if (testCase > 0 && testCase <= WarningController.defaultWarningTimes.length) {
            return DateUtils.addMilliseconds(date, (int) (-1 * minute + WarningController.defaultWarningTimes[testCase - 1]));
        } else {
            return now;
        }
    }

    private void generateReleases(Projects project, int numberOfReleases, int duration,
                                  Date firstDate, int idRunning, Boolean exactDates) {
        for (int n = 1; n <= numberOfReleases; n++) {
            Date releaseDate = DateUtils.addDays(firstDate, duration*n);
            Releases release = new Releases(String.format("Release %d (%s)", n, project.getName()),
                    releaseDate, "", idRunning == n, project);
            toSave.releases.add(release);
            if (exactDates) {
                release.setExactReleaseDate(releaseDate);
            }
        }
    }

    private void generateFeaturesInSprints(Projects project, int numberOfFeatures, int sprintsPerRelease) {
        CyclicIterator<Integer> sizeSprint = new CyclicIterator<>(Arrays.asList(2,3));
        CyclicIterator<EnumFirstEstimate> firstEstimates = new CyclicIterator<>();
        firstEstimates.add(EnumFirstEstimate.VERYLONG);
        firstEstimates.add(EnumFirstEstimate.LONGERTHANAVERAGE);
        firstEstimates.add(EnumFirstEstimate.SHORTERTHANAVERAGE);
        firstEstimates.add(EnumFirstEstimate.LONG);
        firstEstimates.add(EnumFirstEstimate.VERYSHORT);
        firstEstimates.add(EnumFirstEstimate.LONG);
        firstEstimates.add(EnumFirstEstimate.VERYSHORT);
        firstEstimates.add(EnumFirstEstimate.SHORT);
        firstEstimates.add(EnumFirstEstimate.LONG);
        CyclicIterator<EnumPriority> priorities = new CyclicIterator<>();
        priorities.add(EnumPriority.HIGH);
        priorities.add(EnumPriority.VERYHIGH);
        priorities.add(EnumPriority.HIGH);
        priorities.add(EnumPriority.LOW);
        priorities.add(EnumPriority.AVERAGE);

        String featureDesc = "Description feature. Le bigorneau (probable dérivé de bigorne), dans l'acception la plus usuelle et la plus étroite, notamment commerciale, est le plus consommé des petits gastéropodes marins à coquille spiralée.";
        int iFeat = 0;

        for (Releases release : toSave.releases) {
            if (release.getProjects() == project) {
                for (int iSprint = 0; iSprint < sprintsPerRelease; iSprint++) {
                    Sprint sprint = new Sprint(String.format("Sprint %d (%s)", iSprint + 1, project.getName()), release);

                    for (int n = 0; n < sizeSprint.next(); n++) {
                        if (iFeat < numberOfFeatures) {
                            String name = String.format("Feature %d (%s)", iFeat + 1, project.getName());

                            BacklogEntries feature  = new BacklogEntries(name, featureDesc, sprint,
                                    iFeat + 1, project, firstEstimates.next(), priorities.next());
                            feature.save();
                            sprint.addBacklogEntrie(feature);
                            feature.getSprints().add(sprint); //Sinon dans constructeur null pointer exception

                            toSave.features.add(feature);

                            iFeat++;
                        }
                    }
                    toSave.sprints.add(sprint);
                }
            }
        }
    }

    private void generateTasks(BacklogEntries feature, int numberOfTasks) {
        int firstEstimate = 150;
        for (int i = 0; i < numberOfTasks; i++){
            String long_text_description = "En mécanique, une bielle est une pièce dotée de deux articulations, une à chaque extrémité, dans le but de transmettre une force, un mouvement ou une position.";
            SprintTasks sprintTasks = new SprintTasks(String.format("Tache %d", i+1), EnumState.TODO, feature, firstEstimate);
            sprintTasks.setDescription(String.format("Description tache %d. %s", i+1, long_text_description));
            sprintTasks.setSprint(feature.getSprints().get(0));
            toSave.sprintTasks.add(sprintTasks);

        }
    }

    private void setSprintsDates(Date defaultStart) {
        Date lastReleaseDate = defaultStart;
        for (Releases release : toSave.releases) {
            if (release.getReleaseDate().before(lastReleaseDate)) {
                lastReleaseDate = defaultStart;
            }
            ArrayList<Sprint> releaseSprints = new ArrayList<>();
            for (Sprint sprint : toSave.sprints) {
                if (sprint.getReleases() == release) {
                    releaseSprints.add(sprint);
                }
            }
            fitDateSprintsIn(releaseSprints, release, lastReleaseDate);
            lastReleaseDate = release.getReleaseDate();
        }
    }

    private void startSprint(int indexSprintToStart) {
        if (indexSprintToStart >= 0 && indexSprintToStart < toSave.sprints.size()) {
            toSave.sprints.get(indexSprintToStart).setState(Sprint.State.RUNNING);
        }
    }

    private void fitDateSprintsIn(List<Sprint> sprints, Releases release, Date releaseStart) {
        Sprint last = null;
        long releaseLength = release.getReleaseDate().getTime() - releaseStart.getTime();
        Long sprintLength = releaseLength / sprints.size();

        Date start;
        for (Sprint current : sprints) {
            if (last != null) {
                start = last.getEndDate();
            } else {
                start = releaseStart;
            }
            current.setExactStartDate(DateUtils.addMilliseconds(start, 1));

            Date end = DateUtils.addMilliseconds(start, sprintLength.intValue());
            current.setExactEndDate(end);

            last = current;
        }
    }


    private ArrayList<Users> createTestUsers() {
        ArrayList<Users> users = new ArrayList<>();
        users.add(new Users("1", "Etienne", "Martinet"));
        users.add(new Users("2", "Thomas", "Crenn"));
        users.add(new Users("3", "Arnaud", "Regnier"));
        users.add(new Users("4", "Paul", "Dupont"));
        users.add(new Users("5", "Hugo", "Durand"));

        for (Users u : users) {
            try {
                u.setPassword("foobar");
            } catch (PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            }
        }
        Users u_first_login = new Users("6", "Mathieu", "Nivel");
        u_first_login.setHash("");
        users.add(u_first_login);

        return users;
    }

    private class ToSave {
        public ArrayList<Users> users;
        public ArrayList<Projects> projects;
        public ArrayList<Associations> associations;
        public ArrayList<Groups> groups;
        public ArrayList<Members> members;
        public ArrayList<SprintTasks> sprintTasks;
        public ArrayList<BacklogEntries> features;
        public ArrayList<Sprint> sprints;
        public ArrayList<Releases> releases;

        public ToSave() {
            this.releases = new ArrayList<>();
            this.sprints = new ArrayList<>();
            this.features = new ArrayList<>();
            this.sprintTasks = new ArrayList<>();
            this.members = new ArrayList<>();
            this.groups = new ArrayList<>();
            this.associations = new ArrayList<>();
            this.projects = new ArrayList<>();
            this.users = new ArrayList<>();
        }

        /** Save Models to the database
         */
        public void save() {
            for (Users u : users) {
                u.save();
            }
            for(Projects p: projects){
                p.save();
            }
            for(Associations a: associations){
                a.save();
            }
            for(Groups g: groups){
                g.save();
            }
            for(Members m: members){
                m.save();
            }
            for(Releases r: releases){
                r.save();
            }

            for(Sprint s: sprints){
                s.save();
            }
            Ebean.saveAll(features);

            Ebean.saveAll(sprintTasks);
        }

        public void createAssocAndMembers(Groups group, List<Integer> idDev, List<Integer> idAdmin,
                                          List<Integer> idPO, List<Integer> idScrum) {
            for (int userId = 0; userId < users.size(); userId++) {
                checkAddUserAs(userId, idDev, EnumMem.DEVELOPER, group);
                checkAddUserAs(userId, idAdmin, EnumMem.ADMINISTRATOR, group);
                checkAddUserAs(userId, idPO, EnumMem.PRODUCT_OWNER, group);
                checkAddUserAs(userId, idScrum, EnumMem.SCRUM_MASTER, group);
            }
            groups.add(group);
        }

        private void checkAddUserAs(Integer userId, List<Integer> idsRole, EnumMem role, Groups group) {
            Associations assoc;
            Members member;
            for (Integer d : idsRole) {
                if (d.equals(userId)) {
                    assoc = new Associations(users.get(userId), EnumAsso.MEMBER);
                    associations.add(assoc);
                    member = new Members(assoc, group, role);
                    members.add(member);
                    assoc.setMembers(member);
                }
            }
        }
    }
}
