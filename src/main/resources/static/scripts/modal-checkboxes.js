const productList = document.getElementById("product_list");

const maxFlavors = {
    napoletana: 1,
    medium: 1,
    big: 2,
    giant: 2
};

let currentMaxFlavors = 0;

function setFlavorLimit(product) {
    currentMaxFlavors = maxFlavors[product] ?? 0;

    updateFlavorCheckboxes();
}

function updateFlavorCheckboxes() {
    const checkboxes = document.querySelectorAll(
        '.flavor-card input[type="checkbox"]'
    );

    const selected = document.querySelectorAll(
        '.flavor-card input[type="checkbox"]:checked'
    );

    checkboxes.forEach(checkbox => {
        checkbox.disabled =
            !checkbox.checked &&
            selected.length >= currentMaxFlavors;
    });
}

productList.addEventListener('change', event => {
    if (event.target.matches('.flavor-card input[type="checkbox"]')) {
        updateFlavorCheckboxes();
    }
});