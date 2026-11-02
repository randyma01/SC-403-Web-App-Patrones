// Función para transferir datos al modal de confirmación de eliminación
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');
    if (confirmModal) {
        confirmModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-bs-id');
            const descripcion = button.getAttribute('data-bs-descripcion');
            
            const modalIdInput = document.getElementById('modalId');
            const modalDescSpan = document.getElementById('modalDescripcion');
            
            if (modalIdInput) {
                modalIdInput.value = id;
            }
            if (modalDescSpan) {
                modalDescSpan.textContent = descripcion;
            }
        });
    }
});
