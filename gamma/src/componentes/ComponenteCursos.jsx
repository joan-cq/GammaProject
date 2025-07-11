import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import { Modal, Button, Form } from 'react-bootstrap';
import { ComponentePanelAdmin } from './index';

function ComponenteCursos() {
    const [cursos, setCursos] = useState([]);
    const [modalShow, setModalShow] = useState(false);
    const [cursoActual, setCursoActual] = useState({ codigoCurso: '', nombre: '', estado: 'ACTIVO' });
    const [esNuevo, setEsNuevo] = useState(true);

    useEffect(() => {
        cargarCursos();
    }, []);

    const cargarCursos = async () => {
        try {
            const res = await axios.get('http://localhost:8080/curso/list');
            setCursos(res.data);
        } catch (error) {
            console.error("Error cargando cursos:", error);
        }
    };

    const abrirModal = (curso) => {
        if (curso) {
            setCursoActual(curso);
            setEsNuevo(false);
        } else {
            setCursoActual({ codigoCurso: '', nombre: '', estado: 'ACTIVO' });
            setEsNuevo(true);
        }
        setModalShow(true);
    };

    const cerrarModal = () => {
        setModalShow(false);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCursoActual({ ...cursoActual, [name]: value });
    };

    const guardarCurso = async () => {
        try {
            if (esNuevo) {
                await axios.post('http://localhost:8080/curso/add', cursoActual);
                Swal.fire('¡Curso agregado!', '', 'success');
            } else {
                await axios.put(`http://localhost:8080/curso/update/${cursoActual.codigoCurso}`, cursoActual);
                Swal.fire('¡Curso actualizado!', '', 'success');
            }
            cerrarModal();
            cargarCursos();
        } catch (error) {
            console.error("Error guardando curso:", error);
            Swal.fire('Error', 'No se pudo guardar el curso', 'error');
        }
    };

    const toggleEstado = async (codigoCurso) => {
        try {
            await axios.put(`http://localhost:8080/curso/toggle/${codigoCurso}`);
            Swal.fire('¡Estado actualizado!', '', 'success');
            cargarCursos();
        } catch (error) {
            console.error("Error actualizando estado:", error);
            Swal.fire('Error', 'No se pudo actualizar el estado', 'error');
        }
    };

    return (
        <div>
            <ComponentePanelAdmin />
            <div className="container">
                <h3>Gestión de Cursos</h3>
                <button className="btn btn-primary mb-3" onClick={() => abrirModal(null)}>Agregar Curso</button>
                <table className="table table-dark table-striped">
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Nombre</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {cursos.map(curso => (
                            <tr key={curso.codigoCurso}>
                                <td>{curso.codigoCurso}</td>
                                <td>{curso.nombre}</td>
                                <td>{curso.estado}</td>
                                <td>
                                    <button className="btn btn-warning me-2" onClick={() => abrirModal(curso)}>Editar</button>
                                    <button className={`btn ${curso.estado === 'ACTIVO' ? 'btn-danger' : 'btn-success'}`} onClick={() => toggleEstado(curso.codigoCurso)}>
                                        {curso.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            <Modal show={modalShow} onHide={cerrarModal}>
                <Modal.Header closeButton>
                    <Modal.Title>{esNuevo ? 'Agregar Curso' : 'Editar Curso'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group>
                            <Form.Label>Código del Curso</Form.Label>
                            <Form.Control type="text" name="codigoCurso" value={cursoActual.codigoCurso} onChange={handleInputChange} disabled={!esNuevo} />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Nombre del Curso</Form.Label>
                            <Form.Control type="text" name="nombre" value={cursoActual.nombre} onChange={handleInputChange} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="primary" onClick={guardarCurso}>Guardar</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default ComponenteCursos;
