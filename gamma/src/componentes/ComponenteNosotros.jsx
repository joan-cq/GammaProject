import React from "react";
import "../estilos/nosotros.css";

function ComPrincipios ({titulo, texto}) {
    return (
        <div>
            <h1> {titulo} </h1>
            <p>
                {texto}
            </p>
        </div>
    )
}

function ComponenteNosotros () {
    return (
        <>
            <section className="container sec-nosotros">
                <h1> Colegio Gamma </h1>
                <p> El Colegio Gamma es un referente en excelencia académica, ofreciendo una educación de primer nivel que prepara a los estudiantes tanto para la universidad como para la vida. Nuestro enfoque preuniversitario en el nivel secundario garantiza una preparación integral que va más allá de las aulas. Contamos con un equipo docente excepcional, comprometido con el éxito y el desarrollo de cada estudiante. En el Colegio Gamma, no solo nos preocupamos por el rendimiento académico, sino que también nos dedicamos a inculcar valores sólidos que guíen a nuestros alumnos en su camino hacia el éxito profesional y personal. </p>
                <p> Resolución Directoral Regional emitida por UGEL: R.D.Nº 002117-2017-GR </p>
            </section>
            <section className="container sec-principios">
                <ComPrincipios 
                    titulo = "Visión"
                    texto = "Nuestra visión es ser reconocidos como una de las mejores propuestas educativas dentro y fuera de nuestra región. Aspiramos a desarrollar en nuestros educandos aptitudes y actitudes en los diversos campos: científicos, humanos y sociales, que les permitan no solo acceder y ser parte de este mundo globalizado, sino transformarlo. En la Institución Educativa Privada “GAMMA”, vemos un futuro donde nuestros graduados son ciudadanos responsables y productivos, equipados con las habilidades y el conocimiento necesarios para contribuir positivamente a nuestra sociedad. Nos esforzamos por ser una institución que se destaque no solo por su rigurosidad académica, sino también por su compromiso con el desarrollo personal y social de nuestros estudiantes."
                />
                <ComPrincipios 
                    titulo = "Misión"
                    texto = "La Institución Educativa Privada “GAMMA” aspira a ser la mejor opción educativa, proporcionando una enseñanza de calidad con un estilo propio y estrategias metodológicas innovadoras sustentadas en la tecnología. Nuestra misión es descubrir y desarrollar los conocimientos, habilidades, destrezas y valores de nuestros estudiantes, consolidando sus aprendizajes y preparándolos para la vida universitaria y ciudadana. Nos esforzamos por inculcar el aprendizaje y los valores, y por mantener un alto nivel académico y una cultura organizacional sólida. Nuestro objetivo es formar individuos íntegros, capaces de enfrentar los desafíos del mundo moderno con confianza y competencia."
                />
            </section>
            <section className="container seccion-propuesta">
                <div className="row">
                    <div className="col-12 col-sm-6 col-xl-6 imagenNosotros">
                        
                    </div>
                    <div className="col-12 col-sm-6 col-xl-6 informacionNosotros">
                        <div>

                        </div>
                        <div>

                        </div>
                        <div>

                        </div>
                        <div>

                        </div>
                        <div>

                        </div>
                        <div>
                            
                        </div>
                    </div>
                </div>
            </section>
            <section className="container seccion-historia-videos">
                <video controls autoPlay preload='auto' loop className="historia-videos">
                    <source src={require("../recursos/video1.mp4")} type="video/mp4"/>
                </video>
                <video controls autoPlay preload='auto' loop className="historia-videos">
                    <source src={require("../recursos/video1.mp4")} type="video/mp4"/>
                </video>
                <video controls autoPlay preload='auto' loop className="historia-videos">
                    <source src={require("../recursos/video4.mp4")} type="video/mp4"/>
                </video>
            </section>
            <section className="container-fluid seccion-ubi">
                <div>

                </div>
            </section>
        </>
    )
}

export default ComponenteNosotros;