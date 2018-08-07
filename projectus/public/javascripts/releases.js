$(function() {
    var numAccordOpen = parseInt($('.running').attr('data-number'));

    bindEvents();
    function bindEvents() {
        $( "#accordionReleases" ).accordion({
            heightStyle: "content",
            active: numAccordOpen
        });
        $('.releaseInfos').on('click', releaseInfos);

        $( "#accordionDisabledReleases" ).accordion({
            heightStyle: "content"
        });
        $("#openModalAddRelease").on("click", function(){
            $('#dialAddRelease').modal('toggle');
            $('#dialAddRelease input, #dialAddRelease textarea').val('');
        });
        $('.openModalAddSprint').on('click', function() {
            startLoadingMod();
            var releaseId = $(this).closest("div.elemR").attr('data-id');
            valid_sprint_dates.updateWithUrl('/projectus/release/dates/'+releaseId);
        });

        $(".openModalAddSprint").on("click", function(){
            $('#dialAddSprint input, #dialAddSprint textarea').val('');
        });
        $("#addRelease").on("click", addRelease);
        $("#addSprint").on("click", addSprint);
        $(".abortSprint").on("click", {state:false}, postSprintState);
        $(".startSprint").on("click", {state:true}, postSprintState);
        $(".modalDisableRelease").on("click", modalDisableRelease);
        $("#save-edit-btn").on("click", editRelease);
        $(".modalEditRelease").on("click", modalEditRelease);
        $(".activateRelease").on("click", activateRelease);

        $(".table-sprint tbody tr")
            .delegate('button', 'click', function(e){ e.stopImmediatePropagation(); })
            .on("click", sprintInfos);

        valid_sprint_dates.init("#sprintBeginDate", "#sprintEndDate", ".sprintDate");
        $( "#releaseEndDate" ).datepicker({
            minDate: 0,
            onClose: function(selectedDate) {
                $("#sprintEndDate").datepicker( "option", "minDate", selectedDate );
            }
        });
        $( "#edit-date" ).datepicker({
            minDate: 0
        });

        $('#clearBegin').on('click', function(){
            $('#sprintBeginDate').val('');
            valid_sprint_dates.update();
        });

        $('#clearEnd').on('click', function(){
            $('#sprintEndDate').val('');
            valid_sprint_dates.update();
        });

    }

    function refreshContent(response){
        setNumAccordOpen();
        $("#content").html(response);
        bindEvents();
    }

    function setNumAccordOpen(){
        var openAccordion = $('#accordionReleases div.ui-accordion-header-active');
        var id = parseInt(openAccordion.attr('data-number'));
        numAccordOpen = id;
    }

    function modalEditRelease(){
        var id = $(this).closest("div.elemR").attr("data-id");
        startLoading();
        post("/projectus/releases/info_release_edit/"+id, {}, function(response){
            stopLoading();
            $('#dialEditRelease').modal('toggle');
            $('#edit-title').text(response.name);
            $('#edit-title').attr('data-id',id);
            $('#edit-name').val(response.name);
            $('#edit-date').val(response.date);
            $('#edit-desc').val(response.comment);
            var minDate, maxDate;
            if(response.minDate != 0){
                minDate = toDayBegin(new Date(response.minDate));
            }else {
                minDate = 0;
            }
            if(response.maxDate != 0){
                maxDate = toDayEnd(new Date(response.maxDate));
            }else {
                maxDate = null;
            }

            $("#edit-date").datepicker( "option", "minDate", minDate );
            $("#edit-date").datepicker( "option", "maxDate", maxDate );
        });
    }

    function addRelease(){
        var releaseName = $("#releaseName").val();
        var releaseEndDate = $("#releaseEndDate").val();
        var releaseDescription = $("#releaseDescription").val();
        if(releaseName.length == 0){
            $("#releaseError").text("The field name is required !");
            return;
        }
        if(releaseEndDate.length == 0){
            $("#releaseError").text("The field end date is required !");
            return;
        }
        if(releaseDescription.length == 0){
            $("#releaseError").text("The field description is required !");
            return;
        }
        startLoadingMod();
        var data = new Object();
        data["releaseName"] = releaseName;
        data["releaseEndDate"] = releaseEndDate;
        data["releaseDescription"] = releaseDescription;
        post('/projectus/release/add', data, function(response) {
            clearModal();
            refreshContent(response);
            stopLoading();
        });
    }

    function editRelease(){
        var releaseId = $('#edit-title').attr("data-id");
        var releaseName = $("#edit-name").val();
        var releaseDate = $("#edit-date").val();
        var releaseDescription = $("#edit-desc").val();
        if(releaseName.length == 0){
            showError("The field name is required !");
            return;
        }
        if(releaseDescription.length == 0){
            showError("The field description is required !");
            return;
        }
        startLoadingMod();
        var data = new Object();
        data["releaseId"] = releaseId;
        data["releaseName"] = releaseName;
        data["releaseDescription"] = releaseDescription;
        data["releaseDate"] = releaseDate;

        post('/projectus/release/edit_release', data , function(response) {
            clearModal();
            refreshContent(response);
            stopLoading();
        });
    }

    function releaseInfos(){
        var releaseId = $(this).closest("div.elemR").attr('data-id');
        $('#dialInfosRelease').modal('toggle');
        refreshModal('dialInfosRelease');
        get('/projectus/release/infos/'+releaseId, function(response) {
            $("#dialInfosRelease .modal-dialog .modal-content").html(response);
            sizingModal();
        });
    }

    function addSprint(){
        var sprintRelease = $(".ui-accordion-header-active").attr("data-id");
        var sprintName = $("#sprintName").val();
        var sprintBeginDate = $("#sprintBeginDate").val();
        var sprintEndDate = $("#sprintEndDate").val();
        var sprintDescription = $("#sprintDescription").val();
        if(sprintName.length == 0){
            $("#sprintError").text("The field name is required !");
            return;
        }
        if(sprintBeginDate.length == 0){
            $("#sprintError").text("The field begin date is required !");
            return;
        }
        if(sprintEndDate.length == 0){
            $("#sprintError").text("The field end date is required !");
            return;
        }
        if(sprintDescription.length == 0){
            $("#sprintError").text("The field description is required !");
            return;
        }
        startLoadingMod();
        var data = new Object();
        data["sprintRelease"] = sprintRelease;
        data["sprintName"] = sprintName;
        data["sprintBeginDate"] = sprintBeginDate;
        data["sprintEndDate"] = sprintEndDate;
        data["sprintDescription"] = sprintDescription;

        post('/projectus/sprint/add', data , function(response) {
            setNumAccordOpen();
            clearModal();
            refreshContent(response);
            stopLoading();
        });
    }

    function postSprintState(e) {
        var btn = $(this);
        var idSprint = btn.attr('value');
        var actionName = (e.data.state == true) ? 'start' : 'abort';
        var url = '/projectus/sprint/'+actionName+'/'+idSprint;
        $.post(url, function(response){
            refreshContent(response);
        }).fail(function(error) {
            showError(error.responseText);
        });
    }

    function modalDisableRelease(){
        var idRelease = $(this).closest("div.elemR").attr('data-id');
        $('#dialDisRelease').modal('toggle');
        refreshModal('dialDisRelease');
        get('/projectus/dial_disable_release/'+idRelease, function(response) {
            stopLoading();
            $("#dialDisRelease .modal-dialog .modal-content").html(response);
            $('#disableRelease').on('click', disableRelease);
        });
    }

    function activateRelease(){
        startLoading();
        var idRelease = $(this).closest("div.elemR").attr('data-id');
        get('/projectus/activate_release/'+idRelease, function(response){
            refreshContent(response);
            stopLoading();
        });
    }

    function disableRelease(){
        startLoading();
        var idRelease = $(this).parent().attr('id');
        get('/projectus/disable_release/'+idRelease, function(response){
            clearModal();
            refreshContent(response);
            stopLoading();
        });
    }

    function sprintInfos(){
        var sprintId = $(this).attr('data-id');
        $(location).attr('href', '/projectus/sprint/'+sprintId);
    }
});
