package pages;

import com.microsoft.playwright.Page;

public class SecurePage {
    private Page page;

    public SecurePage(Page page) {
        this.page = page;
    }

    // Método para obtener el localizador del mensaje de alerta
    public com.microsoft.playwright.Locator obtenerAlerta() {
        return page.locator("#flash");
    }
}