let catalog = [];

export function setCatalog(newCatalog) {
    if (!Array.isArray(newCatalog)) {
        throw new Error("Catalog must be an array.");
    }

    catalog = newCatalog;
}

export function getCatalog() {
    return catalog;
}

export function findSize(sizeId) {
    for (const category of catalog) {
        const size = category.sizes?.find(
            currentSize => currentSize.id === sizeId
        );

        if (size) {
            return {
                category,
                size
            };
        }
    }

    return null;
}