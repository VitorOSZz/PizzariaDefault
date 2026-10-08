import {
    findSize
} from "../core/catalog-store.js";

import {
    formatPrice
} from "../core/utils.js";

import {
    addItem
} from "../cart/cart-service.js";

import {
    closeCart
} from "../cart/cart.js";

const sectionsProducts =
    document.getElementById(
        "sections_products"
    );

const modal =
    document.getElementById("modal");

const modalProduct =
    document.getElementById(
        "modal_product"
    );

const productList =
    document.getElementById(
        "product_list"
    );

const modalBuy =
    document.getElementById(
        "modal_buy"
    );

const buttonExit =
    document.getElementById(
        "button_exit"
    );

const form =
    document.forms["modal_content"];

const FALLBACK_IMAGE_URL =
    "https://picsum.photos/600/400?random=1";

let currentSelection = null;
let maxProducts = 1;

function createImage(
    imageUrl,
    alt,
    imageFit
) {
    const image =
        document.createElement("img");

    image.src =
        imageUrl || FALLBACK_IMAGE_URL;

    image.alt = alt;

    if (imageFit) {
        image.style.objectFit =
            imageFit.toLowerCase();
    }

    image.addEventListener(
        "error",
        () => {
            image.onerror = null;
            image.src =
                FALLBACK_IMAGE_URL;
        }
    );

    return image;
}

function getMaxProducts(size) {
    /*
     * This rule is temporary.
     *
     * Flavor limits belong to the catalog/domain and should eventually
     * be provided by the backend as part of the Size response.
     */
    if (Number.isInteger(size.maxFlavors)) {
        return size.maxFlavors;
    }

    const sizeName =
        size.name.toLowerCase();

    if (sizeName.includes("napoletana")) {
        return 1;
    }

    if (
        currentSelection.category.name
            .toLowerCase()
            .includes("pizzas")
    ) {
        return 2;
    }

    return 1;
}

function updateCheckboxes() {
    const checkboxes =
        productList.querySelectorAll(
            'input[name="flavor"]'
        );

    const selectedCount =
        productList.querySelectorAll(
            'input[name="flavor"]:checked'
        ).length;

    checkboxes.forEach(
        checkbox => {
            checkbox.disabled =
                !checkbox.checked &&
                selectedCount >= maxProducts;
        }
    );
}

function renderModalHeader(
    category,
    size
) {
    modalProduct.replaceChildren();

    const title =
        document.createElement("h3");

    title.textContent =
        `${category.name} - ${size.name}`;

    modalProduct.appendChild(title);

    if (size.description) {
        const description =
            document.createElement("p");

        description.textContent =
            size.description;

        modalProduct.appendChild(
            description
        );
    }
}

function createProductOption(product) {
    const listItem =
        document.createElement("li");

    const label =
        document.createElement("label");

    label.className =
        "flavor-card";

    const imageContainer =
        document.createElement("div");

    imageContainer.className =
        "image-container";

    imageContainer.appendChild(
        createImage(
            product.imageUrl,
            product.name,
            product.imageFit
        )
    );

    const productInfo =
        document.createElement("div");

    productInfo.className =
        "product-info";

    const title =
        document.createElement("h5");

    title.textContent =
        product.name;

    const description =
        document.createElement("p");

    description.textContent =
        product.description || "";

    const price =
        document.createElement("p");

    const priceSpan =
        document.createElement("span");

    priceSpan.textContent =
        formatPrice(
            product.price
        );

    price.appendChild(priceSpan);

    productInfo.appendChild(title);
    productInfo.appendChild(description);
    productInfo.appendChild(price);

    const checkbox =
        document.createElement("input");

    checkbox.type = "checkbox";
    checkbox.name = "flavor";
    checkbox.value = product.id;

    checkbox.dataset.name =
        product.name;

    checkbox.dataset.price =
        String(product.price);

    label.appendChild(
        imageContainer
    );

    label.appendChild(
        productInfo
    );

    label.appendChild(
        checkbox
    );

    listItem.appendChild(label);

    return listItem;
}

