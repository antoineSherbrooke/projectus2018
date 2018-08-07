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

public class ScrumMaster extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        if(SessionController.getMember().getMemberType() == Members.EnumMem.SCRUM_MASTER){
            return delegate.call(ctx);
        }else{
            Result result = unauthorized("You are not the scrum master");
            return CompletableFuture.completedFuture(result);
        }
    }


}