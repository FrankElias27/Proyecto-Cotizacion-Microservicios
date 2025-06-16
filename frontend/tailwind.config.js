/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",              // Para Angular
    "./node_modules/flowbite/**/*.js"    // Para Flowbite
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')           // Plugin de Flowbite
  ],
}

