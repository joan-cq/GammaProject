package com.gamma.backend.service;

import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.Curso;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdfExportService {

    // RF-11: exporta las notas de un grado/curso/bimestre en un PDF simple de una página
    public byte[] generarReporteNotas(String nombreGrado, Curso curso, Bimestre bimestre,
                                       List<Map<String, Object>> alumnosConNotas) throws IOException {

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            float margin = 50;
            float y = page.getMediaBox().getHeight() - margin;
            float rowHeight = 20;

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(fontBold, 16);
                content.newLineAtOffset(margin, y);
                content.showText("Colegio Gamma - Reporte de Notas");
                content.endText();
                y -= 25;

                content.beginText();
                content.setFont(font, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Grado: " + nombreGrado + "   Curso: " + curso.getNombre() + "   Bimestre: " + bimestre.getNombre());
                content.endText();
                y -= 30;

                content.beginText();
                content.setFont(fontBold, 11);
                content.newLineAtOffset(margin, y);
                content.showText("DNI");
                content.newLineAtOffset(90, 0);
                content.showText("Apellidos y Nombres");
                content.newLineAtOffset(280, 0);
                content.showText("Nota");
                content.endText();
                y -= rowHeight;

                content.moveTo(margin, y + 12);
                content.lineTo(page.getMediaBox().getWidth() - margin, y + 12);
                content.stroke();

                for (Map<String, Object> alumno : alumnosConNotas) {
                    if (y < margin) {
                        break; // Reporte de una sola página; la paginación queda para una futura versión
                    }
                    content.beginText();
                    content.setFont(font, 10);
                    content.newLineAtOffset(margin, y);
                    content.showText(String.valueOf(alumno.get("dni")));
                    content.newLineAtOffset(90, 0);
                    content.showText(alumno.get("apellido") + ", " + alumno.get("nombre"));
                    content.newLineAtOffset(280, 0);
                    Object nota = alumno.get("nota");
                    content.showText(nota != null ? String.valueOf(nota) : "SIN NOTA");
                    content.endText();
                    y -= rowHeight;
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }
}
