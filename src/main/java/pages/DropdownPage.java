package pages;

import com.microsoft.playwright.Page;

public class DropdownPage {
    private Page page;

    // Constructor para compartir la sesión
    public DropdownPage(Page page) {
        this.page = page;
    }

    // Método para elegir una opción usando su atributo 'value'
    public void seleccionarOpcionPorValor(String valor) {
        // COMPLETA ESTA LÍNEA UTILIZANDO EL SELECTOR "#dropdown" Y LA VARIABLE "valor":
        page.selectOption("#dropdown", valor);
    }
}