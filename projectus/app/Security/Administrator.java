package Security;

import controllers.SessionController;
import models.Members;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Administrator extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        if(SessionController.getMember().getMemberType() == Members.EnumMem.ADMINISTRATOR){
            return delegate.call(ctx);
        }else{
            Result result = unauthorized("You are not administrator of the website");
            return CompletableFuture.completedFuture(result);
        }
    }


}