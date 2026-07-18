import React from "react";
import "../estilos/logros.css";

function MiniLogros ({nombre, logro}) {
    return (
        <div> 
            <div> </div>
            <span> {nombre} - {logro} </span>
        </div>
    )
}

//Componente a renderizar
function ComponenteLogros () {
    return (
        <div className="seccion-logros">
            <header> NUESTROS LOGROS </header>
            <div className="container contenedor">
                <MiniLogros 
                    nombre = "PIERO GARCIA CRUZ"
                    logro = "COMERCIO Y NEGOCIOS INTERNACIONALES"
                />
                <MiniLogros 
                    nombre = "LENIN ZAMORA CARLOS"
                    logro = "INGENIERIA ELECTRONICA"
                />
                <MiniLogros 
                    nombre = "ANGELINA TAPIA CUSQUISIBAN"
                    logro = "INGENIERÍA QUÍMICA"
                />
                <MiniLogros 
                    nombre = "SANTIAGO BECERRA COBOS"
                    logro = "INGENIERÍA ELECTRÓNICA"
                />
                <MiniLogros 
                    nombre = "ANDERSON PATAZCA ENEQUE"
                    logro = "INDUSTRIAS ALIMENTARIAS"
                />
                <MiniLogros 
                    nombre = "CRISTIAN PATAZCA LOCONI"
                    logro = "INDUSTRIAS ALIMENTARIAS"
                />
            </div>
        </div>
    )
}

export default ComponenteLogros;