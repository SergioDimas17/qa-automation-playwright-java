package tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Deshabilitado temporalmente por bloqueo 401 de red en la nube")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTest {

    private static Playwright playwright;
    private static APIRequestContext peticion;

    @BeforeAll
    public static void inicializarConsola() {
        playwright = Playwright.create();

        java.util.Map<String, String> cabeceras = new java.util.HashMap<>();
        cabeceras.put("Accept", "application/json");
        cabeceras.put("Content-Type", "application/json"); // 👈 Cabecera crucial para el método POST
        cabeceras.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

        peticion = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://reqres.in")
                .setExtraHTTPHeaders(cabeceras) // Inyectamos las cabeceras de autorización/identidad
        );
    }

    @Test
    @Order(1)
    public void consultarUsuarioNumeroDos() {
        APIResponse respuesta = peticion.get("/api/users/2");
        Assertions.assertEquals(200, respuesta.status());
        System.out.println("¡Respuesta de la API recibida con éxito (200 OK)!");
    }

    @Test
    @Order(2)
    public void validarUsuarioInexistenteDevuelve404() {
        APIResponse respuesta = peticion.get("/api/users/9999");
        Assertions.assertEquals(404, respuesta.status());
        System.out.println("¡Validación de usuario inexistente (404 Not Found) correcta!");
    }

    @Test
    @Order(3)
    public void crearNuevoUsuarioExitoso() {
        java.util.Map<String, String> datosUsuario = new java.util.HashMap<>();
        datosUsuario.put("name", "Sergio");
        datosUsuario.put("job", "QA Automation Leader");

        APIResponse respuesta = peticion.post("/api/users",
                com.microsoft.playwright.options.RequestOptions.create().setData(datosUsuario)
        );

        // Validamos código de creación exitosa (201 Created)
        Assertions.assertEquals(201, respuesta.status());

        // Extraemos y validamos el JSON devuelto usando tu lógica perfecta
        String cuerpoJsonString = respuesta.text();
        assertThat(cuerpoJsonString).contains("QA Automation Leader");

        System.out.println("¡Usuario creado en el backend! Respuesta: " + cuerpoJsonString);
    }

    @AfterAll
    public static void cerrarConsola() {
        if (peticion != null) peticion.dispose();
        if (playwright != null) playwright.close();
    }
}