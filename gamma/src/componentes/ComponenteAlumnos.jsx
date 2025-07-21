import React, { useState, useEffect } from "react";
import apiClient from "./api.js";
import Swal   from "sweetalert2";
import { ComponentePanelAdmin, ComponenteUpdatePassword } from "./index.js";

function ComponenteAlumnos() {
  /* ──────────────────────────────── state ─────────────────────────────── */
  const [alumnos, setAlumnos]     = useState([]);
  const [grados,  setGrados]      = useState([]);

  /* campos del formulario */
  const [dni,   setDni]   = useState("");
  const [nombre,setNombre]= useState("");
  const [apellido,setApellido]=useState("");
  const [celularApoderado,setCelular]=useState("");
  const [genero,setGenero]=useState("");
  const [codigoGrado,setCodigoGrado]=useState("");
  const [estado, setEstado] = useState("");
  const [clave,setClave] = useState("");
  const [mostrarClave,setMostrarClave]=useState(false);

  const [editar, setEditar] = useState(false);
  const [modalPasswordOpen,setModalPasswordOpen]=useState(false);
  const [selectedAlumno,setSelectedAlumno]=useState(null);
  const [error,setError]=useState("");

  /* ─────────────────────────── helpers ─────────────────────────── */
  const formularioSucio =
       dni || nombre || apellido || celularApoderado || genero || codigoGrado || clave;

  const limpiar = ()=>{
      setDni("");setNombre("");setApellido("");setCelular("");
      setGenero("");setCodigoGrado("");setClave("");setError("");setEditar(false);
  };

  /* ───────────────────────── peticiones ───────────────────────── */
  const cargarAlumnos = async ()=>{
      try{ const r = await apiClient.get("/alumno/list");
           setAlumnos(r.data); }
      catch(e){ console.error(e); }
    };
    const cargarGrados = async ()=>{
      try{ const r=await apiClient.get("/grado/list");
           setGrados(r.data); }
      catch(e){ console.error(e); }
    };

  /* ───────────── agregar ───────────── */
  const agregar = ()=>{
      if(!dni||!nombre||!apellido||!celularApoderado||!genero||!codigoGrado||!clave){
         setError("Por favor complete todos los campos"); return; }
      apiClient.post("/alumno/add",{
           dni,nombre,apellido,
           celularApoderado,
           genero,
           codigoGrado,
           clave
      }).then(()=>{
         Swal.fire("¡Enhorabuena!","¡Alumno agregado!","success");
         cargarAlumnos(); limpiar();
      }).catch(e=>{
         console.error(e); setError("No se pudo agregar");
      });
  };

  /* ───────────── actualizar ───────────── */
  const actualizar = ()=>{
      if(!dni||!nombre||!apellido||!celularApoderado||!genero||!codigoGrado||!estado){
         setError("Por favor complete todos los campos"); return; }
      apiClient.put("/alumno/update",{
          dni,nombre,apellido,celularApoderado,genero,codigoGrado,estado
      }).then(()=>{
          Swal.fire("Actualizado","Alumno actualizado","success");
          cargarAlumnos(); limpiar();
      }).catch(e=>{
          console.error(e); setError("Error al actualizar");
      });
  };

  /* ───────────── eliminar ───────────── */
  const eliminar = (dni)=>{
      Swal.fire({title:"¿Eliminar?",icon:"warning",showCancelButton:true})
      .then(res=>{
         if(res.isConfirmed){
           apiClient.delete(`/alumno/delete/${dni}`)
                 .then(()=>{ Swal.fire("Eliminado","","success"); cargarAlumnos(); });
         }
      });
  };

  /* ───────────── edición de fila ───────────── */
  const editarFila = a=>{
      setEditar(true);
      setDni(a.dni); setNombre(a.nombre); setApellido(a.apellido);
      setCelular(a.celularApoderado); setGenero(a.genero);
      setCodigoGrado(a.codigoGrado);
  };

  /* ───────────── efecto inicial ───────────── */
  useEffect(()=>{ cargarAlumnos(); cargarGrados(); },[]);

  /* ─────────────────────────────── render ─────────────────────────────── */
  return (
   <>
    <ComponentePanelAdmin/>
    <div className="container contenedorTabla">
      <h3>Lista Alumnos</h3>
      {error && <div className="alert alert-danger">{error}</div>}

      {/* ───────── Formulario ───────── */}
      <section className="contenedorAdd">
        <table className="table table-dark">
          <thead>
            <tr>
              <th>DNI</th>
              <th>Nombre</th>
              <th>Apellido</th>
              <th>Cel. Apoderado</th>
              <th>Género</th>
              <th>Grado</th>
              {editar ? <th scope="col">Estado</th> : <th scope="col">Contraseña</th>}
            </tr>
          </thead>
          <tbody>
            <tr className="table-success">
  <td>
    <input value={dni} maxLength="8" disabled={editar}
           onChange={e => setDni(e.target.value.replace(/[^0-9]/g, ""))} />
  </td>
  <td>
    <input value={nombre}
           onChange={e => setNombre(e.target.value.replace(/[^A-Za-z\s]/g, ""))} />
  </td>
  <td>
    <input value={apellido}
           onChange={e => setApellido(e.target.value.replace(/[^A-Za-z\s]/g, ""))} />
  </td>
  <td>
    <input value={celularApoderado} maxLength="9"
           onChange={e => setCelular(e.target.value.replace(/[^0-9]/g, ""))} />
  </td>
  <td>
    <select value={genero} onChange={e => setGenero(e.target.value)}>
      <option value="MASCULINO">MASCULINO</option>
      <option value="FEMENINO">FEMENINO</option>
    </select>
  </td>
  <td>
    <select className="w-100" value={codigoGrado} onChange={(e) => setCodigoGrado(e.target.value)}>
      <option value="">Seleccionar Grado</option>
      {grados.map(g => (
        <option key={g.codigoGrado} value={g.codigoGrado}>
          {g.nombreGrado} {g.nivel}
        </option>
            ))}
                </select>
              </td>
              {editar ? (
                <td>
                  <select value={estado} onChange={(e) => setEstado(e.target.value)}>
                    <option value="">Seleccionar Estado</option>
                    <option value="ACTIVO">ACTIVO</option>
                    <option value="INACTIVO">INACTIVO</option>
                  </select>
                </td>
              ) : (
                <td className="password-container">
                  <input
                    type={mostrarClave ? "text" : "password"}
                    value={clave}
                    onChange={(e) => setClave(e.target.value)}
                    placeholder="Contraseña"
                  />
                  <span
                    className="password-toggle-profe"
                    onClick={() => setMostrarClave(!mostrarClave)}
                  >
                    {mostrarClave ? "👁️" : "👁️‍🗨️"}
                  </span>
                </td>
              )}
            </tr>
          </tbody>
          <tbody>
            <tr>
              <td colSpan={editar?7:7}>
                {editar ? (
                  <>
                    <button className="btn btn-warning" onClick={actualizar}>Actualizar</button>
                    <button className="btn btn-secondary ms-2" onClick={limpiar}>Cancelar</button>
                  </>
                ) : (
                  <>
                    <button className="btn btn-success" onClick={agregar}>Agregar</button>
                    {formularioSucio && (
                      <button className="btn btn-secondary ms-2" onClick={limpiar}>Cancelar</button>
                    )}
                  </>
                )}
              </td>
            </tr>
          </tbody>
        </table>
      </section>

      {/* ───────── Tabla de resultados ───────── */}
      <section>
        <table className="table table-dark">
          <thead>
            <tr>
              <th>DNI</th><th>Nombre</th><th>Apellido</th>
              <th>Cel. Apod.</th><th>Género</th><th>Grado</th><th>Año</th>
              <th>Estado</th><th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {alumnos.map(a=>(
              <tr className="table-primary" key={a.dni}>
                <td>{a.dni}</td>
                <td>{a.nombre}</td>
                <td>{a.apellido}</td>
                <td>{a.celularApoderado}</td>
                <td>{a.genero}</td>
                <td>{a.codigoGrado}</td>
                <td>{a.anio}</td>
                <td style={{color:a.estado==="ACTIVO"?"lightgreen":"red"}}>
                    {a.estado==="ACTIVO"?"🟢":"🔴"}
                </td>
                <td>
                  <div className="btn-group">
                    <button className="btn btn-danger"   onClick={()=>eliminar(a.dni)}>Eliminar</button>
                    <button className="btn btn-success"  onClick={()=>editarFila(a)}>Editar</button>
                    <button className="btn btn-warning"  onClick={()=>{setSelectedAlumno(a);setModalPasswordOpen(true);}}>
                      Contraseña
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>

    {/* modal contraseña */}
    {modalPasswordOpen && selectedAlumno && (
      <ComponenteUpdatePassword
        show      ={modalPasswordOpen}
        onClose   ={()=>setModalPasswordOpen(false)}
        alumno    ={selectedAlumno}
        fetchListarUsuarios={cargarAlumnos}
        dni       ={selectedAlumno.dni}
        tipoUsuario="alumno"
      />
    )}
   </>
  );
}

export default ComponenteAlumnos;
