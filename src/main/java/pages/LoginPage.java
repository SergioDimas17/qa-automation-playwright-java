package pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private Page page;

    // El constructor que recibe la página
    public LoginPage(Page page) {
        this.page = page;
    }

    // Acción 1: Escribir Usuario
    public void ingresarUsuario(String textoUsuario) {
        page.locator("#username").fill(textoUsuario);
    }

    // Acción 2: Escribir Contraseña
    public void ingresarPassword(String textoPassword) {
        page.locator("#password").fill(textoPassword);
    }

    // Acción 3: Hacer Clic en el botón real de la página de pruebas
    public void hacerClicEnIngresar() {
        page.locator("button[type='submit']").click();
    }
}