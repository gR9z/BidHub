import debounce from '../utils/debounce.js';

const adjustNavTop = () => {
    const header = document.querySelector('.header');
    const headerHeight = header.offsetHeight;
    document.documentElement.style.setProperty(
        '--header-height',
        `${headerHeight}px`
    );
};

window.addEventListener('resize', debounce(adjustNavTop, 300));

export default adjustNavTop;
