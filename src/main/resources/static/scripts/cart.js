const cartButton = document.querySelector("#cart-button");
const cartLayout = document.querySelector("#cart_layout");

const overlay = document.querySelector("#overlay");
const cartBackButton = document.querySelector("#cart_back_button");

function openCart() {
    cart.style.display = "none";

    overlay.classList.add("open");
    cartLayout.classList.add("open");
}

function closeCart() {
    overlay.classList.remove("open");
    cartLayout.classList.remove("open");

    updateCart();
}

cartButton.addEventListener("click", openCart);
cartBackButton.addEventListener("click", closeCart);
overlay.addEventListener("click", closeCart);