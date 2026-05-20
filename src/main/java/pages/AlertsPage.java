package pages;

import com.microsoft.playwright.Page;

public class AlertsPage {
    private Page page;

    public AlertsPage(Page page) {
        this.page = page;
    }

    // Método 1: Acepta la alerta simple
    public void activarYAceptarAlerta() {
        page.onDialog(dialogo -> dialogo.accept());
        page.click("button[onclick='jsAlert()']");
    }

    // Método 2: Cancela la ventana de confirmación (¡Tu creación!)
    public void activarYCancelarConfirmacion() {
        page.onDialog(dialogo -> dialogo.dismiss());
        page.click("button[onclick='jsConfirm()']");
    }
}