import React, { useState, useEffect } from "react";
import apiClient from "./api.js";
import Swal from 'sweetalert2';
import { ComponentePanelAdmin, ComponenteUpdatePassword } from "./index.js"

function ComponenteAdministradores() {
    const [AdministradorUsuario, setAdministradorUsuario] = useState([]);
    const [dniUsuario, setDniUsuario] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [celular, setCelular] = useState("");
    const [estado, setEstado] = useState("");
    const [clave, setClave] = useState("");
    const [mostrarClave, setMostrarClave] = useState(false);
    const [editar, setEditar] = useState(false);
    const [modalPasswordOpen, setModalPasswordOpen] = useState(false);
    const [selectedAdministrador, setSelectedAdministrador] = useState(null);
    const [error, setError] = useState('');

    const fetchListarAdministrador = async () => {
    try {
        const res = await apiClient.get("/admin/list");
        if (Array.isArray(res.data)) {
            setAdministradorUsuario(res.data);
        } else {
            console.error("Respuesta inesperada del backend:", res.data);
            setAdministradorUsuario([]);
        }
    }   catch (error) {
            console.error("Error al obtener administradores:", error);
            setAdministradorUsuario([]);
        }
    }

    const agregarAdministrador = () => {
    if (!dniUsuario || !nombre || !apellido || !celular || !clave) {
      setError('Por favor, complete todos los campos.');
      return;
    }

    apiClient.post("/admin/add", {
      dni: dniUsuario,
      nombre: nombre,
      apellido: apellido,
      celular: celular,
      clave: clave,
    })
    .then(() => {
      fetchListarAdministrador();
      setDniUsuario("");
      setNombre("");
      setApellido("");
      setCelular("");
      setEstado("");
      setClave("");
      Swal.fire({
        title: '¡Enhorabuena!',
        text: '¡Usuario agregado con éxito!',
        icon: 'success',
      });
      setError(''); // Limpiar el error después de agregar con éxito
    }).catch((error) => {
      console.error("Error al agregar al usuario:", error);
      setError('Error al agregar al usuario. Por favor, inténtelo de nuevo.');
    });
  };
    const eliminarAdministrador = (dni) => {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: "btn btn-success",
                cancelButton: "btn btn-danger"
            },
            buttonsStyling: false
        });
        swalWithBootstrapButtons.fire({
            title: "¿Estas seguro?",
            text: "¡No podrás revertir el cambio!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "¡Si, eliminar!",
            cancelButtonText: "¡No, cancelar!",
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                apiClient.delete(`/admin/delete/${dni}`)
                .then(() => {
                    fetchListarAdministrador();
                });
                swalWithBootstrapButtons.fire({
                title: "¡Enhorabuena!",
                text: "¡Usuario eliminado con éxito!",
                icon: "success"
        }); } else if (
            result.dismiss === Swal.DismissReason.cancel
            ) {
            swalWithBootstrapButtons.fire({
                title: "¡Enhorabuena!",
                text: "¡Administrador no fue eliminado!",
                icon: "success"
            });
        }
        });
    }
    const editarAdministrador = (Administrador) => {
        setEditar(true);
        setDniUsuario(Administrador.dni);
        setNombre(Administrador.nombre);
        setApellido(Administrador.apellido);
        setCelular(Administrador.celular);
        setEstado(Administrador.estado);
        setClave(Administrador.clave);
        setError('');
    }
    const actualizarAdministrador = () => {
        if (!dniUsuario || !nombre || !apellido || !celular || !estado) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        apiClient.put("/admin/update", {
            dni: dniUsuario,
            nombre: nombre,
            apellido: apellido,
            celular: celular,
            estado: estado
        }).then(() => {
            setEditar(false);
            fetchListarAdministrador();
            setDniUsuario("");
            setNombre("");
            setApellido("");
            setCelular("");
            setEstado("");
            setClave("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Usuario actualizado con éxito!',
                icon: 'success',
            });
            setError('');
        }).catch((error) => {
            console.error("Error al actualizar al usuario:", error);
            setError('Error al actualizar al usuario. Por favor, inténtelo de nuevo.');
        });
    }
    const cancelarAdministrador = () => {
        setEditar(false);
        setDniUsuario("");
        setNombre("");
        setApellido("");
        setCelular("");
        setEstado("");
        setClave("");
        setError('');
    }

    useEffect(() => {
        fetchListarAdministrador();
    }, [])

    const editarPasswordAdmin = (Administrador) => {
        setSelectedAdministrador(Administrador);
        setModalPasswordOpen(true);
        setError('');
    };

    const isFormDirty = dniUsuario || nombre || apellido || celular || clave;

    return (
        <>
      <ComponentePanelAdmin />
      <div className='container contenedorTabla'>
        <h3> Lista Administradores: </h3>
        {error && <div className="alert alert-danger">{error}</div>}
        <section className="contenedorAdd">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> DNI </th>
                                <th scope="col"> Nombre </th>
                                <th scope="col"> Apellido </th>
                                <th scope="col"> Celular </th>
                                {editar ? <th scope="col">Estado</th> : <th scope="col">Contraseña</th>}
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td>
                                    <input type="text" value={dniUsuario} onChange={(e) => setDniUsuario(e.target.value.replace(/[^0-9]/g, ''))} placeholder="DNI" disabled={editar} maxLength="8" pattern="\d{8}" title="El DNI debe contener 8 dígitos numéricos."/>
                                </td>
                                <td>
                                    <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value.replace(/[^a-zA-Z\s]/g, ''))} placeholder="Nombre" pattern="[A-Za-z\s]+" title="El nombre solo debe contener letras."/>
                                </td>
                                <td>
                                    <input type="text" value={apellido} onChange={(e) => setApellido(e.target.value.replace(/[^a-zA-Z\s]/g, ''))} placeholder="Apellido" pattern="[A-Za-z\s]+" title="El apellido solo debe contener letras."/>
                                </td>
                                <td>
                                    <input type="text" value={celular} onChange={(e) => setCelular(e.target.value.replace(/[^0-9]/g, ''))} placeholder="Celular" maxLength="9" pattern="\d{9}" title="El celular debe contener 9 dígitos numéricos."/>
                                </td>
                                {editar ? (
                                    <td>
                                        <select value={estado} onChange={(e) => setEstado(e.target.value)}>
                                            <option value="">Seleccionar Estado</option>
                                            <option value="ACTIVO">ACTIVO</option>
                                            <option value="INACTIVO">INACTIVO</option>
                                        </select>
                                    </td>
                                ) : (
                                    <td className="password-container">
                                        <input
                                            type={mostrarClave ? "text" : "password"}
                                            value={clave}
                                            onChange={(e) => setClave(e.target.value)}
                                            placeholder="Contraseña"
                                        />
                                        <span
                                            className="password-toggle-admin"
                                            onClick={() => setMostrarClave(!mostrarClave)}
                                        >
                                            {mostrarClave ? "👁️" : "👁️‍🗨️"}
                                        </span>
                                    </td>
                                )}
                            </tr>
                        </tbody>
                        <tbody>
                            <tr>
                                <td colSpan={6}>
                                    <div role="group" aria-label="Basic mixed styles example">
                                        {
                                            editar ? (
                                               <>
                                                   <button onClick={actualizarAdministrador} className="btn btn-warning"> Actualizar </button>
                                                   <button onClick={cancelarAdministrador} className="btn btn-secondary ms-2"> Cancelar </button>
                                               </>
                                            ) : (
                                               <>
                                                   <button onClick={agregarAdministrador} className="btn btn-success"> Agregar </button>
                                                   {isFormDirty && (
                                                       <button onClick={cancelarAdministrador} className="btn btn-secondary ms-2"> Cancelar </button>
                                                   )}
                                               </>
                                            )
                                        }
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </section>
                <section>
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> DNI </th>
                                <th scope="col"> Nombre </th>
                                <th scope="col"> Apellido </th>
                                <th scope="col"> Celular </th>
                                <th scope="col"> Año </th>
                                <th scope="col"> Estado </th>
                                <th scope="col"> Acciones </th>
                            </tr>
                        </thead>
                        <tbody>
                                {Array.isArray(AdministradorUsuario) ? (
                                    AdministradorUsuario.map((Administrador) => (
                                    <tr className="table-primary" key={Administrador.dni}>
                                        <td>{Administrador.dni}</td>
                                        <td>{Administrador.nombre}</td>
                                        <td>{Administrador.apellido}</td>
                                        <td>{Administrador.celular}</td>
                                        <td>{Administrador.anio}</td>
                                        <td style={{ color: Administrador.estado === 'ACTIVO' ? 'green' : 'red' }}>
                                           {Administrador.estado === 'ACTIVO' ? "🟢" : "🔴"}
                                        </td>
                                        <td>
                                            <div className="btn-group" role="group" aria-label="Basic mixed styles example">
                                                <button onClick={() => {eliminarAdministrador(Administrador.dni)}} type="button" className="btn btn-danger"> Eliminar </button>
                                                    <button onClick={() => {editarAdministrador(Administrador)}} type="button" className="btn btn-success"> Editar Datos </button>
                                                   <button onClick={() => {editarPasswordAdmin(Administrador)}} type="button" className="btn btn-warning"> Cambiar Contraseña </button>
                                                </div>
                                           </td>
                                       </tr>
                                    ))
                                ) : (
                                    <tr>
                                    <td colSpan={7}>No se pudo cargar la lista de administradores.</td>
                                    </tr>
                                )}
                        </tbody>
                    </table>
                </section>
            </div>
            {modalPasswordOpen && selectedAdministrador && (
                <ComponenteUpdatePassword
                    show={modalPasswordOpen}
                    onClose={() => setModalPasswordOpen(false)}
                    administrador={selectedAdministrador}
                    fetchListarUsuarios={fetchListarAdministrador}
                    dni={selectedAdministrador ? selectedAdministrador.dni : null}
                    nombre={selectedAdministrador ? selectedAdministrador.nombre : null}
                    apellido={selectedAdministrador ? selectedAdministrador.apellido : null}
                    tipoUsuario="admin"
                  />
                )}
        </>
    )
}

export default ComponenteAdministradores;
