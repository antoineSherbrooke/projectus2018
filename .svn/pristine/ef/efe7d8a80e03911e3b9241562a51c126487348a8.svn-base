$(function() {
    var MIN_SOURCE_SIZE = 100;
    var MAX_SOURCE_SIZE = 3500;
    var MAX_DESTINATION_SIZE = 100;
    var file;
    var image;
    var canvas;
    var jcrop_api =null;

    $.ajaxSetup({
        type: 'POST',
        timeout: 1000
    });

    $('#file').change(uploadLocallyImage);
    $('#commit').on("click", save);
    $(".formEdit").submit(function(event) {
        var password = $(this).find("[name='password']").val();
        var passwordValid = $(this).find("[name='passwordValid']").val();

        if(password != passwordValid){
            event.preventDefault();
            $("#errorEdit").text("The format of cip isn't correct");
            return;
        }
    });

    function createObjectURL(object) {
        return (window.URL) ? window.URL.createObjectURL(object) : window.webkitURL.createObjectURL(object);
    }

    function revokeObjectURL(url) {
        return (window.URL) ? window.URL.revokeObjectURL(url) : window.webkitURL.revokeObjectURL(url);
    }

    function uploadLocallyImage() {
        if(this.files.length) {
            for(var i in this.files) {
                if(this.files.hasOwnProperty(i)){
                    file = this.files[i];
                    var extension =file.name.split('.').pop().toLowerCase();
                    if(extension != "png" && extension != "jpg" && extension != "jpeg"){
                        showError("Is not a extension valid, please upload PNG or JPG");
                        return;
                    }
                    var src = createObjectURL(this.files[i]);
                    image = new Image();
                    image.src = src;
                    image.onload = function() {
                        if(image.width > MAX_SOURCE_SIZE || image.height > MAX_SOURCE_SIZE) {
                            this.src = "";
                            showError("Image size is to large ! (must be <= "
                                      +MAX_SOURCE_SIZE+"x"+MAX_SOURCE_SIZE+")");
                        } else if (image.width < MIN_SOURCE_SIZE || image.height < MIN_SOURCE_SIZE) {
                            this.src = "";
                            showError("Image size is to small ! (must be >= "
                                      +MIN_SOURCE_SIZE+"x"+MIN_SOURCE_SIZE+")");
                        } else {
                            var $image = $("#target"); 
                            $image.attr('src', src);

                            $("#dialogCrop").modal();
                            if(jcrop_api != null){
                                jcrop_api.destroy();
                            }
                            var dialogWidth = $("#dialogCrop").width();
                            var dialogHeight = $("#dialogCrop").height();
                            console.log("dialog dims", dialogWidth, dialogHeight);
                            $('#target').Jcrop({
                                boxWidth: dialogWidth*0.6,
                                onChange: showPreview,
                                onSelect: showPreview,
                                minSize :[MIN_SOURCE_SIZE, MIN_SOURCE_SIZE],
                                maxSize: [MAX_SOURCE_SIZE, MAX_SOURCE_SIZE],
                                setSelect :[0,0, MIN_SOURCE_SIZE, MIN_SOURCE_SIZE],
                                aspectRatio: 1
                            }, function(){
                                jcrop_api = this;
                            });
                        }
                    };
                }
            }
        }
    }

    function showPreview(c) {
        var coords = {
            x: Math.round(c.x),
            y: Math.round(c.y),
            w: Math.round(c.w),
            h: Math.round(c.h)
        };
        var context;
        $("#preview").empty();
        $("#preview").append("<canvas id=\"canvas\">");
        canvas = $("#canvas")[0];
        context = canvas.getContext("2d");
        var $selectDiv = $("div.jcrop-holder>div:first");
        canvas.width = (coords.w < MAX_DESTINATION_SIZE)? coords.w : MAX_DESTINATION_SIZE;
        canvas.height = (coords.h < MAX_DESTINATION_SIZE)? coords.h : MAX_DESTINATION_SIZE;
        context.drawImage(image, coords.x, coords.y, coords.w, coords.h,
                          0,0,canvas.width,canvas.height);
    }

    function save() {
        var data = new FormData();
        var blob = dataURLtoBlob(canvas.toDataURL('image/jpeg', 0.8));
        data.append('picture', blob);

        $.ajax({
            url: '/projectus/profil/upload/'+canvas.width+"/"+canvas.height,
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function(data){
                $("#image-profil").attr("src", "/projectus/profil/logo/light?"+ new Date().getTime());
                $("#face").attr("src", "/projectus/profil/logo/light?"+ new Date().getTime());
                $("#dialogCrop").modal("hide");
            },
            error : function(error){
                $("#error").text(error.responseText);
            }
        });
    }

    function dataURLtoBlob(dataURL) {
        var BASE64_MARKER = ';base64,';
        var parts, contentType, raw;
        if (dataURL.indexOf(BASE64_MARKER) == -1) {
            parts = dataURL.split(',');
            contentType = parts[0].split(':')[1];
            raw = decodeURIComponent(parts[1]);

            return new Blob([raw], {
                type: contentType
            });
        } else {
            parts = dataURL.split(BASE64_MARKER);
            contentType = parts[0].split(':')[1];
            raw = window.atob(parts[1]);
            var rawLength = raw.length;
            var uInt8Array = new Uint8Array(rawLength);
            for (var i = 0; i < rawLength; ++i) {
                uInt8Array[i] = raw.charCodeAt(i);
            }

            return new Blob([uInt8Array], {
                type: contentType
            });
        }
    }

});

