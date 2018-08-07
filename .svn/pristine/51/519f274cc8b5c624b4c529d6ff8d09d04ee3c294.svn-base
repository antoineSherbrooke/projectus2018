package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.NotNull;

@Entity
public class ProjectUserParticipation extends Model {

	public static final Finder<Integer, ProjectUserParticipation> find = new Finder<Integer, ProjectUserParticipation>(ProjectUserParticipation.class);

	@Id
	private Integer id;
	@NotNull
	private String userPaticipationcip;
	@NotNull
	private String projectPaticipationname;
	private int activity;

	public ProjectUserParticipation(String user, String project) {
		int max;
		this.userPaticipationcip = user;
		this.projectPaticipationname = project;
		this.activity = 1;
		if (ProjectUserParticipation.find.all().size() != 0) {
			max =ProjectUserParticipation.find.all().get(ProjectUserParticipation.find.all().size() - 1).getId();
			for(int i =0 ; i<ProjectUserParticipation.find.all().size();i++) {
				if(ProjectUserParticipation.find.all().get(i).getId()>max) {
					max =ProjectUserParticipation.find.all().get(i).getId();
				}
			}
			this.id = max+ 1;

		} else {

			this.id = 0;

		}
	}

	public void inActivity() {

		this.activity = 1;

	}

	public void offActivity() {

		this.activity = 0;

	}

	public int getActivity() {

		return activity;

	}

	public int getId() {
		return this.id;
	}

	public Users getUserPaticipation() {

		return Users.find.byId(userPaticipationcip);

	}


	public Project getProjectPaticipation() {
		return Project.find.byId(projectPaticipationname);

	}


	public static List<Project> getUserProjects(Users user) {

		List<ProjectUserParticipation> listParticipations = ProjectUserParticipation.find.all();
		ArrayList<Project> listProject = new ArrayList<Project>();

		for (int i = 0; i < listParticipations.size(); i++) {

			if (user.equals(listParticipations.get(i).getUserPaticipation())) {

				listProject.add(listParticipations.get(i).getProjectPaticipation());

			}

		}

		return listProject;

	}


	public static List<Users> getProjectsUserActive(Project project) {

		List<ProjectUserParticipation> listParticipations = ProjectUserParticipation.find.all();
		ArrayList<Users> listActive = new ArrayList<Users>();

		for (int i = 0; i < listParticipations.size(); i++) {

			if (project.equals(listParticipations.get(i).getProjectPaticipation())) {

				if (listParticipations.get(i).getActivity() == 1) {

					listActive.add(listParticipations.get(i).getUserPaticipation());
				}
			}
		}
		return listActive;
	}

	public static List<Users> getProjectsUserInactive(Project project) {

		List<ProjectUserParticipation> listParticipations = ProjectUserParticipation.find.all();
		ArrayList<Users> listInactive = new ArrayList<Users>();

		for (int i = 0; i < listParticipations.size(); i++) {

			if (project.equals(listParticipations.get(i).getProjectPaticipation())) {

				if (listParticipations.get(i).getActivity() == 0) {

					listInactive.add(listParticipations.get(i).getUserPaticipation());
				}
			}
		}
		return listInactive;
	}

	public static List<Users> getProjectsUser(Project project) {

		List<ProjectUserParticipation> listParticipations = ProjectUserParticipation.find.all();
		ArrayList<Users> listUsers = new ArrayList<Users>();

		for (int i = 0; i < listParticipations.size(); i++) {

			if (project.equals(listParticipations.get(i).getProjectPaticipation())) {

				listUsers.add(listParticipations.get(i).getUserPaticipation());
			}
		}
		return listUsers;
	}
	
	public static ProjectUserParticipation getParticipationFromUserProject(Users user, Project project) {

		List<ProjectUserParticipation> listParticipations = ProjectUserParticipation.find.all();
		ProjectUserParticipation ret = null;
		int i = 0;

		while (i < listParticipations.size() && ret == null) {

			if (listParticipations.get(i).getUserPaticipation().equals(user)&& listParticipations.get(i).getProjectPaticipation().equals(project)) {
				ret = listParticipations.get(i);

			}

			i++;

		}

		return ret;
	}
}
