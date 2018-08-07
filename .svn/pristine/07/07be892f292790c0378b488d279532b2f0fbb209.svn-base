$(document).ready(function() {

    var stateString = {
        1: "todo",
        2: "doing",
        3: "toreview",
        4: "done"
    };

    saveState = save;

    $.ajaxSetup({
        type: 'POST',
        timeout: 1000
    })
    var drag = {
        zIndex: 5000,
        appendTo: "body",
        opacity:0.8,
        help:"clone",
        revert:'invalid'
    };

    var accordion = {
        active : 0,
        collapsible:true,
        heightStyle:"content"
    }
    var dragg = $(".state .accordion div li");
    dragg.draggable(drag);
    $(".accordion").accordion(accordion);
    $('.taskOfTheUser').parent().prev().addClass('taskOfTheUser');
    $(".state .accordion li").on("click", informationTask);

    openAccordion();

    $(".state .div-border").droppable({
        accept : function(d) {
            if(d.closest('.state').index() != $(this).parent().index())
                return true;
        },
        drop: function(event, ui) {
            save($(this).parent().index(), ui.draggable.attr('data'), ui.draggable, $(this), "false")
        }
    });


    function openAccordion() {
        var featureState2 = $("#state-doing h6");
        var count1 = 0;
        for(i=0;i<featureState2.length;i++){
            if($(featureState2.get(i)).hasClass('taskOfTheUser')){
                count1 = i;
                break;
            }
        }

        var featureState3 = $("#state-toreview h6");
        var count2 = 0;
        for(i=0;i<featureState3.length;i++){
            if($(featureState3.get(i)).hasClass('taskOfTheUser')){
                count2 = i;
                break;
            }
        }

        $("#state-doing .accordion").accordion('option', 'active' , count1);
        $("#state-toreview .accordion").accordion('option', 'active' , count2);
    }

    function save(indexstate, id, ui, dropable, force){
        var data = new Object();
        data["id_task"] = id;
        data["state"] = indexstate;
        data["force"] = force;
        post(
            '/projectus/dashboard/edit',
            data,
            function(response) {
                var indexThis = $(ui).closest('.state').index();
                var titreFeature = $(ui).parent().prev();
                var featureId = $(titreFeature).attr("data");

                if(Math.abs(indexThis-indexstate) != 1){
                    ui.animate({top:"0px", left:"0px"}, 100);
                    return;
                }

                removeAccordionIfStateEmpty(ui);
                createAccordionIfNotExist(dropable, featureId, titreFeature);

                switchStateTask(ui, response.nb_dev, featureId, dropable);

                $(".accordion").accordion("refresh");
                $(".accordion").accordion("option", "active", 0);

                ifSprintFinish(response);
                editDoingProgress(response, ui);
                refreshChart2();
            },
            function(error){
                ui.animate({top:"0px", left:"0px"}, 100);
                showError(error.responseText);
            }
        );
    }

    function removeAccordionIfStateEmpty(ui){
        if($(ui).parent().find("li").length == 1){
            $(ui).parent().prev().remove();
            $(ui).parent().remove();
        }
    }

    function createAccordionIfNotExist(dropable, featureId, titreFeature){
        var existePas = true;
        var element = $(dropable).find(".feature");
        for(var i=0; i<element.length; i++){
            existePas = existePas && $($(element)[i]).attr("data") != featureId
        }
        if(existePas){
            $(titreFeature).clone().removeClass().addClass("feature")
                .prependTo($(dropable).find(".accordion"))
                .after("<div></div>");
        }
    }
    function giveUp(){
        startLoadingMod();
        var data = new Object();
        var id = $(this).attr('data');
        data["id_task"] = id;
        data["state"] = $(this).attr('data-state');
        post(
            '/projectus/dashboard/giveup',
            data,
            function(response) {
                stopLoading();
                if(response.change){
                    save((response.stateToGo-1), id, $(".state .div-border li[data='"+id+"']"),
                         $("#state-"+stateString[response.stateToGo]), "true");
                }
                $('#infoTask').modal('hide');
            }
        );
    }

    function ifSprintFinish(response){
        if(response.hasOwnProperty("feature_finish")){
            $("#info span").text("Sprint finish, the scrum master can start a new sprint");
            $("#info").removeClass("none");
        }
    }

    function switchStateTask(ui, nbDev, featureId, dropable){
        $("<li></li>").html(ui.html())
            .find(".badge").text(nbDev)
            .parent()
            .attr('data', ui.attr('data'))
            .draggable(drag)
            .on("click", informationTask)
            .appendTo($(dropable).find("h6[data='"+featureId+"']").next());

        $(ui).remove();
    }

    function editDoingProgress(response, ui){
        if(response.hasOwnProperty("doing_progress")){
            $(".accordion li[data='"+ui.attr("data")+"'] .progress-bar:first").css('width', response.doing_progress+"%");
        }
    }

    function editCharts(){
        var todoCount = $("#state-todo .accordion li").length;
        var doingCount = $("#state-doing .accordion li").length;
        var toreviseCount = $("#state-toreview .accordion li").length;
        var doneCount = $("#state-done .accordion li").length;

        refreshCharts(todoCount, doingCount, toreviseCount, doneCount);
    }
    

    function informationTask(){
        var data = new Object();
        data["id_task"] = $(this).attr("data");
        $('#infoTask').modal('toggle');
        refreshModal('infoTask');
        post(
            '/projectus/info/task',
            data,
            function(response){
                stopLoading();
                $("#infoTask .modal-dialog .modal-content").html(response);
                sizingModal();
                $('.giveUp').on('click', giveUp);
            }
        );

    }


});

var saveState;
