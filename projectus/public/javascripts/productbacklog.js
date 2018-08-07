$(function(){
    function fnDuration( a ) {
        var duration = {
            "Very short duration [-2h]" : 1,
            "Short duration [2~4h]" : 2,
            "Shorter than average duration [4~8h]" : 3,
            "Longer than average duration [8~16h]" : 4,
            "Long duration [16~40h]" : 5,
            "Very long duration [+40h]" : 6
        }
        return duration[a];
    }

    jQuery.fn.dataTableExt.oSort['duration-asc'] = function(a,b) {
        var x = fnDuration(a);
        var y = fnDuration(b);
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['duration-desc'] = function(a,b) {
        var x = fnDuration(a);
        var y = fnDuration(b);
        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
    };

    function fnPriority( a ) {
        var priority = {
            "Very Low" : 1,
             "Low" : 2,
             "Average" : 3,
             "High" : 4,
             "Very High" : 5
        }
        return priority[a];
    }

    jQuery.fn.dataTableExt.oSort['priority-asc'] = function(a,b) {
        var x = fnPriority( a );
        var y = fnPriority( b );

        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['priority-desc'] = function(a,b) {
        var x = fnPriority( a );
        var y = fnPriority( b );

        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
    };

    var windowWidth = $(window).width();
    var dialogWidth;
    if($('#chart-width').width() < 100){
        dialogWidth = windowWidth * ($('#chart-width').width() / 100)-150;
    }else {
        dialogWidth = $('#chart-width').width()-150;
    }

    $(window).on('resize', function(){
         dialogWidth = $('#chart-width').width() -150;

         var chart1 = $('#chart1').highcharts();
         chart1.setSize(dialogWidth/2,dialogWidth/2+50, doAnimation=false);

         var chart2 = $('#chart2').highcharts();
         chart2.setSize(dialogWidth/2,dialogWidth/2+50, doAnimation=false);
    });

    listener();

    function refreshContent(response){
        $("#content").html(response);
        listener();
    }

    function listener(){
        $("#openModalAddFeature").on("click", function(){
            removeInfo();
     		 $('#dialAddFeature').modal('toggle');
     	});
    	$("#addFeature").on("click", addFeature);
        $('#featureTable tbody tr, #featureTableDis tbody tr')
            .delegate('img.actionFeature', 'click', function(e){ e.stopImmediatePropagation(); })
            .on('click', function(){ return featureInfosCharts(true, $(this).attr('data'));});
        $('.disableFeature').on('click',disableFeature);
        $('.activateFeature').on('click',activateFeature);
        createDatabase1();
        createDatabase2();

    }

    function addFeature(){
         var featureNumber = $("#featureName").val();
		 var featureName = $("#featureName").val();
		 var featureDescription = $("#featureDescription").val();
		 var featurePriority = $("#featurePriority").val();
		 var featureFirstEstimate = $("#featureFirstEstimate").val();
		 if(featureName.length == 0){
             $("#featureError").text("The field name is required !");
            return;
         }
         if(featureDescription.length == 0){
             $("#featureError").text("The field description is required !");
             return;
         }
         var featureFirstEstimateConv = stringConvertion(featureFirstEstimate);

         var data = new Object();
         data["featureName"] = featureName;
         data["featurePriority"] = featurePriority;
         data["featureDesc"] = featureDescription;
         data["featureDuration"] = featureFirstEstimateConv;
         startLoadingMod();
         $.post('/projectus/feature/add', data , function(response) {})
         .done(function(response){
            clearModal();
            refreshContent(response);
            stopLoading();
         })
         .fail(function(error){
            showError(error.responseText);
            stopLoading();
         });
	 }


    function disableFeature(){
        startLoading();
        var featureId = $(this).parent().parent().attr('data');
        $.post('/projectus/feature/disable/'+featureId, function(response) {})
         .done(function(response){
            refreshContent(response);
            stopLoading();
         })
         .fail(function(error){
            showError(error.responseText);
            stopLoading();
         });
    }

    function activateFeature(){
        startLoading();
        var featureId = $(this).parent().parent().attr('data');
        $.post('/projectus/feature/activate/'+featureId, function(response) {})
         .done(function(response){
            refreshContent(response);
            stopLoading();
         })
         .fail(function(error){
            showError(error.responseText);
            stopLoading();
         });
    }

    function createDatabase1(){
        var sizeTable = $("#featureTable thead th").length - 1;
        if(sizeTable > 0){
            $('#featureTable').DataTable({
                "paging":   false,
                "info":     false,
                "columnDefs": [
                    {
                        "render": function ( data, type, row ) {
                            return row[sizeTable]+" "+data;
                        },
                        "targets": 0
                    },
                    { "visible": false,  "targets": [sizeTable] }
                ],

                "aoColumns": [
                    { "sClass": "number" },
                    { "sClass": "name" },
                    { "sClass": "release" },
                    { "sType": "duration", "sClass": "duration" },
                    { "sType": "priority", "sClass": "priority" },
                    { "sClass": "description" }
                ]
            });
        }

    }

    function createDatabase2(){
        var sizeTable = $("#featureTableDis thead th").length - 1;
        if(sizeTable > 0){
            $('#featureTableDis').DataTable({
                "paging":   false,
                "info":     false,
                "columnDefs": [
                    {
                        "render": function ( data, type, row ) {
                            return row[sizeTable]+" "+data;
                        },
                        "targets": 0
                    },
                    { "visible": false,  "targets": [sizeTable] }
                ],

                "aoColumns": [
                    { "sClass": "number" },
                    { "sClass": "name" },
                    { "sClass": "release" },
                    { "sType": "duration", "sClass": "duration" },
                    { "sType": "priority", "sClass": "priority" },
                    { "sClass": "description" }
                ]
            });
        }
    }

    function featureInfosCharts(action, featureId){
        startLoading();
        $('#dialInfosFeature').modal('toggle');
        refreshModal('dialInfosFeature');
        $.get('/projectus/feature/infos/'+featureId+'/'+action, function(response) {})
        .done(function(response){
            stopLoading();
            $("#dialInfosFeature .modal-dialog .modal-content").html(response);
            sizingModal();
            if(action){
                $("a.featureName").on('click', function(){return focusMe("featureName","input");});
                $('input.featureName').blur(function(){return blurMe("featureName","input");});

                $("a.featureDesc").on('click', function(){return focusMe("featureDesc","input");});
                $('textarea.featureDesc').blur(function(){return blurMe("featureDesc","input");});

                $('a.featureDuration').on('click', function(){return focusMe("featureDuration","select");});
                $('a.featurePriority').on('click', function(){return focusMe("featurePriority","select");});

                $('select.featureDuration').on('change', function(){return blurMe("featureDuration","select");});
                $(document).on('blur', 'select.featureDuration', function(){return blurMe("featureDuration","select");});

                $('select.featurePriority').on('change', function(){return blurMe("featurePriority","select");});
                $(document).on('blur', 'select.featurePriority', function(){return blurMe("featurePriority","select");});
                $('#saveModal').on('click', saveFeatureInfos);
            }
            featureCharts(featureId);
        })
        .fail(function(error){
            stopLoading();
            showError(error.responseText);
        });
     }

    function saveFeatureInfos(){
        startLoadingMod();
        var data = new Object();
        var featureDuration = $('span.featureDuration').html();
        data["featureId"] = $("#header-feature").attr("data");
        data["featureName"] = $('span.featureName').html();
        data["featurePriority"] = $('span.featurePriority').html();
        data["featureDesc"] = $('span.featureDesc').html();
        data["featureDuration"] = stringConvertion(featureDuration);

        $.post('/projectus/feature/edit', data , function(response) {})
         .done(function(response){
            clearModal();
            refreshContent(response);
            stopLoading();
         })
         .fail(function(error){
            showError(error.responseText);
            stopLoading();
         });
     }
});
    function featureCharts(featureId){
        $.get('/projectus/feature/datas/'+featureId, function(response) {})
        .done(function(response){
            var asData = false;
            var datasDev = response.datasDev;
            var arrayDev = [];
            for(var key in datasDev){
                for(var key2 in datasDev[key]){
                    if(datasDev[key][key2] != 0){
                        arraytmpDev = [ key2, datasDev[key][key2] * 60000];
                        arrayDev.push(arraytmpDev);
                        asData = true;
                    }
                }
            }

            var datasDay = response.datasDay;
            var arrayDay = [];
            for(var key in datasDay){
                arraytmpDay = [key, datasDay[key] * 60000];
                arrayDay.push(arraytmpDay);
            }
            windowWidth = $(window).width();

            if($('#chart-width').width() < 100){
                dialogWidth = windowWidth * ($('#chart-width').width() / 100)-150;
            }else {
                dialogWidth = $('#chart-width').width()-150;
            }

            if(asData){
                $('.charts').removeClass('none');
            }

            launchChart1(arrayDev);
            launchChart2(arrayDay);

        })
        .fail(function(error){
            showError(error.responseText);
        });

    }

    function launchChart1(datas){
        console.log(dialogWidth);
        console.log(windowWidth);
        $('#chart1').highcharts({
            chart: {
                plotBackgroundColor: null,
                //plotShadow: false
                borderWidth: 2,
                borderRadius: 7,
                borderColor: '#D8D8D8',
                width:dialogWidth/2,
                height:dialogWidth/2+50
            },
            title: {
                text: 'Time<br>Worked<br>per Dev',
                align: 'center',
                verticalAlign: 'middle',
                y: 40
            },
            credits: {
                  enabled: false
            },
            xAxis: {
                type: 'category'
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: true,
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white',
                            textShadow: '0px 1px 2px black'
                        }
                    },
                    startAngle: -90,
                    endAngle: 90,
                    center: ['50%', '75%']
                }
            },
            tooltip: {
                pointFormatter: function() {
                    var time = (Highcharts.numberFormat(this.y/3600000,2)).split('.')[0] + 'h';
                    if(Highcharts.numberFormat(this.y/3600000,2).split('.')[1] != 0){
                        time = time + Highcharts.numberFormat(this.y/3600000,2).split('.')[1] * 60 / 100 ;
                    }
                    return  '<b>Hours : </b>' + time;
                }
            },
            series: [{
                type: 'pie',
                name: 'Hours',
                innerSize: '50%',
                data: datas
            }],
        });

    }

    function launchChart2(datas){
        $('#chart2').highcharts({
            chart: {
                type: 'column',
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                borderWidth: 2,
                borderRadius: 7,
                borderColor: '#D8D8D8',
                width:dialogWidth/2,
                height:dialogWidth/2+50
            },
            title: {
                text: 'Time Worked per Day'
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                labels: {
                    formatter: function() {
                        // show the labels as whole hours (3600000 milliseconds = 1 hour)
                        return Highcharts.numberFormat(this.value/3600000,0);
                    }
                },
                title: {
                    text: 'Hours'
                },
                tickInterval: 3600000 // number of milliseconds in one hour
            },
            credits: {
                  enabled: false
              },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        formatter: function() {
                            var time = (Highcharts.numberFormat(this.y/3600000,2)).split('.')[0] + 'h';
                            if(Highcharts.numberFormat(this.y/3600000,2).split('.')[1] != 0){
                                time = time + Highcharts.numberFormat(this.y/3600000,2).split('.')[1] * 60 / 100 ;
                            }
                            return time;
                        }
                    }
                }
            },
            tooltip: {
                formatter: function() {
                    var time = (Highcharts.numberFormat(this.y/3600000,2)).split('.')[0] + 'h';
                    if(Highcharts.numberFormat(this.y/3600000,2).split('.')[1] != 0){
                        time = time + Highcharts.numberFormat(this.y/3600000,2).split('.')[1] * 60 / 100 ;
                    }
                    return  '<b>' + this.series.name +' : </b>' + time;
                }
            },
            series: [{
                name: 'Hours',
                colorByPoint: true,
                data: datas
            }]
        });

    }

    function focusMe(id, type){
        if(type=="input"){
            valBefore = $('.input.'+id).val();
            $('span.'+id).addClass('none');
            $('.input.'+id).removeClass('none').focus();
            $('a.'+id).addClass('none');
        }
        if(type=='select'){
            valBefore = $('select.'+id).val();
            $('span.'+id).addClass('none');
            $('select.'+id).removeClass('none').focus();
            $('a.'+id).addClass('none');
        }
        $('#closeModal').removeClass('btn-primary').addClass('btn-default');
        $('#saveModal').removeClass('none');
    }

    function blurMe(id, type){
        if(type=="input"){
            var error = false;
            var thisInput = $('.input.'+id);
            if(thisInput.val() == "" || thisInput.val() == " "){
                $('.error.'+id).html("This field is required !");
                thisInput.val(valBefore);
                error = true;
            }
            $('span.'+id).removeClass('none');
            $('span.'+id).html(thisInput.val());
            thisInput.addClass('none');
            $('a.'+id).removeClass('none');
            if(!error) {
                $('.error.'+id).html("");
            }
        }
        if(type=='select'){
            var thisSelect = $('select.'+id);
            $('span.'+id).removeClass('none');
            $('span.'+id).html(thisSelect.val());
            thisSelect.addClass('none');
            $('a.'+id).removeClass('none');
        }
    }

    function stringConvertion(str) {
       var conversion = {
        "Very short duration [-2h]" : "VERYSHORT",
        "Short duration [2~4h]" : "SHORT",
        "Shorter than average duration [4~8h]": "SHORTERTHANAVERAGE",
        "Longer than average duration [8~16h]" : "LONGERTHANAVERAGE",
        "Long duration [16~40h]" : "LONG",
        "Very long duration [+40h]" :"VERYLONG"
       };
       return conversion[str];
    }
    var valBefore;

