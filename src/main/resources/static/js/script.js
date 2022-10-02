$(document).ready(function(){
//    let elem = document.querySelector('#about');
//    let rect = elem.getBoundingClientRect();


    $(window).scroll(function(){
        // sticky navbar on scroll script

        if(this.scrollY > 25){
            $('.nav').addClass("scrolling");
        }else{
            $('.nav').removeClass("scrolling");
        }
    });

    $('.open-menu').click(function(){
        $('.nav .navbar .nav-right ul').toggleClass('active');
        $('.nav .navbar .nav-right .menu-btn i').toggleClass('active');
    });

    $('.pop-event .up-header #close-btn').click(function(){
        $('.pop-event').removeClass('show');
    })


});