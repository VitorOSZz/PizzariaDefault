// Imports

import {getSlug} from "./utils.js";

// Functions

const CATALOG_ENDPOINT = `/${getSlug()}/api/catalog`;

export async function getCatalog() {
    const response = await fetch(CATALOG_ENDPOINT, {
        headers: {
            Accept: "application/json"
        }
    });

    if (!response.ok) {
        throw new Error(
            `Failed to load catalog: ${response.status} ${response.statusText}`
        );
    }

    const catalog = await response.json();

    if (!Array.isArray(catalog)) {
        throw new Error("Catalog response must be an array.");
    }

    return normalizeCatalog(catalog);
}

function normalizeCatalog(catalog) {
    return catalog.map(category => ({
        id: category.id,
        name: category.name,
        description: category.description,
        sortOrder: category.sort_order ?? 0,

        sizes: Array.isArray(category.sizes)
            ? category.sizes.map(size => ({
                id: size.id,
                name: size.name,
                description: size.description,
                imageUrl: size.imageUrl,
                imageFit: size.imageFit,
                sortOrder: size.sort_order ?? 0,

                products: Array.isArray(size.products)
                    ? size.products.map(product => ({
                        id: product.id,
                        name: product.name,
                        description: product.description,
                        price: product.price,
                        imageUrl: product.imageUrl,
                        imageFit: product.imageFit,
                        sortOrder: product.sortOrder ?? 0
                    }))
                    : []
            }))
            : [],

        products: Array.isArray(category.products)
            ? category.products
            : []
    }));
}