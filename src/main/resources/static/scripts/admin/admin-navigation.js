document.addEventListener("DOMContentLoaded", () => {
    const main = document.querySelector("main");
    const productsButton = document.getElementById("products-page-button");
    const settingsButton = document.getElementById("settings-page-button");
    const productsPage = main.innerHTML;

    function setActivePage(button) {
        document.querySelector("#aside-and-content aside button.active")?.classList.remove("active");
        button.classList.add("active");
    }

    productsButton.addEventListener("click", () => {
        main.innerHTML = productsPage;
        main.querySelectorAll("[data-bound]").forEach(element => delete element.dataset.bound);
        setActivePage(productsButton);
        initializeProductPage();
    });

    settingsButton.addEventListener("click", () => {
        renderSettingsPage();
        setActivePage(settingsButton);
    });
});
