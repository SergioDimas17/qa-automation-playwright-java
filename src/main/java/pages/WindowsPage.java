package pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class WindowsPage {
    private Page page;

    // Constructor estándar
    public WindowsPage(Page page) {
        this.page = page;
    }

    // Método avanzado para capturar y retornar la nueva pestaña abierta
    public Page abrirNuevaPestaña(BrowserContext contexto) {
        // Ordenamos al contexto esperar que una página nazca a raíz del clic interno
        Page nuevaPestaña = contexto.waitForPage(() -> {
            page.click("text=Click Here");
        });

        // Retornamos la nueva pestaña para que el test pueda hacerle aserciones
        return nuevaPestaña;
    }
}