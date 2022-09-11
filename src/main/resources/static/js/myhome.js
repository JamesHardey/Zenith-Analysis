$(document).ready(function(){
    alert('welcome');
    $('.cv-sample h4').click(function(){
        $('.cv-sample .cv-display').toggleClass('show');
    });  


    $('.cover-letter-sample h4').click(function(){
        $('.cover-letter-sample .cv-display').toggleClass('show');
    });
    
    
    $('.courses').click(function(){
        $('.main .recent-assignment').removeClass('active');
        $('.main .courses-class').addClass('active');
    })


    $('.assignment').click(function(){
        $('.main .courses-class').removeClass('active');
        $('.main .recent-assignment').addClass('active');
    })

    $('#btn-course').click(function(){
        $('aside.aside-left .assignment ul').removeClass('show');
        $('aside.aside-left .courses ul').toggleClass('show');
    });

    $('#btn-ass').click(function(){
        $('aside.aside-left .courses ul').removeClass('show');
        $('aside.aside-left .assignment ul').toggleClass('show');
    });


    $('up-com-btn').on('click', function(evt){
        evt.preventDefault();
        var href =
    })


});