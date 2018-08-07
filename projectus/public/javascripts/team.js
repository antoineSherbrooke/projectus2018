$(function() {
    var windowWidth = $(window).width();
    var dialogWidth;
    if($('#chart-width').width() < 100){
        dialogWidth = windowWidth * ($('#chart-width').width() / 100)-150;
    }else {
        dialogWidth = $('#chart-width').width()-150;
    }
    listener();

    $(window).on('resize', function(){
         windowWidth = $(window).width();
         if($('#chart-width').width() < 100){
             dialogWidth = windowWidth * ($('#chart-width').width() / 100)-150;
         }else {
             dialogWidth = $('#chart-width').width()-150;
         }
         var chart1 = $('#chart1').highcharts();
         chart1.setSize(dialogWidth/12 * 7,dialogWidth/12 * 7 +50, doAnimation=false);

         var chart2 = $('#chart2').highcharts();
         chart2.setSize(dialogWidth/12 * 7,dialogWidth/12 * 7 +50, doAnimation=false);
    });

    function listener() {
         $(".activate-member").on("click",activateMember);
         $(".deactivate-member").on("click",deactivateMember);
         $("#reset-btn").on("click",resetMember);
         $(".reset-member").on("click", function(){
            $('#confirmReset').modal('toggle');
            $('#reset-btn').attr('data',$(this).attr("data"));
         });
         $(".infos-member").on("click", informationMember);
         $("#btnDialAddMember").on("click",dialAddMember);
         $("#tasks-executed").DataTable();
         $(".giveRole").on('click', modalGiveRole);

    }

    function refreshContent(response){
        $("#content").html(response);
        listener();
    }

	function deactivateMember(){
		 var memberId = $(this).attr('data');
		 var btn = $(this);
		 startLoading();
		 $.ajax({
			   url: '/projectus/deactivatemember/'+memberId,
			   type: 'GET',
			   error: function(jqXHR, textStatus, errorThrown) {
			        stopLoading();
			        location.reload();
                   //showError(textStatus);
			   },
			   success: function(response) {
                    refreshContent(response);
                    stopLoading();
                    location.reload();
			   }
		});
	 }
	 
	function activateMember(){
		 var memberId = $(this).attr('data');
		 var btn = $(this);
		 startLoading();
		 $.ajax({
			   url: '/projectus/activatemember/'+memberId,
			   type: 'GET',
			   error: function(jqXHR, textStatus, errorThrown) {
			        stopLoading();
			        location.reload();
				    //showError(textStatus);
			   },
			   dataType: 'html',
			   success: function(response) {
			        refreshContent(response);
			        stopLoading();
			        location.reload();
			   }
		});
	 }

    function informationMember(){
    	var userCip = $(this).attr('data');
        refreshModal('infoMember');
        $('#infoMember').modal('toggle');
    	$.get('/projectus/info/member/'+userCip, function(response) {})
    	.done(function(response){
    	    stopLoading();
            $("#infoMember .modal-dialog .modal-content").html(response);
            $("#tabs").tabs();
            sizingModal();
            getDatas(userCip);
            $("#tasks-executed").DataTable({
                "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "All"]]
            });

            $("#tasks-reviewed").DataTable({
                "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "All"]]
            });
    	})
    	.fail(function(error){
    	    stopLoading();
            showError(error.responseText);
    	});
	}

    function getDatas(userCip){
        startLoadingMod();
        $.get('/projectus/datas/member/'+userCip, function(response) {})
        .done(function(response){
            stopLoading();
            var asDatas = false;
            var datasDay = response.datasDay;
            var arrayDay = [];
            for(var key in datasDay){
                arraytmpDay = [key, datasDay[key] * 60000];
                arrayDay.push(arraytmpDay);
            }
            var datasTasks = response.datasTasks;
            var arrayTasks = [];
            for(var key in datasTasks){
                arraytmpTasks = [key, datasTasks[key]];
                arrayTasks.push(arraytmpTasks);
                if(datasTasks[key] != 0){
                    asDatas = true;
                }
            }
            if(asDatas){
                $('.chartTasks').removeClass('none');
            }
            launchCharts(arrayDay, arrayTasks);
        })
        .fail(function(error){
            stopLoading();
            showError(error.responseText);
        });
    }

    function dialAddMember(){
        $('#dialAddMember').modal('toggle');
        refreshModal('dialAddMember');
        $.get('/projectus/dial-add-member', function(response) {})
        .done(function(response){
            stopLoading();
            $("#dialAddMember .modal-dialog .modal-content").html(response);
            $(".btn-add-member").on("click",addNewMember);
        }).fail(function(error){
            stopLoading();
            showError(error.responseText);
        });
    }

    function addNewMember(){
        var memberFirstName = $('.modal-body #member-first-name').val();
        var memberLastName = $('.modal-body #member-last-name').val();
        var memberCip = $('.modal-body #member-cip').val();

        if(memberFirstName == "" || memberFirstName == " "){
            $('.input-error').html("The first name is required");
        }else if(memberLastName == "" || memberLastName == " "){
            $('.input-error').html("The last name is required");
        }else if(memberCip == "" || memberCip == " " || !memberCip.match(/^([A-Z]{4})([0-9]{4})$/i)){
            $('.input-error').html("The format of cip isn't correct");
        }else{
            startLoadingMod();
            var data = new Object();
            data["memberCip"] = memberCip;
            data["memberFirstName"] = memberFirstName;
            data["memberLastName"] = memberLastName;
            $.post('/projectus/add/member', data ,function(response) {})
            .done(function(response){
               stopLoading();
               clearModal();
               refreshContent(response);
            }).fail(function(error){
                stopLoading();
                showError(error.responseText);
            });
        }
    }

    function modalGiveRole(){
         var memberId = $(this).attr('data');
         var btn = $(this);
         $('#modalGiveRole').modal('toggle');
         refreshModal('modalGiveRole');
         $.ajax({
               url: '/projectus/team/modal/give/role/'+memberId,
               type: 'GET',
               error: function(jqXHR, textStatus, errorThrown) {
                   stopLoading();
                   showError(textStatus);
               },
               success: function(response) {
                    stopLoading();
                    $("#modalGiveRole .modal-dialog .modal-content").html(response);
                    $("#btn-give-role").on('click', giveRole);
               }
        });
     }

     function giveRole(){
          startLoadingMod();
          var memberDonatorId = $("#memberDonatorId").attr('data');
          var userReceiverId = $('#select-user').val();
          console.log("Member :"+memberDonatorId);
          console.log("User :"+userReceiverId);
          //$('#modalGiveRole').modal('hide');
          var data = new Object();
          data["memberDonatorId"] = memberDonatorId;
          data["userReceiverId"] = userReceiverId;
          $.post('/projectus/team/give/role', data ,function(response) {})
          .done(function(response){
             location.reload();
             stopLoading();
             clearModal();
             refreshContent(response);
          }).fail(function(error){
                stopLoading();
                location.reload();

                //refreshContent(response);
             //location.replace('/projectus/logout');
             //showError(error.responseText);
          });
      }

    function resetMember(){
        var mid = $(this).attr("data");
        console.log(mid);
        startLoading();
        $.post('/projectus/reset/'+mid, function(response) {})
               .done(function(response){
                    showSuccess(response);
                    $("#confirmReset").modal("hide");
                    stopLoading();
               }).fail(function(error){
                    stopLoading();
                    showError(error.responseText);
               });
    }

    function launchCharts(arrayDays, arrayTasks){
        //var arrayDays = [["26-05-2016", 12500000],["27-05-2016", 11200000],["28-05-2016", 11700000],["29-05-2016", 13800000],["30-05-2016", 900000],["31-05-2016", 12500000],["01-06-2016", 11200000],["02-06-2016", 11700000],["03-06-2016", 13800000],["04-06-2016", 900000],["05-06-2016", 12500000],["06-06-2016", 11200000],["07-06-2016", 11700000],["08-06-2016", 13800000],["10-06-2016", 900000]];
        windowWidth = $(window).width();
        if($('#chart-width').width() < 100){
            dialogWidth = windowWidth * ($('#chart-width').width() / 100)-150;
        }else {
            dialogWidth = $('#chart-width').width()-150;
        }

        var datasDays = [];
        for(var i = 0; i< arrayDays.length; i++){
            var year = parseInt(arrayDays[i][0].split('-')[2]);
            var day = parseInt(arrayDays[i][0].split('-')[0]);
            var mouth = parseInt(arrayDays[i][0].split('-')[1]) - 1;
            var date = Date.UTC(year, mouth, day);
            datasDays.push([date, arrayDays[i][1]]);
            if(arrayDays[i][1] != 0){
                $('.chartDays').removeClass('none');
            }
        }
        launchChart1(arrayTasks);
        launchChart2(datasDays);
    }

    function launchChart1(datas){
        $('#chart1').highcharts({
            chart: {
                type: 'column',
                plotBackgroundColor: null,
                //plotShadow: false
                borderWidth: 2,
                borderRadius: 7,
                borderColor: '#D8D8D8',
                width:dialogWidth/12 * 7,
                height:dialogWidth/12 * 7 +50
            },
            title: {
                text: 'Tasks Overview'
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Amount'
                }
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
                        format: '{point.y}'
                    }
                }
            },
            series: [{
                name: 'Tasks',
                innerSize: '50%',
                data: datas
            }]
        });

    }

    function launchChart2(datas){
        $('#chart2').highcharts('StockChart', {
            chart: {

                borderWidth: 2,
                borderRadius: 7,
                borderColor: '#D8D8D8',
                width:dialogWidth/12 * 7 ,
                height:dialogWidth/12 * 7 +50
            },
            rangeSelector: {
                selected: 1
            },

            title: {
                text: 'Time worked per Day'
            },
            yAxis: {
                labels: {
                    formatter: function() {
                        // show the labels as whole hours (3600000 milliseconds = 1 hour)
                        return Highcharts.numberFormat(this.value/3600000,0)+'h';
                    }
                },
                title: {
                    text: 'Hours'
                },
                tickInterval: 3600000 // number of milliseconds in one hour
            },
            tooltip: {
                pointFormatter: function() {
                    var time = (Highcharts.numberFormat(this.y/3600000,2)).split('.')[0] + 'h';
                    if(Highcharts.numberFormat(this.y/3600000,2).split('.')[1] != 0){
                        time = time + Highcharts.numberFormat(this.y/3600000,2).split('.')[1] * 60 / 100 ;
                    }
                    return  '<b>Hours : </b>' +
                            time;
                },
                xDateFormat: '%e-%b-%Y'
            },
            scrollbar: {
                barBackgroundColor: 'gray',
                barBorderRadius: 7,
                barBorderWidth: 0,
                buttonBackgroundColor: 'gray',
                buttonBorderWidth: 0,
                buttonBorderRadius: 7,
                trackBackgroundColor: 'none',
                trackBorderWidth: 1,
                trackBorderRadius: 8,
                trackBorderColor: '#CCC'
            },

            series: [{
                name: 'Hours',
                data: datas,
                tooltip: {
                    valueDecimals: 2
                },
                marker : {
                    enabled : true,
                    radius : 5
                }
            }]
        });


    }
});





      
      
