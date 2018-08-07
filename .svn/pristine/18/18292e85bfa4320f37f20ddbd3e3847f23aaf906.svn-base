$(function(){
 $("#open-modal-edit-project").on("click", function(){
        $('#dial-edit-project').modal('toggle');
    });
    $("#open-modal-edit-project").on("click", addInfoProject);
    $("#edit-project").on("click", editProject);

 function addInfoProject(){
        startLoading();
        $.ajax({
            url: "/projectus/project_info/modal_add_project",
            type: 'POST',
            error: function(jqXHR, textStatus, errorThrown) {
                stopLoading();
                showError(textStatus);
            },
            dataType: 'json',
            success: function(data) {
                    stopLoading();
                    $('#input-edit-team-name').val(data.team_name);
                    $('#input-edit-email').val(data.email);
                    $('#input-edit-description').val(data.description);
            }
        });
    }

  function editProject(){
  		 var team_name = $("#input-edit-team-name").val();
  		 var description = $("#input-edit-description").val();
  		 var email = $("#input-edit-email").val();
  		 if(email == ""){
  		    email = null;
  		 }
         if(!validateEmail(email) && email != null){
             $("#editError").text("The format of email isn't correct");
             return;
         }
         fieldEmptyError(team_name,"team name",'#editError');
         fieldEmptyError(description,"description",'#editError');

         if((validateEmail(email) || email == null) && !fieldEmptyError(team_name,"The field team name  is required !",'#editError') && !fieldEmptyError(description,"The field description  is required !",'#editError')){
           var data = new Object();
           data["team_name"] = team_name;
           data["email"] = email;
           data["description"] = description;
           startLoadingMod();
           $.post('/projectus/project_info/edit', data , function(response) {})
           .done(function(response){
               stopLoading();
               $("#dial-edit-project").modal("hide");
               location.reload();
           })
           .fail(function(error){
                  stopLoading();
                  showError(error.responseText);
           });
         }
  	}

  function validateEmail(email) {
     var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
     return re.test(email);
  }

});

