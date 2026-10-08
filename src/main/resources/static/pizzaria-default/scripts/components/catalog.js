import { formatPrice } from  "../core/utils.js";

const FALLBACK_IMAGE_URL = "https://picsum.photos/600/400?random=1";

function sortByOrder(items) {
    return [...items].sort((a, b) => b.sortOrder - a.sortOrder);
}

function getLowestPrice(products) {
    const prices = products
        .map(product => product.price)
        .filter(Number.isInteger);

    return prices.length > 0
        ? Math.min(...prices)
        : null;
}

/*
 * Centralizes image handling so every card always renders an image.
 * The API supplies the primary URL, while the frontend guarantees
 * a fallback when the URL is missing or the image request fails.
 */
function createImage(imageUrl, alt, imageFit = null) {
    const image = document.createElement("img");

    image.src = imageUrl || FALLBACK_IMAGE_URL;
    image.alt = alt;

    if (imageFit) {
        image.style.objectFit = imageFit.toLowerCase();
    }

    image.addEventListener("error", () => {
        image.onerror = null;
        image.src = FALLBACK_IMAGE_URL;
    });

    return image;
}

function createStartingPrice(products) {
    const lowestPrice = getLowestPrice(products);

    const paragraph = document.createElement("p");
    paragraph.textContent = "A partir de ";

    const price = document.createElement("span");

    price.textContent = lowestPrice === null
        ? "consulte"
        : formatPrice(lowestPrice);

    paragraph.appendChild(price);

    return paragraph;
}

function createSizeCard(size, category) {
    const button = document.createElement("button");

    button.className = "card";
    button.type = "button";

    button.dataset.sizeId = size.id;
    button.dataset.categoryId = category.id;

    const content = document.createElement("div");

    const title = document.createElement("h4");
    title.textContent = size.name;

    content.appendChild(title);

    if (size.description) {
        const description = document.createElement("p");
        description.textContent = size.description;

        content.appendChild(description);
    }

    content.appendChild(
        createStartingPrice(size.products)
    );

    button.appendChild(content);

    button.appendChild(
        createImage(
            size.imageUrl,
            size.name,
            size.imageFit
        )
    );

    return button;
}

function createSizes(sizes, category) {
    const cards = document.createElement("div");

    cards.className = "cards";

    for (const size of sortByOrder(sizes)) {
        cards.appendChild(
            createSizeCard(size, category)
        );
    }

    return cards;
}

function createProductCard(product) {
    const button = document.createElement("button");

    button.className = "card";
    button.type = "button";

    button.dataset.productId = product.id;

    const content = document.createElement("div");

    const title = document.createElement("h4");
    title.textContent = product.name;

    content.appendChild(title);

    if (product.description) {
        const description = document.createElement("p");
        description.textContent = product.description;

        content.appendChild(description);
    }

    const price = document.createElement("p");
    price.textContent = formatPrice(product.price);

    content.appendChild(price);

    button.appendChild(content);

    button.appendChild(
        createImage(
            product.imageUrl,
            product.name,
            product.imageFit
        )
    );

    return button;
}

function createProducts(products) {
    const cards = document.createElement("div");

    cards.className = "cards";

    for (const product of sortByOrder(products)) {
        cards.appendChild(
            createProductCard(product)
        );
    }

    return cards;
}

function createCategorySection(category) {
    const section = document.createElement("section");

    section.id = `category-${category.id}`;

    const title = document.createElement("h3");
    title.textContent = category.name;

    section.appendChild(title);

    if (category.description) {
        const description = document.createElement("p");
        description.textContent = category.description;

        section.appendChild(description);
    }

    if (category.sizes?.length > 0) {
        section.appendChild(
            createSizes(category.sizes, category)
        );
    }

    if (category.products?.length > 0) {
        section.appendChild(
            createProducts(category.products)
        );
    }

    return section;
}

export function renderCatalog(catalog) {
    const container = document.getElementById("sections_products");

    if (!container) {
        throw new Error(
            'Element "#sections_products" was not found.'
        );
    }

    container.replaceChildren();

    const fragment = document.createDocumentFragment();

    for (const category of sortByOrder(catalog)) {
        fragment.appendChild(
            createCategorySection(category)
        );
    }

    container.appendChild(fragment);
}