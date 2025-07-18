import "../estilos/panel.css"
import { Link, useNavigate } from "react-router-dom";

function ComponentePanelProfesores() {
    const navigate = useNavigate();

    const handleLogout = () => {
        navigate('/login');
    };

    return (
        <nav className="barraPanel">
            <Link to="/panel/listanotas">Listado Notas</Link>
            {/*<Link to="/panel/listaasistencia">Listado Asistencia</Link>*/}
            <button onClick={handleLogout} className="btn btn-danger">Cerrar Sesión</button>
        </nav>
    );
}

export default ComponentePanelProfesores;





