
async function loadProducts() {
    const basic_url = "/api/products/"

    let products_end_points = [
        basic_url + "pizzas/medium",
        basic_url + "pizzas/big",
        basic_url + "pizzas/giant",
        basic_url + "drinks/soda"]

    sessionStorage.clear()
    let products = {}
    for (const productKey in products_end_points) {
        const endpoint = products_end_points[productKey];
        const response = await fetch(endpoint);
        const response_modal = await fetch(endpoint + "/modal")

        products[endpoint] = await response.text();
        products[endpoint + "/modal"] = await response_modal.text();
    }
    sessionStorage.setItem("products", JSON.stringify(products))
}

document.addEventListener("DOMContentLoaded", async () => {
    await loadProducts();
    console.log("products loaded.")
});
