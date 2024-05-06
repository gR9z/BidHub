const displayDeleteConfirmation = document.getElementById('delete-btn-disp');
const deleteConfirm = document.getElementById('delete-confirmation');

displayDeleteConfirmation.addEventListener('click', (e) => {
    deleteConfirm.style.display = 'flex';

})