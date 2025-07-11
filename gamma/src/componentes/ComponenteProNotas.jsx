import React, { useEffect, useState } from "react";
import { useAuth } from '../App';
import axios from "axios";
import Swal from 'sweetalert2';
import { Modal, Button, Form } from "react-bootstrap";
import { ComponentePanelProfesores } from "./index";

function ComponenteProNotas() {
    const auth = useAuth();
    const [bimestres, setBimestres] = useState([]);
    const [grados, setGrados] = useState([]);
    const [alumnos, setAlumnos] = useState([]);
    const [bimestreSeleccionado, setBimestreSeleccionado] = useState("");
    const [gradoSeleccionado, setGradoSeleccionado] = useState("");
    const [modalShow, setModalShow] = useState(false);
    const [notaModal, setNotaModal] = useState({ dni: "", nombre: "", apellido: "", nota: "", accion: "", idNota: null });

    const [codigoCurso, setCodigoCurso] = useState(auth.usuario?.CodigoCurso || "");

    const cargarFiltrosIniciales = async () => {
        try {
            const bimestreRes = await axios.get("http://localhost:8080/bimestre/activos");
            setBimestres(bimestreRes.data);

            const gradosRes = await axios.get(`http://localhost:8080/grado_curso/filtrar/${codigoCurso}`);
            setGrados(gradosRes.data);
        } catch (error) {
            console.error("Error cargando filtros:", error);
        }
    };

    const cargarAlumnosYNotas = async () => {
        if (!bimestreSeleccionado || !gradoSeleccionado) {
            Swal.fire("Atención", "Por favor, selecciona un bimestre y un grado.", "info");
            return;
        }
        console.log("Cargando alumnos con:", { gradoSeleccionado, bimestreSeleccionado, codigoCurso });
        try {
            const alumnosRes = await axios.get(`http://localhost:8080/notas/alumnos?codigoGrado=${gradoSeleccionado}&idBimestre=${bimestreSeleccionado}&codigoCurso=${codigoCurso}`);
            console.log("Respuesta del backend:", alumnosRes.data);
            setAlumnos(alumnosRes.data);
            if (alumnosRes.data.length === 0) {
                Swal.fire("Información", "No se encontraron alumnos para los filtros seleccionados.", "info");
            }
        } catch (error) {
            console.error("Error cargando alumnos o notas:", error);
            Swal.fire("Error", "Hubo un problema al cargar los datos de los alumnos.", "error");
        }
    };

    const abrirModal = (alumno, accion, idNota = null, notaActual = "") => {
        setNotaModal({
            dni: alumno.dni,
            nombre: alumno.nombre,
            apellido: alumno.apellido,
            nota: notaActual,
            accion,
            idNota,
        });
        setModalShow(true);
    };

    const guardarNota = async () => {
        const { dni, nota, accion, idNota } = notaModal;
        if (!nota || isNaN(nota)) {
            Swal.fire("Error", "Ingresa una nota válida", "error");
            return;
        }

        try {
            if (accion === "AGREGAR") {
                await axios.post("http://localhost:8080/nota/add", {
                    dniAlumno: dni,
                    codigoCurso: codigoCurso,
                    idBimestre: bimestreSeleccionado,
                    nota: nota,
                });
                Swal.fire("¡Nota agregada!", "", "success");
            } else {
                await axios.put(`http://localhost:8080/nota/update`, {
                    idNota: idNota,
                    nota: nota,
                });
                Swal.fire("¡Nota actualizada!", "", "success");
            }
            setModalShow(false);
            await cargarAlumnosYNotas();
        } catch (error) {
            console.error("Error guardando nota:", error);
            Swal.fire("Error", "No se pudo guardar la nota", "error");
        }
    };


    useEffect(() => {
        cargarFiltrosIniciales();
    }, [codigoCurso]);


    return (
        <div>
            <ComponentePanelProfesores />
            <div className="container">
                <h3>Notas por Bimestre</h3>
                <div className="row mb-3">
                    <div className="col">
                        <label>Bimestre</label>
                        <select className="form-select" value={bimestreSeleccionado} onChange={(e) => setBimestreSeleccionado(e.target.value)}>
                            <option value="">Selecciona un bimestre</option>
                            {bimestres.map(b => (
                                <option key={b.id} value={b.id}>{b.nombre}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col">
                        <label>Grado</label>
                        <select className="form-select" value={gradoSeleccionado} onChange={(e) => setGradoSeleccionado(e.target.value)}>
                            <option value="">Selecciona un grado</option>
                            {grados.map(g => (
                                <option key={g.codigoGrado} value={g.codigoGrado}>{g.nombreGrado} {g.nivel}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col d-flex align-items-end">
                        <button className="btn btn-primary" onClick={cargarAlumnosYNotas}>Buscar</button>
                    </div>
                </div>

                {alumnos.length > 0 && (
                    <div>
                        <h5>Notas de Alumnos del {grados.find(g => g.codigoGrado === gradoSeleccionado)?.nombreGrado} {grados.find(g => g.codigoGrado === gradoSeleccionado)?.nivel} -- {bimestres.find(b => b.id === parseInt(bimestreSeleccionado))?.nombre}</h5>
                        <table className="table table-dark table-striped">
                            <thead>
                                <tr>
                                    <th>DNI</th>
                                    <th>Nombre</th>
                                    <th>Apellido</th>
                                    <th>Grado</th>
                                    <th>Nota</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                        <tbody>
                            {alumnos.map(alumno => {
                                return (
                                    <tr key={alumno.dni}>
                                        <td>{alumno.dni}</td>
                                        <td>{alumno.nombre}</td>
                                        <td>{alumno.apellido}</td>
                                        <td>{alumno.codigo_grado}</td>
                                        <td>{alumno.nota !== null ? alumno.nota : "SIN NOTA"}</td>
                                        <td>
                                            <button
                                                className={`btn ${alumno.nota !== null ? 'btn-warning' : 'btn-success'}`}
                                                onClick={() =>
                                                    abrirModal(alumno, alumno.nota !== null ? "EDITAR" : "AGREGAR", alumno.idNota, alumno.nota)
                                                }
                                            >
                                                {alumno.nota !== null ? "Editar" : "Agregar"}
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                        </table>
                    </div>
                )}

                <Modal show={modalShow} onHide={() => setModalShow(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>{notaModal.accion === "AGREGAR" ? "Agregar Nota" : "Editar Nota"}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p><strong>Alumno:</strong> {notaModal.nombre} {notaModal.apellido}</p>
                        <p><strong>DNI:</strong> {notaModal.dni}</p>
                        <Form.Group>
                            <Form.Label>Nota</Form.Label>
                            <Form.Control
                                type="number"
                                value={notaModal.nota}
                                onChange={(e) => setNotaModal({ ...notaModal, nota: e.target.value })}
                                placeholder="00.00"
                                min="0" max="20"
                            />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setModalShow(false)}>Cancelar</Button>
                        <Button variant="primary" onClick={guardarNota}>Guardar</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </div>
    );
}

export default ComponenteProNotas;
