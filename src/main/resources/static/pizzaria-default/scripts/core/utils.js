export function getSlug() {
    return window.location.pathname.split('/').filter(Boolean).pop();
}

export function price_cents_to_price(price) {
    return price / 100.0;
}

function get_session_storage(key) {
    return JSON.parse(sessionStorage.getItem(key));
}

export function get_categories() {
    return get_session_storage("categories")
}

export function get_product_by_category(category_id) {
    return get_session_storage(category_id)
}

export function get_products_prices() {
    return get_session_storage("product_prices");
}

export function get_sizes() {
    return get_session_storage("sizes")
}