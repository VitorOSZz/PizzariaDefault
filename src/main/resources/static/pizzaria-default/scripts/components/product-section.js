import {get_categories, get_product_by_category, price_cents_to_price, get_sizes} from "../core/utils.js";

function get_category_lower_price(category) {
    const products = JSON.parse(sessionStorage.getItem(category));
    const prices = JSON.parse(sessionStorage.getItem("products_prices"));

    const categoryPrices = products
        .map(product =>
            prices.find(price => price.productId === product.id)?.price
        )
        .filter(price => price !== undefined);

    return Math.min(...categoryPrices);
}

export function create_products(products) {
}

export function create_sizes(sizes) {
    const cards = document.createElement("div");
    cards.classList = "cards";
    // Make HTML

    sizes.forEach(size => {
        const lower_price = get_category_lower_price(size.categoryId);
        const category = JSON.parse(sessionStorage.getItem(size.categoryId));
        //console.log(JSON.stringify(size) + " lower_price: " + lower_price)

        console.log("create_sizes size in for loop: " + JSON.stringify(size))
        const button = document.createElement("button");

        button.classList = "card";
        button.type = "button";
        button.dataset.category = category.category.id;

        const div = document.createElement("div");
        const h4 = document.createElement("h4");
        h4.textContent = category.category.name

        const p1 = document.createElement("p");
        if (category.category.description !== null) {
            p1.textContent = category.category.description;
        }

        const p2 = document.createElement("p");
        p2.textContent = "A partir de ";
        let span = document.createElement("span")
        span.textContent = `R$ ${price_cents_to_price(lower_price)}`
        p2.append(span);

        const img = document.createElement("img")
        img.src = product.imageUrl;

        div.appendChild(h4);
        div.appendChild(p1);
        div.appendChild(p2);
        div.appendChild(img);
        button.appendChild(div);

        cards.appendChild(button);
    })

    return cards;
}

export function orderCategories() {
    // 1- Get Categories
    let categories = get_categories();
    console.log("Order Categories: " + categories);
    // 2- Order by sorter_order

    return categories.sort((a, b) => {
        return a.sort_order - b.sort_order;
    });
}

export function generate_section(category) {
    const section = document.createElement("section");
    section.id = category.name;

    const h3 = document.createElement("h3");
    h3.textContent = category.name;
    section.appendChild(h3);

    console.log("this category: " + category.name);
    //let products = get_product_by_category(category.id)

    //section.appendChild(create_products(products));

    let sizes = get_sizes().filter((size) => size.categoryId === category.id);
    console.log("Sizes filtered: " + JSON.stringify(sizes));
    section.appendChild(create_sizes(sizes));

    const sections_products = document.getElementById("sections_products");
    sections_products.appendChild(section);
}

// const categories = await orderCategories();
// generate_section(categories[0]);
