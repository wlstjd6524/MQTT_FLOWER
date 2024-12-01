//Hook up the tweet display

$(document).ready(function() {
    $('#home').vegas({
        slides: [
            { src: 'images/01.jpg' },
            { src: 'images/02.jpg' }
        ],
        overlay: true,
        transition: 'fade',
        transitionDuration: 1000,
        delay: 5000,
        animation: 'random'
    });
});
