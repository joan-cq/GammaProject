package com.gamma.backend.controller;

import com.gamma.backend.model.Grado;
import com.gamma.backend.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grado")
public class GradoController {

    @Autowired
    private GradoRepository gradoRepository;

    // GET /grado/list
    @GetMapping("/list")
    public List<Grado> listarGrados() {
        return gradoRepository.findAll();
    }

    // POST /grado/add
    @PostMapping("/add")
    public Grado agregarGrado(@RequestBody Grado grado) {
        return gradoRepository.save(grado);
    }

    // PUT /grado/update
    @PutMapping("/update")
    public Grado actualizarGrado(@RequestBody Grado grado) {
        return gradoRepository.save(grado);
    }

    // DELETE /grado/delete/{codigoGrado}
    @DeleteMapping("/delete/{codigoGrado}")
    public void eliminarGrado(@PathVariable String codigoGrado) {
        gradoRepository.deleteById(codigoGrado);
    }
}
