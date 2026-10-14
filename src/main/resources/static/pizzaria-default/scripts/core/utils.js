export function getSlug() {
    return window.location.pathname.split('/').filter(Boolean).pop();
}

export function formatPrice(priceCents) {
    return (priceCents / 100).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}

let imageStoragePromise = null;

function normalizeStorageUrl(storageUrl) {
    return storageUrl.endsWith("/")
        ? storageUrl.slice(0, -1)
        : storageUrl;
}

function joinImageUrl(storageUrl, imageUrl = "") {
    if (!imageUrl) {
        return storageUrl;
    }

    return `${normalizeStorageUrl(storageUrl)}/${encodeURI(imageUrl)}`;
}

async function fetchImageStorage() {
    const response = await fetch(`/${getSlug()}/api/images/getImageStorage`, {
        headers: {
            Accept: "application/json, text/plain"
        }
    });

    if (!response.ok) {
        throw new Error(
            `Failed to load image storage: ${response.status} ${response.statusText}`
        );
    }

    return await response.text();
}

export async function getImageStorage(imageUrl = "") {
    if (!imageStoragePromise) {
        imageStoragePromise = fetchImageStorage();
    }

    return joinImageUrl(await imageStoragePromise, imageUrl);
}

export async function getStoredImageUrl(imageUrl) {
    if (!imageUrl) {
        return "";
    }

    if (imageUrl.startsWith("http") || imageUrl.startsWith("/")) {
        return imageUrl;
    }

    return getImageStorage(imageUrl);
}
