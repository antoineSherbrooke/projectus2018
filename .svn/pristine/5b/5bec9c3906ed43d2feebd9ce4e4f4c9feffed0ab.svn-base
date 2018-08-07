$(function() {
    time_setter.init(workingTime);

    var idTask = parseInt($("div.modal-body").attr("id"), 10);

    var stateClass = $("section").attr("class");
    var state;
    var stateVal;
    var nextStateClass;
    switch(stateClass) {
    case "state-todo":
        state = "TODO";
        stateVal = 1;
        nextStateClass = "state-doing";
        break;
    case "state-doing":
        state = "DOING";
        stateVal = 2;
        nextStateClass = "state-toreview";
        break;
    case "state-toreview":
        state = "TOREVIEW";
        stateVal = 3;
        nextStateClass = "state-done";
        break;
    case "state-done":
        state = "DONE";
        stateVal = 4;
        break;
    default:
        console.log("Error wrong stateClass");
    }

    var $sliderWorking = $(".slider-working");
    var $progressRemaining = $(".progress-remaining");
    $sliderWorking.slider({range: "min",
                           min: 0,
                           max: remainingTime-workingTimeOthers,
                           value: workingTime,
                           step:15,
                           slide: function(event, ui) {
                               updateProgressRemaining(ui.value);
                               workingTime = ui.value;
                               editLabels(ui.value);
                               pubsub.emit("dashboard_task.working_time_updated", workingTime+workingTimeOthers);
                           }
                          });
    $progressRemaining.progressbar({max: remainingTime, disabled: false});
    updateProgressRemaining($sliderWorking.slider("value"));

    //events
    pubsub.on("time_setter.updated", remainingHasChanged);
    $(".add-com").on("click", addComment);
    $("#btn-take").on("click", takeTask);
    $("#save-task").on("click", close);
    if(state == "DOING" || state == "TOREVIEW"){
        $("#save-task").on("click", save);
    }

    function addComment(){
        var $comment_error = $("#comment_error");
        $comment_error.addClass("none");
        var text = $(".text-com").val();
        if(text == ""){
            $comment_error.text("Comment required !");
            $comment_error.removeClass("none");
            return;
        }
        var data = new Object();
        data["id_task"] = idTask;
        data["text"] = text;
        startLoadingMod();
        $.post('/projectus/add_com', data , function(response) {
            stopLoading();
            $('#comments-list').prepend(
                '<div class="col-lg-12">'+
                    '<div><span class="bold">'+response.member_name+
                    '</span> ('+response.member_status+
                    ')  <span class="italic right">'+response.date+
                    '</span></div>'+
                    '<p class="header-comment">'+text+'</p>'+
                    '</div>');
            $(".text-com").val("");
        }, 'json')
            .fail(function(){
                stopLoading();
                $comment_error.text("Error during send post!");
                $comment_error.removeClass("none");
            });
    }

    function takeTask(){
        startLoadingMod();
        var stringState = {
            "DOING" : "Worked on the task",
            "TOREVIEW" : "Review the task"
        };
        var button = $(this);
        $.post('/projectus/dashboard/take/task/'+idTask, {}, function(response) {
            stopLoading();
            $(".accordion li[data='"+idTask+"'] .badge").text(response.nb_dev);
            $(button).remove();
            $("#members").append("<tr>" +
                                 "<td>"+response.firstName + " "+response.lastName+"</td>"+
                                 "<td>0 h 0</td>"+
                                 "<td>"+stringState[state]+"</td>"+
                                 "</tr>"
                                );
            $("#logHours").removeClass("none");
            $('.giveUp').removeClass('none');
        }).fail(function(error){
            stopLoading();
            showError(error.responseText);
        });
    }
    
    function remainingHasChanged(newRemaining) {
        remainingTime = newRemaining;
        $progressRemaining.progressbar( "option", "max", newRemaining);
        $sliderWorking.slider( "option", "max", newRemaining-workingTimeOthers);
        var valueSlider = $sliderWorking.slider("value");
        updateProgressRemaining(valueSlider);
        editLabels(valueSlider);
    }

    function updateProgressRemaining(sliderValue) {
        var valueProgressRemaining = remainingTime - workingTimeOthers - sliderValue;
        var percentProgress = parseInt(100*valueProgressRemaining/remainingTime);
        $progressRemaining.progressbar( "option", "value", valueProgressRemaining);
        $progressRemaining.css('width', percentProgress+"%");
    }

    function editLabels(ui){
        printTimeLabel("#timeWorked", ui);
        printTimeLabel("#own", ui);
        printTimeLabel("#remaining", remainingTime-workingTimeOthers-workingTime);
    }

    function close(){
        $("#infoTask").modal("hide");
    }

    function save(){
        startLoadingMod();
        post('/projectus/dashboard/state/'+state+'/task/log/'+idTask+"/"+workingTime+"/"+
             remainingTime, {}, function(response){
            stopLoading();
            close();
            if(remainingTime-workingTimeOthers-workingTime == 0){
                saveState(stateVal, idTask, $(".accordion li[data='"+idTask+"']"),
                          $("#"+nextStateClass+" .div-border"));
            }
            var finalPercent = (workingTime+workingTimeOthers)*100/remainingTime;
            if(state === "DOING"){
                $(".accordion li[data='"+idTask+"'] .progress-bar:first").css('width', finalPercent+"%");
            }else{
                $(".accordion li[data='"+idTask+"'] .progress-bar:last").css('width', finalPercent+"%");
            }
            refreshChart1();
        });
    }

});


