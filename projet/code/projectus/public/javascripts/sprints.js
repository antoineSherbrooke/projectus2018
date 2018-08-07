$(function() {
    var numAccordOpen;
    bindEvents();

    function refreshContent(response){
        setNumAccordOpen();
        $("#content").html(response);
        bindEvents();
        time_setter.init();
    }

    function setNumAccordOpen(){
        var openAccordion = $('#accordionFeatures div.ui-accordion-header-active');
        var id = parseInt(openAccordion.attr('data-number'));
        numAccordOpen = id;
    }

    function bindEvents(){
        $( "#accordionFeatures" ).accordion({
            heightStyle: "content",
            active: numAccordOpen
        });

        $( "#accordionCharts" ).accordion({
            heightStyle: "content",
            active : -10,
            collapsible:true
        });
        $('#addFeatureToSprint').on('click', modalAddFeatureToSprint);
        $('.featureInfos').on('click', function(){return featureInfosCharts(false, $(this).closest(".elemF").attr('data-id'));});
        $('.table-tasks tbody tr').delegate('img.actionFeature', 'click', function(e){ e.stopImmediatePropagation(); })
            .on('click', infosTask);
        $('#addTask').on('click', addTask);
        $('#editTask').on('click', editTask);
        $('#editSprint').on('click', editSprint);
        $('.edit-task-modal').on('click', function(){ return infosTaskEdit($(this).closest(".elemT").attr("data-id"));});
        $('#openModalEditSprint').on('click', function(){ return infosSprintEdit($("#sprintTitle").attr("data-id"));});

        valid_sprint_dates.init('#edit-begin-sprint', '#edit-end-sprint', '.edit-date-sprint');

        $('.openModalAddTask').on('click', clearValues);

        $('#clearBegin').on('click', function(){
            $('#edit-begin-sprint').val('');
            valid_sprint_dates.update();
        });

        $('#clearEnd').on('click', function(){
            $('#edit-end-sprint').val('');
            valid_sprint_dates.update();
        });

        launchCharts();
    }

    function featureInfosCharts(action, featureId){
        $('#dialInfosFeature').modal('toggle');
        refreshModal('dialInfosFeature');
        get('/projectus/feature/infos/'+featureId+'/'+action, function(response) {
            $("#dialInfosFeature .modal-dialog .modal-content").html(response);
            featureCharts(featureId);
        });
    }

    function infosTask(){
        var taskId = $(this).closest(".elemT").attr("data-id");
        $('#dialInfosTask').modal('toggle');
        refreshModal('dialInfosTask');
        get('/projectus/task/'+taskId, function(response) {
            stopLoading();
            $("#dialInfosTask .modal-dialog .modal-content").html(response);
            sizingModal();
        });
    }
    function infosTaskEdit(taskId){
        startLoadingMod();
        $('#dialEditTask').modal('toggle');
        get('/projectus/task/modaledit/'+taskId, function(response) {
            stopLoading();
            pubsub.emit("sprints.task.hasEstimate", response.estimate);
            $('#edit-name-task').val(response.name).attr('data-id',taskId);
            $('#edit-desc-task').val(response.description);
        });
    }

    function infosSprintEdit(sprintId){
        startLoadingMod();
        valid_sprint_dates.updateWithUrl('/projectus/sprint/dates/'+sprintId);

        $('#dialEditSprint').modal('toggle');
        get('/projectus/sprint/modaledit/'+sprintId, function(response) {
            stopLoading();
            $('#edit-name-sprint').val(response.name).attr('data-id',sprintId);
            $('#edit-desc-sprint').val(response.description);
            $('#edit-begin-sprint').val(response.beginDate);
            $('#edit-end-sprint').val(response.endDate);
        });
    }

    function editTask(){
        startLoadingMod();
        var data = new Object();
        data["taskId"] = $('#edit-name-task').closest(".elemT").attr('data-id');
        data["taskName"] = $('#edit-name-task').val();
        data["taskDescription"] = $('#edit-desc-task').val();
        data["taskEstimate"] = time_setter.get().toString();

        post('/projectus/task/edit', data , function(response) {
            clearModal();
            refreshContent(response);
            stopLoading();
        });
    }

    function editSprint(){
        var data = new Object();
        var name = $('#edit-name-sprint').val();
        var desc = $('#edit-desc-sprint').val();
        var begin = $('#edit-begin-sprint').val();
        var end = $('#edit-end-sprint').val();
        var allIsFilled = !fieldEmptyError(name, "name", '#editSprintError') &&
                !fieldEmptyError(desc, "description", '#editSprintError') &&
            !fieldEmptyError(begin, "begin date", '#editSprintError') &&
            !fieldEmptyError(end, "end date", '#editSprintError');

        if (allIsFilled) {
            startLoadingMod();
            data["sprintId"] = $('#sprintTitle').attr('data-id');
            data["sprintName"] = $('#edit-name-sprint').val();
            data["sprintDescription"] = $('#edit-desc-sprint').val();
            data["sprintBegin"] = $('#edit-begin-sprint').val();
            data["sprintEnd"] = $('#edit-end-sprint').val();

            post('/projectus/sprint/edit', data , function(response) {
                clearModal();
                refreshContent(response);
                stopLoading();
            });
        }
    }

    function addTask(){
        var sprintId = $('#sprintId').attr('data-id');
        var taskName = $('input#taskName').val();
        var taskDescription = $('textarea#taskDescription').val();
        var featureId = $('.ui-accordion-header-active').attr('data-id');
        if(!fieldEmptyError(taskName, "name", '#taskError') && !fieldEmptyError(taskDescription, "description", '#taskError')) {
            startLoadingMod();
            $('#taskError').html("");

            var data = new Object();
            data["sprintId"] = sprintId;
            data["featureId"] = featureId;
            data["taskName"] = taskName;
            data["taskDescription"] = taskDescription;
            data["taskTime"] = time_setter.get();

            post('/projectus/task/add', data, function(response) {
                clearModal();
                refreshContent(response);
                stopLoading();
            });
        }
    }

    function modalAddFeatureToSprint(){
        var sprintId = $('#sprintTitle').attr('data-id');
        $('#dialAddFeatureToSprint').modal('toggle');
        refreshModal('dialAddFeatureToSprint');
        get('/projectus/feature/modal/addtosprint/'+sprintId, function(response) {
            stopLoading();
            $("#dialAddFeatureToSprint .modal-dialog .modal-content").html(response);
            sizingModal();
            createDatabase2();
            $(".check input").on('click', function(){
                $('#confirmAddFeature').removeClass('none');
            });

            $('#confirmAddFeature').on('click', confirmAddFeature);
        });
    }

    function createDatabase2(){
        var sizeTable = $("#featureTableSprint thead th").length - 1;
        $('#featureTableSprint').DataTable({
            "paging":   false,
            "info":     false,
            "order": [[ 1, "asc" ]],
            "aoColumns": [
                { "sClass": "check","bSortable": false},
                { "sClass": "number" },
                { "sClass": "name" },
                { "sClass": "sprints" },
                { "sType": "duration", "sClass": "duration" },
                { "sType": "priority", "sClass": "priority" },
                { "sClass": "description" }
            ]
        });
    }

    function confirmAddFeature(){
        var featuresElem = $(".check input:checked");
        var features = [];
        var stringFeaturesId = "";
        var sprintId = $('#sprintTitle').attr('data-id');
        for(i=0; i<featuresElem.length;i++){
            var featureId = $(featuresElem.get(i)).attr('data');
            features.push(featureId);
            if(i!=0){
                stringFeaturesId = stringFeaturesId +";"+featureId;
            }else {
                stringFeaturesId = stringFeaturesId +""+featureId;
            }
        }
        startLoadingMod();
        post('/projectus/sprint/add/feature/'+sprintId+'/'+stringFeaturesId, {}, function(response) {
            clearModal();
            refreshContent(response);
            stopLoading();
        });
    }

    function clearValues(){
        $('input[type="checkbox"]').attr('checked', false);
        $("input[type=text]").val("");
        $("textarea").val("");
        time_setter.reset();
    }

});




