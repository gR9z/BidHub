const displayElement = document.getElementById("delete-btn");
const deleteConfirm = document.getElementById("delete-confirmation");
const formProfile = document.getElementById("form-edit-profile");


deleteConfirm.style.display = "none";
console.log("oui");
displayElement.addEventListener('click', (e) => {
    deleteConfirm.style.display = "block";
    formProfile.style.display = "none";



})