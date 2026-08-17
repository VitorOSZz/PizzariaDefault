const productList = document.getElementById("product_list");

const maxFlavors = {
    napoletana: 1,
    medium: 1,
    big: 2,
    giant: 2
};

let currentMaxFlavors = 0;
let currentSize;

function setFlavorLimit(product) {
    currentMaxFlavors = maxFlavors[product] ?? 0;

    updateFlavorCheckboxes();
}

function updateFlavorCheckboxes() {
    const checkboxes = document.querySelectorAll(
        '.flavor-card input[type="checkbox"]'
    );

    const selected = document.querySelectorAll(
        '.flavor-card input[type="checkbox"]:checked'
    );

    checkboxes.forEach(checkbox => {
        checkbox.disabled =
            !checkbox.checked &&
            selected.length >= currentMaxFlavors;
    });
}

productList.addEventListener('change', event => {
    if (event.target.matches('.flavor-card input[type="checkbox"]')) {
        updateFlavorCheckboxes();
    }
});

// Submited

const form = document.forms["modal_content"];

form.addEventListener("submit", (event) => {
    console.log("submitted");
    if (!validateForm()) {
        return;
    }

    const submitButton = form.querySelector('button[type="submit"]');

    submitButton.disabled = true;

    const item = createItem();
    saveItem(item)

    console.log(item);
    modal.close();
});

function validateForm() {
    let flavors_chosen = 0;

    form["flavor"].forEach(flavor => {
        if (flavor.checked) {
            flavors_chosen++;
        }
    });

    if (flavors_chosen < 1) {
        console.log("Por favor escolha pelo menos um sabor.");
        return false;
    }

    if (flavors_chosen > currentMaxFlavors) {
        console.log("Limite de sabores por pizza excedido.");
        return false;
    }

    return true;
}

function createItem() {
    let type = "pizza";
    const item = [];

    form["flavor"].forEach(flavor => {
        if (flavor.checked) {
            item.push({
                id: flavor.value,
                name: flavor.dataset.name,
                price: Number(flavor.dataset.price)
            })
        }
    });

    const price = Math.max(
        ...item.map(flavor => flavor.price)
    );
    const observation = form["observation"].value;

    return {
        id: crypto.randomUUID(),
        type: type,
        size: currentSize,
        flavors: item,
        price: price,
        observation: observation,
        quantity: 1
    };
}

function saveItem(item) {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];

    const existingItem = cart.find(cartItem => {
        const cartFlavors = cartItem.flavors
            .map(flavor => flavor.id)
            .sort();

        const itemFlavors = item.flavors
            .map(flavor => flavor.id)
            .sort();

        return (
            cartItem.size === item.size &&
            JSON.stringify(cartFlavors) === JSON.stringify(itemFlavors) &&
            cartItem.observation.trim() === item.observation.trim()
        );
    });

    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push(item);
    }

    localStorage.setItem("cart", JSON.stringify(cart));
}