import React from "react";
import "../estilos/inicio.css";

function SubTestimonios ({imagen, nombre, texto}) {
    return (
        <div className="contenedor-tes">
            <div className="subcontenedor-tes"> 
                <img src={require(`../recursos/${imagen}.jpg`)} alt="Imagen" />
                <h6> {nombre} </h6>
            </div>
            <p> {texto} </p>
        </div>
    )
}
function SubContenedoresGrados ({imagen, grado}) {
    return (
        <div className="subContenedoresGrados d-flex justify-content-center align-items-center flex-column">
            <img src={require(`../recursos/${imagen}.jpg`)} alt="Imagen" />
            <button> {grado} </button>
        </div>
    )
}
function SubCartasValores ({icono, titulo, texto}) {
    return (
        <div className="maincontainer">
            <div className="card">
                <div className="thefront">
                    <i className={`bi bi-${icono}`}> </i>
                    <h6> {titulo} </h6>
                </div>
                <div className="theback">
                    <h6> {titulo} </h6>
                    <p> {texto} </p>
                </div>
            </div>
        </div>
    )
}

//Componente a renderizar
export function ComponenteInicio () {
    return (
        <>
            <section className="container-fluid imagen-princi">
            </section>
            <div className="svg">
                <svg
                    preserveAspectRatio="none"
                    viewBox="0 0 1200 120"
                    xmlns="http://www.w3.org/2000/svg"
                    >
                    <path
                        d="M0 0v46.29c47.79 22.2 103.59 32.17 158 28 70.36-5.37 136.33-33.31 206.8-37.5 73.84-4.36 147.54 16.88 218.2 35.26 69.27 18 138.3 24.88 209.4 13.08 36.15-6 69.85-17.84 104.45-29.34C989.49 25 1113-14.29 1200 52.47V0z"
                        opacity=".25"
                    />
                    <path
                        d="M0 0v15.81c13 21.11 27.64 41.05 47.69 56.24C99.41 111.27 165 111 224.58 91.58c31.15-10.15 60.09-26.07 89.67-39.8 40.92-19 84.73-46 130.83-49.67 36.26-2.85 70.9 9.42 98.6 31.56 31.77 25.39 62.32 62 103.63 73 40.44 10.79 81.35-6.69 119.13-24.28s75.16-39 116.92-43.05c59.73-5.85 113.28 22.88 168.9 38.84 30.2 8.66 59 6.17 87.09-7.5 22.43-10.89 48-26.93 60.65-49.24V0z"
                        opacity=".5"
                    />
                    <path d="M0 0v5.63C149.93 59 314.09 71.32 475.83 42.57c43-7.64 84.23-20.12 127.61-26.46 59-8.63 112.48 12.24 165.56 35.4C827.93 77.22 886 95.24 951.2 90c86.53-7 172.46-45.71 248.8-84.81V0z" />
                </svg>
            </div>
            <section className="contenedorGrados container-fluid d-flex justify-content-center align-items-center">
                <SubContenedoresGrados 
                    imagen = "IMAGEN3"
                    grado = "Secundaria"
                />
                <SubContenedoresGrados 
                    imagen = "5"
                    grado = "Pre Universitaria"
                />
            </section>
            <section className="container seccion-propuesta">
                <div className="row">
                    <div className="col-12 col-sm-6 col-xl-6 imagen">
                        <video autoPlay preload='auto' loop muted>
                            <source src={require("../recursos/slogan.mp4")} type="video/mp4"/>
                        </video>
                    </div>
                    <div className="col-12 col-sm-6 col-xl-6 informacion">
                        <h1> PROPUESTA EDUCATIVA </h1>
                        <p> 
                            La Institución Educativa Privada “GAMMA” es una institución relativamente joven, puesta en funcionamiento en el año 2017 en el quehacer educativo.
                            El 28 de octubre de 2015 por iniciativa del Lic. Arturo Santisteban Jacinto y Lic. Eduardo de la Cruz Porras, quienes preocupados por la formación y educación de los adolescentes de Ciudad de Dios y San José, crean la Institución Educativa Particular “GAMMA”. Los albores de esta escuela en sus pocos años ya es conocida y reconocida donde se ha ganado el cariño y el respeto de nuestros coterráneos, con sus dos emblemas particulares la disciplina y la excelencia académica que distingue a esta casa de estudio día a día en su misión y consecuentemente en su visión.
                        </p>
                    </div>
                </div>
            </section>
            <section className="container video">
                <video autoPlay preload='auto' loop muted>
                    <source src={require("../recursos/NuestrosPilaresEstudiantiles.mp4")} type="video/mp4"/>
                </video>
            </section>
            <section className="container-fluid">
                <header className="titulo-valores"> NUESTROS PILARES </header>
                <section className="container-fluid seccion-valores">
                    <SubCartasValores 
                        icono = "mortarboard-fill"
                        titulo = "Académico"
                        texto = "Este pilar se centra en proporcionar una educación de calidad que promueva el aprendizaje y el desarrollo intelectual de los estudiantes. Incluye currículos sólidos, métodos de enseñanza efectivos y evaluaciones justas para medir el progreso académico."
                    />
                    <SubCartasValores 
                        icono = "check-circle-fill"
                        titulo = "Ético y Moral"
                        texto = "Los colegios suelen enfatizar la importancia de cultivar valores éticos y morales en los estudiantes, como la honestidad, la responsabilidad, el respeto y la empatía. Estos valores suelen integrarse en el currículo y se refuerzan a través de actividades extracurriculares y la cultura escolar en general."
                    />
                    <SubCartasValores 
                        icono = "heart-fill"
                        titulo = "Bienestar y Salud"
                        texto = "Los colegios también se preocupan por el bienestar físico y emocional de los estudiantes. Esto implica proporcionar un entorno seguro y saludable, así como acceso a recursos y apoyo para promover estilos de vida saludables y manejo efectivo del estrés."
                    />
                    <SubCartasValores 
                        icono = "trophy-fill"
                        titulo = "Excelencia y Desempeño"
                        texto = "Muchos colegios aspiran a la excelencia en todas las áreas, ya sea académica, deportiva, artística o comunitaria. Fomentan la ambición, la perseverancia y el esfuerzo para que los estudiantes alcancen su máximo potencial en todos los aspectos de sus vidas."
                    />
                </section>
            </section>
            <section className="container-fluid seccion-convenio">
                <h2> NUESTROS CONVENIOS </h2>
                <div className="contenedor-convenios">
                    <img src={require("../recursos/utp-logo.jpg")} alt="Imagen UTP" />
                    <img src={require("../recursos/logo-uss.png")} alt="Imagen USS" />
                </div>
            </section>
            <section className="container-fluid seccion-convenio">
                <h2> NUESTRA PREPARACIÓN A UNIVERSIDAD ESPECIALIZADA</h2>
                <div className="contenedor-convenios">
                    <img src={require("../recursos/prg-loto.png")} alt="Imagen PRG" />
                </div>
            </section>
            <h2 className="titulo-logros"> TESTIMONIOS </h2>
            <section className="container-fluid seccion-imagenes">
                <SubTestimonios 
                    imagen = "alumno1"
                    nombre = "Naydelin Carrilo"
                    texto = "Es un colegio muy prestigioso con una excelente enseñanza académica; con unos profesores con muy buen prestigio; cuenta con los valores que deben tener cada colegio. Infunden el respeto."
                />
                <SubTestimonios 
                    imagen = "alumno2"
                    nombre = "Maria de los Ángeles"
                    texto = "Me gusta estudiar en el colegio Gamma por la buena enseñanza que da, porque hay buenos profesores que te dan consejos para que te esfuerces más en tus estudios. Sus Olimpiadas son muy diferentes y emocionantes, te preparan para ingresar a las mejores universidades."
                />
                <SubTestimonios 
                    imagen = "alumno3"
                    nombre = "Daniel Teque"
                    texto = "Recomendaría al colegio GAMMA por sus enseñanzas, es muy buena y participa en concursos de Matemática y de Comunicación. También porque hay buenos profesores"
                />
                <SubTestimonios 
                    imagen = "alumno4"
                    nombre = "Astrid Flores"
                    texto = "Me gusta estudiar en el colegio GAMMA por su manera de enseñar a los alumnos, por su preparación en los distintos concursos y por los consejos del día a día."
                />
                <SubTestimonios 
                    imagen = "alumno5"
                    nombre = "Ariana Chunga"
                    texto = "Estudiar en este colegio ha sido una experiencia increíble. Los profesores son muy dedicados y siempre buscan la manera de hacernos entender los temas más complicados"
                />
                <SubTestimonios 
                    imagen = "alumno6"
                    nombre = "Marita Zapata"
                    texto = "Lo que más valoro de mi colegio es el ambiente de respeto y compañerismo que se vive día a día. Aquí no solo aprendo académicamente, sino también valores importantes para la vida."
                />
                <SubTestimonios 
                    imagen = "alumno7"
                    nombre = "Antony Patazca"
                    texto = "El colegio me ha brindado muchas oportunidades para desarrollar mis habilidades y me ha preparado bien para los retos futuros."
                />
                <SubTestimonios 
                    imagen = "alumno8"
                    nombre = "Keysi Rodriguez"
                    texto = "Aprecio mucho el enfoque práctico de la enseñanza aquí; realmente siento que estoy aplicando lo que aprendo en situaciones reales."
                />
                <SubTestimonios 
                    imagen = "alumno9"
                    nombre = "Eneque Akemi"
                    texto = "Los proyectos y actividades extracurriculares del colegio me han ayudado a descubrir mis pasiones y a crecer como persona."
                />
                <SubTestimonios 
                    imagen = "alumno10"
                    nombre = "Oliver Luis"
                    texto = "Gracias al apoyo constante de los maestros, he podido superar mis expectativas académicas y alcanzar metas que nunca pensé posibles"
                />
            </section>
        </>
    )
}

export default ComponenteInicio;