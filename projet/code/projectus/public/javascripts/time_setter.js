var time_setter = (function () {
    var html = '<div class="col-lg-12 btn-group btn-slider">'+
            ' <button class="col-lg-2 btn btn-primary moins1h">-1h</button>'+
            ' <button class="col-lg-2 btn btn-primary moins15m">-15m</button>'+
            ' <span class="col-lg-4 bold labelTime">0 h 00</span>'+
            ' <button class="col-lg-2 btn btn-primary plus15m">+15m</button>'+
            ' <button class="col-lg-2 btn btn-primary plus1h">+1h</button>'+
            ' </div>';

    var min = 15;
    var max = 99*60 + 59;
    var default_time = 15;
    var time = default_time;

    init();

    function init() {
        $(".timeSetter").html(html);
        bindEvents();
        reset();
    }

    function reset() {
        var valueToSet = default_time;
        $(".timeSetter").each(function() {
            var dataValue = $(this).data("value");
            if (typeof dataValue !== 'undefined') {
                valueToSet = parseInt(dataValue, 10);
                return false;
            } else {
                return true;
            }
        });
        set(valueToSet);
    }

    function toDefault() {
        set(default_time);
    }

    function bindEvents() {
        $(".plus1h").on("click", plus60);
        $(".moins1h").on("click", moins60);
        $(".plus15m").on("click",  plus15);
        $(".moins15m").on("click", moins15);
        pubsub.on("sprints.task.hasEstimate", set);
        pubsub.on("dashboard_task.working_time_updated", setMinimum);
    }

    function setMinimum(newmin) {
        if (newmin < 0) {
            min = 0;
        } else {
            min = newmin;
        }
    }

    function updateLabel(){
        printTimeLabel(".labelTime", time);
        pubsub.emit("time_setter.updated", time);
    }

    function set(newtime) {
        if (newtime < min) {
            time = min;
        } else if (newtime > max) {
            time = max;
        } else {
            time = newtime;
        }
        updateLabel();
    }

    function plus60() {
        set(time + 60);
    }
    function moins60() {
        set(time - 60);
    }
    function plus15() {
        set(time + 15);
    }
    function moins15() {
        set(time - 15);
    }

    function get() {
        return time;
    }


    return {
        init: init,
        reset: reset,
        toDefault: toDefault,
        set: set,
        get: get
    };
})();

function printTimeLabel(selector, time) {
    var heure = Math.floor(time/60);
    var minute = ((time%60 < 10)? "0" : "");
    minute += time%60;
    $(selector).text(heure+"h"+minute);
}
