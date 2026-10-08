(() => {
    'use strict';

    const toggleBtn = document.querySelector('.toggle-password');
    const passwordInput = document.getElementById('password');
    const loginForm = document.getElementById('login-form');
    const submitBtn = document.getElementById('submit-btn');

    // Alternar visibilidade da senha
    if (toggleBtn && passwordInput) {
        toggleBtn.addEventListener('click', () => {
            const isVisible = toggleBtn.getAttribute('aria-pressed') === 'true';
            const nextVisible = !isVisible;

            passwordInput.type = nextVisible ? 'text' : 'password';
            toggleBtn.setAttribute('aria-pressed', String(nextVisible));
            toggleBtn.setAttribute('aria-label', nextVisible ? 'Ocultar senha' : 'Mostrar senha');
        });
    }

    // Let Spring Security process the form-urlencoded POST.
    if (loginForm) {
        loginForm.addEventListener('submit', () => {
            setLoading(true);
        });
    }

    function setLoading(isLoading) {
        if (!submitBtn) return;
        
        const btnText = submitBtn.querySelector('.btn-text');
        const spinner = submitBtn.querySelector('.btn-spinner');

        // Desabilita o botão enquanto carrega
        submitBtn.disabled = isLoading;
        
        // Atualiza o texto do botão
        if (btnText) {
            btnText.textContent = isLoading ? 'Entrando...' : 'Entrar';
        }

        // Controle direto da visibilidade do spinner
        if (spinner) {
            spinner.hidden = !isLoading; // Se isLoading for false, hidden vira true (esconde)
        }
}

})();
