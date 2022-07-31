$(document).ready(function(){
    $('.open-side-menu').click(function(){
        $('aside').toggleClass('open');
        $('.side-menu i').toggleClass('open');
    });  
});