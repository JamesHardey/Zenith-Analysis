$(document).ready(function(){
    $('.open-menu').click(function(){
        $('.side-bar').toggleClass('active');
        $('header').toggleClass('active');
        $('.main-content').toggleClass('active');
    });

    $('.dp-image').click(function(){
        $('.profile').toggleClass('view');
        $('.arrow-container').toggleClass('view');
    });


    $('#delBtn').click(function(evt){
        evt.preventDefault();
        $('.alert-dialog').addClass('pop');
        var href = $('#delBtn').attr('href');
        $('deleteUserForm').attr('action',href);
    });

    $('#cancel').click(function(){
        $('.alert-dialog').removeClass('pop')
    })
});

