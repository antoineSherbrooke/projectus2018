$(function() {
    $("#openModalAddMeeting").on("click", function(){
        $('#dialAddMeeting').modal('toggle');
    });
    $("#openModalAddMeeting").on("click", clearValues);
    $("#okAddMeeting").on("click", addMeeting);

    $("div#completed-list > a.openModalInfoMeeting")
        .delegate('button', 'click', function(e){ e.stopImmediatePropagation(); })
        .on("click", infoMeeting);

    $(".openModalConcludeMeeting").on("click", infoConcludeMeeting);
    $(".openModalConcludeMeeting").on("click", clearValues);
    $("#conclude-meeting").on("click", concludeMeeting);

    function addMeeting(){
        var name = $("#meetingName").val();
        var dayOrder = $("#meetingDayOrder").val();
        var conclusion = $("#meetingConclusion").val();
        var timeWorked = time_setter.get();
        var members = [];
        var oneChecked = false;
        $('input.checkboxMember').each(function(){
            oneChecked = oneChecked || this.checked;
            members.push({ cip: $(this).val(), checked : this.checked });
        });
        if (!oneChecked){
            $("meetingErrorAdd").text("You must add meeting particpants");
        }
        var allIsFilled = oneChecked && !fieldEmptyError(name,"name","#meetingErrorAdd")
                && !fieldEmptyError(dayOrder,"day","#meetingErrorAdd")
                && !fieldEmptyError(conclusion,"report","#meetingErrorAdd");
        if (allIsFilled) {
            var data = { name: name, dayOrder: dayOrder, members: JSON.stringify(members),
                         timeWorked:timeWorked, conclusion: conclusion };
            startLoadingMod();
            post('/projectus/meetings/add', data, function(response){
                stopLoading();
                var newLink = $("a.openModalInfoMeeting:last").clone();
                newLink.text(name).attr("data",response.id).on("click", infoMeeting);
                newLink.appendTo("#completed-list");
                $("#no-meeting-completed").addClass("none");
                $("#dialAddMeeting").modal("hide");
            });
        }
    }

    function concludeMeeting(){
        var id = $("#title-conclude").attr("data");
        var name = $("#meeting-name-conclude").text();
        var conclusion = $("#meeting-conclusion-conclude").val();
        var timeWorked = time_setter.get();
        var members = [];
        var oneChecked = false;
        $('input.checkbox-member-conclude').each(function(){
            oneChecked = oneChecked || this.checked;
            members.push({ cip: $(this).val(), checked : this.checked });
        });
        if (!oneChecked){
            $("#meetingErrorConclude").text("You must add meeting particpants");
        }
        var allIsFilled = oneChecked && !fieldEmptyError(name,"name","#meetingErrorConclude")
                && !fieldEmptyError(conclusion,"report","#meetingErrorConclude");
        if (allIsFilled) {
            var data = { id: id, members: JSON.stringify(members), timeWorked: timeWorked,
                         conclusion: conclusion };
            startLoadingMod();
            post('/projectus/meetings/conclude', data, function(response){
                stopLoading();
                error418(conclusion);
                var newLink = $("a.openModalInfoMeeting:last").clone();
                newLink.text(name).attr("data",response.id).on("click", infoMeeting);
                newLink.appendTo("#completed-list");
                $("#span-not-started-"+id).remove();
                $("#no-meeting-completed").addClass("none");
                $("#no-meeting-activate").addClass("none");
                if($(".list-not-started").length == 0){
                    $("#no-meeting-activate").removeClass("none");
                }
                $("#dial-conclude-meeting").modal("hide");
            });
        }
    }

    function infoMeeting(){
        startLoading();
        post("/projectus/meetings/info/"+$(this).attr("data"), {}, function(response){
            stopLoading();
            $('#dialInfoMeeting').modal('toggle');
            $(".modal-title-info").text(response.name);
            $("#name-info").text(response.name);
            $("#day-order-info").text(response.dayorder);
            var meetingMembers = response.meetingMembers;
            $("#meeting-members-info").empty();
            for(var i in meetingMembers){
                var member = meetingMembers[i];
                var checked = (member.active)? "checked" : "";
                var checkbox = '<div class="checkbox"><label><input class="checkboxMember" type="checkbox" value="'+member.cip+'" '+checked+' disabled> '+ member.name +'</label></div>';
                $("#meeting-members-info").append(checkbox);
            }
            printTimeLabel("#time-worked-info", response.timeworked);
            $("#conclusion-info").text(response.conclusion);
        });
    }

    function infoConcludeMeeting(){
        startLoading();
        post("/projectus/meetings/info/"+$(this).attr("data"), {}, function(response){
            stopLoading();
            $('#dial-conclude-meeting').modal('toggle');
            $("#title-conclude").text(response.name).attr("data",response.id);
            $("#meeting-name-conclude").text(response.name);
            $("#meeting-day-order-conclude").text(response.dayorder);
        });
    }

    function clearValues(){
        $('input[type="checkbox"]').attr('checked', false);
        $("input[type=text]").val("");
        $("textarea").val("");
        time_setter.reset();
    }

    function error418(conclusion){
        if(conclusion=="teapot" || conclusion=="418" || conclusion=="OKLM" || conclusion=="unicorn" || conclusion=="yolo" || conclusion=="easy peasy lemon squeezy" || conclusion=="coffee inside the teapot"){
            $("body").html("");
            $("body").css({'background':'url(assets/images/error418.png) no-repeat fixed center'});
            $("body").css({'background-color':'#96776B'});
            $("body").css({'background-size':'contain'});
        }
    }
});
