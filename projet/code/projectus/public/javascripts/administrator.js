  $(function() {

    $("[data-dismiss='modal']").on("click", function(){
        $("input[type='text'], textarea").val("");
    })

    $("#btn-toggle").on("click", function(){
        	var nav = $('[role="navigation"]').hasClass("nav-close") ? "open" : "close";
        	$.post('/projectus/switch/navigation/'+nav, function(response) {
                $('[role="navigation"]').toggleClass("nav-close");
        	}).fail(function(error){
        		showError(error.responseText);
        	});
    });

    $(".form").submit(function(event) {
        var cip = $(this).find("[name='cip']").val();

        if(!cip.match(/^([A-Z]{4})([0-9]{4})$/i)){
            event.preventDefault();
            $("#superError").text("The format of cip isn't correct");
            return;
        }

    });

    $("#project").submit(function(event) {
        var name = $(this).find("[name='name']").val();

        if(name.length < 4){
            event.preventDefault();
            $("#superError").text("The field name must have a minimum of 4 letters");
            return;
        }

    });

    $(".addProjectSupervise").on("click", function(){
        var data = new Object();
        $("#dialogAddProject").modal();
        data["id_supervisor"] = $(this).attr("data");
        post(
            "/projectus/supervisor/getDialogProject",
            data,
            function(response){
                $("#contenu").replaceWith(response);
            }
        );
    });

  });

  function showError(descriptionError){
      $("#error-info span").text(descriptionError);
      $("#error-info").removeClass("none");
      setTimeout(function(){
          $("#error-info").addClass("none");
      }, 5000);
  }

  function post(url, data , success){
      $.post(url, data, function(response) {})
        .done(success)
    	.fail(function(error){
   		 	showError(error.responseText);
    	});
  }