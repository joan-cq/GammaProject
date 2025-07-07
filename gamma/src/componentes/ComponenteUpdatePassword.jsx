import React, { useState } from 'react';
import Swal from 'sweetalert2';

function ComponenteUpdatePassword({ fetchListarUsuarios, dni,nombre, apellido, onClose, tipoUsuario }) {
  const [nuevaClave, setNuevaClave] = useState('');
  const [confirmarClave, setConfirmarClave] = useState('');
  const [mostrarNuevaClave, setMostrarNuevaClave] = useState(false);
  const [mostrarConfirmarClave, setMostrarConfirmarClave] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (nuevaClave !== confirmarClave) {
      setError('Las contraseñas no coinciden');
      return;
    }

    if (nuevaClave.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    try {
      let url = `http://localhost:8080/admin/updatePassword/${dni}`;
      if (tipoUsuario === 'alumno') {
        url = `http://localhost:8080/alumno/updatePassword/${dni}`;
      } else if (tipoUsuario === 'profesor') {
        url = `http://localhost:8080/profesor/updatePassword/${dni}`;
      }

      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nuevaClave }),
      });

      if (!response.ok) {
        throw new Error('Error al actualizar la contraseña');
      }

      // Después de actualizar la contraseña, puedes cerrar el modal y recargar la lista
      fetchListarUsuarios();
      Swal.fire({
        title: '¡Enhorabuena!',
        text: '¡Contraseña actualizada con éxito!',
        icon: 'success',
      });
    } catch (error) {
      console.error('Error al actualizar la contraseña:', error);
      setError('Error al actualizar la contraseña');
    }
  };

  return (
    <div className='container contenedorTabla' style={{ margin: '20px auto', maxWidth: '600px', padding: '20px', border: '1px solid #ccc', borderRadius: '5px' }}>
      <h3>Actualizar Contraseña: {nombre} {apellido}</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="nuevaClave" className="form-label">Nueva Contraseña</label>
          <div className="password-container">
            <input
              type={mostrarNuevaClave ? "text" : "password"}
              className="form-control"
              id="nuevaClave"
              value={nuevaClave}
              onChange={(e) => setNuevaClave(e.target.value)}
            />
            <span
              className="password-toggle"
              onClick={() => setMostrarNuevaClave(!mostrarNuevaClave)}
            >
              {mostrarNuevaClave ? "👁️" : "👁️‍🗨️"}
            </span>
          </div>
        </div>
        <div className="mb-3">
          <label htmlFor="confirmarClave" className="form-label">Confirmar Contraseña</label>
          <div className="password-container">
            <input
              type={mostrarConfirmarClave ? "text" : "password"}
              className="form-control"
              id="confirmarClave"
              value={confirmarClave}
              onChange={(e) => setConfirmarClave(e.target.value)}
            />
            <span
              className="password-toggle"
              onClick={() => setMostrarConfirmarClave(!mostrarConfirmarClave)}
            >
              {mostrarConfirmarClave ? "👁️" : "👁️‍🗨️"}
            </span>
          </div>
        
        </div>
        <button type="submit" className="btn btn-primary">Actualizar Contraseña</button>
        <button type="button" className="btn btn-secondary" onClick={onClose}>Cancelar</button>
      </form>
    </div>
  );
}

export default ComponenteUpdatePassword;