const endingGenerate = document.getElementById("ending-generate");
const endingButton = document.getElementById("ending-button");

const sizes = {
    napoletana: "Napoletana",
    medium: "Media",
    big: "Grande",
    giant: "Familia"
};

function parsePrice(itemPrice) {
    if (itemPrice <= 0) {
        return "0,00";
    }

    return (itemPrice / 100).toFixed(2).replace(".", ",");
}

function getCartItems() {
    return JSON.parse(localStorage.getItem("cart")) || [];
}

function generateFlavors(flavors) {
    if (flavors.length > 1) {
        return flavors.map(flavor => `
        <div class="checkout-card-flavor">
            <span>${flavor.name}</span>
        </div>
    `).join("");
        // <span>R$ ${parsePrice(flavor.price)}</span>
    } else if (flavors.length === 1) {
        return `
            <div class="checkout-card-flavor">
                <span>${flavors[0].name}</span>
            </div>`;
    } else {
        return `Algo deu errado, porfavor comunique a Pizzaria.`
    }
}

function generateCheckout() {
    const cart = getCartItems().filter(item => item.quantity > 0);

    if (cart.length === 0) {
        endingButton.style.display = "none";
        endingGenerate.innerHTML = `
            <div class="checkout-empty">
                <h2>Seu carrinho esta vazio</h2>
                <a href="/">Voltar para o cardapio</a>
            </div>
        `;
        return;
    }

    endingButton.style.display = "block";
    endingGenerate.innerHTML = cart.map(item => {

        let title;
        let content;

        if (item.type === "pizzas") {
            title = `Pizza ${sizes[item.size] ?? item.size}`;

            content = `
                <div class="checkout-card-flavors">
                    ${generateFlavors(item.flavors)}
                </div>
            `;
        } else if (item.type === "drinks") {
            title = "Refrigerante";

            content = `
                <div class="checkout-card-flavors">
                    <p>${item.name}</p>
                </div>
            `;
        }

        return `
            <article class="checkout-card" id="${item.id}">
                <div>
                    <h2>${title}</h2>
                </div>

                ${content}

                ${(item.observation ?? "").trim() !== "" ? `
                    <p class="checkout-card-observation">
                        Obs: ${item.observation}
                    </p>
                ` : ""}

                <div class="checkout-card-footer">
                    <span class="checkout-card-price">
                        R$ ${parsePrice(item.price * item.quantity)}
                    </span>

                    <span class="checkout-card-quantity">
                        x${item.quantity}
                    </span>
                </div>
            </article>
        `;
    }).join("");
}

function generateFooter() {
    let ending_footer = document.getElementById("ending-footer");

    let cart = getCartItems().filter(item => item.quantity > 0);

    if (cart.length === 0) {
        ending_footer.display = "none";
        return;
    }

    let total = 0
    cart.forEach(item => {
        total += item.price * item.quantity
    });

    ending_footer.innerHTML = `<div>Total</div><div><span>R$ ${parsePrice(total)}</span></div>`;
}

generateFooter();
generateCheckout();
