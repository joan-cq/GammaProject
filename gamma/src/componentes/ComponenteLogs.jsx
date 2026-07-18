import React, { useEffect, useMemo, useState } from 'react';
import apiClient from './api.js';
import { ComponentePanelAdmin } from './index';

const COLOR_CATEGORIA = {
    INFO: 'bg-info text-dark',
    CREACION: 'bg-success',
    MODIFICACION: 'bg-warning text-dark',
    ELIMINACION: 'bg-danger',
    LOGIN: 'bg-primary',
    ERROR: 'bg-dark',
};

const ComponenteLogs = () => {
    const [logs, setLogs] = useState([]);
    const [categoria, setCategoria] = useState('TODAS');
    const [entidad, setEntidad] = useState('TODAS');
    const [busqueda, setBusqueda] = useState('');

    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const response = await apiClient.get('/logs');
                setLogs(response.data);
            } catch (error) {
                console.error('Error al obtener los logs:', error);
            }
        };
        fetchLogs();
    }, []);

    const entidades = useMemo(() => {
        const unicas = new Set(logs.map(l => l.entidad).filter(Boolean));
        return Array.from(unicas).sort();
    }, [logs]);

    const logsFiltrados = logs.filter(log => {
        if (categoria !== 'TODAS' && log.categoria !== categoria) return false;
        if (entidad !== 'TODAS' && log.entidad !== entidad) return false;
        if (busqueda.trim()) {
            const texto = busqueda.trim().toLowerCase();
            const coincide =
                (log.usuarioDni || '').toLowerCase().includes(texto) ||
                (log.mensaje || '').toLowerCase().includes(texto);
            if (!coincide) return false;
        }
        return true;
    });

    return (
        <div>
            <ComponentePanelAdmin />
            <div className="container mt-4">
                <h2>Registros del Sistema</h2>

                <div className="row mb-3 g-2">
                    <div className="col-auto">
                        <label className="form-label mb-0">Categoría</label>
                        <select className="form-select" value={categoria} onChange={(e) => setCategoria(e.target.value)}>
                            <option value="TODAS">Todas</option>
                            <option value="INFO">Info</option>
                            <option value="CREACION">Creación</option>
                            <option value="MODIFICACION">Modificación</option>
                            <option value="ELIMINACION">Eliminación</option>
                            <option value="LOGIN">Inicio de sesión</option>
                            <option value="ERROR">Error</option>
                        </select>
                    </div>
                    <div className="col-auto">
                        <label className="form-label mb-0">Módulo</label>
                        <select className="form-select" value={entidad} onChange={(e) => setEntidad(e.target.value)}>
                            <option value="TODAS">Todos</option>
                            {entidades.map(e => <option key={e} value={e}>{e}</option>)}
                        </select>
                    </div>
                    <div className="col-auto flex-grow-1">
                        <label className="form-label mb-0">Buscar (usuario o mensaje)</label>
                        <input
                            type="text"
                            className="form-control"
                            value={busqueda}
                            onChange={(e) => setBusqueda(e.target.value)}
                            placeholder="DNI del usuario o parte del mensaje..."
                        />
                    </div>
                </div>

                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Categoría</th>
                            <th scope="col">Módulo</th>
                            <th scope="col">Usuario</th>
                            <th scope="col">Mensaje</th>
                            <th scope="col">Fecha y Hora</th>
                        </tr>
                    </thead>
                    <tbody>
                        {logsFiltrados.map((log, index) => (
                            <tr key={log.id}>
                                <th scope="row">{index + 1}</th>
                                <td>
                                    <span className={`badge ${COLOR_CATEGORIA[log.categoria] || 'bg-secondary'}`}>
                                        {log.categoria || 'INFO'}
                                    </span>
                                </td>
                                <td>{log.entidad || '—'}</td>
                                <td>{log.usuarioDni || '—'}</td>
                                <td>{log.mensaje}</td>
                                <td>{new Date(log.timestamp).toLocaleString()}</td>
                            </tr>
                        ))}
                        {logsFiltrados.length === 0 && (
                            <tr><td colSpan={6} className="text-center">No hay registros con esos filtros.</td></tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ComponenteLogs;
