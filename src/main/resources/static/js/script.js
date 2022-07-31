$(document).ready(function(){
    $(window).scroll(function(){
        // sticky navbar on scroll script
        if(this.scrollY > 20){
            $('.nav').addClass("scrolling");
        }else{
            $('.nav').removeClass("scrolling");
        }
    });
    
    $('.open-menu').click(function(){
        $('.nav .navbar .nav-right ul').toggleClass('active');
        $('.nav .navbar .nav-right .menu-btn i').toggleClass('active');
    });

    
});