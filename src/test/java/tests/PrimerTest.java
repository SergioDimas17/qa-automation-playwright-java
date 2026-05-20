package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pages.AlertsPage;
import pages.CheckboxesPage;
import pages.DropdownPage;
import pages.IframePage;
import pages.LoginPage;
import pages.SecurePage;
import pages.UploadPage;
import pages.WindowsPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrimerTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext contexto;
    private Page page;
    private Properties propiedades;
    private boolean testExitoso = false;

    public void cargarConfiguraciones() {
        propiedades = new Properties();
        try {
            FileInputStream archivo = new FileInputStream("src/test/resources/config.properties");
            propiedades.load(archivo);
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de configuración.");
        }
    }

    @BeforeEach
    public void setUp() {
        cargarConfiguraciones();
        testExitoso = false;

        String modoHeadlessTexto = propiedades.getProperty("headless").trim();
        boolean modoHeadlessBoolean = Boolean.parseBoolean(modoHeadlessTexto);

        playwright = Playwright.create();

        try {
            System.out.println("Intentando iniciar la prueba en Firefox...");
            browser = playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(modoHeadlessBoolean).setSlowMo(1000)
            );
        } catch (Exception e) {
            System.out.println("¡Ups! Firefox falló. Iniciando Chromium de respaldo...");
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(modoHeadlessBoolean).setSlowMo(1000)
            );
        }

        contexto = browser.newContext(
                new Browser.NewContextOptions().setRecordVideoDir(Paths.get("evidencias/videos/"))
        );

        page = contexto.newPage();
    }

    @Test
    @Order(1)
    public void miPrimeraPruebaConJUnit() {
        String laUrl = propiedades.getProperty("url");
        String elUsuario = propiedades.getProperty("usuario");
        String laContrasena = propiedades.getProperty("password");

        page.navigate(laUrl);

        LoginPage loginPage = new LoginPage(page);
        SecurePage securePage = new SecurePage(page);

        loginPage.ingresarUsuario(elUsuario);
        loginPage.ingresarPassword(laContrasena);
        loginPage.hacerClicEnIngresar();

        assertThat(securePage.obtenerAlerta()).containsText("You logged into a secure area!");

        testExitoso = true;
    }

    @Test
    @Order(2)
    public void miSegundaPruebaDropdown() {
        String laUrlDropdown = propiedades.getProperty("dropdowns");
        page.navigate(laUrlDropdown);

        DropdownPage dropdownPage = new DropdownPage(page);
        dropdownPage.seleccionarOpcionPorValor("1");

        assertThat(page.locator("#dropdown")).hasValue("1");

        testExitoso = true;
    }

    @Test
    @Order(3)
    public void miTerceraPruebaCheckboxes() {
        String laUrlCasillas = propiedades.getProperty("casillas");
        page.navigate(laUrlCasillas);

        CheckboxesPage checkboxesPage = new CheckboxesPage(page);
        checkboxesPage.marcarPrimeraCasilla();

        assertThat(page.locator("form#checkboxes input:nth-child(1)")).isChecked();

        testExitoso = true;
    }

    @Test
    @Order(4)
    public void miCuartaPruebaAlertas() {
        String laUrlAlertas = propiedades.getProperty("alertas");
        page.navigate(laUrlAlertas, new Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));

        AlertsPage alertsPage = new AlertsPage(page);
        alertsPage.activarYAceptarAlerta();

        assertThat(page.locator("p#result")).containsText("You successfully clicked an alert");

        testExitoso = true;
    }

    @Test
    @Order(5)
    public void miQuintaPruebaConfirmacionCancelada() {
        String laUrlAlertas = propiedades.getProperty("alertas");
        page.navigate(laUrlAlertas, new Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));

        AlertsPage alertsPage = new AlertsPage(page);
        alertsPage.activarYCancelarConfirmacion();

        assertThat(page.locator("p#result")).containsText("You clicked: Cancel");

        testExitoso = true;
    }

    @Test
    @Order(6)
    public void miSextaPruebaCargaDeArchivo() {
        String laUrlCarga = propiedades.getProperty("carga");
        page.navigate(laUrlCarga);

        UploadPage uploadPage = new UploadPage(page);
        uploadPage.subirDocumento("documento_prueba.txt");

        assertThat(page.locator("h3")).containsText("File Uploaded!");

        testExitoso = true;
    }

    @Test
    @Order(7)
    public void miSeptimaPruebaIframe() {
        String laUrlMarcos = propiedades.getProperty("marcos");
        page.navigate(laUrlMarcos);

        IframePage iframePage = new IframePage(page);

        // Inyectamos texto simulando la pulsación secuencial de teclas
        iframePage.escribirEnElEditor("Hola, estoy testeando un iFrame de forma profesional");

        // Validamos usando la aserción Web-First encadenada
        assertThat(page.frameLocator("#mce_0_ifr").locator("#tinymce")).containsText("Hola, estoy testeando un iFrame de forma profesional");

        testExitoso = true;
    }

    @Test
    @Order(8)
    public void miOctavaPruebaMultiplesVentanas() {
        String laUrlVentanas = propiedades.getProperty("ventanas").trim();
        page.navigate(laUrlVentanas);

        WindowsPage windowsPage = new WindowsPage(page);

        // Tu línea perfecta integrada pasando el contexto
        Page nuevaPestaña = windowsPage.abrirNuevaPestaña(contexto);

        // Validamos la aserción en la pestaña emergente
        assertThat(nuevaPestaña.locator("h3")).containsText("New Window");

        testExitoso = true;
    }

    @AfterEach
    public void tearDown() {
        if (testExitoso != true) {
            long tiempoUnico = System.currentTimeMillis();
            System.out.println("¡Alerta! El test falló. Capturando evidencia única...");
            page.screenshot(new Page.ScreenshotOptions().setPath(
                    Paths.get("evidencias/error_login_" + tiempoUnico + ".png")
            ));
        }

        if (contexto != null) contexto.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}