import React, { useEffect, useState } from "react";
import { useAuth } from '../App';
import apiClient from "./api.js";
import Swal from 'sweetalert2';
import { ComponentePanelProfesores } from "./index";

function ComponenteProAnuncios() {
    const auth = useAuth();
    const [anuncios, setAnuncios] = useState([]);
    const [titulo, setTitulo] = useState("");
    const [contenido, setContenido] = useState("");
    const [enviando, setEnviando] = useState(false);

    const cargarAnuncios = async () => {
        try {
            const res = await apiClient.get("/anuncios");
            setAnuncios(res.data);
        } catch (error) {
            console.error("Error cargando anuncios:", error);
            Swal.fire("Error", "No se pudieron cargar los anuncios.", "error");
        }
    };

    useEffect(() => {
        cargarAnuncios();
    }, []);

    const publicarAnuncio = async (e) => {
        e.preventDefault();
        if (!titulo.trim() || !contenido.trim()) {
            Swal.fire("Atención", "Completa el título y el contenido del anuncio.", "info");
            return;
        }
        setEnviando(true);
        try {
            await apiClient.post("/anuncios/add", {
                titulo,
                contenido,
                dniAutor: auth.usuario?.dni,
            });
            Swal.fire("¡Anuncio publicado!", "", "success");
            setTitulo("");
            setContenido("");
            await cargarAnuncios();
        } catch (error) {
            console.error("Error publicando anuncio:", error);
            Swal.fire("Error", "No se pudo publicar el anuncio", "error");
        } finally {
            setEnviando(false);
        }
    };

    const eliminarAnuncio = async (id) => {
        const confirmacion = await Swal.fire({
            title: "¿Eliminar anuncio?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar",
        });
        if (!confirmacion.isConfirmed) return;

        try {
            await apiClient.delete(`/anuncios/delete/${id}`);
            Swal.fire("Eliminado", "", "success");
            await cargarAnuncios();
        } catch (error) {
            console.error("Error eliminando anuncio:", error);
            Swal.fire("Error", "No se pudo eliminar el anuncio", "error");
        }
    };

    return (
        <div>
            <ComponentePanelProfesores />
            <div className="container">
                <h3>Anuncios Institucionales</h3>

                <form onSubmit={publicarAnuncio} className="mb-4">
                    <div className="mb-2">
                        <label>Título</label>
                        <input
                            type="text"
                            className="form-control"
                            value={titulo}
                            onChange={(e) => setTitulo(e.target.value)}
                            placeholder="Título del anuncio"
                        />
                    </div>
                    <div className="mb-2">
                        <label>Contenido</label>
                        <textarea
                            className="form-control"
                            rows="3"
                            value={contenido}
                            onChange={(e) => setContenido(e.target.value)}
                            placeholder="Escribe el anuncio..."
                        />
                    </div>
                    <button type="submit" className="btn btn-primary" disabled={enviando}>
                        {enviando ? "Publicando..." : "Publicar Anuncio"}
                    </button>
                </form>

                <h5>Anuncios publicados</h5>
                {anuncios.length > 0 ? (
                    <table className="table table-dark table-striped">
                        <thead>
                            <tr>
                                <th>Título</th>
                                <th>Contenido</th>
                                <th>Autor</th>
                                <th>Fecha</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            {anuncios.map(a => (
                                <tr key={a.id}>
                                    <td>{a.titulo}</td>
                                    <td>{a.contenido}</td>
                                    <td>{a.nombreAutor}</td>
                                    <td>{a.fechaPublicacion?.slice(0, 10)}</td>
                                    <td>
                                        <button className="btn btn-danger btn-sm" onClick={() => eliminarAnuncio(a.id)}>
                                            Eliminar
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : <p>No hay anuncios publicados todavía.</p>}
            </div>
        </div>
    );
}

export default ComponenteProAnuncios;
