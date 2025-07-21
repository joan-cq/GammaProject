import "../estilos/panel.css"
import { Link, useNavigate } from "react-router-dom";

function ComponentePanel() {
    const navigate = useNavigate();

    const handleLogout = () => {
        // Aquí puedes agregar la lógica de limpieza de sesión, como borrar tokens.
        navigate('/login');
    };

    return (
        <nav className="barraPanel">
            <Link to="/panel/listaalumnos">Listado Alumnos</Link>
            <Link to="/panel/listacursos">Listado Cursos</Link>
            <Link to="/panel/listaadmin">Listado Administradores</Link>
            <Link to="/panel/listamaestros">Listado Profesores</Link>
            <Link to="/panel/anioescolar">Año Activo</Link>
            <Link to="/panel/logs">Logs</Link>
            <button onClick={handleLogout} className="btn btn-danger">Cerrar Sesión</button>
        </nav>
    );
}

export default ComponentePanel;





