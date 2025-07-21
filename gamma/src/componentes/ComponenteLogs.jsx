import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ComponentePanelAdmin } from './index';

const ComponenteLogs = () => {
    const [logs, setLogs] = useState([]);
    const [filtro, setFiltro] = useState('todos');
    const [adminDropdownOpen, setAdminDropdownOpen] = useState(false);
    const [profesorDropdownOpen, setProfesorDropdownOpen] = useState(false);

    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const response = await axios.get('http://localhost:8080/logs');
                setLogs(response.data);
            } catch (error) {
                console.error('Error al obtener los logs:', error);
            }
        };
        fetchLogs();
    }, []);

    const filtrarLogs = (categoria) => {
        setFiltro(categoria);
    };

    const logsFiltrados = logs.filter(log => {
        if (filtro === 'todos') return true;
        if (filtro === 'login') return log.message.toLowerCase().includes('sesión');
        return log.message.toLowerCase().includes(filtro);
    });

    return (
        <div>
        <ComponentePanelAdmin />
        <div className="container mt-4">
            <h2>Registros del Sistema</h2>
            <div className="btn-group mb-3" role="group">
                <button type="button" className="btn btn-primary" onClick={() => filtrarLogs('todos')}>Todos</button>
                <button type="button" className="btn btn-info" onClick={() => filtrarLogs('login')}>Inicios de Sesión</button>
                <div className="btn-group" role="group">
                    <button id="adminDropdown" type="button" className="btn btn-secondary dropdown-toggle" onClick={() => setAdminDropdownOpen(!adminDropdownOpen)} aria-expanded={adminDropdownOpen}>
                        Administradores
                    </button>
                    <ul className={`dropdown-menu ${adminDropdownOpen ? 'show' : ''}`} aria-labelledby="adminDropdown">
                        <li><a className="dropdown-item" href="#" onClick={() => { filtrarLogs('alumnos'); setAdminDropdownOpen(false); }}>Alumnos</a></li>
                        <li><a className="dropdown-item" href="#" onClick={() => { filtrarLogs('cursos'); setAdminDropdownOpen(false); }}>Cursos</a></li>
                        <li><a className="dropdown-item" href="#" onClick={() => { filtrarLogs('administradores'); setAdminDropdownOpen(false); }}>Administradores</a></li>
                        <li><a className="dropdown-item" href="#" onClick={() => { filtrarLogs('profesores'); setAdminDropdownOpen(false); }}>Profesores</a></li>
                        <li><a className="dropdown-item" href="#" onClick={() => { filtrarLogs('año escolar'); setAdminDropdownOpen(false); }}>Año Activo</a></li>
                    </ul>
                </div>
                <div className="btn-group" role="group">
                    <button id="profesorDropdown" type="button" className="btn btn-secondary dropdown-toggle" onClick={() => setProfesorDropdownOpen(!profesorDropdownOpen)} aria-expanded={profesorDropdownOpen}>
                        Profesores
                    </button>
                    <ul className={`dropdown-menu ${profesorDropdownOpen ? 'show' : ''}`} aria-labelledby="profesorDropdown">
                        <li><a className="dropdown-item" href="#" onClick={() => { filtrarLogs('notas'); setProfesorDropdownOpen(false); }}>Notas</a></li>
                    </ul>
                </div>
            </div>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Nivel</th>
                        <th scope="col">Mensaje</th>
                        <th scope="col">Fecha y Hora</th>
                    </tr>
                </thead>
                <tbody>
                    {logsFiltrados.map((log, index) => (
                        <tr key={log.id}>
                            <th scope="row">{index + 1}</th>
                            <td>{log.level}</td>
                            <td>{log.message}</td>
                            <td>{new Date(log.timestamp).toLocaleString()}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    </div>
    );
};

export default ComponenteLogs;