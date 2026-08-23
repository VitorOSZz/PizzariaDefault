const cartButton = document.querySelector("#cart-button");
const cartLayout = document.querySelector("#cart_layout");
const cartGenerate = document.querySelector("#cart_generate");

const overlay = document.querySelector("#overlay");
const cartBackButton = document.querySelector("#cart_back_button");

function openCart() {
    cart.style.display = "none";

    modal.close();
    overlay.classList.remove("open");
    cartLayout.classList.remove("open");

    updateCart();
    overlay.classList.add("open");
    cartLayout.classList.add("open");
}

function closeCart() {
    overlay.classList.remove("open");
    cartLayout.classList.remove("open");
    updateCartButton();
}

cartButton.addEventListener("click", openCart);
cartBackButton.addEventListener("click", closeCart);
overlay.addEventListener("click", closeCart);

function parsePrice(itemPrice) {

    if (itemPrice <= 0) {
        return "0,00"
    }
    itemPrice = String(itemPrice);
    return itemPrice.slice(0, -2) + "," + itemPrice.slice(-2, itemPrice.length);
}

function generateCart() {
    cartGenerate.innerHTML = '';
    if (localStorage.getItem('cart') == null) {
        return;
    }

    let text = ""
    let items = JSON.parse(localStorage.cart);

    items.forEach(item => {

        // Skip items with quantity <= 0
        if (item.quantity <= 0) {
            return;
        }

        console.log("Generating cart, type: " + item.type)

        if (item.type === "pizzas") {
            let size = {
                "medium": "Média",
                "big": "Grande",
                "giant": "Família",
                "napoletana": "Napoletana"
            }[item.size];

            let flavors = "Pizza";
            if (item.flavors.length > 1) {
                item.flavors.forEach(flavor => {
                    flavors += " metade " + flavor.name + " e";
                })
                flavors = flavors.slice(0, -" e".length)
            } else {
                flavors += " de " + item.flavors[0].name;
            }
            let itemPrice = String(item.price);
            let price = parsePrice(itemPrice);
            let quantity = item.quantity;

            text += `<div class="cart_card" id="${item.id}">
                    <div class="cart_card_info">
                        <h5>Pizza ${size}</h5>
                        <p>${flavors}<br></p>`

            if (item.observation !== "") {
                text += `<p class="cart_card_observation">Obs: ${item.observation}</p>`
            }

            text += `<p><span>R$ ${price}</span></p>
                </div>
                    <div class="cart_card_buttons">
                        <button class="button-less" data-product="${item.id}">-</button>
                        <div id="quantity-${item.id}">${quantity}</div>
                        <button class="button-plus" data-product="${item.id}">+</button>
                    </div>
                </div>`;
        } else if (item.type === "drinks") {
            let itemPrice = String(item.price);
            let price = parsePrice(itemPrice);
            let quantity = item.quantity;

            let option = {
                "water": "Água",
                "soda": "Refrigerante",
                "beer": "Cerveja"
            }[item.option];

            text += `<div class="cart_card" id="${item.id}">
        <div class="cart_card_info">
            <h5>${option}</h5>
            <p>${item.name}</p>`;

            if (item.observation !== "") {
                text += `<p class="cart_card_observation">Obs: ${item.observation}</p>`;
            }

            text += `<p><span>R$ ${price}</span></p>
        </div>

        <div class="cart_card_buttons">
            <button class="button-less" data-product="${item.id}">-</button>
            <div id="quantity-${item.id}">${quantity}</div>
            <button class="button-plus" data-product="${item.id}">+</button>
        </div>
    </div>`;
        }
    })
    cartGenerate.innerHTML = text;
}

function generateFooter() {
    let cart_footer_price = document.querySelector("#cart_footer_price p");
    let cart_footer_finish = document.querySelector("#cart_footer_finish p");

    let totalPrice = 0;
    if (localStorage.getItem('cart') !== null) {
        let items = JSON.parse(localStorage.cart);

        items.forEach(item => {
            totalPrice += item.price * item.quantity;
        })
    }

    let text = `<p>R$ ${parsePrice(totalPrice)}</p>`;

    cart_footer_price.innerHTML = text;
    cart_footer_finish.innerHTML = text;
}


function updateCart() {
    generateCart();
    generateFooter();
}

cartGenerate.addEventListener("click", (event) => {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];

    if (
        event.target.classList.contains("button-plus") ||
        event.target.classList.contains(("button-less"))) {

        if (event.target.classList.contains("button-plus")) {
            cart.forEach(item => {
                if (item.id === event.target.dataset.product) {
                    item.quantity += 1;
                }
            })
        }

        if (event.target.classList.contains("button-less")) {
            cart.forEach(item => {
                if (item.id === event.target.dataset.product) {
                    if (item.quantity <= 1) {
                        item.quantity = 0;
                    } else {
                        item.quantity -= 1;
                    }
                }})
        }

        localStorage.setItem("cart", JSON.stringify(cart));
        updateCart();
    }
})
