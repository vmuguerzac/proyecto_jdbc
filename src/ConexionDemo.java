import java.sql.*;
import java.util.Scanner;

public class ConexionDemo {
    static Scanner scanner = new Scanner(System.in); // Insrtanciar Scanner
    static Connection conexion = null;
    public static void main(String[] args){
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
        try(Connection conn = DriverManager.getConnection(url, usuario, pwd)){
            System.out.println("Conexion OK: " + conn.getCatalog());
            conexion = conn;
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static boolean InsertarLibro(){
        // Crear el objeto libro vacio
        Libro libro = new Libro();
        System.out.println("Agregar Libro: ");
        // Capturar datos del usuario
        System.out.println("Ingrese el ISBN del libro: ");
        libro.setIsbn(scanner.nextLine());
        System.out.println("Ingrese el Titulo del libro: ");
        libro.setTitulo(scanner.nextLine());
        System.out.println("Ingrese el Autor del libro: ");
        libro.setAutor(scanner.nextLine());
        System.out.println("Ingrese el Precio del libro: ");
        libro.setPrecio(scanner.nextDouble());
        System.out.println("Ingrese el Stock del libro: ");
        libro.setStock(scanner.nextInt());
        // Insertar el libro en la DB
        // preparar el query
        String query = 
            "INSERT INTO libros(isbn, titulo, autor, precio, stock) " +
            "VALUES(?, ?, ?, ?, ?)";
        // usar el preparedstatement con try catch con resources
        try(PreparedStatement ps = conexion.prepareStatement(query)){
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getStock());
            // Ejecutar
            int filas = ps.executeUpdate(); // Ejecutar INSERT, UPDATE o DELETE
            System.out.println("(" + filas + ")" + " Filas insertadas");
            if(filas > 0) return true;
        }catch(SQLException ex){
            System.out.println("Hubo un error al agregar el libro: " + ex.getMessage());
        }
        return false;
    }
}
