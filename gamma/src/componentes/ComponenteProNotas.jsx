import React, { useState, useEffect } from "react";
import {ComponentePanelProfesores} from "./index.js";
import axios from "axios";
import Swal from 'sweetalert2';

function ComponenteProNotas() {
    const [notas, setNotas] = useState([]);
    const [notasId, setNotasId] = useState("");
    const [notasDniAlumno, setNotasDniAlumno] = useState("");
    const [notasCalificacion, setNotasCalificacion] = useState("");
    const [notasCodigoCurso, setNotasCodigoCurso] = useState("");
    const [editar, setEditar] = useState(false);

    let resultado = Array.isArray(notas) ? notas : [];
    if (notasDniAlumno) {
        resultado = resultado.filter((dato) =>
            dato.dni_alumno.toString().includes(notasDniAlumno.toString())
        );
    }

    const fetchListaNotas = async () => {
        try {
            const apiURL = await fetch(`http://localhost:8080/notas`);
            if (!apiURL.ok) {
                console.log("LA API NOTAS NO EXISTE");
            }
            const data = await apiURL.json();
            setNotas(data);
        } catch (error) {
            console.log(error);
        }
    }
    const agregarNota = () => {
        axios.post("http://localhost:8080/nota/add", {
            nota: notasCalificacion,
            dni_alumno: notasDniAlumno,
            codigo_curso: notasCodigoCurso,
        }).then(() => {
            fetchListaNotas();
            setNotasId("");
            setNotasDniAlumno("");
            setNotasCalificacion("");
            setNotasCodigoCurso("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Nota agregado con éxito!',
                icon: 'success',
            });
        })
    };
    const eliminarNota = (id) => {
        axios.delete(`http://localhost:8080/nota/delete/${id}`)
            .then(() => {
                fetchListaNotas();
                Swal.fire({
                    title: '¡Enhorabuena!',
                    text: '¡Nota eliminada con éxito!',
                    icon: 'success',
                });
            });
    }
    const editarNota = (nota) => {
        setEditar(true);
        setNotasId(nota.id);
        setNotasDniAlumno(nota.alumno.dni);
        setNotasCodigoCurso(nota.curso.codigoCurso);
        setNotasCalificacion(nota.nota);
    }
    const actualizarNota = () => {
        axios.put(`http://localhost:8080/nota/update/${notasId}`, {
            nota: notasCalificacion,
        }).then(() => {
            setEditar(false);
            fetchListaNotas();
            setNotasId("");
            setNotasDniAlumno("");
            setNotasCalificacion("");
            setNotasCodigoCurso("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Nota actualizada con éxito!',
                icon: 'success',
            });
        }).catch((error) => {
            console.error("Error al actualizar el la nota:", error);
        });
    }

    useEffect(() => {
        fetchListaNotas();
    }, [])
    console.log(notas);
    return (
        <>
            <ComponentePanelProfesores/>
            <div className='container contenedorTabla'>
                <h3> Lista Notas: </h3>
                <section className="contenedorAddAlumno">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> ID Nota </th>
                                <th scope="col"> DNI Alumno </th>
                                <th scope="col"> Código Curso </th>
                                <th scope="col"> Calificación </th>
                                <th scope="col">Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td>
                                    <input placeholder="No llenar" type="number" value={notasId} onChange={(e) => setNotasId(e.target.value)} disabled/>
                                </td>
                                <td>
                                    <input type="number" value={notasDniAlumno} onChange={(e) => setNotasDniAlumno(e.target.value)} />
                                </td>
                                <td>
                                    <input type="text" value={notasCodigoCurso} onChange={(e) => setNotasCodigoCurso(e.target.value)}/>
                                </td>
                                <td>
                                    <input placeholder="00.00" type="text" value={notasCalificacion} onChange={(e) => setNotasCalificacion(e.target.value)}/>
                                </td>
                                <td>
                                    <div role="group" aria-label="Basic mixed styles example">
                                        {
                                            editar === true ?
                                            <button onClick={actualizarNota} className="btn btn-warning"> Actualizar </button> :
                                            <button onClick={agregarNota} className="btn btn-success"> Agregar </button>
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
                                <th scope="col"> ID Nota </th>
                                <th scope="col"> DNI </th>
                                <th scope="col"> Código Curso </th>
                                <th scope="col"> Calificación </th>
                                <th scope="col">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                resultado.map((nota) => 
                                    <tr className="table-primary" key={nota.id_nota}>
                                        <td> {nota.id_nota} </td>
                                        <td> {nota.dni_alumno} </td>
                                        <td> {nota.codigo_curso} </td>
                                        <td> {nota.nota} </td>
                                        <td>
                                            <div className="btn-group" role="group" aria-label="Basic mixed styles example">
                                                <button onClick={() => {editarNota(nota)}} type="button" className="btn btn-success"> Editar </button>
                                                <button onClick={() => {eliminarNota(nota.id)}} type="button" className="btn btn-danger"> Eliminar </button>
                                            </div>
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                </section>
            </div>
        </>
    )
}

export default ComponenteProNotas;
