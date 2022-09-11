$(document).ready(function(){
    $('.open-side-menu').click(function(){
        $('aside').toggleClass('open');
        $('.side-menu i').toggleClass('open');
    });


    $('.course-edit').click(function(evt){
            evt.preventDefault();
            var href = $(this).attr('href');
            $.get(href,function(course,status){
                $('#edit-courses-modal .form #id').val(course.id);
                $('#edit-courses-modal .form #title').val(course.title);
                $('#edit-courses-modal .form #price').val(course.price);
                $('#edit-courses-modal .form #details').val(course.details);
            });

            $('#edit-courses-modal').toggleClass('show');
        });
});