function renderProducts(products) {
    productList.replaceChildren();

    const fragment =
        document.createDocumentFragment();

    for (const product of products) {
        fragment.appendChild(
            createProductOption(product)
        );
    }

    productList.appendChild(fragment);
}

function getSelectedProducts() {
    return [
        ...form.querySelectorAll(
            'input[name="flavor"]:checked'
        )
    ].map(input => ({
        id: input.value,
        name: input.dataset.name,
        price: Number(input.dataset.price)
    }));
}

function validateSelection(products) {
    if (products.length === 0) {
        window.alert(
            "Por favor escolha pelo menos um produto."
        );

        return false;
    }

    if (products.length > maxProducts) {
        window.alert(
            `Você pode escolher no máximo ${maxProducts} opção(ões).`
        );

        return false;
    }

    return true;
}

function getUnitPrice(products) {
    return Math.max(
        ...products.map(
            product => product.price
        )
    );
}

function createCartItem(products) {
    if (!currentSelection) {
        throw new Error(
            "No active catalog selection."
        );
    }

    return {
        id: crypto.randomUUID(),

        categoryId:
        currentSelection.category.id,

        categoryName:
        currentSelection.category.name,

        sizeId:
        currentSelection.size.id,

        sizeName:
        currentSelection.size.name,

        products,

        unitPrice:
            getUnitPrice(products),

        observation:
            form.elements.observation.value.trim(),

        quantity: 1
    };
}

function resetModal() {
    form.reset();

    productList
        .querySelectorAll(
            'input[name="flavor"]'
        )
        .forEach(
            checkbox => {
                checkbox.disabled = false;
            }
        );
}

function openProductModal(sizeId) {
    const selection =
        findSize(sizeId);

    if (!selection) {
        console.error(
            `Size "${sizeId}" not found in catalog.`
        );

        window.alert(
            "Não foi possível carregar este produto."
        );

        return;
    }

    if (
        !selection.size.products ||
        selection.size.products.length === 0
    ) {
        window.alert(
            "Este produto ainda não está disponível."
        );

        return;
    }

    currentSelection = selection;

    maxProducts =
        getMaxProducts(
            selection.size
        );

    renderModalHeader(
        selection.category,
        selection.size
    );

    renderProducts(
        selection.size.products
    );

    resetModal();

    modalBuy.disabled = false;

    closeCart();

    modal.showModal();
}

function closeProductModal() {
    currentSelection = null;

    modal.close();
}

function handleCatalogClick(event) {
    const button =
        event.target.closest(
            ".card[data-size-id]"
        );

    if (!button) {
        return;
    }

    openProductModal(
        button.dataset.sizeId
    );
}

function handleProductSelection(event) {
    if (
        !event.target.matches(
            'input[name="flavor"]'
        )
    ) {
        return;
    }

    updateCheckboxes();
}

function handleSubmit(event) {
    event.preventDefault();

    if (!currentSelection) {
        console.error(
            "Cannot submit without a current selection."
        );

        return;
    }

    const products =
        getSelectedProducts();

    if (!validateSelection(products)) {
        return;
    }

    modalBuy.disabled = true;

    try {
        const item =
            createCartItem(products);

        addItem(item);

        closeProductModal();
    } catch (error) {
        console.error(
            "Failed to add item to cart.",
            error
        );

        window.alert(
            "Não foi possível adicionar o item ao carrinho."
        );
    } finally {
        modalBuy.disabled = false;
    }
}

sectionsProducts?.addEventListener(
    "click",
    handleCatalogClick
);

productList?.addEventListener(
    "change",
    handleProductSelection
);

form?.addEventListener(
    "submit",
    handleSubmit
);

buttonExit?.addEventListener(
    "click",
    closeProductModal
);

modal?.addEventListener(
    "click",
    event => {
        if (event.target === modal) {
            closeProductModal();
        }
    }
);

modal?.addEventListener(
    "close",
    () => {
        currentSelection = null;
    }
);