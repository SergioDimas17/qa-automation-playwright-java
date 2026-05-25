// Define la carpeta (paquete) donde se encuentra resguardado este archivo de prueba [cite: 149]
package tests;

// IMPORTACIONES DE PLAYWRIGHT: Traemos las herramientas nativas para controlar el navegador [cite: 48, 149]

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.SecurePage;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

// Declaración de tu clase de prueba pública (el contenedor de todo el ejercicio) [cite: 149]
public class IniciosDeSesionTest { // [cite: 149]

    // VARIABLES GLOBALES (DE INSTANCIA): Se declaran aquí arriba para que todos los métodos puedan usarlas [cite: 63, 145]
    private Playwright playwright; // Almacena el motor principal de Playwright [cite: 149]
    private Browser browser;       // Almacena el navegador que se mantendrá abierto durante el test [cite: 149]
    private Page page;             // Almacena la pestaña interactiva del navegador [cite: 149]
    private boolean testExitoso = false; // Nuestra "Bandera lógica" para saber si la prueba falló o pasó [cite: 65, 149]

    // BLOQUE DE PREPARACIÓN: Se ejecuta automáticamente ANTES de que empiece la prueba [cite: 56, 151]
    @BeforeEach // [cite: 48]
    public void setUp() { // [cite: 64, 155]

        // Reiniciamos la bandera en 'false' al inicio de cada test para asegurar limpieza lógica [cite: 65, 156]
        testExitoso = false;

        // Encendemos el servidor/motor central de Playwright en tu computadora [cite: 157]
        playwright = Playwright.create();

        // RED DE SEGURIDAD (TRY-CATCH): Intentamos abrir el navegador principal sin que el programa muera [cite: 73, 157]
        try {
            // Imprime un aviso informativo en la consola de IntelliJ [cite: 162]
            System.out.println("Intentando iniciar la prueba en Firefox...");

            // Si la variable "CI" existe, corre oculto (true); si no, corre visual (false)
            boolean modoOculto = System.getenv("CI") != null;

            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions()
                    .setHeadless(modoOculto)
            );
        }
        // PLAN DE RESPALDO (CATCH): Si tu computadora no tiene Firefox o este falla, se activa este bloque [cite: 73, 162]
        catch (Exception e) {
            // Informa en consola que se activó la contingencia [cite: 162]
            System.out.println("¡Ups! Firefox falló. Iniciando Chromium de respaldo...");

            // Lanza el navegador Chromium (base de Chrome) para no detener tu automatización [cite: 162]
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
            );
        }

        // Una vez elegido el navegador con éxito, abrimos una pestaña nueva en blanco [cite: 159, 168]
        page = browser.newPage();
    }

    // BLOQUE DE ACCIÓN: Aquí se describe el comportamiento real de la prueba de negocio [cite: 58, 174]
    @Test // [cite: 48]
    public void loginExitoso() { // [cite: 176]

        // VARIABLES LOCALES: Datos fijos que usará este test específico para realizar las acciones [cite: 190]
        String laUrl = "https://the-internet.herokuapp.com/login"; // Dirección web de pruebas [cite: 190]
        String elUsuario = "tomsmith";                             // Credencial de usuario correcta [cite: 190]
        String laContrasena = "SuperSecretPassword!";              // Contraseña correcta del sitio web [cite: 190]

        // Le ordenamos a la pestaña del navegador viajar a la dirección URL guardada arriba [cite: 177]
        page.navigate(laUrl);

        // INSTANCIACIÓN DE OBJETOS: Creamos las páginas reales pasándoles nuestra pestaña activa [cite: 87, 178]
        LoginPage loginPage = new LoginPage(page);   // Construye la pantalla de login con sus herramientas de escritura [cite: 48, 178]
        SecurePage securePage = new SecurePage(page); // Construye la pantalla segura con sus herramientas de lectura [cite: 48, 178]

        // INTERACCIÓN CON LA INTERFAZ: Usamos las acciones empaquetadas dentro de tus Page Objects [cite: 179]
        loginPage.ingresarUsuario(elUsuario);    // Busca el campo de usuario y escribe el texto asignado [cite: 48, 179]
        loginPage.ingresarPassword(laContrasena); // Busca el campo de contraseña y escribe el texto secreto [cite: 48, 179]
        loginPage.hacerClicEnIngresar();         // Ejecuta el clic sobre el botón de envío del formulario [cite: 48, 179]

        // ASERCIÓN (VALIDACIÓN): Verificamos que la alerta de la página contenga el mensaje de éxito esperado [cite: 180, 191]
        // Si esta línea falla, el test se interrumpe inmediatamente y viaja directo al @AfterEach [cite: 67]
        assertThat(securePage.obtenerAlerta()).containsText("You logged into a secure area!"); // [cite: 192]

        // RECOMPENSA LÓGICA: Si la aserción de arriba pasó, esta línea se ejecuta y confirma el éxito [cite: 68, 181]
        testExitoso = true;
    }

    // BLOQUE DE LIMPIEZA: Se ejecuta obligatoriamente al final, haya pasado o fallado el test [cite: 59, 193]
    @AfterEach // [cite: 59]
    public void tearDown() { // [cite: 64, 196]

        // EVALUACIÓN DE EVIDENCIAS: Si la bandera sigue en 'false' (usando el operador de negación '!') [cite: 70, 206, 207]
        if (!testExitoso) { // Significa: "Si NO fue exitoso..." [cite: 207]
            // Imprime una advertencia de error en la consola del sistema [cite: 201]
            System.out.println("¡Alerta! El test falló. Capturando evidencia en la carpeta evidencias/...");

            // Le ordena a Playwright tomar una foto de la pantalla actual y guardarla en la carpeta designada [cite: 202]
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("evidencias/error_login.png")));
        }

        // CIERRE SEGURO DE RECURSOS: Evitamos dejar procesos colgados consumiendo memoria RAM en tu PC [cite: 60, 198]
        if (browser != null)
            browser.close();       // Si el navegador se llegó a abrir, ciérralo por completo [cite: 60, 198]
        if (playwright != null)
            playwright.close(); // Si el motor de Playwright está activo, apágalo limpiamente [cite: 60, 198]
    }
}