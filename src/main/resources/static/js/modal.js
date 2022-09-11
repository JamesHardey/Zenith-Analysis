$(document).ready(function(){

    $('.modal-close').click(function(evt){
        evt.preventDefault();
        $('.modal').removeClass('show');
    })


     $('.delete-btn').click(function(evt){
        evt.preventDefault();
        var href = $(this).attr('href');
        $('#delete-confirm').attr('href',href);
        $('#delete-modal').toggleClass('show');
    });



    /* Edit Event script */

    $('.event-edit').click(function(evt){
        evt.preventDefault();
        var href = $(this).attr('href');
        $.get(href,function(event,status){
            $('#edit-event-modal .form #id').val(event.id);
            $('#edit-event-modal .form #title').val(event.title);
            $('#edit-event-modal .form #sub-time').val(event.time);
            $('#edit-event-modal .form #sub-date').val(event.date);
            $('#edit-event-modal .form #details').val(event.details);
        });
        $('#edit-event-modal').toggleClass('show');
    });





    /* Edit Assignment and Delete Assignment script */

    $('.assignment-edit').click(function(evt){
            evt.preventDefault();
            var href = $(this).attr('href');
            $.get(href,function(ass,status){
                $('#edit-assignment-modal .form #id').val(ass.id);
                $('#edit-assignment-modal .form #title').val(ass.title);
                $('#edit-assignment-modal .form #course').val(ass.course);
                $('#edit-assignment-modal .form #time').val(ass.time);
                 $('#edit-assignment-modal .form #date').val(ass.submissionDate);
                $('#edit-assignment-modal .form #details').val(ass.details);
            });

            $('#edit-assignment-modal').toggleClass('show');
        });


    /* Edit Course */

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


    /*Edit Uploaded Link*/

    $('.upload-edit').click(function(evt){
            evt.preventDefault();
            var href = $(this).attr('href');
            $.get(href,function(upload,status){
                $('#edit-upload-modal .form #id').val(upload.id);
                $('#edit-upload-modal .form #title').val(upload.title);
                $('#edit-upload-modal .form #url').val(upload.url);
                $('#edit-upload-modal .form #course').val(upload.course);
                $('#edit-upload-modal .form #message').val(upload.message);
            });

            $('#edit-upload-modal').toggleClass('show');
        });

    
});