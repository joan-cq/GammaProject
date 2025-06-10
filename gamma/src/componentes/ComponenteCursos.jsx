import React, { useState, useEffect } from "react";
import axios from "axios";
import Swal from 'sweetalert2';
import {ComponentePanelAdmin} from "./index.js"

function ComponenteCursos() {
    const [cursos, setCursos] = useState([]);
    const [codigoCurso, setCodigoCurso] = useState("");
    const [nivelCurso, setNivelCurso] = useState("");
    const [editar, setEditar] = useState(false);

    const fetchListarCursos = async () => {
        try {
            const apiURL = await fetch("http://localhost:8080/curso/list");
            if (!apiURL.ok) {
                console.log("LA API CURSO NO EXISTE");
            }
            const data = await apiURL.json();
            setCursos(data);
        } catch (error) {
            console.log(error);
        }
    }
    const agregarCurso = () => {
        axios.post("http://localhost:8080/curso/add", {
            codigoCurso: codigoCurso,
            nivel: nivelCurso,
        }).then(() => {
            fetchListarCursos();
            setCodigoCurso("");
            setNivelCurso("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Curso agregado con éxito!',
                icon: 'success',
            });
        })
    };
    const eliminarCurso = (codigoCurso) => {
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
                axios.delete(`http://localhost:8080/curso/delete/${codigoCurso}`)
                .then(() => {
                    fetchListarCursos();
                });
                swalWithBootstrapButtons.fire({
                title: "¡Enhorabuena!",
                text: "¡Curso eliminado con éxito!",
                icon: "success"
        }); } else if (
            result.dismiss === Swal.DismissReason.cancel
            ) {
            swalWithBootstrapButtons.fire({
                title: "¡Enhorabuena!",
                text: "¡Curso no fue eliminado!",
                icon: "success"
            });
            }
        });
    }
    const editarCurso = (curso) => {
        setEditar(true);
        setCodigoCurso(curso.codigoCurso);
        setNivelCurso(curso.nivel);
    }
    const actualizarCurso = () => {
        axios.put("http://localhost:8080/curso/update", {
            codigoCurso: codigoCurso,
            nivel: nivelCurso,
        }).then(() => {
            setEditar(false);
            fetchListarCursos();
            setCodigoCurso("");
            setNivelCurso("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Curso actualizado con éxito!',
                icon: 'success',
            });
        }).catch((error) => {
            console.error("Error al actualizar el cursos:", error);
        });
    }
    
    useEffect(() => {
        fetchListarCursos();
    }, [])

    return (
        <>
            <ComponentePanelAdmin />
            <div className='container contenedorTabla'>
                <h3> Lista Cursos: </h3>
                <section className="contenedorAddAlumno">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> Código </th>
                                <th scope="col"> Nivel </th>
                                <th scope="col">Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td>
                                    <input type="text" value={codigoCurso} onChange={(e) => setCodigoCurso(e.target.value)}/>
                                </td>
                                <td>
                                    <input type="text" value={nivelCurso} onChange={(e) => setNivelCurso(e.target.value)}/>
                                </td>
                                <td>
                                    <div role="group" aria-label="Basic mixed styles example">
                                        {
                                            editar === true ?
                                            <button onClick={actualizarCurso} className="btn btn-warning"> Actualizar </button> :
                                            <button onClick={agregarCurso} className="btn btn-success"> Agregar </button>
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
                                <th scope="col"> Código </th>
                                <th scope="col"> Nivel </th>
                                <th scope="col">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                cursos.map((curso) =>
                                    <tr className="table-primary" key={curso.codigoCurso}>
                                        <td> {curso.codigoCurso} </td>
                                        <td> {curso.nivel} </td>
                                        <td>
                                            <div className="btn-group" role="group" aria-label="Basic mixed styles example">
                                                <button onClick={() => {eliminarCurso(curso.codigoCurso)}} type="button" className="btn btn-danger"> Eliminar </button>
                                                <button onClick={() => {editarCurso(curso)}} type="button" className="btn btn-success"> Actualizar </button>
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

export default ComponenteCursos;
