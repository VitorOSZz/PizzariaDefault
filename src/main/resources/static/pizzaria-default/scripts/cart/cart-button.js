const menu = document.getElementById("menu");
const cartButton = document.getElementById("cart");

let cartOpen = false;

function setVisible(visible) {
    if (!cartButton) {
        return;
    }

    cartButton.style.display =
        visible ? "block" : "none";
}

export function setCartOpen(open) {
    cartOpen = open;

    updateCartButton();
}

export function updateCartButton() {
    if (!menu) {
        return;
    }

    if (cartOpen) {
        setVisible(false);
        return;
    }

    const menuTop =
        menu.getBoundingClientRect().top;

    setVisible(
        menuTop <= window.innerHeight
    );
}

updateCartButton();

window.addEventListener(
    "scroll",
    updateCartButton,
    { passive: true }
);