$(document).ready(function(){

    $('.aside-left i').click(function(){
        $('.aside-left .courses i').toggleClass('show');
        $('.aside-left .courses .sub-menu').toggleClass('show');
    });



    $('#btn-ass').click(function(){
        $('.aside-left .courses i').removeClass('show');
        $('.aside-left .courses .sub-menu').removeClass('show');
        $('.main .courses-class').removeClass('active');
        $('.main .my-course').removeClass('active');
        $('.main .recent-assignment').addClass('active');
    });


    $('.sub-menu #item-1').click(function(evt){
        $('.aside-left .courses i').removeClass('show');
        $('.aside-left .courses .sub-menu').removeClass('show');
        $('.main .recent-assignment').removeClass('active');
        $('.main .courses-class').removeClass('active');
        $('.main .my-course .presentation').removeClass('show');
        $('.main .my-course .outline').addClass('show');
        $('.main .my-course').addClass('active');
    });

    $('.sub-menu #item-2').click(function(evt){
        $('.aside-left .courses i').removeClass('show');
        $('.aside-left .courses .sub-menu').removeClass('show');
        $('.main .recent-assignment').removeClass('active');
        $('.main .courses-class').removeClass('active');
        $('.main .my-course .outline').removeClass('show');
        $('.main .my-course .presentation').addClass('show');
        $('.main .my-course').addClass('active');
    });


    $('#btn-act').click(function(){
        $('.aside-left .courses i').removeClass('show');
        $('.aside-left .courses .sub-menu').removeClass('show');
        $('.main .recent-assignment').removeClass('active');
        $('.main .my-course').removeClass('active');
        $('.main .courses-class').addClass('active');
    });



    $('#cv-dis').click(function(){
        $('.aside-right .res-cl-sample .cv-sample').toggleClass('show');
        $('i.arrow').toogleClass('drop');
    });


    $('#cl-dis').click(function(){
        $('.aside-right .res-cl-sample .cover-letter-sample').toggleClass('show');
        $('i.arrow').toogleClass('drop');
    });


    $('.link').click(function(evt){
        let links = document.querySelectorAll('.link');
        links.forEach((n) => {
           n.classList.remove('active')
        });
        evt.currentTarget.classList.add('active');
    });

});