package Security;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SessionController;
import models.Members;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Developer extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        try {
            if(SessionController.getMember().getMemberType() == Members.EnumMem.DEVELOPER){
                return delegate.call(ctx);
            }else{
                Result result = unauthorized("You are not a developer !");
                return CompletableFuture.completedFuture(result);
            }
        }catch (Exception e){
            Result result = unauthorized("You are not a developer !");
            return CompletableFuture.completedFuture(result);
        }

    }


}