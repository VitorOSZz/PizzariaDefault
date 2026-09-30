// Imports

import {getSlug} from "./utils.js";

// Functions

async function getData(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(error.message);
    }
}

export async function get_categories() {
    let data = await getData(`/${getSlug()}/api/categories`);
    console.log(data);
    return data;
}

export async function get_products_by_category(category_id) {
    return await getData(`/${getSlug()}/api/products/${category_id}`);
}

export async function get_sizes() {
    return await getData(`/${getSlug()}/api/sizes`);
}

export async function get_products() {
    return await getData(`/${getSlug()}/api/products`);
}

export async function get_products_prices() {
    return await getData(`/${getSlug()}/api/products/prices/`)
}