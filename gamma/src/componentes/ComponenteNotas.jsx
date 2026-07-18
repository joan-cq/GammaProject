import React, { useState } from "react";
import Swal from 'sweetalert2';
import "../estilos/notas.css";
import "../estilos/login.css";

function ComponenteNotas() {
    const [dni, setDni] = useState('');
    const [password, setPassword] = useState('');
    const [alumno, setAlumno] = useState(null);
    const [error, setError] = useState('');
    const [anioActivo, setAnioActivo] = useState('');
    const [grado, setGrado] = useState(null);
    const [cursos, setCursos] = useState([]);
    const [cursoSeleccionado, setCursoSeleccionado] = useState(null);
    const [bimestreSeleccionado, setBimestreSeleccionado] = useState(null);
    const [notas, setNotas] = useState([]);
    const [todasLasNotas, setTodasLasNotas] = useState([]);
    const [vista, setVista] = useState('notas'); // notas | todas | asistencia | anuncios
    const [asistencias, setAsistencias] = useState([]);
    const [anuncios, setAnuncios] = useState([]);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ dni, clave: password }),
            });

            const data = await response.json();

            if (response.ok && data.rol === 'ALUMNO') {
                localStorage.setItem('token', data.token);
                await fetchInitialData(dni);
                Swal.fire({
                    title: '¡Bienvenido!',
                    text: `Inicio de sesión correcto.`,
                    icon: 'success',
                });
            } else {
                const errorMsg = data.error || 'Credenciales incorrectas o no tienes el rol de Alumno.';
                setError(errorMsg);
                setAlumno(null);
                Swal.fire({
                    title: '¡Error de Autenticación!',
                    text: errorMsg,
                    icon: 'error',
                });
            }
            setDni('');
            setPassword('');
        } catch (err) {
            setError('Error al conectar con el servidor.');
            console.error(err);
            Swal.fire({
                title: '¡Error de Conexión!',
                text: 'No se pudo conectar con el servidor. Inténtalo más tarde.',
                icon: 'error',
            });
            setDni('');
            setPassword('');
        }
    };

    const fetchInitialData = async (alumnoDni) => {
        try {
            const token = localStorage.getItem('token');
            const authHeader = { 'Authorization': `Bearer ${token}` };

            const alumnosResponse = await fetch('http://localhost:8080/alumno/list', { headers: authHeader });
            const alumnosData = await alumnosResponse.json();
            const currentAlumno = alumnosData.find(a => a.dni === alumnoDni);
            if (!currentAlumno) throw new Error("No se encontraron los datos del alumno.");
            setAlumno(currentAlumno);

            const aniosResponse = await fetch('http://localhost:8080/anioescolar/list', { headers: authHeader });
            const aniosData = await aniosResponse.json();
            const activeYear = aniosData.find(a => a.estado === 'ACTIVO');
            setAnioActivo(activeYear ? activeYear.anio : 'No definido');

            const gradosResponse = await fetch('http://localhost:8080/grado/list', { headers: authHeader });
            const gradosData = await gradosResponse.json();
            const currentGrado = gradosData.find(g => g.codigoGrado === currentAlumno.codigoGrado);
            setGrado(currentGrado);

            if (currentAlumno.codigoGrado) {
                const cursosResponse = await fetch(`http://localhost:8080/grado_curso/cursos/${currentAlumno.codigoGrado}`, { headers: authHeader });
                const cursosData = await cursosResponse.json();
                setCursos(cursosData);
            }
        } catch (error) {
            console.error("Error al cargar datos iniciales:", error);
            setError("No se pudieron cargar los datos del alumno.");
            Swal.fire('Error', 'No se pudieron cargar los datos completos del alumno.', 'error');
        }
    };

    const handleCursoClick = (curso) => {
        setVista('notas');
        setCursoSeleccionado(curso);
        setBimestreSeleccionado(null);
        setNotas([]);
    };

    const handleBimestreClick = async (bimestre) => {
        setBimestreSeleccionado(bimestre);
        if (cursoSeleccionado && alumno && grado) {
            try {
                const token = localStorage.getItem('token');
                const response = await fetch(`http://localhost:8080/notas/alumnos?codigoGrado=${grado.codigoGrado}&idBimestre=${bimestre}&codigoCurso=${cursoSeleccionado.codigoCurso}`, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                const data = await response.json();
                const notasAlumno = data.filter(n => n.dni === alumno.dni);
                setNotas(notasAlumno);
            } catch (error) {
                console.error("Error al obtener las notas:", error);
                setNotas([]);
            }
        }
    };

    const handleVerTodasLasNotas = async () => {
        setVista('todas');
        setCursoSeleccionado(null);
        setBimestreSeleccionado(null);
        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/notas/alumno/${alumno.dni}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            const notasAgrupadas = data.reduce((acc, nota) => {
                const curso = nota.curso;
                if (!acc[curso]) {
                    acc[curso] = [];
                }
                acc[curso].push(nota);
                return acc;
            }, {});
            setTodasLasNotas(notasAgrupadas);
        } catch (error) {
            console.error("Error al obtener todas las notas:", error);
            setTodasLasNotas({});
        }
    };

    const handleVerAsistencia = async () => {
        setVista('asistencia');
        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/asistencia/alumno/${alumno.dni}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            setAsistencias(data);
        } catch (error) {
            console.error("Error al obtener la asistencia:", error);
            setAsistencias([]);
        }
    };

    const handleVerAnuncios = async () => {
        setVista('anuncios');
        try {
            const token = localStorage.getItem('token');
            const response = await fetch('http://localhost:8080/anuncios', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            setAnuncios(data);
        } catch (error) {
            console.error("Error al obtener los anuncios:", error);
            setAnuncios([]);
        }
    };

    const renderAsistencia = () => (
        <div className="asistencia-section">
            <h5>Historial de Asistencia</h5>
            {asistencias.length > 0 ? (
                <table className="table table-striped table-bordered">
                    <thead className="thead-dark">
                        <tr><th>Fecha</th><th>Curso</th><th>Estado</th></tr>
                    </thead>
                    <tbody>
                        {asistencias.map((a) => (
                            <tr key={a.id}><td>{a.fecha}</td><td>{a.curso}</td><td>{a.estado}</td></tr>
                        ))}
                    </tbody>
                </table>
            ) : <p>No hay registros de asistencia todavía.</p>}
        </div>
    );

    const renderAnuncios = () => (
        <div className="anuncios-section">
            <h5>Anuncios Institucionales</h5>
            {anuncios.length > 0 ? (
                anuncios.map((a) => (
                    <div key={a.id} className="anuncio-card mb-3 p-3 border rounded">
                        <h6>{a.titulo}</h6>
                        <p>{a.contenido}</p>
                        <small>{a.nombreAutor} · {a.fechaPublicacion?.slice(0, 10)}</small>
                    </div>
                ))
            ) : <p>No hay anuncios publicados todavía.</p>}
        </div>
    );

    const renderVistaNormal = () => (
        <>
            <div className="cursos-section">
                <h5>Selecciona un curso:</h5>
                <div className="btn-group">
                    {cursos.map((curso) => (
                        <button
                            key={curso.codigoCurso}
                            onClick={() => handleCursoClick(curso)}
                            className={`btn btn-outline-primary ${cursoSeleccionado?.codigoCurso === curso.codigoCurso ? 'active' : ''}`}
                        >
                            {curso.nombre}
                        </button>
                    ))}
                </div>
            </div>

            {cursoSeleccionado && (
                <div className="bimestres-section">
                    <h5>Selecciona un bimestre para ver las notas de {cursoSeleccionado.nombre}:</h5>
                    <div className="btn-group">
                        {[1, 2, 3, 4].map((bim) => (
                            <button
                                key={bim}
                                onClick={() => handleBimestreClick(bim)}
                                className={`btn btn-outline-secondary ${bimestreSeleccionado === bim ? 'active' : ''}`}
                            >
                                Bimestre {bim}
                            </button>
                        ))}
                    </div>
                </div>
            )}

            {bimestreSeleccionado && (
                <div className="notas-section">
                    <h5>Notas del Bimestre {bimestreSeleccionado} para {cursoSeleccionado.nombre}:</h5>
                    {notas.length > 0 ? (
                        <table className="table table-striped table-bordered">
                            <thead className="thead-dark"><tr><th>Calificación</th></tr></thead>
                            <tbody>
                                {notas.map((nota, index) => (
                                    <tr key={nota.idNota || index}><td>{nota.nota ?? 'No registrada'}</td></tr>
                                ))}
                            </tbody>
                        </table>
                    ) : <p>No hay notas registradas para este bimestre.</p>}
                </div>
            )}
        </>
    );

    const renderVistaTodasLasNotas = () => (
        <div className="todas-notas-section">
            <h5>Resumen de Todas las Notas</h5>
            <button className="btn btn-secondary mb-4" onClick={() => setVista('notas')}>Volver a la vista normal</button>
            {Object.keys(todasLasNotas).length > 0 ? (
                Object.entries(todasLasNotas).map(([curso, notasDelCurso]) => (
                    <div key={curso} className="curso-notas-block">
                        <h6>{curso}</h6>
                        <table className="table table-sm table-bordered">
                            <thead className="thead-light">
                                <tr>
                                    <th>Bimestre 1</th>
                                    <th>Bimestre 2</th>
                                    <th>Bimestre 3</th>
                                    <th>Bimestre 4</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{notasDelCurso.find(n => n.bimestre === 1)?.nota ?? '-'}</td>
                                    <td>{notasDelCurso.find(n => n.bimestre === 2)?.nota ?? '-'}</td>
                                    <td>{notasDelCurso.find(n => n.bimestre === 3)?.nota ?? '-'}</td>
                                    <td>{notasDelCurso.find(n => n.bimestre === 4)?.nota ?? '-'}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                ))
            ) : <p>No se encontraron notas para mostrar.</p>}
        </div>
    );

    if (!alumno) {
        return (
            <section className="container login loginAdmin">
                <div className="login-form">
                    <p> Ingresa tu usuario y contraseña </p>
                    <img src={require("../recursos/insignia.jpg")} alt="Logo" />
                    <form onSubmit={handleLogin}>
                        <input type="text" name="dni" value={dni} onChange={(e) => setDni(e.target.value)} placeholder="Ingresa tu DNI:" required={true} />
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} name="clave" placeholder="Ingresa tu contraseña:" required={true} />
                        <input type="submit" value="Ingresar" />
                    </form>
                    {error && <p className="error-message">{error}</p>}
                </div>
            </section>
        );
    }

    return (
        <div className="notas-container">
            <div className="header-info">
                <h2>Bienvenido, {alumno.nombre} {alumno.apellido}</h2>
                {grado && <h3>{grado.nombreGrado} - {grado.nivel}</h3>}
                <h4>Año Escolar: {anioActivo}</h4>
                <button onClick={() => setAlumno(null)} className="btn btn-danger">Cerrar Sesión</button>
            </div>

            <div className="ver-todas-container d-flex gap-2 flex-wrap">
                {vista !== 'notas' && (
                    <button className="btn btn-ver-todas" onClick={() => setVista('notas')}>Ver Notas</button>
                )}
                {vista !== 'todas' && (
                    <button className="btn btn-ver-todas" onClick={handleVerTodasLasNotas}>Ver Todas las Notas</button>
                )}
                {vista !== 'asistencia' && (
                    <button className="btn btn-ver-todas" onClick={handleVerAsistencia}>Ver Asistencia</button>
                )}
                {vista !== 'anuncios' && (
                    <button className="btn btn-ver-todas" onClick={handleVerAnuncios}>Ver Anuncios</button>
                )}
            </div>

            {vista === 'todas' && renderVistaTodasLasNotas()}
            {vista === 'asistencia' && renderAsistencia()}
            {vista === 'anuncios' && renderAnuncios()}
            {vista === 'notas' && renderVistaNormal()}
        </div>
    );
}

export default ComponenteNotas;