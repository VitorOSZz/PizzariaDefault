const main = document.querySelector("#menu");
const cart = document.querySelector("#cart");

function updateCartButton() {
    const mainTop = main.getBoundingClientRect().top;

    if (mainTop <= window.innerHeight) {
        cart.style.display = "block";
    } else {
        cart.style.display = "none";
    }
}
updateCartButton();
window.addEventListener("scroll", updateCartButton);
