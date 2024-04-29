const toggleNav = () => {
    const navMenu = document.querySelector('#nav');
    const navToggle = document.querySelector('#nav-toggle');

    if (navToggle) {
        navToggle.addEventListener('click', () => {
            navMenu.classList.toggle('show-menu');
        });
    }
};

export default toggleNav;
