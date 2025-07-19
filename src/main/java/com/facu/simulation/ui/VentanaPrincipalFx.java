package com.facu.simulation.ui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class VentanaPrincipalFx extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carga el archivo FXML que define la interfaz de usuario.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/facu/simulation/ui/VentanaPrincipal.fxml"));
        Parent root = loader.load();

        // Carga la hoja de estilos CSS para un diseño moderno.
        URL css = getClass().getResource("/com/facu/simulation/ui/estilos.css");
        if (css != null) {
            root.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("Advertencia: No se pudo encontrar el archivo de estilos 'estilos.css'.");
        }

        // Configura la escena principal.
        Scene scene = new Scene(root, 1600, 900);

        // Configura el escenario (la ventana principal).
        primaryStage.setTitle("Simulación de Barcos en Bahía - Versión JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1400);
        primaryStage.setMinHeight(800);
        primaryStage.setMaximized(true); // Iniciar maximizada
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Lanza la aplicación JavaFX.
        launch(args);
    }
}