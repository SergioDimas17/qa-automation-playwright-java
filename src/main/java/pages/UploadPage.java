package pages;

import com.microsoft.playwright.Page;

import java.nio.file.Paths;

public class UploadPage {
    private Page page;

    // Constructor para heredar la sesión activa
    public UploadPage(Page page) {
        this.page = page;
    }

    // Método completo para automatizar la carga de archivos
    public void subirDocumento(String nombreArchivo) {
        page.setInputFiles("#file-upload", Paths.get(nombreArchivo));
        page.click("#file-submit");
    }
}