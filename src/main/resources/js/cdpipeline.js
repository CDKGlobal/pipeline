jQuery(document).ready(function() {
	var duration = 500;
	jQuery(window).scroll(function() {
		if (jQuery(this).scrollTop() > 0) {
			jQuery('.back-to-top').fadeIn(duration);
		} else {
			jQuery('.back-to-top').fadeOut(duration);
		}
	});
				
	jQuery('.back-to-top').click(function(event) {
		event.preventDefault();
		jQuery('html, body').animate({scrollTop: 0}, duration);
		return false;
	})
});