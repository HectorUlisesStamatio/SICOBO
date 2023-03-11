/*=============== PROFILE OPTIONS ===============*/
const showSocial = (toggleCard, socialCard) => {
    try {
        const toggle = document.getElementById(toggleCard),
            social = document.getElementById(socialCard)

        toggle.addEventListener('click', () => {
            if (social.classList.contains('animation')) {
                social.classList.add('down-animation')

                setTimeout(() => {
                    social.classList.remove('down-animation')
                }, 1000)
            }

            social.classList.toggle('animation')
        })
    }catch(e){}
    
}
showSocial('card-toggle', 'card-social')

/* PROFILE MODAL */
function profile() {
    let modal = document.getElementById("profile__container");
    if (modal.classList.contains('show-modal')) {
        modal.classList.remove('show-modal');
    } else {
        modal.classList.add('show-modal');
    }

}