const productList = document.getElementById("product_list");

const maxFlavors = {
    napoletana: 1,
    medium: 1,
    big: 2,
    giant: 2,
    water: 1,
    soda: 1,
    beer: 1
};

let currentMaxFlavors = 0;
let currentType;
let currentOption;

function setFlavorLimit(product) {
    console.log("setFlavorLimit: " + product)
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
        checkbox.disabled = !checkbox.checked && selected.length >= currentMaxFlavors;
    });
}

productList.addEventListener('change', event => {
    if (event.target.matches('.flavor-card input[type="checkbox"]')) {
        updateFlavorCheckboxes();
    }
});

// Submitted

const form = document.forms["modal_content"];

form.addEventListener("submit", (event) => {
    event.preventDefault();
    console.log("submitted");

    console.log("Current Type: " + currentType);
    const flavors = [...form.querySelectorAll('[name="flavor"]:checked')];
    if (!validateForm(flavors)) {
        return;
    }

    const submitButton = form.querySelector('button[type="submit"]');
    submitButton.disabled = true;

    console.log(
        flavors.map(flavor => ({
            id: flavor.value,
            name: flavor.dataset.name
        }))
    );
    const item = createItem(flavors);
    saveItem(item)

    console.log(item);
    modal.close();
});

function validateForm(flavors) {
    let flavors_chosen = 0;

    flavors.forEach(flavor => {
        if (flavor.checked) {
            flavors_chosen++;
        }
    });

    if (flavors_chosen < 1) {
        console.log("Por favor escolha pelo menos um sabor.");
        return false;
    }

    if (flavors_chosen > currentMaxFlavors) {
        window.alert("Limite de sabores por pizza excedido.");
        return false;
    }

    return true;
}

function createItem(flavors = []) {

    const observation = form["observation"].value;

    if (currentType === "pizzas") {

        const selectedFlavors = flavors.map(flavor => ({
            id: flavor.value,
            name: flavor.dataset.name,
            price: Number(flavor.dataset.price)
        }));

        const price = Math.max(
            ...selectedFlavors.map(flavor => flavor.price)
        );

        return {
            id: crypto.randomUUID(),
            type: currentType,
            size: currentOption,
            flavors: selectedFlavors,
            price: price,
            observation: observation,
            quantity: 1
        };
    }

    // drinks
    const drink = flavors[0];

    return {
        id: crypto.randomUUID(),
        type: currentType,
        option: currentOption,
        itemId: drink.value,
        name: drink.dataset.name,
        price: Number(drink.dataset.price),
        observation: observation,
        quantity: 1
    };
}

function saveItem(item) {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];

    const existingItem = cart.find(cartItem => {

        if (cartItem.type !== item.type) {
            return false;
        }

        // Pizza
        if (item.type === "pizzas") {
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
        }

        // Drink
        if (item.type === "drinks") {
            console.log("cartItem.itemId: " + cartItem.itemId);
            console.log("item.itemId: " + item.itemId);

            return (
                cartItem.itemId === item.itemId &&
                cartItem.observation.trim() === item.observation.trim()
            );
        }

        return false;
    });

    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push(item);
    }

    localStorage.setItem("cart", JSON.stringify(cart));
}