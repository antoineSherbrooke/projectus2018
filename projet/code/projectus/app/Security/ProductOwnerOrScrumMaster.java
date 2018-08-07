package Security;


import controllers.SessionController;
import models.Members;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ProductOwnerOrScrumMaster extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        if (SessionController.getMember().getMemberType() == Members.EnumMem.SCRUM_MASTER || SessionController.getMember().getMemberType() == Members.EnumMem.PRODUCT_OWNER) {
            return delegate.call(ctx);
        } else {
            Result result = unauthorized("You are not the scrum master or product owner");
            return CompletableFuture.completedFuture(result);
        }
    }
}
