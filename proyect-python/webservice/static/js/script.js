// JavaScript para cambiar las imágenes al pasar el ratón
document.addEventListener('DOMContentLoaded', () => {

  const addHoverEffect = (iconId, normalImg, hoverImg) => {
    const iconElement = document.getElementById(iconId);
    const imgElement = iconElement.querySelector('img');

    iconElement.addEventListener('mouseover', () => {
      imgElement.src = hoverImg;
    });

    iconElement.addEventListener('mouseout', () => {
      imgElement.src = normalImg;
    });
  };

  addHoverEffect('facebook-icon', '/static/img/f1.png', '/static/img/facebookico.png');
  addHoverEffect('twitter-icon', '/static/img/t1.png', '/static/img/twitterico.png');
  addHoverEffect('google-icon', '/static/img/g1.png', '/static/img/googleico.png');
});



// tarjeta oculta 

const cardContainer = document.querySelector('.card-container');
const loginCard = document.querySelector('.card');
const registrationCard = document.querySelector('.registration-card');
const registerLink = document.querySelector('.text-white-50.fw-bold');

registerLink.addEventListener('click', (e) => {
  e.preventDefault();
  // Mueve la tarjeta de inicio de sesion a la izquierda
  loginCard.style.transform = 'translateX(-50%)';
  // Muestra la tarjeta de registro desplazandola hacia la vista
  setTimeout(() => {
    registrationCard.style.display = 'block';
    registrationCard.style.opacity = '1';
    registrationCard.style.transform = 'translateX(0%)';
  }, 900);
});