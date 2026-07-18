import React, { useEffect, useState } from "react";
import { useAuth } from '../App';
import apiClient from "./api.js";
import Swal from 'sweetalert2';
import { ComponentePanelProfesores } from "./index";

const ESTADOS = ["PRESENTE", "AUSENTE", "TARDANZA", "JUSTIFICADO"];

function ComponenteProAsistencia() {
    const auth = useAuth();
    const [grados, setGrados] = useState([]);
    const [alumnos, setAlumnos] = useState([]);
    const [gradoSeleccionado, setGradoSeleccionado] = useState("");
    const [fecha, setFecha] = useState(() => new Date().toISOString().slice(0, 10));
    const [guardando, setGuardando] = useState("");

    useEffect(() => {
        const cargarGrados = async () => {
            const curso = auth.usuario?.codigoCurso;
            if (!curso) return;
            try {
                const gradosRes = await apiClient.get(`/grado_curso/filtrar/${curso}`);
                setGrados(gradosRes.data);
            } catch (error) {
                console.error("Error cargando grados:", error);
                Swal.fire("Error", "No se pudieron cargar los grados del curso.", "error");
            }
        };
        cargarGrados();
    }, [auth.usuario]);

    const cargarAlumnos = async () => {
        if (!gradoSeleccionado || !fecha) {
            Swal.fire("Atención", "Selecciona un grado y una fecha.", "info");
            return;
        }
        const curso = auth.usuario?.codigoCurso;
        try {
            const res = await apiClient.get(
                `/asistencia/alumnos?codigoGrado=${gradoSeleccionado}&codigoCurso=${curso}&fecha=${fecha}`
            );
            setAlumnos(res.data.map(a => ({ ...a, estadoSeleccionado: a.estado || "" })));
            if (res.data.length === 0) {
                Swal.fire("Información", "No se encontraron alumnos para el grado seleccionado.", "info");
            }
        } catch (error) {
            console.error("Error cargando alumnos:", error);
            Swal.fire("Error", "Hubo un problema al cargar los alumnos.", "error");
        }
    };

    const cambiarEstadoLocal = (dni, estado) => {
        setAlumnos(prev => prev.map(a => a.dni === dni ? { ...a, estadoSeleccionado: estado } : a));
    };

    const guardarAsistencia = async (alumno) => {
        if (!alumno.estadoSeleccionado) {
            Swal.fire("Atención", "Selecciona un estado antes de guardar.", "info");
            return;
        }
        const curso = auth.usuario?.codigoCurso;
        setGuardando(alumno.dni);
        try {
            await apiClient.post("/asistencia/registrar", {
                dniAlumno: alumno.dni,
                codigoCurso: curso,
                fecha: fecha,
                estado: alumno.estadoSeleccionado,
            });
            Swal.fire("¡Asistencia guardada!", "", "success");
            setAlumnos(prev => prev.map(a => a.dni === alumno.dni ? { ...a, estado: alumno.estadoSeleccionado } : a));
        } catch (error) {
            console.error("Error guardando asistencia:", error);
            Swal.fire("Error", "No se pudo guardar la asistencia", "error");
        } finally {
            setGuardando("");
        }
    };

    return (
        <div>
            <ComponentePanelProfesores />
            <div className="container">
                <h3>Registro de Asistencia</h3>
                <div className="row mb-3">
                    <div className="col">
                        <label>Grado</label>
                        <select className="form-select" value={gradoSeleccionado} onChange={(e) => setGradoSeleccionado(e.target.value)}>
                            <option value="">Selecciona un grado</option>
                            {grados.map(g => (
                                <option key={g.codigoGrado} value={g.codigoGrado}>{g.nombreGrado} {g.nivel}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col">
                        <label>Fecha</label>
                        <input type="date" className="form-control" value={fecha} onChange={(e) => setFecha(e.target.value)} />
                    </div>
                    <div className="col d-flex align-items-end">
                        <button className="btn btn-primary" onClick={cargarAlumnos}>Buscar</button>
                    </div>
                </div>

                {alumnos.length > 0 && (
                    <table className="table table-dark table-striped">
                        <thead>
                            <tr>
                                <th>DNI</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Estado</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            {alumnos.map(alumno => (
                                <tr key={alumno.dni}>
                                    <td>{alumno.dni}</td>
                                    <td>{alumno.nombre}</td>
                                    <td>{alumno.apellido}</td>
                                    <td>
                                        <select
                                            className="form-select"
                                            value={alumno.estadoSeleccionado}
                                            onChange={(e) => cambiarEstadoLocal(alumno.dni, e.target.value)}
                                        >
                                            <option value="">Sin registrar</option>
                                            {ESTADOS.map(e => <option key={e} value={e}>{e}</option>)}
                                        </select>
                                    </td>
                                    <td>
                                        <button
                                            className="btn btn-success"
                                            disabled={guardando === alumno.dni}
                                            onClick={() => guardarAsistencia(alumno)}
                                        >
                                            {guardando === alumno.dni ? "Guardando..." : "Guardar"}
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

export default ComponenteProAsistencia;
