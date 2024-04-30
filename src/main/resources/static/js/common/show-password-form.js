const authFormIcon = document.querySelector('#auth-form__icon');
const input = document.querySelector('#password-register')

authFormIcon.addEventListener('click', () => {
    const isPassword = input.getAttribute('type') === 'password';
    input.setAttribute('type', isPassword ? 'text' : 'password');

    authFormIcon.classList.toggle('ri-eye-line', !isPassword);
    authFormIcon.classList.toggle('ri-eye-off-line', isPassword);
});

