
// For the scrolling-to-top button
jQuery(document).ready(function() {
	var duration = 500;
	jQuery(window).scroll(function() {
		if (jQuery(this).scrollTop() > 0) {
			jQuery('#scroll-to-top').fadeIn(duration);
		} else {
			jQuery('#scroll-to-top').fadeOut(duration);
		}
	});
				
	jQuery('#scroll-to-top').click(function(event) {
		event.preventDefault();
		jQuery('html, body').animate({scrollTop: 0}, duration);
		return false;
	})
});

// Show the 'changes since last completion' in list view
function showListView() {
	document.getElementById("change-table").style.display = "none";
	document.getElementById("change-list").style.display = "table";
	document.getElementById("list-view-button").disabled = true;
	document.getElementById("table-view-button").disabled = false;
	document.getElementById("select-table-button").style.display = "none";
	document.getElementById("select-table-button").disabled = true;
}

// Show the 'changes since last completion' in table view
function showTableView() {
	document.getElementById("change-table").style.display = "table";
	document.getElementById("change-list").style.display = "none";
	document.getElementById("list-view-button").disabled = false;
	document.getElementById("table-view-button").disabled = true;
	document.getElementById("select-table-button").style.display = "block";
	document.getElementById("select-table-button").disabled = false;

}

// Show the 'changes since last completion' from the table
function selectTable(el) {
    var body = document.body, range, sel;
    if (document.createRange && window.getSelection) {
       	range = document.createRange();
        sel = window.getSelection();
        sel.removeAllRanges();
        try {
            range.selectNodeContents(el);
            sel.addRange(range);
        } catch (e) {
           	 range.selectNode(el);
            sel.addRange(range);
        }
    } else if (body.createTextRange) {
        range = body.createTextRange();
        range.moveToElementText(el);
        range.select();
    }
}

