//events - a super-basic Javascript (publish subscribe) pattern
//based on https://gist.github.com/learncodeacademy/777349747d8382bfb722
var pubsub = (function() {
    var events = {};

    //subscribe (in a responder to a event, once) : on("nameEvent", doSomething);
    function on(eventName, handler) {
        events[eventName] = events[eventName] || [];
        events[eventName].push(handler);
    }

    //unsubscribe (in a responder to a event, once) : off("nameEvent", doSomething);
    function off(eventName, handler) {
        if (events[eventName]) {
            for (var i = 0; i < events[eventName].length; i++) {
                if( events[eventName][i] === handler ) {
                    events[eventName].splice(i, 1);
                    break;
                }
            }
        }
    }

    //publish (in the source, when there is an event) : emit("nameEvent", dataForHandlers)
    function emit(eventName, data) {
        if (events[eventName]) {
            events[eventName].forEach(function(handler) {
                handler(data);
            });
        }
    }

    return {
        on: on,
        off: off,
        emit: emit
    };
})();