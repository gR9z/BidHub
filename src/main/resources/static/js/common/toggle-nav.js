const toggleNav = () => {
    const navMenu = document.querySelector('#nav');
    const navToggle = document.querySelector('#nav-toggle');

    if (navToggle) {
        navToggle.addEventListener('click', (event) => {
            navMenu.classList.toggle('show-menu');
            event.stopPropagation();
        });

    }

    document.addEventListener('click', (event) => {
        if (!navMenu.contains(event.target) && !navToggle.contains(event.target)) {
            navMenu.classList.remove('show-menu');
        }
    });

};

export default toggleNav;
