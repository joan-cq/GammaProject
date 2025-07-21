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
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ dni: idUsuario, clave: clave })
            });

            const data = await response.json();

            if (response.ok) {
                localStorage.setItem('token', data.token);

                if (data.rol === "ADMINISTRADOR" || data.rol === "PROFESOR") {
                    Swal.fire({
                        title: '¡Bienvenido!',
                        text: '¡Credenciales Correctas!',
                        icon: 'success',
                    });

                    auth.iniciarSesion(data);

                    if (data.rol === "ADMINISTRADOR") {
                        navigate('/panel/listaalumnos');
                    } else if (data.rol === "PROFESOR") {
                        navigate('/panel/listanotas');
                    }
                } else {
                    Swal.fire({
                        title: '¡Acceso Denegado!',
                        text: 'Los alumnos no tienen permitido el acceso aquí.',
                        icon: 'error',
                    });
                }
            } else {
                Swal.fire({
                    title: '¡Error!',
                    text: data.error || '¡Credenciales Incorrectas!',
                    icon: 'error',
                });
                setClave("");
                setIdUsuario("");
            }
        } catch (error) {
            console.error('Error al conectar con el backend:', error);
            Swal.fire({
                title: '¡Error!',
                text: '¡Error al conectar con el backend!',
                icon: 'error',
            });
            setClave("");
            setIdUsuario("");
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



