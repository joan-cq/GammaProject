import React, { useEffect, useState } from "react";
import axios from "axios";
import Swal from 'sweetalert2';
import { Modal, Button, Form } from "react-bootstrap";
import { ComponentePanelProfesores } from "./index";

function ComponenteProNotas() {
    const [bimestres, setBimestres] = useState([]);
    const [grados, setGrados] = useState([]);
    const [alumnos, setAlumnos] = useState([]);
    const [notas, setNotas] = useState([]);
    const [bimestreSeleccionado, setBimestreSeleccionado] = useState("");
    const [gradoSeleccionado, setGradoSeleccionado] = useState("");
    const [modalShow, setModalShow] = useState(false);
    const [notaModal, setNotaModal] = useState({ dni: "", nombre: "", apellido: "", nota: "", accion: "", idNota: null });
    const profesorDNI = localStorage.getItem("dni"); // Asegúrate de guardar el dni al hacer login

    const cargarFiltrosIniciales = async () => {
        try {
            const bimestreRes = await axios.get("http://localhost:8080/bimestre/activos");
            setBimestres(bimestreRes.data);

            const codigoCurso = localStorage.getItem("codigoCurso");
            const gradosRes = await axios.get(`http://localhost:8080/grado_curso/filtrar/${codigoCurso}`);
            setGrados(gradosRes.data);
        } catch (error) {
            console.error("Error cargando filtros:", error);
        }
    };

    const cargarAlumnosYNotas = async () => {
        if (!bimestreSeleccionado || !gradoSeleccionado) return;
        try {
            const alumnosRes = await axios.get(`http://localhost:8080/alumno/grado/${gradoSeleccionado}`);
            setAlumnos(alumnosRes.data);

            const notasRes = await axios.get(`http://localhost:8080/nota?grado=${gradoSeleccionado}&bimestre=${bimestreSeleccionado}&dniProfesor=${profesorDNI}`);
            setNotas(notasRes.data);
        } catch (error) {
            console.error("Error cargando alumnos o notas:", error);
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
                    codigoCurso: gradoSeleccionado.split("-")[1], // Asegúrate de enviar correctamente el curso
                    idBimestre: bimestreSeleccionado,
                    nota,
                });
                Swal.fire("¡Nota agregada!", "", "success");
            } else {
                await axios.put(`http://localhost:8080/nota/update`, {
                    nota,
                });
                Swal.fire("¡Nota actualizada!", "", "success");
            }
            setModalShow(false);
            cargarAlumnosYNotas();
        } catch (error) {
            console.error("Error guardando nota:", error);
            Swal.fire("Error", "No se pudo guardar la nota", "error");
        }
    };

    const obtenerNotaAlumno = (dniAlumno) => {
        const nota = notas.find(n => n.alumno.dni === dniAlumno);
        return nota || null;
    };

    useEffect(() => {
        localStorage.setItem("codigoCurso", "COM");
        cargarFiltrosIniciales();
    }, []);

    useEffect(() => {
        cargarAlumnosYNotas();
    }, [bimestreSeleccionado, gradoSeleccionado]);

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
                                <option key={b.idBimestre} value={b.idBimestre}>{b.nombre}</option>
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
                </div>

                {alumnos.length > 0 && (
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
                                const notaObj = obtenerNotaAlumno(alumno.dni);
                                return (
                                    <tr key={alumno.dni}>
                                        <td>{alumno.dni}</td>
                                        <td>{alumno.nombre}</td>
                                        <td>{alumno.apellido}</td>
                                        <td>{alumno.codigoGrado}</td>
                                        <td>{notaObj ? notaObj.nota : "SIN NOTA"}</td>
                                        <td>
                                            <button
                                                className={`btn ${notaObj ? 'btn-warning' : 'btn-success'}`}
                                                onClick={() =>
                                                    abrirModal(alumno, notaObj ? "EDITAR" : "AGREGAR", notaObj?.idNota, notaObj?.nota)
                                                }
                                            >
                                                {notaObj ? "Editar" : "Agregar"}
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
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
