buttons = document.querySelectorAll('.card');
button_exit = document.getElementById("button_exit");

const modal = document.getElementById("modal");

buttons.forEach(button => {
    button.addEventListener('click', (event) => {
        console.log("Button clicked");
        modal.showModal()
    });
});

button_exit.addEventListener('click', (event) => {
    console.log("Closing modal");
    modal.close();
})