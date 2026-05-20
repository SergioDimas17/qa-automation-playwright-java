package pages;

import com.microsoft.playwright.Page;

public class CheckboxesPage {
    private Page page;

    // Constructor para heredar la sesión del navegador
    public CheckboxesPage(Page page) {
        this.page = page;
    }

    // Método corregido: sin variables innecesarias y con selector exacto
    public void marcarPrimeraCasilla() {
        page.check("form#checkboxes input:nth-child(1)");
    }
}