import React, { useState, useEffect } from "react";
import axios from "axios";
import Swal from 'sweetalert2';
import { ComponentePanelAdmin, ComponenteUpdatePassword } from "./index.js";

function ComponenteAlumnos() {
    const [alumnos, setAlumnos] = useState([]);
    const [grados, setGrados] = useState([]);
    const [dni, setDni] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [celularApoderado, setCelularApoderado] = useState("");
    const [genero, setGenero] = useState("");
    const [codigoGrado, setCodigoGrado] = useState("");
    const [estado, setEstado] = useState("");
    const [clave, setClave] = useState("");
    const [mostrarClave, setMostrarClave] = useState(false);
    const [editar, setEditar] = useState(false);
    const [modalPasswordOpen, setModalPasswordOpen] = useState(false);
    const [selectedAlumno, setSelectedAlumno] = useState(null);
    const [error, setError] = useState('');

    const fetchListarAlumnos = async () => {
        try {
            const res = await axios.get("http://localhost:8080/alumno/list");
            setAlumnos(res.data);
        } catch (error) {
            console.error("Error al obtener alumnos:", error);
        }
    };

    const fetchGrados = async () => {
        try {
            const res = await axios.get("http://localhost:8080/grado/list");
            setGrados(res.data);
        } catch (error) {
            console.error("Error cargando grados:", error);
        }
    };

    const limpiarFormulario = () => {
        setDni("");
        setNombre("");
        setApellido("");
        setCelularApoderado("");
        setGenero("");
        setCodigoGrado("");
        setEstado("");
        setClave("");
        setError('');
        setEditar(false);
    };

    const agregarAlumno = () => {
        if (!dni || !nombre || !apellido || !celularApoderado || !genero || !codigoGrado || !clave) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        axios.post("http://localhost:8080/alumno/add", {
            dni, nombre, apellido, celularApoderado, genero, codigo_grado: codigoGrado, clave
        })
        .then(() => {
            fetchListarAlumnos();
            limpiarFormulario();
            Swal.fire('¡Enhorabuena!', '¡Alumno agregado con éxito!', 'success');
        }).catch((error) => {
            console.error("Error al agregar al alumno:", error);
            setError('Error al agregar al alumno. Por favor, inténtelo de nuevo.');
        });
    };

    const eliminarAlumno = (dni) => {
        Swal.fire({
            title: '¿Estas seguro?',
            text: "¡No podrás revertir el cambio!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '¡Si, eliminar!',
            cancelButtonText: '¡No, cancelar!',
        }).then((result) => {
            if (result.isConfirmed) {
                axios.delete(`http://localhost:8080/alumno/delete/${dni}`)
                .then(() => {
                    fetchListarAlumnos();
                    Swal.fire('¡Enhorabuena!', '¡Alumno eliminado con éxito!', 'success');
                });
            }
        });
    };

    const editarAlumno = (alumno) => {
        setEditar(true);
        setDni(alumno.dni);
        setNombre(alumno.nombre);
        setApellido(alumno.apellido);
        setCelularApoderado(alumno.celularApoderado);
        setGenero(alumno.genero);
        setCodigoGrado(alumno.codigoGrado);
        setEstado(alumno.estado);
    };

    const actualizarAlumno = () => {
        if (!dni || !nombre || !apellido || !celularApoderado || !genero || !codigoGrado || !estado) {
            setError('Por favor, complete todos los campos.');
            return;
        }

        axios.put("http://localhost:8080/alumno/update", {
            dni, nombre, apellido, celularApoderado, genero, codigo_grado: codigoGrado, estado
        }).then(() => {
            fetchListarAlumnos();
            limpiarFormulario();
            Swal.fire('¡Enhorabuena!', '¡Alumno actualizado con éxito!', 'success');
        }).catch((error) => {
            console.error("Error al actualizar al alumno:", error);
            setError('Error al actualizar al alumno. Por favor, inténtelo de nuevo.');
        });
    };

    useEffect(() => {
        fetchListarAlumnos();
        fetchGrados();
    }, []);

    const editarPasswordAlumno = (alumno) => {
        setSelectedAlumno(alumno);
        setModalPasswordOpen(true);
    };

    return (
        <>
            <ComponentePanelAdmin />
            <div className='container contenedorTabla'>
                <h3> Lista Alumnos: </h3>
                {error && <div className="alert alert-danger">{error}</div>}
                <section className="contenedorAdd">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col">DNI</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Apellido</th>
                                <th scope="col">Cel. Apoderado</th>
                                <th scope="col">Género</th>
                                <th scope="col">Grado</th>
                                {editar ? <th scope="col">Estado</th> : <th scope="col">Contraseña</th>}
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td><input type="text" value={dni} onChange={(e) => setDni(e.target.value)} placeholder="DNI" disabled={editar} maxLength="8" /></td>
                                <td><input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} placeholder="Nombre" /></td>
                                <td><input type="text" value={apellido} onChange={(e) => setApellido(e.target.value)} placeholder="Apellido" /></td>
                                <td><input type="text" value={celularApoderado} onChange={(e) => setCelularApoderado(e.target.value)} placeholder="Celular" maxLength="9" /></td>
                                <td>
                                    <select value={genero} onChange={(e) => setGenero(e.target.value)}>
                                        <option value="">Seleccionar Género</option>
                                        <option value="MASCULINO">MASCULINO</option>
                                        <option value="FEMENINO">FEMENINO</option>
                                    </select>
                                </td>
                                <td>
                                    <select value={codigoGrado} onChange={(e) => setCodigoGrado(e.target.value)}>
                                        <option value="">Seleccionar Grado</option>
                                        {grados.map(g => <option key={g.codigoGrado} value={g.codigoGrado}>{g.nombreGrado}</option>)}
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
                                        <input type={mostrarClave ? "text" : "password"} value={clave} onChange={(e) => setClave(e.target.value)} placeholder="Contraseña" />
                                        <span className="password-toggle" onClick={() => setMostrarClave(!mostrarClave)}>{mostrarClave ? "👁️" : "👁️‍🗨️"}</span>
                                    </td>
                                )}
                            </tr>
                        </tbody>
                        <tbody>
                            <tr>
                                <td colSpan={7}>
                                    <div>
                                        {editar ? (
                                            <>
                                                <button onClick={actualizarAlumno} className="btn btn-warning">Actualizar</button>
                                                <button onClick={limpiarFormulario} className="btn btn-secondary ms-2">Cancelar</button>
                                            </>
                                        ) : (
                                            <button onClick={agregarAlumno} className="btn btn-success">Agregar</button>
                                        )}
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
                                <th scope="col">DNI</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Apellido</th>
                                <th scope="col">Cel. Apoderado</th>
                                <th scope="col">Género</th>
                                <th scope="col">Grado</th>
                                <th scope="col">Año</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Acciones</th>
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
                                    <td>{alumno.nombreGrado}</td>
                                    <td>{alumno.anio}</td>
                                    <td style={{ color: alumno.estado === 'ACTIVO' ? 'green' : 'red' }}>{alumno.estado === 'ACTIVO' ? "🟢" : "🔴"}</td>
                                    <td>
                                        <div className="btn-group">
                                            <button onClick={() => eliminarAlumno(alumno.dni)} className="btn btn-danger">Eliminar</button>
                                            <button onClick={() => editarAlumno(alumno)} className="btn btn-success">Editar</button>
                                            <button onClick={() => editarPasswordAlumno(alumno)} className="btn btn-warning">Contraseña</button>
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
                    fetchListarUsuarios={fetchListarAlumnos}
                    dni={selectedAlumno ? selectedAlumno.dni : null}
                    tipoUsuario="alumno"
                />
            )}
        </>
    );
}

export default ComponenteAlumnos;