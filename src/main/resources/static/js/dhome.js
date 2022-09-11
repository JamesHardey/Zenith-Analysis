$(document).ready(function(){

    $('#btn-course p').click(function(){

        $('.main .recent-assignment').removeClass('active');
        $('.main .courses-class').addClass('active');
    });

    $('#btn-ass').click(function(){

        $('.main .courses-class').removeClass('active');
        $('.main .recent-assignment').addClass('active');
    });



    $('#cv-dis').click(function(){
        $('.aside-right .res-cl-sample .cv-sample').toggleClass('show');
        $('i.arrow').toogleClass('drop');
    });


    $('#cl-dis').click(function(){
        $('.aside-right .res-cl-sample .cover-letter-sample').toggleClass('show');
        $('i.arrow').toogleClass('drop');
    });


    // $('.courses li').click(function(){
    //     $('.main .recent-assignment').removeClass('active');
    //     $('.main .courses-class').addClass('active');
    // })


    // $('.assignment li').click(function(){
    //     $('.main .courses-class').removeClass('active');
    //     $('.main .recent-assignment').addClass('active');
    // });

    $('.link').click(function(evt){
        let links = document.querySelectorAll('.link');
        links.forEach((n) => {
           n.classList.remove('active')
        });
        evt.currentTarget.classList.add('active');
        console.log(evt);
    });

});