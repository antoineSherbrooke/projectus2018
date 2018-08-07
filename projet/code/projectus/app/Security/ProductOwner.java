package Security;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SessionController;
import models.Members;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class ProductOwner extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        if(SessionController.getMember().getMemberType() == Members.EnumMem.PRODUCT_OWNER){
            return delegate.call(ctx);
        }else{
            Result result = unauthorized("You are not the Product Owner");
            return CompletableFuture.completedFuture(result);
        }
    }


}