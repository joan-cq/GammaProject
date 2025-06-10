import "../estilos/panel.css"
import { Link } from "react-router-dom";

function ComponentePanelProfesores() {
    return (
        <nav className="barraPanel">
            <Link to="/panel/listanotas">Listado Notas </Link>
            <Link to="/panel/listaasistencia">Listado Asistencia </Link>
        </nav>
    );
}

export default ComponentePanelProfesores;





