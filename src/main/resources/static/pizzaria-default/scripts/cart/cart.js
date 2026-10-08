import {
    getActiveItems,
    getCartTotal,
    updateItemQuantity,
    hasItems
} from "./cart-service.js";

import {
    formatPrice
} from "../core/utils.js";

import {
    updateCartButton,
    setCartOpen
} from "./cart-button.js";

const cartButton =
    document.getElementById("cart-button");

const cartLayout =
    document.getElementById("cart_layout");

const cartGenerate =
    document.getElementById("cart_generate");

const overlay =
    document.getElementById("overlay");

const cartBackButton =
    document.getElementById("cart_back_button");

const cartFooterPrice =
    document.querySelector("#cart_footer_price p");

const cartFooterFinish =
    document.querySelector("#cart_footer_finish p");

const buttonEnding =
    document.getElementById("button-checkout");

const modal =
    document.getElementById("modal");

function createQuantityButton(
    className,
    itemId,
    text
) {
    const button =
        document.createElement("button");

    button.className = className;
    button.type = "button";
    button.dataset.itemId = itemId;
    button.textContent = text;

    return button;
}

function createItemTitle(item) {
    return item.sizeName;
}

function createProductsDescription(item) {
    return item.products
        .map(product => product.name)
        .join(" / ");
}

function createObservation(item) {
    if (!item.observation?.trim()) {
        return null;
    }

    const paragraph =
        document.createElement("p");

    paragraph.className =
        "cart_card_observation";

    paragraph.textContent =
        `Obs: ${item.observation}`;

    return paragraph;
}

function createCartCard(item) {
    const card =
        document.createElement("article");

    card.className = "cart_card";
    card.id = item.id;

    const info =
        document.createElement("div");

    info.className =
        "cart_card_info";

    const title =
        document.createElement("h5");

    title.textContent =
        createItemTitle(item);

    const products =
        document.createElement("p");

    products.textContent =
        createProductsDescription(item);

    const price =
        document.createElement("p");

    const priceSpan =
        document.createElement("span");

    priceSpan.textContent =
        formatPrice(item.unitPrice);

    price.appendChild(priceSpan);

    info.appendChild(title);
    info.appendChild(products);
    info.appendChild(price);

    const observation =
        createObservation(item);

    if (observation) {
        info.appendChild(observation);
    }

    const controls =
        document.createElement("div");

    controls.className =
        "cart_card_buttons";

    controls.appendChild(
        createQuantityButton(
            "button-less",
            item.id,
            "-"
        )
    );

    const quantity =
        document.createElement("div");

    quantity.id =
        `quantity-${item.id}`;

    quantity.textContent =
        item.quantity;

    controls.appendChild(quantity);

    controls.appendChild(
        createQuantityButton(
            "button-plus",
            item.id,
            "+"
        )
    );

    card.appendChild(info);
    card.appendChild(controls);

    return card;
}

function renderItems() {
    cartGenerate.replaceChildren();

    const items =
        getActiveItems();

    const fragment =
        document.createDocumentFragment();

    for (const item of items) {
        fragment.appendChild(
            createCartCard(item)
        );
    }

    cartGenerate.appendChild(fragment);
}

function renderFooter() {
    const total =
        formatPrice(
            getCartTotal()
        );

    if (cartFooterPrice) {
        cartFooterPrice.textContent = total;
    }

    if (cartFooterFinish) {
        cartFooterFinish.textContent = total;
    }
}

export function updateCart() {
    renderItems();
    renderFooter();
}

export function openCart() {
    modal?.close();

    cartLayout.classList.add("open");
    overlay.classList.add("open");

    setCartOpen(true);

    updateCart();
}

export function closeCart() {
    cartLayout.classList.remove("open");
    overlay.classList.remove("open");

    setCartOpen(false);
    updateCartButton();
}

function changeItemQuantity(itemId, amount) {
    updateItemQuantity(
        itemId,
        amount
    );

    updateCart();
}

function handleQuantityButton(event) {
    const button =
        event.target.closest(
            ".button-plus, .button-less"
        );

    if (!button) {
        return;
    }

    const amount =
        button.classList.contains("button-plus")
            ? 1
            : -1;

    changeItemQuantity(
        button.dataset.itemId,
        amount
    );
}

cartButton?.addEventListener(
    "click",
    openCart
);

cartBackButton?.addEventListener(
    "click",
    closeCart
);

overlay?.addEventListener(
    "click",
    event => {
        if (event.target === overlay) {
            closeCart();
        }
    }
);

cartGenerate?.addEventListener(
    "click",
    handleQuantityButton
);

updateCart();