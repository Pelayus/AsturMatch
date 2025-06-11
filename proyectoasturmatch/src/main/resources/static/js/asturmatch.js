document
    .getElementById('BotonUsuario')
    .addEventListener(
        'click',
        function () {
            const dropdown = document
                .getElementById('DatosUsuarioDesplegable');
            dropdown.style.display = dropdown.style.display === 'block' ? 'none'
                : 'block';
        });

//Se cierra el menú si se hace clic fuera de él
document.addEventListener('click', function (event) {
    const button = document.getElementById('BotonUsuario');
    const dropdown = document.getElementById('DatosUsuarioDesplegable');
    if (event.target !== button && !dropdown.contains(event.target)) {
        dropdown.style.display = 'none';
    }
});

// Toggle menu de navegación para moviles
const navToggle = document.getElementById('navToggle');
const navMenu = document.getElementById('navMenu');

navToggle.addEventListener('click', () => {
    navMenu.classList.toggle('active');
});

