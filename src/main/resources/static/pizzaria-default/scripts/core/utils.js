export function getSlug() {
    return window.location.pathname.split('/').filter(Boolean).pop();
}

export function formatPrice(priceCents) {
    return (priceCents / 100).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}