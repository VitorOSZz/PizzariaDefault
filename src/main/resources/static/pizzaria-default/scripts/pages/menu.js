import {
    get_categories,
    get_products_by_category,
    get_products_prices,
    get_sizes
} from "../core/api.js";

import {
    generate_section,
    orderCategories
} from "../components/product-section.js";

addEventListener("DOMContentLoaded", async () => {
    console.log("Generating..")
    // Generate
    localStorage.clear();
    sessionStorage.clear();
    const categories = await get_categories();

    const categoryIds = categories.map(category => category.id);
    sessionStorage.setItem("categoriesIds", JSON.stringify(categoryIds));
    sessionStorage.setItem("categories", JSON.stringify(categories));
    //console.log("categories: " + sessionStorage.getItem("categories"));

    for (const category of categories) {
        console.log("Products of " + category.name);
        const products = await get_products_by_category(category.id);
        sessionStorage.setItem(category.id, JSON.stringify(products));
        //console.log(sessionStorage.getItem(category.id));
    }

    const sizes = await get_sizes();
    sessionStorage.setItem("sizes", JSON.stringify(sizes))
    console.log("Sizes: " + sessionStorage.getItem("sizes"))

    const product_prices = await get_products_prices();
    sessionStorage.setItem("products_prices", JSON.stringify(product_prices))
    console.log("Prices: " + sessionStorage.getItem("products_prices"))

    // Code

    for ( const category of orderCategories()) {
        console.log(category)
        generate_section(category)
    }

})
