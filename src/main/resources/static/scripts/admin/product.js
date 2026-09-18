const PRODUCTS_KEY = "admin-products";
const productSizes = {
    pizza: ["Napoletana", "Media", "Grande", "Familia"],
    drink: ["750ml", "2l"],
    calzone: ["Calzone"]
};

const initialProducts = [
    {
        id: "mock-pizza-calabresa",
        type: "pizza",
        name: "Pizza de Calabresa",
        description: "Calabresa, queijo, cebola e oregano",
        image: "",
        status: "active",
        sizes: [
            { name: "Napoletana", price: 29.9 },
            { name: "Media", price: 39.9 },
            { name: "Grande", price: 49.9 },
            { name: "Familia", price: 59.9 }
        ]
    },
    {
        id: "mock-pizza-frango",
        type: "pizza",
        name: "Pizza de Frango com Catupiry",
        description: "Frango desfiado, catupiry e oregano",
        image: "",
        status: "active",
        sizes: [
            { name: "Napoletana", price: 32.9 },
            { name: "Media", price: 42.9 },
            { name: "Grande", price: 52.9 },
            { name: "Familia", price: 62.9 }
        ]
    },
    {
        id: "mock-pizza-mussarela",
        type: "pizza",
        name: "Pizza de Mussarela",
        description: "Mussarela, tomate e manjericao",
        image: "",
        status: "active",
        sizes: [
            { name: "Napoletana", price: 27.9 },
            { name: "Media", price: 37.9 },
            { name: "Grande", price: 47.9 },
            { name: "Familia", price: 57.9 }
        ]
    }
];

let editingProductId = null;
let activeFilter = "all";
let productPageInitialized = false;
let productListLoadedFromBackend = false;

function getProducts() {
    try {
        return JSON.parse(localStorage.getItem(PRODUCTS_KEY)) || [];
    } catch (error) {
        return [];
    }
}

function saveProducts(products) {
    localStorage.setItem(PRODUCTS_KEY, JSON.stringify(products));
}

function initializeProducts() {
    if (localStorage.getItem(PRODUCTS_KEY) === null) saveProducts(initialProducts);
}

function imagePath(imageName) {
    if (!imageName) return "";
    if (imageName.startsWith("http") || imageName.startsWith("data:") || imageName.startsWith("/")) return imageName;
    return `/api/images/cards/${imageName}`;
}

function normalizeApiProduct(product, type, sizeName) {
    return {
        id: `${type}-${product.id}-${sizeName || product.size || "item"}`,
        sourceId: product.id,
        type,
        name: product.name,
        description: product.description || "",
        image: imagePath(product.imageName),
        status: "active",
        sizes: [
            {
                name: sizeName || product.size || "Unico",
                price: Number(product.price || 0) / 100
            }
        ]
    };
}

