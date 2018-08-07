var click = 0;
var dialogWidth = $('.btn_accordion').width() - 150;

$(window).on('resize', function(){
    dialogWidth = $('.btn_accordion').width() -150;
    var chart1 = $('#chart1, #chart3').highcharts();
    chart1.setSize(dialogWidth/2,dialogWidth/2, doAnimation=false);
    var chart2 = $('#chart2, #chart5').highcharts();
    chart2.setSize(dialogWidth/2,dialogWidth/2, doAnimation=false);
});

$( "#accordion" ).accordion({
    collapsible: true,
    active: false,
    heightStyle: "content"
});

launchCharts();

function roundDecimal(nombre, precision){
    var precision = precision || 2;
    var tmp = Math.pow(10, precision);
    return Math.round( nombre*tmp )/tmp;
}

function launchCharts(){
    var sprintId = $('#sprintId').attr('data-id');
    $.ajax({
        url: '/projectus/getDatas/'+sprintId,
        type: 'GET',
        error: function(jqXHR, textStatus, errorThrown) {
            alert(textStatus);
        },
        credits: {
            enabled: false
        },
        dataType: 'json',
        success: function(datas) {
            var data = datas.times;
            var array = [];
            for(var key in data){
                for(var key2 in data[key]){
                    arraytmp = [ key2, data[key][key2] * 60000];
                    array.push(arraytmp);
                }
            }
            var todoCount = $("#state-todo .accordion li").length;
            var doingCount = $("#state-doing .accordion li").length;
            var toreviseCount = $("#state-toreview .accordion li").length;
            var doneCount = $("#state-done .accordion li").length;

            var overview = [['Todo', todoCount],
                            ['Doing', doingCount],
                            ['To review', toreviseCount],
                            ['Done', doneCount]];
            launchChart1(array);
            launchChart2(overview);


            var datasTestSerie1 = [];
            var datasTestSerie2 = [];
            var datasTestSerie3 = [];
            var datasTestSerie4 = [];
            var datasTestX = [];
            var numOfDays = parseInt(datas.numOfDays) - 1;
            var numOfTasks = parseInt(datas.numOfTasks);

            for(var i = 0; i <= numOfDays ; i++){
                datasTestX.push("Day "+(i));
            }

            for(var i = numOfDays; i >= 0; i--){
                datasTestSerie1.push(roundDecimal(i * (numOfTasks/numOfDays), 2));
                datasTestSerie3.push(roundDecimal((numOfTasks/numOfDays), 2));
            }


            var datas3 = datas.burnDown;
            for(key3 in datas3){
                datasTestSerie2.push(datas3[key3])
            }

            launchChart5(datasTestX, datasTestSerie1, datasTestSerie2);
            click = 1;
        }
    });
}

function launchChart1(datas){
    $('#chart1, #chart3').highcharts({
        chart: {
            type: 'column',
            plotBackgroundColor: null,
            plotBorderWidth: 0,
            borderWidth: 2,
            borderRadius: 7,
            borderColor: '#D8D8D8',
            width:dialogWidth/2,
            height:dialogWidth/2
        },
        title: {
            text: 'Time Worked'
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
            height:dialogWidth/2
        },
        title: {
            text: 'Progress Overview'
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
            colorByPoint: true,
            data: datas
        }]
    });
}

function launchChart5(dataX, dataSerie1, dataSerie2){
    $('#chart5').highcharts({
        chart: {
            borderWidth: 2,
            borderRadius: 7,
            borderColor: '#D8D8D8',
            width:dialogWidth/2,
            height:dialogWidth/2
        },
        title: {
            text: 'Sprint Burndown Chart',
            x: -10 //center
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
        colors: ['blue', 'red'],
        plotOptions: {
            line: {
                lineWidth: 3
            },
            tooltip: {
                hideDelay: 200
            }
        },
        xAxis: {
            categories: dataX
        },
        yAxis: {
            title: {
                text: 'Remaining work (tasks)'

            },
            type: 'linear',
            min:0,
            tickInterval :1

        },

        tooltip: {
            valueSuffix: ' tasks remaining',
            crosshairs: true,
            shared: true
        },
        legend: {
            layout: 'horizontal',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
        },
        series: [{
            name: 'Ideal Burn',
            color: 'rgba(255,0,0,0.25)',
            lineWidth: 2,

            data: dataSerie1
        }, {
              name: 'Actual Burn',
              color: 'rgba(0,120,200,0.75)',
              marker: {
                  radius: 6
              },
              data: dataSerie2
          }]
    });
}

function refreshChart1(){
    var sprintId = $('#sprintId').attr('data-id');
    $.ajax({
        url: '/projectus/getDatas/'+sprintId,
        type: 'GET',
        error: function(jqXHR, textStatus, errorThrown) {
            alert(textStatus);
        },
        dataType: 'json',
        success: function(datas) {
            var data = datas.times;
            var array1 = [];
            var array2 = [];
            for(var key in data){
                for(var key2 in data[key]){
                    arraytmp1 = [ key2, data[key][key2] * 60000];
                    array1.push(arraytmp1);
                }
            }

            if(click == 1){
                var chart1 = $('#chart1').highcharts();
                chart1.series[0].setData(array1,true);
            }
        }
    });
}

function refreshChart2(){
    var chart2 = $('#chart2').highcharts();
    var todoCount = $("#state-todo .accordion li").length;
    var doingCount = $("#state-doing .accordion li").length;
    var toreviseCount = $("#state-toreview .accordion li").length;
    var doneCount = $("#state-done .accordion li").length;

    var overview = [['Todo', todoCount],
                    ['Doing', doingCount],
                    ['To revise', toreviseCount],
                    ['Done', doneCount]];

    chart2.series[0].setData(overview,true);
}



