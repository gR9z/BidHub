const toggleTheme = () => {
    const themeToggle = document.querySelector('#theme-toggle');

    const updateThemeAndIcon = () => {
        const htmlElement = document.querySelector('html');
        const currentTheme = htmlElement.getAttribute('data-theme');

        if (currentTheme === 'dark') {
            htmlElement.setAttribute('data-theme', 'light');
            themeToggle.className = 'ri-moon-line';
        }

        if (currentTheme === 'light') {
            htmlElement.setAttribute('data-theme', 'dark');
            themeToggle.className = 'ri-sun-line';
        }

        localStorage.setItem('theme', htmlElement.getAttribute('data-theme'));
    };

    const savedTheme = localStorage.getItem('theme') || 'light';
    document.querySelector('html').setAttribute('data-theme', savedTheme);
    themeToggle.className =
        savedTheme === 'dark' ? 'ri-sun-line' : 'ri-moon-line';

    themeToggle.addEventListener('click', updateThemeAndIcon);
};

export default toggleTheme;
