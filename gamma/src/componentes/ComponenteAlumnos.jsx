import React, { useState, useEffect } from "react";
import axios from "axios";
import Swal from 'sweetalert2';
import { ComponentePanelAdmin, ComponenteUpdatePassword } from "./index.js";

function ComponenteAlumnos() {
    const [alumnos, setAlumnos] = useState([]);
    const [alumnoDNI, setAlumnoDNI] = useState("");
    const [alumnoNombre, setAlumnoNombre] = useState("");
    const [alumnoApellido, setAlumnoApellido] = useState("");
    const [alumnoCelApoderado, setAlumnoCelApoderado] = useState("");
    const [alumnoGenero, setAlumnoGenero] = useState("");
    const [alumnoNivel, setAlumnoNivel] = useState("");
    const [alumnoGrado, setAlumnoGrado] = useState("");
    const [estado, setEstado] = useState("");
    const [rol, setRol] = useState("");
    const [clave, setClave] = useState("");
    const [mostrarClave, setMostrarClave] = useState(false);
    const [editar, setEditar] = useState(false);
    const [modalPasswordOpen, setModalPasswordOpen] = useState(false);
    const [selectedAlumno, setSelectedAlumno] = useState(null);
    const [error, setError] = useState('');

    const fetchListarAlumno = async () => {
        try {
            const apiURL = await fetch("http://localhost:8080/alumno/list");
            if (!apiURL.ok) {
                console.log("LA API ALUMNO NO EXISTE");
            }
            const data = await apiURL.json();
            setAlumnos(data);
        } catch (error) {
            console.log(error);
        }
    };

    const agregarAlumno = () => {
        if (!alumnoDNI || !alumnoNombre || !alumnoApellido || !alumnoCelApoderado || !alumnoGenero || !alumnoNivel || !alumnoGrado || !estado) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        axios.post("http://localhost:8080/alumno/add", {
            dni: alumnoDNI,
            nombre: alumnoNombre,
            apellido: alumnoApellido,
            celularApoderado: alumnoCelApoderado,
            genero: alumnoGenero,
            nivel: alumnoNivel,
            grado: alumnoGrado,
            estado: estado,
            rol: rol,
            clave: clave
        })
            .then(() => {
                fetchListarAlumno();
                setAlumnoDNI("");
                setAlumnoNombre("");
                setAlumnoApellido("");
                setAlumnoCelApoderado("");
                setAlumnoGenero("");
                setAlumnoNivel("");
                setAlumnoGrado("");
                setEstado("");
                setRol("");
                setClave("");
                Swal.fire({
                    title: '¡Enhorabuena!',
                    text: '¡Alumno agregado con éxito!',
                    icon: 'success',
                });
                setError('');
            }).catch((error) => {
                console.error("Error al agregar al alumno:", error);
                setError('Error al agregar al alumno. Por favor, inténtelo de nuevo.');
            });
    };

    const eliminarAlumno = (dni) => {
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
                axios.delete(`http://localhost:8080/alumno/delete/${dni}`)
                    .then(() => {
                        fetchListarAlumno();
                        Swal.fire({
                            title: "¡Enhorabuena!",
                            text: "¡Alumno eliminado con éxito!",
                            icon: "success"
                        });
                    }).catch((error) => {
                        console.error("Error al eliminar al alumno:", error);
                        Swal.fire({
                            title: "¡Error!",
                            text: "¡No se pudo eliminar al alumno!",
                            icon: "error"
                        });
                    });
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                Swal.fire({
                    title: "¡Enhorabuena!",
                    text: "¡Alumno no fue eliminado!",
                    icon: "success"
                });
            }
        });
    };

    const editarAlumno = (alumno) => {
        setEditar(true);
        setAlumnoDNI(alumno.dni);
        setAlumnoNombre(alumno.nombre);
        setAlumnoApellido(alumno.apellido);
        setAlumnoCelApoderado(alumno.celularApoderado);
        setAlumnoGenero(alumno.genero);
        setAlumnoNivel(alumno.nivel);
        setAlumnoGrado(alumno.grado);
        setRol(alumno.rol);
        setEstado(alumno.estado);
        setError('');
    }

    const actualizarAlumno = () => {
        if (!alumnoDNI || !alumnoNombre || !alumnoApellido || !alumnoCelApoderado || !alumnoGenero || !alumnoNivel || !alumnoGrado || !estado) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        axios.put("http://localhost:8080/alumno/update", {
            dni: alumnoDNI,
            nombre: alumnoNombre,
            apellido: alumnoApellido,
            celularApoderado: alumnoCelApoderado,
            genero: alumnoGenero,
            nivel: alumnoNivel,
            grado: alumnoGrado,
            estado: estado,
            user: {
                rol: rol
            }
        }).then(() => {
            setEditar(false);
            fetchListarAlumno();
            setAlumnoDNI("");
            setAlumnoNombre("");
            setAlumnoApellido("");
            setAlumnoCelApoderado("");
            setAlumnoGenero("");
            setAlumnoNivel("");
            setAlumnoGrado("");
            setEstado("");
            setRol("");
            setClave("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Alumno actualizado con éxito!',
                icon: 'success',
            });
            setError('');
        }).catch((error) => {
            console.error("Error al actualizar al alumno:", error);
            setError('Error al actualizar al alumno. Por favor, inténtelo de nuevo.');
        });
    }

    const cancelarAlumno = () => {
        setEditar(false);
        setAlumnoDNI("");
        setAlumnoNombre("");
        setAlumnoApellido("");
        setAlumnoCelApoderado("");
        setAlumnoGenero("");
        setAlumnoNivel("");
        setAlumnoGrado("");
        setEstado("");
        setRol("");
        setClave("");
        setError('');
    };

    useEffect(() => {
        fetchListarAlumno();
    }, []);

    const editarPasswordAlumno = (alumno) => {
        setSelectedAlumno(alumno);
        setModalPasswordOpen(true);
        setError('');
    };

    return (
        <>
            <ComponentePanelAdmin />
            <div className='container contenedorAlumno'>
                <h3> Lista Alumnos: </h3>
                {error && <div className="alert alert-danger">{error}</div>}
                <section className="contenedorAdd">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> DNI </th>
                                <th scope="col"> Nombre </th>
                                <th scope="col"> Apellido </th>
                                <th scope="col"> Celular_Apoderado </th>
                                <th scope="col"> Género </th>
                                <th scope="col"> Nivel </th>
                                <th scope="col"> Grado </th>                              
                                <th scope="col"> Rol </th>
                                {editar ? null : (<th scope="col"> Contraseña </th>)}
                                <th scope="col"> Estado </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td>
                                    <input type="text" value={alumnoDNI} onChange={(e) => setAlumnoDNI(e.target.value)} placeholder="DNI" />
                                </td>
                                <td>
                                    <input type="text" value={alumnoNombre} onChange={(e) => setAlumnoNombre(e.target.value)} placeholder="Nombre" />
                                </td>
                                <td>
                                    <input type="text" value={alumnoApellido} onChange={(e) => setAlumnoApellido(e.target.value)} placeholder="Apellido" />
                                </td>
                                <td>
                                    <input type="text" value={alumnoCelApoderado} onChange={(e) => setAlumnoCelApoderado(e.target.value)} placeholder="Celular" />
                                </td>
                                <td>
                                    <select value={alumnoGenero} onChange={(e) => setAlumnoGenero(e.target.value)} placeholder="Género">
                                        <option value="">Seleccionar Género</option>
                                        <option value="M">M</option>
                                        <option value="F">F</option>
                                    </select>
                                </td>
                                <td>
                                    <select value={alumnoNivel} onChange={(e) => setAlumnoNivel(e.target.value)} placeholder="Nivel">
                                        <option value="">Seleccionar Nivel</option>
                                        <option value="PRIMARIA">Primaria</option>
                                        <option value="SECUNDARIA">Secundaria</option>
                                    </select>
                                </td>
                                <td>
                                    <select value={alumnoGrado} onChange={(e) => setAlumnoGrado(e.target.value)} placeholder="Grado">
                                        <option value="">Seleccionar Grado</option>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                    </select>
                                </td>
                                <td>
                                    <select value={rol} onChange={(e) => setRol(e.target.value)} placeholder="Rol">
                                        <option value="">Seleccionar Rol</option>
                                        <option value="ALUMNO">ALUMNO</option>
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
                                <td colSpan={10}>
                                    <div role="group" aria-label="Basic mixed styles example">
                                        {
                                            editar === true ?
                                                <>
                                                    <button onClick={actualizarAlumno} className="btn btn-warning"> Actualizar </button>
                                                    <button onClick={cancelarAlumno} className="btn btn-secondary"> Cancelar </button>
                                                </> :
                                                <button onClick={agregarAlumno} className="btn btn-success"> Agregar </button>
                                        }
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </section>
                <section >
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> DNI </th>
                                <th scope="col"> Nombre </th>
                                <th scope="col"> Apellido </th>
                                <th scope="col"> Celular_Apoderado </th>
                                <th scope="col"> Género </th>
                                <th scope="col"> Nivel </th>
                                <th scope="col"> Grado </th>
                                <th scope="col"> Rol </th>
                                <th scope="col"> Estado </th>
                                <th scope="col"> Acciones </th>
                            </tr>
                        </thead>
                        <tbody>
                            {alumnos.map((alumno) => (
                                <tr className="table-primary" key={alumno.dni}>
                                    <td>{alumno.dni}</td>
                                    <td>{alumno.nombre}</td>
                                    <td>{alumno.apellido}</td>
                                    <td>{alumno.celularApoderado}</td>
                                    <td>{alumno.genero}</td>
                                    <td>{alumno.nivel}</td>
                                    <td>{alumno.grado}</td>
                                    <td>{alumno.user.rol}</td>
                                    <td style={{ color: alumno.estado === 'activo' ? 'green' : 'red' }}>
                                        {alumno.estado === 'activo' ? "🟢" : "🔴"}
                                    </td>
                                    <td>
                                        <div className="btn-group" role="group" aria-label="Basic mixed styles example">
                                            <button onClick={() => { eliminarAlumno(alumno.dni) }} type="button" className="btn btn-danger"> Eliminar </button>
                                            <button onClick={() => { editarAlumno(alumno) }} type="button" className="btn btn-success"> Editar Datos </button>
                                            <button onClick={() => { editarPasswordAlumno(alumno) }} type="button" className="btn btn-warning"> Cambiar Contraseña </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </section>
            </div>
            {modalPasswordOpen && selectedAlumno && (
                <ComponenteUpdatePassword
                    show={modalPasswordOpen}
                    onClose={() => setModalPasswordOpen(false)}
                    alumno={selectedAlumno}
                    fetchListarUsuarios={fetchListarAlumno}
                    dni={selectedAlumno ? selectedAlumno.dni : null}
                    tipoUsuario="alumno"
                />
            )}
        </>
    );
}

export default ComponenteAlumnos;