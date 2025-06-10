import React, {useState, useEffect} from 'react'
import {ComponentePanelProfesores} from "./index.js";
import axios from 'axios';
import Swal from 'sweetalert2';

function ComponenteAsistencia() {
    const [dataAsistencia, setdataAsistencia] = useState([])
    const [idAsistencia, setIdAsistencia] = useState("");
    const [dniAlumno, setDniAlumno] = useState("");
    const [codigoCurso, setCodigoCurso] = useState("");
    const [nombreCurso, setNombreCurso] = useState("");
    const [fechaAsistencia, setFechaAsistencia] = useState("");
    const [asistencia, setAsistencia] = useState();
    let resultado = dataAsistencia;

    if (dniAlumno) {
        resultado = resultado.filter((dato) => 
            dato.DNI.toString().includes(dniAlumno.toString())
        );
    }

    const fetchDataAsistencia = async () => {
        try {
            const apiUrl = await fetch("http://localhost:8080/asistencia/list");
            if (apiUrl.ok) {
                const data = await apiUrl.json();
                setdataAsistencia(data);
            } else {
                console.log("No se encontró la API");
            }
        } catch (error) {
            console.log(error);
        }
    }

    const agregarAsistencia = () => {
        axios.post("http://localhost:8080/asistencia/add", {
            DNI: dniAlumno,
            Codigo: codigoCurso,
            Nombre: nombreCurso,
            Fecha: fechaAsistencia,
            Asistencia: asistencia,
        }).then(() => {
            fetchDataAsistencia();
            setDniAlumno("");
            setCodigoCurso("");
            setNombreCurso("");
            setNombreCurso("");
            setFechaAsistencia("");
            setAsistencia("");
            Swal.fire({
                title: '¡Enhorabuena!',
                text: '¡Asistencia agregada con éxito!',
                icon: 'success',
            });
        })
    };
    
    useEffect(() => {
        fetchDataAsistencia();
    }, [])
    return (
        <>
            <ComponentePanelProfesores/>
            <div className='container contenedorTabla'>
                <h3> Lista Asistencia: </h3>
                <section className="contenedorAddAlumno">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> ID Asistencia </th>
                                <th scope="col"> DNI Alumno </th>
                                <th scope="col"> Código Curso </th>
                                <th scope="col"> Nombre Curso </th>
                                <th scope="col"> Fecha </th>
                                <th scope="col"> Asistencia </th>
                                <th scope="col">Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="table-success">
                                <td>
                                    <input placeholder="No llenar" type="number" value={idAsistencia} onChange={(e) => setIdAsistencia(e.target.value)}/>
                                </td>
                                <td>
                                    <input type="number" value={dniAlumno} onChange={(e) => setDniAlumno(e.target.value)} />
                                </td>
                                <td>
                                    <input type="text" value={codigoCurso} onChange={(e) => setCodigoCurso(e.target.value)}/>
                                </td>
                                <td>
                                    <input type="text" value={nombreCurso} onChange={(e) => setNombreCurso(e.target.value)}/>
                                </td>
                                <td>
                                    <input  placeholder="yyyy-mm-dd" title="Formato: yyyy-mm-dd" type="text" value={fechaAsistencia} onChange={(e) => setFechaAsistencia(e.target.value)}/>
                                </td>
                                <td>
                                    <input placeholder="Si: 1 - No: 0" type="text" value={asistencia} onChange={(e) => setAsistencia(e.target.value)}/>
                                </td>
                                <td>
                                    <div role="group" aria-label="Basic mixed styles example">
                                        <button onClick={agregarAsistencia} className="btn btn-success"> Agregar </button>
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
                                <th scope="col"> ID Asistencia </th>
                                <th scope="col"> DNI Alumno </th>
                                <th scope="col"> Código Curso </th>
                                <th scope="col"> Nombre Curso </th>
                                <th scope="col"> Fecha </th>
                                <th scope="col"> Asistencia </th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                resultado.map((asistencia) => 
                                    <tr className="table-primary" key={asistencia.IDAsistencia}>
                                        <td> {asistencia.IDAsistencia} </td>
                                        <td> {asistencia.DNI} </td>
                                        <td> {asistencia.Codigo} </td>
                                        <td> {asistencia.Nombre} </td>
                                        <td> 
                                            {new Date(asistencia.Fecha).toLocaleDateString('es-PE', {
                                                day: '2-digit',
                                                month: '2-digit',
                                                year: 'numeric'
                                            }).replace(/\//g, ' - ')}
                                        </td>
                                        <td> {(asistencia.Asistencia ? "🟢" : "🔴")} </td>
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

export default ComponenteAsistencia;
