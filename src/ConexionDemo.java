import java.sql.*;
import java.util.Scanner;

public class ConexionDemo {
    
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); // Insrtanciar Scanner
        String url = "jdbc:mysql://localhost:3306/pageturner";
        String usuario = "root";
        String pwd = "victor18";
        int opc = -1;
        MostrarMenu();
        System.out.print("Seleccione una opcion del menu: ");
        opc = scanner.nextInt();
        switch (opc) {
            case 1:
                VerificarConexion(url, usuario, pwd);
                break;
            case 2:
                InsertarLibro();
            default:
                break;
        }
    }

    public static void MostrarMenu(){
        System.out.println("=============== Sistema de Gestion de Libros =============");
        System.out.println("Menu: ");
        System.out.println("1. Verificar Conexion");
        System.out.println("2. Insertar Libro ");
    }

    public static void VerificarConexion(String url, String usuario, String pwd){
        try(Connection conexion = DriverManager.getConnection(url, usuario, pwd)){
            System.out.println("Conexion OK: " + conexion.getCatalog());
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static boolean InsertarLibro(){
        return false;
    }
}
