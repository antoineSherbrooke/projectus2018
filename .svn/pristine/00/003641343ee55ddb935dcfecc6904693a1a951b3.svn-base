package Security;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SessionController;
import models.Members;
import org.jetbrains.annotations.Contract;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import static play.mvc.Controller.request;

public class SuperAdministrator extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        if(SessionController.isSuperAdministrator()){
            return delegate.call(ctx);
        }else{
            Html res = new Html("<body style='background:rgb(164, 0, 0)'><p style='color:white; text-align:center; text-transform:uppercase; font-size:40px'>Vous avez essayer d'accéder à une zone de haute sécurité, vos données on été sauvegardées la sanction vous sera bientôt transmis ! <br /> IP :"+request().remoteAddress());
            Result result = unauthorized(res);
            return CompletableFuture.completedFuture(result);
        }
    }


}