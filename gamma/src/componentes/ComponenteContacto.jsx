import { React, useEffect, useRef} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../estilos/contacto.css";

function ComponenteContacto () {
    const formRef = useRef(null);
    useEffect(() =>{
        const form = formRef.current;
        if (form) {
            form.addEventListener("submit", (event) => {
                event.preventDefault();
                const nombre = event.target.elements.fname.value;
                const correoElec = event.target.elements.email.value;
                const numero = event.target.elements.phone.value;
                const dni = event.target.elements.dni.value;
                const mensaje = event.target.elements.message.value;
                const correo = "colegiopreugamma@gmail.com";
                const headGmail = "Información Colegio Gamma";
                const encodedAsunto = encodeURI(headGmail);
                const bodyGmail = `Nombre: ${nombre}\nCorreo Electronico: ${correoElec}\nN° Celular: ${numero}\nDNI: ${dni}\nMensaje: ${mensaje}`;
                const encodedEmailBody = encodeURI(bodyGmail);
                const gmailUrl = `https://mail.google.com/mail/u/0/?view=cm&fs=1&to=${correo}&su=${encodedAsunto}&body=${encodedEmailBody}`;
                window.open(gmailUrl, '_blank');
                setTimeout(() => form.reset(), 1000);
            });
        }
    }, [])
    return (
        <section className="container-fluid contenedor-contac">
            <h1> CONTÁCTANOS </h1>
            <div className="row">
                <div className="col-12 col-sm-4 col-xxl-4 info">
                    <h4> CONTACTOS DIRECTOS: </h4>
                    <h6> 944466270 </h6>
                    <h6> Correo: areasistemas@corporaciongajel.com </h6>
                    <h6> Av. San Jose Mz A Lote 07 Ciudad de Dios</h6>
                    <p> 08:00 AM - 19:00 PM Lunes a Sábado </p>
                </div>
                <div className="col-12 col-sm-8 col-xxl-8 formulario">
                    <form ref={formRef} action="/submit_form" method='POST'>
                        <div>
                            <label htmlFor="fname">Nombre: </label><br/>
                            <input type="text" id="fname" name="fname" placeholder="Ingresa tu nombre" required={true} /><br/>
                            <label htmlFor="email">Correo Electrónico: </label><br/>
                            <input type="email" id="email" name="email" placeholder="Ingresa tu email" required={true} /><br/>
                            <label htmlFor="phone">Nro° de Celular:</label><br/>
                            <input type="number" id="phone" name="phone" placeholder="Ingresa tu celular" required={true} /><br/>
                        </div>
                        <div>
                            <label htmlFor="dni">DNI:</label><br/>
                            <input type="number" id="dni" name="dni" placeholder="Ingresa tu DNI" required={true} /><br/>
                            <label htmlFor="message">Mensaje:</label><br/>
                            <textarea id="message" name="message" placeholder="Ingresa tu mensaje" required={true}></textarea><br/>
                            <input type="submit" value="ENVIAR" />
                        </div>
                    </form>
                </div>
            </div>
        </section>
    )
}

export default ComponenteContacto;