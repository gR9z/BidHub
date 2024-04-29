const authFormIcon = document.querySelector('#auth-form__icon');

authFormIcon.addEventListener('click', () => {
    const isPassword = input.getAttribute('type') === 'password';
    input.setAttribute('type', isPassword ? 'text' : 'password');

    element.classList.toggle('ri-eye-line', !isPassword);
    element.classList.toggle('ri-eye-off-line', isPassword);
});

