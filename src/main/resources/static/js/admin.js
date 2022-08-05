$(document).ready(function(){
    $('.open-menu').click(function(){
        $('.side-bar').toggleClass('active');
        $('header').toggleClass('active');
        $('.main-content').toggleClass('active');
    });

    $('.dp-image').click(function(){
        $('.profile').toggleClass('view');
        $('.arrow-container').toggleClass('view');
    })
});