import React, { useState } from "react";
import "../estilos/notas.css";

function ComponenteNotas () {
    const [alumnoDNI, setAlumnoDNI] = useState('');
    const [alumnoNombre, setAlumnoNombre] = useState(false);
    const [alumnoDatos, setAlumnoDatos] = useState([]);
    const [mostrarSeccion, setMostrarSeccion] = useState(false);

    const buscarDatosPorDNI = async () => {
        try {
            if (!alumnoDNI) {
                setAlumnoNombre(false);
                setMostrarSeccion(false);
                return;
            }
            const response = await fetch(`http://localhost:8080/buscarPorDNI/${alumnoDNI}`);
            const data = await response.json();
            if (data.length > 0) {
                setAlumnoDatos(data);
                setMostrarSeccion(true);
                setAlumnoNombre(true);
            } else {
                setMostrarSeccion(false);
                setAlumnoNombre(false);
            }
        } catch (error) {
            console.error("¡HUBO UN FALLO CON LA API!", error);
            setMostrarSeccion(false);
        }
    };

    console.log(alumnoDatos);
    return (
        <>
            <section className="container login">
                <div className="login-form">
                    {alumnoNombre ? alumnoDatos.map((nombre) => <h3 key={nombre.DNI}> {nombre.Nombre} </h3>) : "Ingrese DNI"}
                    <img src={require("../recursos/insignia.jpg")} alt="Logo" />
                    <form action="./login.html" method="POST">
                        <input value={alumnoDNI} onChange={(e) => setAlumnoDNI(e.target.value)} type="number" name="alumnoDNI" placeholder="DNI del Alumno:" required={true} id="alumnoDNI" />
                        <input onClick={buscarDatosPorDNI} type="button" value="Buscar" id="submit" />
                    </form>
                </div>
            </section>
            <section className={mostrarSeccion ? "seccionNotas" : "seccionOculta"}>
                <div className="container contenedorDatosPersonales">
                    <table className="table table-dark table-striped-columns">
                        <thead>
                            <tr>
                                <th scope="col">DNI</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Apellido</th>
                                <th scope="col">Fecha de Nacimiento</th>
                                <th scope="col">Género</th>
                                <th scope="col">Télefono Apoderado</th>
                            </tr>
                        </thead>
                        {alumnoDatos.map((alumno) => (
                            <tbody key={alumno.DNI}>
                                <tr>
                                    <td>{alumno.DNI}</td>
                                    <td>{alumno.Nombre}</td>
                                    <td>{alumno.Apellido}</td>
                                    <td>
                                        {new Date(alumno.FechaNacimiento).toLocaleDateString('es-PE', {
                                            day: '2-digit',
                                            month: '2-digit',
                                            year: 'numeric'
                                        }).replace(/\//g, ' - ')}
                                    </td>
                                    <td>{alumno.Genero}</td>
                                    <td>{alumno.TelefonoApoderado}</td>
                                </tr> 
                            </tbody>
                        ))}
                    </table>
                </div>
                <div className="container contenedor">
                    <table className="table">
                        <thead  className="table-danger">
                            <tr>
                                <th scope="col"> Cursos </th>
                                <th scope="col"> Notas </th>
                            </tr>
                        </thead>
                        {alumnoDatos.map((alumno) => (
                            <tbody key={alumno.DNI}>
                                {alumno.NombresCursos ? alumno.NombresCursos.split(',').map((curso, index) => (
                                    <tr key={index} className="table-danger">
                                        <td>{curso}</td>
                                        <td>{alumno.Calificaciones.split(',')[index]}</td>
                                    </tr>
                                )) : (
                                    <tr className="table-danger">
                                        <td colSpan="2"> ¡Lastima, no hay cursos ni notas registradas! </td>
                                    </tr>
                                )}
                            </tbody>
                        ))}
                    </table>
                </div>
            </section>
        </>
    )

}

export default ComponenteNotas;