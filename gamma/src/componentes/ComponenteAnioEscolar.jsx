import React, { useEffect, useState } from 'react';
import apiClient from './api.js';
import Swal from 'sweetalert2';
import { ComponentePanelAdmin } from "./index.js";

function ComponenteAnioEscolar() {
    const [listaAniosEscolares, setListaAniosEscolares] = useState([]);
    const [error, setError] = useState('');

    const obtenerAnios = async () => {
        try {
            const res = await apiClient.get("/anioescolar/list");
            setListaAniosEscolares(res.data);
        } catch (err) {
            console.error("Error al obtener años escolares:", err);
            setError('No se pudo cargar la lista de años escolares.');
        }
    };

    const cambiarEstadoAnio = (id, nuevoEstado) => {
        Swal.fire({
            title: `¿Seguro que deseas ${nuevoEstado === "ACTIVO" ? "activar" : "desactivar"} este año?`,
            text: "Esta acción modificará el estado del año escolar.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: nuevoEstado === "ACTIVO" ? "#28a745" : "#d33",
            cancelButtonColor: "#6c757d",
            confirmButtonText: `Sí, ${nuevoEstado === "ACTIVO" ? "activar" : "desactivar"}`
        }).then((result) => {
            if (result.isConfirmed) {
                apiClient.put("/anioescolar/update", {
                    id: id,
                    estado: nuevoEstado,
                })
                .then(() => {
                    Swal.fire(
                        "¡Éxito!",
                        `El año fue ${nuevoEstado === "ACTIVO" ? "activado" : "desactivado"} correctamente.`,
                        "success"
                    );
                    obtenerAnios();
                })
                .catch(() => {
                    Swal.fire("Error", "No se pudo cambiar el estado", "error");
                });
            }
        });
    };

    useEffect(() => {
        obtenerAnios();
    }, []);

    return (
        <>
            <ComponentePanelAdmin />
            <div className='container contenedorAlumno'>
                <h3> Año Activo </h3>
                {error && <div className="alert alert-danger">{error}</div>}
                <section className="contenedorAdd">
                    <table className="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col"> Año </th>
                                <th scope="col"> Estado </th>
                                <th scope="col"> Acción </th>
                            </tr>
                        </thead>
                        <tbody>
                            {listaAniosEscolares.length > 0 ? (
                                listaAniosEscolares.map((anio) => (
                                    <tr className="table-primary" key={anio.id}>
                                        <td>{anio.anio}</td>
                                        <td style={{ color: anio.estado === 'ACTIVO' ? 'green' : 'red' }}>
                                            {anio.estado === 'ACTIVO' ? "🟢 ACTIVO" : "🔴 INACTIVO"}
                                        </td>
                                        <td>
                                            <button
                                                className={`btn ${anio.estado === 'ACTIVO' ? 'btn-danger' : 'btn-success'}`}
                                                onClick={() =>
                                                    cambiarEstadoAnio(anio.id, anio.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO')
                                                }
                                            >
                                                {anio.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="3">No hay años escolares registrados.</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </section>
            </div>
        </>
    );
}

export default ComponenteAnioEscolar;
