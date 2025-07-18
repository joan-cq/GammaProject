import React, { useState } from "react";
import "../estilos/notas.css";

function ComponenteNotas () {
    const [alumnoDNI, setAlumnoDNI] = useState('');
    const [alumnoPassword, setAlumnoPassword] = useState('');
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
            <section className="container login loginAdmin">
                <div className="login-form">
                    {alumnoNombre ? alumnoDatos.map((nombre) => <h3 key={nombre.DNI}> {nombre.Nombre} </h3>) : "Ingrese DNI"}
                    <img src={require("../recursos/insignia.jpg")} alt="Logo" />
                    <form action="./login.html" method="POST">
                        <input value={alumnoDNI} onChange={(e) => setAlumnoDNI(e.target.value)} type="text" name="alumnoDNI" placeholder="DNI del Alumno:" required={true} id="alumnoDNI" />                       
                        <input value={alumnoPassword} onChange={(e) => setAlumnoPassword(e.target.value)} type="password" name="alumnoPassword" placeholder="Contraseña del Alumno:" required={true} id="alumnoPassword" />
                        <input onClick={buscarDatosPorDNI} type="button" value="Buscar" id="submit" />
                    </form>
                </div>
            </section>
            
        </>
    )

}

export default ComponenteNotas;