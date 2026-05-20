package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class IframePage {
    private Page page;

    public IframePage(Page page) {
        this.page = page;
    }

    public void escribirEnElEditor(String texto) {
        // 1. Capturamos el localizador del cuerpo interno
        Locator editor = page.frameLocator("#mce_0_ifr").locator("#tinymce");

        // 2. Ejecutamos el bypass de JavaScript para romper el modo lectura
        editor.evaluate("el => el.setAttribute('contenteditable', 'true')");

        // 3. Inyectamos el texto de forma limpia y directa
        editor.fill(texto);
    }
}