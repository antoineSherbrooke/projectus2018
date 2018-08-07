
var valid_sprint_dates = (function() {
    var TODAY;
    var beginDate;
    var endDate;
    var existingSprintsDates;
    var responseBegin;

    var selectorDates = "";
    var selectorBeginDate = "";
    var selectorEndDate = "";
    function init(_beginDate, _endDate, _dates) {
        TODAY = toDayBegin(new Date());
        beginDate = TODAY;
        responseBegin = "";

        selectorBeginDate = _beginDate;
        selectorEndDate = _endDate;
        selectorDates = _dates;
        $.datepicker.setDefaults({
            dateFormat: "DD, d MM, yy"
        });
        $(selectorBeginDate).datepicker({
            defaultDate: "+1w",
            onClose: updateSprintEndDateBoundaries
        });
        $(selectorEndDate).datepicker({
            onClose: updateSprintBeginDateBoundaries
        });
    }

    function updateWithUrl(url) {
        get(url, function(response) {
            stopLoading();
            endDate = toDayEnd(new Date(response.end));
            existingSprintsDates = response.sprintsDates.sort();
            responseBegin = response.begin;
            $(selectorDates).datepicker("option", "minDate", 0 );

            update();
        });
    }

    function update() {
        $(selectorDates).datepicker("option", "maxDate", endDate);

        function excludeIfExisting(date){
            var dateString = jQuery.datepicker.formatDate('yy-mm-dd', date);
            var selectable = ($.inArray(dateString, existingSprintsDates) == -1);
            return [selectable];
        }
        $(selectorDates).datepicker("option", "beforeShowDay", excludeIfExisting);

        if(responseBegin != ""){
            beginDate = toDayBegin(new Date(responseBegin));
            if(beginDate.getTime() > TODAY.getTime()){
                $(selectorDates).datepicker("option", "minDate", beginDate);
            }
        }
    }
    
    function updateSprintEndDateBoundaries(selectedDate) {
        if(selectedDate != ""){
            $(selectorEndDate).datepicker( "option", "minDate", selectedDate );
            var dateSelectedTime = new Date($(this).datepicker('getDate').getTime());
            var lastValidDate;
            for(var i = 0; i < existingSprintsDates.length; i++){
                var date = toDayBegin(new Date(existingSprintsDates[i]));
                if(dateSelectedTime <= date.getTime() && date.getTime() <= endDate){
                    lastValidDate = date;
                }
            }
            var maxDate = (lastValidDate == null)? endDate : lastValidDate;
            $(selectorEndDate).datepicker( "option", "maxDate", maxDate);
        }
    }

    function updateSprintBeginDateBoundaries(selectedDate) {
        if(selectedDate != ""){
            $(selectorBeginDate).datepicker( "option", "maxDate", selectedDate);
            var dateSelectedTime = new Date($(this).datepicker('getDate').getTime());
            var firstValidDate;
            for(var i = existingSprintsDates.length-1; i >= 0; i--){
                var date = toDayBegin(new Date(existingSprintsDates[i]));
                if(beginDate <= date.getTime() && date.getTime() <= dateSelectedTime) {
                    firstValidDate = date;
                }
            }
            var minDate = (firstValidDate == null)? beginDate : firstValidDate;
            $(selectorBeginDate).datepicker( "option", "minDate", minDate);
        }
    }

    function toDayBegin(date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        return date;
    }
    function toDayEnd(date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        date.setMilliseconds(999);
        return date;
    }

    return {
        init: init,
        updateWithUrl: updateWithUrl,
        update: update
    };
})();
