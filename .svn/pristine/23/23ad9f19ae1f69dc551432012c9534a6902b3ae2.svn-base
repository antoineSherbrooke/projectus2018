package controllers;

import models.Members;
import models.Users;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.PasswordStorage;
import views.html.firstLogin;
import views.html.profil;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfilController extends Controller {

    @Inject private FormFactory formFactory;

    private static final int MIN_IMAGE_DIMENSION = 10;
    private static final int MAX_IMAGE_DIMENSION = 100;

    public Result index(){
        Users user = null;
        Members member = null;
        try {
            user = SessionController.getUsers();
            member = SessionController.getMember();
        }catch (Exception e){}
        return ok(profil.render(user,member));
    }

    public Result upload(int width, int height) throws IOException {
        Users user = SessionController.getUsers();
        Http.RequestBody body = request().body();
        Http.MultipartFormData.FilePart picture = body.asMultipartFormData().getFile("picture");
        if (picture != null) {
            File file = (File) picture.getFile();
            BufferedImage img = ImageIO.read(file);
            int realWidth = img.getWidth();
            int realHeight = img.getHeight();

            if (realHeight != height || realWidth != width) {
                return badRequest("Indicated dimensions and actual dimensions of the image are different");
            } else if(realHeight > MAX_IMAGE_DIMENSION || realWidth > MAX_IMAGE_DIMENSION){
                return badRequest("The dimensions of the picture are too big");
            } else if(realHeight < MIN_IMAGE_DIMENSION || realWidth < MIN_IMAGE_DIMENSION) {
                return badRequest("The dimensions of the picture are too small");
            } else {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(img, "jpeg", output);

                output.flush();
                user.setImage(output.toByteArray());
                output.close();

                user.save();
                return ok("File uploaded");
            }
        } else {
            return badRequest("Missing file");
        }
    }

    public Result showImageLight(){
        return showImage(SessionController.getUsers(), "");
    }
    public Result showImageDark() {
        return showImage(SessionController.getUsers(),"_black");
    }

    public Result showImageLogo(String cip) {
        Users user = Users.find.where().eq("cip", cip).findUnique();
        return showImage(user, "_black");
    }

    private Result showImage(Users user, String color){
        if(user.getImage() == null){
            return redirect("/projectus/assets/images/ic_face"+color+".svg");
        }
        return ok(user.getImage()).as("image/jpeg");
    }

    public Result editProfil(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String firstName = requestData.get("firstName");
        String lastName = requestData.get("lastName");
        String password = requestData.get("password");
        String password2 = requestData.get("passwordValid");

        if(firstName.isEmpty() || lastName.isEmpty() || !password.equals(password2)){
            return badRequest("You mising form fields");
        }

        Users user = SessionController.getUsers();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if(!"".equals(password)){
            try {
                user.setPassword(password);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                return badRequest(e.getMessage());
            }
        }
        user.save();

        return redirect(routes.ProfilController.index().url());
    }

    public Result firstLogin(){
        if(session().containsKey("firstLogin")){
            return ok(firstLogin.render());
        }else{
            return redirect(routes.HomeController.index());
        }
    }

    public Result changePassword() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String password = requestData.get("password");
        String validPassword = requestData.get("validPassword");

        if (session().containsKey("firstLogin")) {

            if (password.isEmpty() || validPassword.isEmpty()) {
                flash("error", "The fields are required");
                return redirect(routes.ProfilController.firstLogin());
            }
            if (!password.equals(validPassword)) {
                flash("error", "The passwords are not the same");
                return redirect(routes.ProfilController.firstLogin());
            }
            String cip = session("cip");
            Users user = Users.find.where().eq("cip", cip).findUnique();
            try {
                user.setPassword(password);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                flash("error", e.getMessage());
                return redirect(routes.ProfilController.firstLogin());
            }
            user.update();
            session().clear();
            return new SessionController().loginGet(cip, password);
        }
        return badRequest("Error first login invalid");
    }
    
}

