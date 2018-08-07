package Security;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import controllers.routes;
import models.Members;
import org.jetbrains.annotations.Contract;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class Secure extends Action.Simple {  
	
    public CompletionStage<Result> call(Http.Context ctx) {
    	if(ctx.session().containsKey("connected")){
    		return delegate.call(ctx);
    	}else{
    		Result r = redirect(routes.HomeController.index().url());
    		return CompletableFuture.completedFuture(r);
    	}
    }

}