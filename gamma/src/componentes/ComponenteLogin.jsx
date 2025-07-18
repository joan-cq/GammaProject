import "../estilos/login.css";
import Swal from 'sweetalert2';
import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../App'; 

function ComponenteLogin() {
    const [idUsuario, setIdUsuario] = useState("");
    const [clave, setClave] = useState("");
    const navigate = useNavigate();
    const auth = useAuth();

    const fetchCredenciales = async () => {
        try {
            const requestBody = JSON.stringify({
                dni: idUsuario,
                clave: clave
            });

            console.log("Enviando solicitud a /auth/login con:", requestBody);

            const response = await fetch('http://localhost:8080/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: requestBody
            });

            const data = await response.json();

            console.log("Respuesta del backend:", data);

            if (response.ok) {
                Swal.fire({
                    title: '¡Bienvenido!',
                    text: '¡Credenciales Correctas!',
                    icon: 'success',
                });
                
                if (data.rol === "PROFESOR") {
                    const codigoCursoResponse = await fetch(`http://localhost:8080/profesor/codigo_curso?dni=${idUsuario}`);
                    const codigoCursoData = await codigoCursoResponse.json();
                    console.log("Respuesta del backend para codigo_curso:", codigoCursoData);
                    auth.iniciarSesion({ Rol: data.rol, Clave: data.clave, CodigoCurso: codigoCursoData.codigo_curso });
                } else {
                    auth.iniciarSesion({ Rol: data.rol, Clave: data.clave });
                }

                // Redirigir al panel correspondiente según el rol
                if (data.rol === "ADMINISTRADOR") {
                    navigate('/panel/listaalumnos');
                } else if (data.rol === "PROFESOR") {
                    navigate('/panel/listanotas');
                } else {
                    navigate('/inicio');
                }
            } else {
                console.log("Error en la autenticación:", data);
                Swal.fire({
                    title: '¡Error!',
                    text: '¡Credenciales Incorrectas!',
                    icon: 'error',
                });
            }
        } catch (error) {
            console.error('Error al conectar con el backend:', error);
            Swal.fire({
                title: '¡Error!',
                text: '¡Error al conectar con el backend!',
                icon: 'error',
            });
        }
    };

    return (
        <>
            <section className="container login loginAdmin">
                <div className="login-form">
                    <p> Ingresa tu usuario y contraseña </p>
                    <img src={require("../recursos/insignia.jpg")} alt="Logo" />
                    <form method="POST">
                        <input  type="text" name="dni" value={idUsuario} onChange={(e) => setIdUsuario(e.target.value)} placeholder="Ingresa tu DNI:" required={true} id="idUsuario" />
                        <input  type="password" value={clave} onChange={(e) => setClave(e.target.value)} name="clave" placeholder="Ingresa tu contraseña:" required={true} id="password" />
                        <input  type="button" value="Ingresar" onClick={fetchCredenciales} id="submit"/>
                    </form>
                </div>
            </section>
        </>
    )
}

export default ComponenteLogin;



