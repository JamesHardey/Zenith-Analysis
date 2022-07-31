$(document).ready(function(){
    $('.open-menu').click(function(){
        $('.side-bar').toggleClass('active');
        $('header').toggleClass('active');
        $('.main-content').toggleClass('active');
    });  
});