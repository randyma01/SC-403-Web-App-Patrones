// Función para hacer un preview de una imagen seleccionada
function mostrarImagen(input) {
    if (input.files && input.files[0]) {
        const imagen = input.files[0];
        const maximo = 1024 * 1024; // Se limita a 1 MB
        if (imagen.size <= maximo) {
            var lector = new FileReader();
            lector.onload = function (e) {
                $('#blah').attr('src', e.target.result).height(200);
            };
            lector.readAsDataURL(input.files[0]);
        } else {
            alert("La imagen seleccionada es muy grande... no debe superar 1 MB.");
        }
    }
}

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

// Cerrar automáticamente los toasts de alerta después de 4 segundos
setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => t.classList.remove('show'));
}, 4000);
