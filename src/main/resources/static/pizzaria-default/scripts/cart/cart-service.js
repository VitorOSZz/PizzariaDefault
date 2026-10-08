import {
    getCart,
    saveCart
} from "./cart-storage.js";

function getProductIds(products) {
    return products
        .map(product => product.id)
        .sort();
}

function areProductsEqual(firstProducts, secondProducts) {
    const firstIds = getProductIds(firstProducts);
    const secondIds = getProductIds(secondProducts);

    if (firstIds.length !== secondIds.length) {
        return false;
    }

    return firstIds.every(
        (id, index) => id === secondIds[index]
    );
}

function areItemsEqual(firstItem, secondItem) {
    return (
        firstItem.categoryId === secondItem.categoryId &&
        firstItem.sizeId === secondItem.sizeId &&
        firstItem.observation === secondItem.observation &&
        areProductsEqual(
            firstItem.products,
            secondItem.products
        )
    );
}

export function addItem(item) {
    const cart = getCart();

    const existingItem = cart.find(
        currentItem => areItemsEqual(
            currentItem,
            item
        )
    );

    if (existingItem) {
        existingItem.quantity += item.quantity;
    } else {
        cart.push(item);
    }

    saveCart(cart);
}

export function updateItemQuantity(itemId, amount) {
    const cart = getCart();

    const item = cart.find(
        currentItem => currentItem.id === itemId
    );

    if (!item) {
        return;
    }

    item.quantity = Math.max(
        0,
        item.quantity + amount
    );

    saveCart(cart);
}

export function getActiveItems() {
    return getCart().filter(
        item => item.quantity > 0
    );
}

export function getCartTotal() {
    return getActiveItems().reduce(
        (total, item) => {
            return total + (
                item.unitPrice * item.quantity
            );
        },
        0
    );
}

export function hasItems() {
    return getActiveItems().length > 0;
}