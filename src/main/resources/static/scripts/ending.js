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
    return flavors.map(flavor => `
        <div class="checkout-card-flavor">
            <span>${flavor.name}</span>
            <span>R$ ${parsePrice(flavor.price)}</span>
        </div>
    `).join("");
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
    endingGenerate.innerHTML = cart.map(item => `
        <article class="checkout-card" id="${item.id}">
            <div>
                <h2>${item.type} ${sizes[item.size] ?? item.size}</h2>
            </div>

            <div class="checkout-card-flavors">
                ${generateFlavors(item.flavors)}
            </div>

            ${(item.observation ?? "").trim() !== "" ? `
                <p class="checkout-card-observation">
                    Obs: ${item.observation}
                </p>
            ` : ""}

            <footer class="checkout-card-footer">
                <span class="checkout-card-price">
                    R$ ${parsePrice(item.price * item.quantity)}
                </span>
                <span class="checkout-card-quantity">
                    x${item.quantity}
                </span>
            </footer>
        </article>
    `).join("");
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

    ending_footer.innerHTML = `<div>Total</div><div>R$ ${parsePrice(total)}</div>`;
}

generateFooter();
generateCheckout();
