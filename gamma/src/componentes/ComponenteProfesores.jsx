import React, { useState, useEffect } from "react";
import apiClient from "./api.js";
import Swal from 'sweetalert2';
import { ComponentePanelAdmin, ComponenteUpdatePassword } from "./index.js"

function ComponenteProfesores() {
    const [ProfesorUsuario, setProfesorUsuario] = useState([]);
    const [dniUsuario, setDniUsuario] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [celular, setCelular] = useState("");
    const [curso, setCurso] = useState("");
    const [cursos, setCursos] = useState([]);
    const [estado, setEstado] = useState("");
    const [clave, setClave] = useState("");
    const [mostrarClave, setMostrarClave] = useState(false);
    const [editar, setEditar] = useState(false);
    const [modalPasswordOpen, setModalPasswordOpen] = useState(false);
    const [selectedProfesor, setSelectedProfesor] = useState(null);
    const [error, setError] = useState('');

    const fetchListarProfesor = async () => {
        try {
            const response = await apiClient.get("/profesor/list");
            setProfesorUsuario(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    const fetchCursos = async () => {
        try {
            const res = await apiClient.get("/curso/list");
            setCursos(res.data.filter(c => c.estado === 'ACTIVO'));
        } catch (error) {
            console.error("Error cargando cursos:", error);
        }
    };

    const agregarProfesor = () => {
    if (!dniUsuario || !nombre || !apellido || !celular || !curso || !clave) {
      setError('Por favor, complete todos los campos.');
      return;
    }

    apiClient.post("/profesor/add", {
      dni: dniUsuario,
      nombre: nombre,
      apellido: apellido,
      celular: celular,
      curso: curso,
      clave: clave,
    })
    .then(() => {
      fetchListarProfesor();
      setDniUsuario("");
      setNombre("");
      setApellido("");
      setCelular("");
      setCurso("");
      setClave("");
      setEstado("");
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
                apiClient.delete(`/profesor/delete/${dni}`)
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
        setClave(Profesor.clave);
        setError('');
    }
    const actualizarProfesor = () => {
        if (!dniUsuario || !nombre || !apellido || !celular || !curso || !estado) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        apiClient.put("/profesor/update", {
            dni: dniUsuario,
            nombre: nombre,
            apellido: apellido,
            celular: celular,
            curso: curso,
            estado: estado // Enviar el codigoCurso
        }).then(() => {
            setEditar(false);
            fetchListarProfesor();
            setDniUsuario("");
            setNombre("");
            setApellido("");
            setCelular("");
            setCurso("");
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
        setEstado("");
        setClave("");
        setError('');
    }
    useEffect(() => {
        fetchListarProfesor();
        fetchCursos();
    }, []);

    const editarPasswordAdmin = (Profesor) => {
        setSelectedProfesor(Profesor);
        setModalPasswordOpen(true);
        setError('');
    };

    const isFormDirty = dniUsuario || nombre || apellido || celular || curso || clave;

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
                                <td>
                                    <select className="w-100" value={curso} onChange={(e) => setCurso(e.target.value)}>
                                        <option value="">Seleccionar Curso</option>
                                        {cursos.map(c => <option key={c.codigoCurso} value={c.codigoCurso}>{c.nombre}</option>)}
                                    </select>
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
                                            className="password-toggle-profe"
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
                                                   <button onClick={actualizarProfesor} className="btn btn-warning"> Actualizar </button>
                                                   <button onClick={cancelarProfesor} className="btn btn-secondary ms-2"> Cancelar </button>
                                               </>
                                            ) : (
                                               <>
                                                   <button onClick={agregarProfesor} className="btn btn-success"> Agregar </button>
                                                   {isFormDirty && (
                                                       <button onClick={cancelarProfesor} className="btn btn-secondary ms-2"> Cancelar </button>
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
                                <th scope="col"> Curso </th>
                                <th scope="col"> Año </th>
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
                                        <td>{Profesor.anio}</td>
                                        <td style={{ color: Profesor.estado === 'ACTIVO' ? 'green' : 'red' }}>
                                           {Profesor.estado === 'ACTIVO' ? "🟢" : "🔴"}
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
                    nombre={selectedProfesor ? selectedProfesor.nombre : null}
                    apellido={selectedProfesor ? selectedProfesor.apellido : null}
                    tipoUsuario="profesor"
                  />
                )}
        </>
    )
}

export default ComponenteProfesores;
