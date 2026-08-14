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

        currentSize = option;


        // Generate Product
        console.log("Product: " + product);

        response = await fetch(`/api/products/${type}/${option}`);
        let products = await response.json();
        console.log(products)

        product_list.innerHTML = "";

        products.forEach(product => {
            const li = document.createElement("li");

            let price = product.price / 100;

            li.innerHTML = `
                <label class="flavor-card">
                    <img src="/images/cards/${product.imageName}" alt="">
                    <div>
                        <h5>${product.name}</h5>
                        <p>${product.description}<br><span>R$ ${price.toFixed(2).replace(".", ",")}</span></p>
                    </div>
                    <input type="checkbox" name="flavor"
                    value="${product.id}"
                    data-name="${product.name}"
                    data-price="${product.price}">
                </label>
            `;

            //console.log("Product id: " + product.id)

            product_list.appendChild(li);
        });

        modal.showModal()

        if (type === "pizzas") {
            setFlavorLimit(option);
        } else {
            setFlavorLimit(type)
        }
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
