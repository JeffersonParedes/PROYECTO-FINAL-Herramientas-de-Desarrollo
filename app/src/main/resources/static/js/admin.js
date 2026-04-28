/* 
Lógica del Panel Admin
*/


document.addEventListener('DOMContentLoaded', function () {
    // Confirmar antes de eliminar contenido reportado
    document.querySelectorAll('.btn-outline-danger').forEach(function (btn) {
        btn.addEventListener('click', function () {
            if (!confirm('¿Estás seguro de que quieres eliminar este contenido?')) {
                event.preventDefault();
            }
        });
    });
});
