$(function() {

	$('#disableRelease').on('click', disableRelease);
	
	function disableRelease(){
		var idRelease = $(this).parent().attr('id');
		$.ajax({
			   url: '/projectus/disable_release/'+idRelease,
			   data: {
			      format: 'json'
			   },
			   type: 'GET',
			   error: function(jqXHR, textStatus, errorThrown) {
				    alert(textStatus);
			   },
			   dataType: 'json',
			   success: function(data) {
				   if(!data.error){
					   $('#accordionReleases').find("."+idRelease).clone().appendTo($('#accordionDisabledReleases'));
					   $('#accordionReleases').find("."+idRelease).remove();
					   $('#accordionDisabledReleases').find(".addSprint").remove();


					   $('#accordionDisabledReleases').find("."+idRelease).find("a").removeClass("modalDisableRelease").addClass("activateRelease").find("img").attr("src","assets/images/ic_add_black.svg");
					   $('#accordionDisabledReleases').find("."+idRelease).find("a").on('click', activateRelease);
				   }
			   }
		});
		
	}
});

