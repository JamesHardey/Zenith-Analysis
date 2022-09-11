
let slideIndex = 0;
showSlides(slideIndex);
startAnimate();

function plusSlides(n){
    clearTimeout()
    showSlides(slideIndex += n);
    startAnimate
}


function currentSlide(n){
    clearTimeout()
    showSlides(slideIndex = n);
    startAnimate
}


function showSlides(n){
    
    let i;
    let slides = document.getElementsByClassName('slides');
    let dots = document.getElementsByClassName('dot');

    if(n > slides.length) { slideIndex = 1 }

    if(n < 1) { slideIndex = slides.length }

    for(i = 0; i<slides.length; i++){
        slides[i].style.display = "none";
    }

    for(i = 0; i<dots.length; i++){
        dots[i].className = dots[i].className.replace('active','')
    }
    
    slides[slideIndex - 1].style.display = "block";
    dots[slideIndex- 1].className += " active";

}



function startAnimate(){
    let i;
    let slides = document.getElementsByClassName("slides");
    let dots = document.getElementsByClassName('dot');

    for(i = 0; i<slides.length; i++){
        slides[i].style.display="none";
    }

    for(i = 0; i<dots.length; i++){
        dots[i].className = dots[i].className.replace('active','')
    }

    slideIndex++;

    if(slideIndex > slides.length){
        slideIndex = 1
    }

    slides[slideIndex - 1].style.display = "block";
    dots[slideIndex- 1].className += " active";
    setTimeout(startAnimate, 5000);
}

