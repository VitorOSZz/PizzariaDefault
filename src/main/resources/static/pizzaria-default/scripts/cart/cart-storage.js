const CART_KEY = "cart";

export function getCart() {
    const storedCart = localStorage.getItem(CART_KEY);

    if (!storedCart) {
        return [];
    }

    try {
        const cart = JSON.parse(storedCart);

        if (!Array.isArray(cart)) {
            throw new Error("Cart is not an array.");
        }

        return cart;
    } catch (error) {
        console.error(
            "Failed to read cart from localStorage.",
            error
        );

        localStorage.removeItem(CART_KEY);

        return [];
    }
}

export function saveCart(cart) {
    localStorage.setItem(
        CART_KEY,
        JSON.stringify(cart)
    );
}

export function clearCart() {
    localStorage.removeItem(CART_KEY);
}