const main = document.querySelector("#menu");
const cart = document.querySelector("#cart");

function updateCart() {
    const mainTop = main.getBoundingClientRect().top;

    if (mainTop <= window.innerHeight) {
        cart.style.display = "block";
    } else {
        cart.style.display = "none";
    }
}
updateCart();
window.addEventListener("scroll", updateCart);
