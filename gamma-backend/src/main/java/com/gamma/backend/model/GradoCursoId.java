package com.gamma.backend.model;

import java.io.Serializable;
import java.util.Objects;

public class GradoCursoId implements Serializable {
    private String grado;
    private String curso;
    private Integer anioEscolar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradoCursoId that = (GradoCursoId) o;
        return Objects.equals(grado, that.grado) &&
               Objects.equals(curso, that.curso) &&
               Objects.equals(anioEscolar, that.anioEscolar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grado, curso, anioEscolar);
    }
}