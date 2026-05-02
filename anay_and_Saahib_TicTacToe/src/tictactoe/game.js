document.addEventListener("DOMContentLoaded", () => {
    const clickSound = new Audio("sounds/click.wav");
    const winSound = new Audio("sounds/win.wav");
    const drawSound = new Audio("sounds/draw.wav");

    const boardElement = document.getElementById("board");
    const message = document.getElementById("message");

    fetch("/game")
        .then(res => res.text())
        .then(html => {
            document.body.innerHTML = html;
        });
    
    boardElement.addEventListener("click", () => clickSound.play()); // This script is mostly for client-side animations

    function showWin() {
        winSound.play();
        message.innerHTML = "🎉 You win!";
    }

    function showDraw() {
        drawSound.play();
        message.innerHTML = "🤝 Draw!";
    }
});
