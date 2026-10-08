import {
    getActiveItems,
    getCartTotal
} from "../cart/cart-service.js";

import {
    formatPrice
} from "../core/utils.js";

const endingGenerate =
    document.getElementById("ending-generate");

const endingButton =
    document.getElementById("ending-button");

const endingFooter =
    document.getElementById("ending-footer");

function createProduct(product) {
    const element = document.createElement("div");

    element.className = "checkout-card-flavor";

    const name = document.createElement("span");

    name.textContent = product.name;

    element.appendChild(name);

    return element;
}

function createObservation(item) {
    if (!item.observation?.trim()) {
        return null;
    }

    const element = document.createElement("p");

    element.className = "checkout-card-observation";
    element.textContent = `Obs: ${item.observation}`;

    return element;
}

function createCheckoutCard(item) {
    const article = document.createElement("article");

    article.className = "checkout-card";
    article.id = item.id;

    const title = document.createElement("h2");

    title.textContent = item.sizeName;

    article.appendChild(title);

    const products = document.createElement("div");

    products.className = "checkout-card-flavors";

    for (const product of item.products) {
        products.appendChild(
            createProduct(product)
        );
    }

    article.appendChild(products);

    const observation = createObservation(item);

    if (observation) {
        article.appendChild(observation);
    }

    const footer = document.createElement("div");

    footer.className = "checkout-card-footer";

    const price = document.createElement("span");

    price.className = "checkout-card-price";

    price.textContent = formatPrice(
        item.unitPrice * item.quantity
    );

    const quantity = document.createElement("span");

    quantity.className = "checkout-card-quantity";
    quantity.textContent = `x${item.quantity}`;

    footer.appendChild(price);
    footer.appendChild(quantity);

    article.appendChild(footer);

    return article;
}

function renderEmptyCheckout() {
    endingGenerate.replaceChildren();

    const container = document.createElement("div");

    container.className = "checkout-empty";

    const title = document.createElement("h2");

    title.textContent = "Seu carrinho está vazio";

    const link = document.createElement("a");

    link.href = "/";
    link.textContent = "Voltar para o cardápio";

    container.appendChild(title);
    container.appendChild(link);

    endingGenerate.appendChild(container);

    endingButton.style.display = "none";
    endingFooter.style.display = "none";
}

function renderItems(items) {
    endingGenerate.replaceChildren();

    const fragment =
        document.createDocumentFragment();

    for (const item of items) {
        fragment.appendChild(
            createCheckoutCard(item)
        );
    }

    endingGenerate.appendChild(fragment);
}

function renderFooter() {
    endingFooter.replaceChildren();

    const label = document.createElement("div");

    label.textContent = "Total";

    const priceContainer = document.createElement("div");

    const price = document.createElement("span");

    price.textContent = formatPrice(
        getCartTotal()
    );

    priceContainer.appendChild(price);

    endingFooter.appendChild(label);
    endingFooter.appendChild(priceContainer);
}

export function initializeCheckout() {
    const items = getActiveItems();

    if (items.length === 0) {
        renderEmptyCheckout();
        return;
    }

    endingButton.style.display = "block";
    endingFooter.style.display = "block";

    renderItems(items);
    renderFooter();
}