import "../estilos/panel.css"
import { Link } from "react-router-dom";

function ComponentePanel() {
    return (
        <nav className="barraPanel">
            <Link to="/panel/listaalumnos">Listado Alumnos</Link>
            <Link to="/panel/listacursos">Listado Cursos</Link>
            <Link to="/panel/listaadmin">Listado Administradores </Link>
            <Link to="/panel/listamaestros">Listado Profesores </Link>
        </nav>
    );
}

export default ComponentePanel;