async function fetchJson(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Erro ao buscar ${url}`);
    return response.json();
}

async function loadProductsFromBackend() {
    const requests = [
        { type: "pizza", size: "Napoletana", url: "/api/products/pizzas/napoletana" },
        { type: "pizza", size: "Media", url: "/api/products/pizzas/medium" },
        { type: "pizza", size: "Grande", url: "/api/products/pizzas/big" },
        { type: "pizza", size: "Familia", url: "/api/products/pizzas/giant" },
        { type: "drink", url: "/api/products/drinks/soda" },
        { type: "drink", url: "/api/products/drinks/water" },
        { type: "drink", url: "/api/products/drinks/beer" }
    ];

    const responses = await Promise.allSettled(requests.map(request => fetchJson(request.url)));
    const productsByKey = new Map();

    responses.forEach((response, index) => {
        if (response.status !== "fulfilled") return;
        const request = requests[index];
        response.value.forEach(apiProduct => {
            if (request.type === "pizza") {
                const key = `pizza-${apiProduct.id}`;
                const existingProduct = productsByKey.get(key) || normalizeApiProduct(apiProduct, request.type, request.size);
                if (productsByKey.has(key)) {
                    existingProduct.sizes.push({
                        name: request.size,
                        price: Number(apiProduct.price || 0) / 100
                    });
                }
                productsByKey.set(key, existingProduct);
                return;
            }

            productsByKey.set(`${request.type}-${apiProduct.id}`, normalizeApiProduct(apiProduct, request.type, apiProduct.size));
        });
    });

    const products = [...productsByKey.values()];
    if (!products.length) return;

    saveProducts(products);
    productListLoadedFromBackend = true;
}

function escapeHtml(value) {
    return String(value || "").replace(/[&<>'"]/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        "'": "&#039;",
        '"': "&quot;"
    }[character]));
}

function formatPrice(price) {
    const formattedPrice = Number(price || 0).toLocaleString("en-US", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
    return `R$ ${formattedPrice}`;
}

function updateImagePreview(source = "") {
    const preview = document.getElementById("preview-image");
    const placeholder = document.querySelector("#image-preview span");
    preview.src = source;
    preview.hidden = !source;
    placeholder.hidden = Boolean(source);
}

function renderProducts() {
    const products = getProducts().filter(product => activeFilter === "all" || product.type === activeFilter);
    const productList = document.getElementById("products-list");
    const count = document.querySelector("main h3");
    const allProducts = getProducts();

    count.textContent = `${allProducts.length} ${allProducts.length === 1 ? "produto cadastrado" : "produtos cadastrados"}`;
    productList.innerHTML = products.map(product => {
        const prices = (product.sizes || []).map(size => Number(size.price)).filter(Number.isFinite);
        const lowestPrice = prices.length ? Math.min(...prices) : 0;
        const highestPrice = prices.length ? Math.max(...prices) : 0;
        const priceText = prices.length > 1 && lowestPrice !== highestPrice
            ? `${formatPrice(lowestPrice)} - ${formatPrice(highestPrice)}`
            : formatPrice(lowestPrice);
        const image = product.image || "/api/images/icons/cart.webp";
        const statusLabel = product.status === "active" ? "Ativo" : "Inativo";

        return `<tr>
            <td><div class="product-name">
                <img src="${escapeHtml(image)}" alt="">
                <div class="product-text"><h4>${escapeHtml(product.name)}</h4><p>${escapeHtml(product.description)}</p></div>
            </div></td>
            <td><span class="${escapeHtml(product.type)}">${product.type === "drink" ? "Bebida" : product.type === "calzone" ? "Calzone" : "Pizza"}</span></td>
            <td class="price">${priceText}</td>
            <td><button type="button" class="status ${product.status}" data-status-id="${product.id}" aria-label="Alterar status de ${escapeHtml(product.name)}">${statusLabel}</button></td>
            <td><div class="product-options">
                <button type="button" class="edit" data-edit-id="${product.id}" aria-label="Editar ${escapeHtml(product.name)}"><img src="/api/images/icons/edit.svg" alt=""></button>
                <button type="button" class="delete" data-delete-id="${product.id}" aria-label="Excluir ${escapeHtml(product.name)}"><img src="/api/images/icons/delete.svg" alt=""></button>
            </div></td>
        </tr>`;
    }).join("");
}

function renderSizeFields(type, selectedSizes = []) {
    const container = document.getElementById("size-prices");
    container.innerHTML = productSizes[type].map((size, index) => {
        const savedSize = selectedSizes.find(item => item.name === size);
        const isSelected = Boolean(savedSize);
        const inputValue = savedSize ? Number(savedSize.price).toFixed(2) : "";
        return `<div class="size-price-row">
            <div class="size-checkbox">
                <input type="checkbox" id="size-enabled-${index}" name="size-enabled-${index}" value="${size}"${isSelected ? " checked" : ""}>
            </div>
            <div class="size-price-content">
                <label for="size-${index}">${size}</label>
                <div class="price-input"><span>R$</span><input type="number" id="size-${index}" name="size-${index}" min="0" step="0.01" placeholder="0.00" value="${escapeHtml(inputValue)}"${isSelected ? " required" : " disabled"}></div>
            </div>
        </div>`;
    }).join("");
    container.querySelectorAll("input[type='checkbox']").forEach(checkbox => {
        checkbox.addEventListener("change", () => {
            const priceInput = checkbox.closest(".size-price-row").querySelector("input[type='number']");
            priceInput.disabled = !checkbox.checked;
            priceInput.required = checkbox.checked;
        });
    });
}

function resetProductForm() {
    const form = document.querySelector("#product form");
    form.reset();
    editingProductId = null;
    document.querySelector("#product h1").textContent = "Adicionar produto";
    document.querySelector("#product_header p").textContent = "Preencha os dados do novo produto";
    document.getElementById("save-product").textContent = "Adicionar produto";
    updateImagePreview();
    renderSizeFields("pizza");
}

function edit_product(productId = null) {
    const dialog = document.getElementById("product");
    const product = productId ? getProducts().find(item => item.id === productId) : null;

    resetProductForm();
    if (product) {
        editingProductId = product.id;
        document.querySelector("#product h1").textContent = "Editar produto";
        document.querySelector("#product_header p").textContent = "Atualize os dados do produto";
        document.getElementById("save-product").textContent = "Salvar alterações";
        document.querySelector(`[name="type"][value="${product.type}"]`).checked = true;
        document.getElementById("name").value = product.name;
        document.getElementById("desc").value = product.description;
        document.getElementById("image_url").value = product.image || "";
        updateImagePreview(product.image || "");
        document.querySelector(`[name="status"][value="${product.status}"]`).checked = true;
        renderSizeFields(product.type, product.sizes);
    }

    if (!dialog.open) dialog.showModal();
}

function handleFormSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const type = formData.get("type");
    const product = {
        id: editingProductId || crypto.randomUUID(),
        type,
        name: formData.get("name").trim(),
        description: formData.get("desc").trim(),
        image: formData.get("image_url").trim(),
        status: formData.get("status"),
        sizes: productSizes[type]
            .map((name, index) => ({
                name,
                price: Number(formData.get(`size-${index}`)),
                enabled: formData.has(`size-enabled-${index}`)
            }))
            .filter(size => size.enabled)
            .map(({ name, price }) => ({ name, price }))
    };
    const products = getProducts();
    const productIndex = products.findIndex(item => item.id === product.id);
    if (productIndex >= 0) products[productIndex] = product;
    else products.push(product);
    saveProducts(products);
    event.currentTarget.closest("dialog").close();
    renderProducts();
}

function bindProductList() {
    const productList = document.getElementById("products-list");
    if (!productList || productList.dataset.bound === "true") return;
    productList.dataset.bound = "true";
    productList.addEventListener("click", event => {
        const actionButton = event.target.closest("button");
        if (!actionButton) return;
        const editId = actionButton.dataset.editId;
        const deleteId = actionButton.dataset.deleteId;
        const statusId = actionButton.dataset.statusId;
        if (editId) edit_product(editId);
        if (statusId) {
            const products = getProducts();
            const product = products.find(item => item.id === statusId);
            if (!product) return;
            product.status = product.status === "active" ? "inactive" : "active";
            saveProducts(products);
            renderProducts();
        }
        if (deleteId) {
            saveProducts(getProducts().filter(product => product.id !== deleteId));
            renderProducts();
        }
    });
}

function bindProductFilters() {
    document.querySelectorAll("#table-and-nav nav button").forEach(button => {
        if (button.dataset.bound === "true") return;
        button.dataset.bound = "true";
        button.addEventListener("click", () => {
            document.querySelector(".border-bottom-active")?.classList.remove("border-bottom-active");
            button.classList.add("border-bottom-active");
            activeFilter = { "everyting-btn": "all", "pizzas-btn": "pizza", "calzones-btn": "calzone", "drinks-btn": "drink" }[button.id];
            renderProducts();
        });
    });
}

async function initializeProductPage() {
    initializeProducts();
    if (!productPageInitialized) {
        const form = document.querySelector("#product form");
        form.addEventListener("submit", handleFormSubmit);
        const imageUrl = document.getElementById("image_url");
        const imageFile = document.getElementById("image_file");
        imageUrl.addEventListener("input", () => updateImagePreview(imageUrl.value.trim()));
        imageFile.addEventListener("change", () => {
            const [file] = imageFile.files;
            if (!file) return;
            const reader = new FileReader();
            reader.addEventListener("load", () => {
                imageUrl.value = reader.result;
                updateImagePreview(reader.result);
            });
            reader.readAsDataURL(file);
        });
        document.querySelectorAll("[name='type']").forEach(input => input.addEventListener("change", () => renderSizeFields(input.value)));
        productPageInitialized = true;
    }
    bindProductList();
    bindProductFilters();
    renderSizeFields("pizza");
    renderProducts();
    if (!productListLoadedFromBackend) {
        try {
            await loadProductsFromBackend();
            renderProducts();
        } catch (error) {
            console.warn("Nao foi possivel carregar os produtos do backend.", error);
        }
    }
}

document.addEventListener("DOMContentLoaded", initializeProductPage);
