$(function() {
    $("#btn-toggle").on("click", function(){
        var nav = $('[role="navigation"]').hasClass("nav-close") ? "open" : "close";
        $.post('/projectus/switch/navigation/'+nav, function(response) {
            $('[role="navigation"]').toggleClass("nav-close");
        }).fail(function(){
            showError(error.responseText);
        });
    });

    $('[data-toggle="tooltip"]').tooltip();

    $(".alert .close").on("click", function(e){
        $(this).parent().addClass("none");
    });

    
});

function sizingModal(){
    //    setTimeout(function(){
    //        $('.modal.in .modal-body').css('max-height', window.innerHeight - (160 + $('.modal.in .modal-header').height()));
    //    }, 150);
}

function showError(descriptionError){
    if (descriptionError.length < 200 || true) {
        $("#error-info span").text(descriptionError);
        console.log("showError:", descriptionError);
        $("#error-info").removeClass("none");
        setTimeout(function(){
            $("#error-info").addClass("none");
        }, 5000);
    } else {
        document.open();
        document.write(descriptionError);
        document.close();
    }
}

function showSuccess(descriptionSuccess){
    $("#success span").text(descriptionSuccess);
    $("#success").removeClass("none");
    setTimeout(function(){
        $("#success").addClass("none");
    }, 5000);
}

function refreshModal(idModal){
    $("#"+idModal+" .modal-dialog .modal-content").html(
        "<div class='modal-header'>"+
            "<button type='button' class='close' data-dismiss='modal'>&times;</button>"+
            "<div class='page-header'>"+
            '<div class="col-lg-12 center-block">'+
            "<h2 id='nom-de-la-tache' style='text-align:center;'>Loading...</h2>"+
            '</div>'+
            '<div class="wrapper">'+
            '<svg class="hourglass" viewBox="0 0 120 206" preserveAspectRatio="none">'+
            '<path class="middleSpinner" d="M120 0H0v206h120V0zM77.1 133.2C87.5 140.9 92 145 92 152.6V178H28v-25.4c0-7.6 4.5-11.7 14.9-19.4 6-4.5 13-9.6 17.1-17 4.1 7.4 11.1 12.6 17.1 17zM60 89.7c-4.1-7.3-11.1-12.5-17.1-17C32.5 65.1 28 61 28 53.4V28h64v25.4c0 7.6-4.5 11.7-14.9 19.4-6 4.4-13 9.6-17.1 16.9z"/>'+
            '<path class="outer" d="M93.7 95.3c10.5-7.7 26.3-19.4 26.3-41.9V0H0v53.4c0 22.5 15.8 34.2 26.3 41.9 3 2.2 7.9 5.8 9 7.7-1.1 1.9-6 5.5-9 7.7C15.8 118.4 0 130.1 0 152.6V206h120v-53.4c0-22.5-15.8-34.2-26.3-41.9-3-2.2-7.9-5.8-9-7.7 1.1-2 6-5.5 9-7.7zM70.6 103c0 18 35.4 21.8 35.4 49.6V192H14v-39.4c0-27.9 35.4-31.6 35.4-49.6S14 81.2 14 53.4V14h92v39.4C106 81.2 70.6 85 70.6 103z"/>'+
            '</svg>'+
            '</div>'+
            "</div>"+
            "</div>"
    );
}


function escapeHtml(text) {
    var map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };

    return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}

function post(url, data , success, error){
    if(success === undefined || success === null) {
        console.log("this post(from base.js) must have 3+ arguments ! (a success function is needed)");
        return;
    }
    if(error === undefined || error === null) {
        error = function(error){
            stopLoading();
            showError(error.responseText);
        };
    }
    $.post(url, data, function(response) {}).done(success).fail(error);
}

function get(url, success, error){
    if(success === undefined || success === null || url === undefined || url === null) {
        console.log("this get(from base.js) must have 2+ arguments ! (a success function is needed)");
        return;
    }
    if(error === undefined || error === null) {
        error = function(error){
            stopLoading();
            showError(error.responseText);
        };
    }
    $.get(url, function(response) {}).done(success).fail(error);
}

function clearModal(){
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
}

function removeInfo(){
    $("input[type=text]").val("");
    $("textarea").val("");
}

$(window).on('resize', function(){
    sizingModal();
});

function fieldEmptyError(fieldValue, fieldName, selectorError){
    if(fieldValue.length == 0 || fieldValue === " ") {
        $(selectorError).text("The field " + fieldName + " is required");
        return true;
    } else {
        return false;
    }
}

function startLoading(){
    $("#maskLoad").css('display', 'block');
}
function startLoadingMod(){
    $("#maskLoadModal").css('display', 'block');
}

function stopLoading(){
    $("#maskLoad").css('display', 'none');
    $("#maskLoadModal").css('display', 'none');
}

