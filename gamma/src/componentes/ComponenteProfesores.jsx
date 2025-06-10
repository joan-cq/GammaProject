import React, { useState, useEffect } from "react";
import axios from "axios";
import Swal from 'sweetalert2';
import { ComponentePanelAdmin, ComponenteUpdatePassword } from "./index.js"

function ComponenteProfesores() {
    const [ProfesorUsuario, setProfesorUsuario] = useState([]);
    const [dniUsuario, setDniUsuario] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [celular, setCelular] = useState("");
    const [curso, setCurso] = useState("");
    const [estado, setEstado] = useState("");
    const [rol, setRol] = useState("");
    const [clave, setClave] = useState("");
    const [mostrarClave, setMostrarClave] = useState(false);
    const [editar, setEditar] = useState(false);
    const [modalPasswordOpen, setModalPasswordOpen] = useState(false);
    const [selectedProfesor, setSelectedProfesor] = useState(null);
    const [error, setError] = useState('');

    const fetchListarProfesor = async () => {
        try {
            const apiURL = await fetch("http://localhost:8080/profesor/list");
            if (!apiURL.ok) {
                console.log("LA API PROFESOR NO EXISTE");
            }
            const data = await apiURL.json();
            setProfesorUsuario(data);
        } catch (error) {
            console.log(error);
        }
    }

    const agregarProfesor = () => {
    if (!dniUsuario || !nombre || !apellido || !celular || !estado || !rol || !clave) {
      setError('Por favor, complete todos los campos.');
      return;
    }

    if (estado === '' || rol === '') {
      setError('Por favor, seleccione un estado y un rol.');
      return;
    }

    axios.post("http://localhost:8080/profesor/add", {
      dni: dniUsuario,
      nombre: nombre,
      apellido: apellido,
      celular: celular,
      curso: curso,
      rol: rol,
      clave: clave,
      estado: estado,
    })
    .then(() => {
      fetchListarProfesor();
      setDniUsuario("");
      setNombre("");
      setApellido("");
      setCelular("");
      setCurso("");
      setEstado("");
      setRol("");
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
    const eliminarProfesor = (dni) => {
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
                axios.delete(`http://localhost:8080/profesor/delete/${dni}`)
                .then(() => {
                    fetchListarProfesor();
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
                text: "¡Profesor no fue eliminado!",
                icon: "success"
            });
        }
        });
    }
    const editarProfesor = (Profesor) => {
        setEditar(true);
        setDniUsuario(Profesor.dni);
        setNombre(Profesor.nombre);
        setApellido(Profesor.apellido);
        setCelular(Profesor.celular);
        setCurso(Profesor.codigoCurso);
        setEstado(Profesor.estado);
        setRol(Profesor.rol);
        setClave(Profesor.clave);
        setError('');
    }
    const actualizarProfesor = () => {
        if (!dniUsuario || !nombre || !apellido || !celular || !curso || !estado || !rol) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        axios.put("http://localhost:8080/profesor/update", {
            dni: dniUsuario,
            nombre: nombre,
            apellido: apellido,
            celular: celular,
            curso: curso, // Enviar el codigoCurso
            estado: estado,
            rol: rol
        }).then(() => {
            setEditar(false);
            fetchListarProfesor();
            setDniUsuario("");
            setNombre("");
            setApellido("");
            setCelular("");
            setCurso("");
            setRol("");
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
    const cancelarProfesor = () => {
        setEditar(false);
        setDniUsuario("");
        setNombre("");
        setApellido("");
        setCelular("");
        setCurso("");
        setRol("");
        setEstado("");
        setClave("");
        setError('');
    }
    useEffect(() => {
        fetchListarProfesor();
    }, []);

    const editarPasswordAdmin = (Profesor) => {
        setSelectedProfesor(Profesor);
        setModalPasswordOpen(true);
        setError('');
    };

    return (
        <>
      <ComponentePanelAdmin />
      <div className='container contenedorTabla'>
        <h3> Lista Profesores: </h3>
        {error && <div className="alert alert-danger">{error}</div>}
        <section className="contenedorAdd">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> DNI </th>
                                <th scope="col"> Nombre </th>
                                <th scope="col"> Apellido </th>
                                <th scope="col"> Celular </th>
                                <th scope="col"> Curso </th>
                                <th scope="col"> Rol </th>
                                {editar ? null : (<th scope="col"> Contraseña </th>)}
                                <th scope="col"> Estado </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td>
                                    <input type="text" value={dniUsuario} onChange={(e) => setDniUsuario(e.target.value)} placeholder="DNI"/>
                                </td>
                                <td>
                                    <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} placeholder="Nombre"/>
                                </td>
                                <td>
                                    <input type="text" value={apellido} onChange={(e) => setApellido(e.target.value)} placeholder="Apellido"/>
                                </td>
                                <td>
                                    <input type="text" value={celular} onChange={(e) => setCelular(e.target.value)} placeholder="Celular"/>
                                </td>
                                <td>
                                    <select className="w-100" value={curso} onChange={(e) => setCurso(e.target.value)} placeholder="Curso">
                                        <option value="">Seleccionar Curso</option>
                                        <option value="MATEMATICA">MATEMATICA</option>
                                        <option value="COMUNICACION">COMUNICACION</option>
                                    </select>
                                </td>
                                <td>
                                    <select className="w-100" value={rol} onChange={(e) => setRol(e.target.value)} placeholder="Rol">
                                        <option value="">Seleccionar Rol</option>
                                        <option value="PROFESOR">PROFESOR</option>
                                    </select>
                                </td>
                                {editar ? null : (
                                    <td className="password-container">
                                        <>
                                            <input
                                                type={mostrarClave ? "text" : "password"}
                                                value={clave}
                                                onChange={(e) => setClave(e.target.value)}
                                                placeholder="Contraseña"
                                            />
                                            <span
                                                className="password-toggle"
                                                onClick={() => setMostrarClave(!mostrarClave)}
                                            >
                                                {mostrarClave ? "👁️" : "👁️‍🗨️"}
                                            </span>
                                        </>
                                    </td>
                                )}
                                <td>
                                    <select value={estado} onChange={(e) => setEstado(e.target.value)} placeholder="Estado">
                                        <option value="">Seleccionar Estado</option>
                                        <option value="activo">ACTIVO</option>
                                        <option value="inactivo">INACTIVO</option>
                                    </select>
                                </td>
                            </tr>
                        </tbody>
                        <tbody>
                            <tr>
                                <td colSpan={8}>
                                    <div role="group" aria-label="Basic mixed styles example">
                                        {
                                            editar === true ?
                                            <>
                                                <button onClick={actualizarProfesor} className="btn btn-warning"> Actualizar </button>
                                                <button onClick={cancelarProfesor} className="btn btn-secondary"> Cancelar </button>
                                            </> :
                                            <button onClick={agregarProfesor} className="btn btn-success"> Agregar </button>
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
                                <th scope="col"> Curso </th>
                                <th scope="col"> Rol </th>
                                <th scope="col"> Contraseña </th>
                                <th scope="col"> Estado </th>
                                <th scope="col"> Acciones </th>
                            </tr>
                        </thead>
                        <tbody>
                                {ProfesorUsuario.map((Profesor) => (
                                    <tr className="table-primary" key={Profesor.dni}>
                                        <td>{Profesor.dni}</td>
                                        <td>{Profesor.nombre}</td>
                                        <td>{Profesor.apellido}</td>
                                        <td>{Profesor.celular}</td>
                                        <td>{Profesor.codigoCurso}</td>
                                        <td>{Profesor.rol}</td>
                                        <td>{"********"}</td>
                                        <td style={{ color: Profesor.estado === 'activo' ? 'green' : 'red' }}>
                                           {Profesor.estado === 'activo' ? "🟢" : "🔴"}
                                        </td>
                                        <td>
                                            <div className="btn-group" role="group" aria-label="Basic mixed styles example">
                                                <button onClick={() => {eliminarProfesor(Profesor.dni)}} type="button" className="btn btn-danger"> Eliminar </button>
                                                    <button onClick={() => {editarProfesor(Profesor)}} type="button" className="btn btn-success"> Editar Datos </button>
                                                   <button onClick={() => {editarPasswordAdmin(Profesor)}} type="button" className="btn btn-warning"> Cambiar Contraseña </button>
                                                </div>
                                           </td>
                                       </tr>
                                    ))}
                        </tbody>
                    </table>
                </section>
            </div>
            {modalPasswordOpen && selectedProfesor && (
                <ComponenteUpdatePassword
                    show={modalPasswordOpen}
                    onClose={() => setModalPasswordOpen(false)}
                    profesor={selectedProfesor}
                    fetchListarUsuarios={fetchListarProfesor}
                    dni={selectedProfesor ? selectedProfesor.dni : null}
                    tipoUsuario="profesor"
                  />
                )}
        </>
    )
}

export default ComponenteProfesores;
