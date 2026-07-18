package com.gamma.backend.repository;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.model.AnioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    Administrador findByDni(String dni);
    List<Administrador> findByAnioEscolar(AnioEscolar anioEscolar); // Buscar todos los del año activo
    List<Administrador> findByAnioEscolarIn(List<AnioEscolar> anios); //Buscar dentro de todos los años activos
    List<Administrador> findByAnioEscolar_Anio(int anio); // Buscar por número de año
}
