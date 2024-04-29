'use strict';

import toggleTheme from './common/toggle-theme.js';
import toggleNav from './common/toggle-nav.js';
import adjustNavTop from "./common/adjust-nav-top.js";

(() => {
    toggleTheme();
    toggleNav();
    adjustNavTop();
})();
