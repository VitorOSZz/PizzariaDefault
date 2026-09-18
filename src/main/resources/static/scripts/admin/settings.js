const SETTINGS_KEY = "admin-settings";
const defaultSettings = {
    phone: "(41) 12345...",
    sizes: [
        { id: 1, name: "Napoletana", enabled: true },
        { id: 2, name: "Media", enabled: true },
        { id: 3, name: "Grande", enabled: true },
        { id: 4, name: "Família", enabled: true }
    ]
};

function getSettings() {
    try {
        const savedSettings = JSON.parse(localStorage.getItem(SETTINGS_KEY)) || {};
        const sizes = savedSettings.sizes || defaultSettings.sizes;
        return {
            ...defaultSettings,
            ...savedSettings,
            sizes: sizes.map(size => ({ ...size, enabled: size.enabled !== false }))
        };
    } catch (error) {
        return { ...defaultSettings, sizes: [...defaultSettings.sizes] };
    }
}

function saveSettings(settings) {
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
}

function settingsRowTemplate(size) {
    return `<div class="settings-size-row" data-size-id="${size.id}">
        <div class="settings-size-id" title="Código interno">${size.id}</div>
        <input type="text" name="size-name" value="${escapeHtml(size.name)}" placeholder="Nome do tamanho" required>
    </div>`;
}

function formatPhone(value) {
    const text = String(value || "").trim();
    if (!text || text.includes("...")) return text;
    const digits = text.replace(/\D/g, "");
    if (digits.length === 11) return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
    if (digits.length === 10) return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`;
    return text;
}

function renderSettingsPage() {
    const settings = getSettings();
    const phone = formatPhone(settings.phone || defaultSettings.phone);
    document.querySelector("main").innerHTML = `<section id="settings-page">
        <div class="settings-header">
            <div>
                <h2>Configuracoes</h2>
                <p>Atualize o telefone e escolha os tamanhos que sua pizzaria oferece.</p>
            </div>
            <button type="submit" form="settings-form" class="settings-save">Salvar configuracoes</button>
        </div>
        <form id="settings-form">
            <div class="settings-field">
                <label for="pizzeria-phone">Telefone da pizzaria</label>
                <input type="tel" id="pizzeria-phone" name="phone" value="${escapeHtml(phone)}" placeholder="(00) 00000-0000">
            </div>
            <fieldset class="settings-fieldset">
                <legend>Tamanhos</legend>
                <p class="settings-help">Escolha os nomes dos tamanhos usados no cardápio.</p>
                <div id="settings-sizes">${settings.sizes.map(settingsRowTemplate).join("")}</div>
                <button type="button" id="add-size" class="settings-add">Adicionar tamanho</button>
            </fieldset>
        </form>
    </section>`;

    document.getElementById("settings-form").addEventListener("submit", event => {
        event.preventDefault();
        const sizes = [...document.querySelectorAll(".settings-size-row")].map(row => ({
            id: Number(row.dataset.sizeId),
            name: row.querySelector("[name='size-name']").value.trim()
        })).filter(size => size.name);
        saveSettings({
            phone: document.getElementById("pizzeria-phone").value.trim(),
            sizes
        });
        const button = document.querySelector(".settings-save");
        button.textContent = "Configuracoes salvas";
        setTimeout(() => button.textContent = "Salvar configuracoes", 1600);
    });

    document.getElementById("pizzeria-phone").addEventListener("blur", event => {
        event.target.value = formatPhone(event.target.value);
    });

    document.getElementById("add-size").addEventListener("click", () => {
        const rows = [...document.querySelectorAll(".settings-size-row")];
        const nextId = rows.reduce((highestId, row) => Math.max(highestId, Number(row.dataset.sizeId)), 0) + 1;
        document.getElementById("settings-sizes").insertAdjacentHTML("beforeend", settingsRowTemplate({ id: nextId, name: "", enabled: true }));
        document.querySelector(`#settings-sizes [data-size-id="${nextId}"] [name="size-name"]`).focus();
    });
}

function escapeHtml(value) {
    return String(value || "").replace(/[&<>'"]/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        "'": "&#039;",
        '"': "&quot;"
    }[character]));
}
