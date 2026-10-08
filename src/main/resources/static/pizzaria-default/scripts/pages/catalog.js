import { getCatalog } from "../core/api.js";
import { setCatalog } from "../core/catalog-store.js";
import { renderCatalog } from "../components/catalog.js";

import "../cart/cart.js";
import "../modal/modal.js";

async function initializeCatalogPage() {
    try {
        const catalog =
            await getCatalog();

        setCatalog(catalog);
        renderCatalog(catalog);
    } catch (error) {
        console.error(
            "Failed to initialize catalog page.",
            error
        );

        const container =
            document.getElementById(
                "sections_products"
            );

        if (container) {
            container.textContent =
                "Não foi possível carregar o catálogo.";
        }
    }
}

initializeCatalogPage();