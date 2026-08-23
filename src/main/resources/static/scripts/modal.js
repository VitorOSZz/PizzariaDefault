const buttons = document.querySelectorAll('.card');
const button_exit = document.getElementById("button_exit");
const modal_product = document.getElementById("modal_product");

const modal = document.getElementById("modal");
const product_list = document.getElementById("product_list");

buttons.forEach(button => {
    button.addEventListener('click', async (event) => {
        console.log("Button clicked");

        document.getElementById("modal_buy").disabled = false;

        const product = button.dataset.product;
        // Generate modal_product
        modal_product.innerHTML = "";
        const [type, option] = product.split("/");
        let response = await fetch(`/api/products/${type}/${option}/modal`);
        modal_product.innerHTML = await response.text();

        currentType = type;
        currentOption = option;

        // Generate Product
        console.log("Product: " + product);

        response = await fetch(`/api/products/${type}/${option}`);
        let products = await response.json();
        console.log(products)

        product_list.innerHTML = "";

        products.forEach(product => {
            let li = document.createElement("li");
            let price = product.price / 100;

            let text =
                `
                <label class="flavor-card">
        <div class="image-container">
            <img 
                src="/images/cards/${product.imageName}" 
                class="${product.imageFit.toLowerCase()}" 
                alt=""
            >
        </div>

        <div class="product-info">
            <h5>${product.name}</h5>
                `

            if (product.description != null) {
                text += `
<p>${product.description}</p>
<p><span>R$ ${price.toFixed(2).replace(".", ",")}</span></p>`
            } else {
                text += `
<p></p>
<p><span>R$ ${price.toFixed(2).replace(".", ",")}</span></p>`
            }

            text +=
                `
</div>

        <input 
            type="checkbox" 
            name="flavor"
            value="${product.id}"
            data-name="${product.name}"
            data-price="${product.price}"
            data-type="${type}"
        >
    </label>`;
            li.innerHTML = text;
            product_list.appendChild(li);
        });
        switch (type) {
            case "pizzas":
                setFlavorLimit(option);
                break;
            case "drinks":
                setFlavorLimit(option)
                break;
            default:
                window.alert("Desculpe algo deu errado. Favor contatar a Pizzaria.")
        }

        form.reset();
        closeCart()
        modal.showModal()
    });
});

// Close modal

button_exit.addEventListener('click', (event) => {
    console.log("Closing modal");
    modal.close();
})

modal.addEventListener("click", (event) => {
    if (event.target === modal) {
        modal.close();
    }
});
