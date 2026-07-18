import "../estilos/panel.css"
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../App";

function ComponentePanelProfesores() {
    const navigate = useNavigate();
    const auth = useAuth();

    const handleLogout = () => {
        navigate('/login');
    };

    return (
        <nav className="barraPanel">
            {auth.usuario?.nombreCurso && (
                <span className="curso-docente">Curso: {auth.usuario.nombreCurso}</span>
            )}
            <Link to="/panel/listanotas">Listado Notas</Link>
            <Link to="/panel/listaasistencia">Listado Asistencia</Link>
            <Link to="/panel/listaanuncios">Anuncios</Link>
            <button onClick={handleLogout} className="btn btn-danger">Cerrar Sesión</button>
        </nav>
    );
}

export default ComponentePanelProfesores;





