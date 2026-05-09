import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConexionDemo {
    static Scanner scanner = new Scanner(System.in);
    // DAOS
    static LibroDAO libroDAO = null;
    
    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/pageturner";
        String usuario = "root";
        String pwd = "victor18";
        Libro libro = null;
        String isbn;

        int opc = -1;
        do {
            MostrarMenu();
            System.out.print("Seleccione una opcion del menu: ");
            opc = scanner.nextInt();
            scanner.nextLine(); // consume \n
            switch (opc) {
                case 1:
                    EstablecerConexion(url, usuario, pwd);
                    break;
                case 2: // Create
                    libro = CapturarDatosUsuario();
                    libroDAO.InsertarLibro(libro);
                    break;
                case 3: // Read
                    libroDAO.ListarLibros();
                    break;
                case 4: // Update
                    isbn = CapturarISBN();
                    libro = CapturarDatosUsuario();
                    libroDAO.ActualizarLibro(isbn, libro);
                    break;
                case 5:
                    isbn = CapturarISBN();
                    libroDAO.EliminarLibro(isbn);
                    break;
                default:
                    break;
            }
        }while(opc != -1);
        scanner.close();
    }

    public static void MostrarMenu(){
        System.out.println("=============== Sistema de Gestion de Libros =============");
        System.out.println("Menu: ");
        System.out.println("1. Establecer Conexion con BD");
        System.out.println("2. Insertar Libro ");
        System.out.println("3. Leer Libros ");
        System.out.println("4. Actualizar Libro");
        System.out.println("5. Eliminar Libro");
        System.out.println("-1. Salir del Sistema ");
    }

    public static Libro CapturarDatosUsuario(){
        Libro libroACrear = new Libro();
        // Capturar datos del usuario
        System.out.println("Ingrese el ISBN del libro: ");
        libroACrear.setIsbn(scanner.nextLine());
        System.out.println("Ingrese el Titulo del libro: ");
        libroACrear.setTitulo(scanner.nextLine());
        System.out.println("Ingrese el Autor del libro: ");
        libroACrear.setAutor(scanner.nextLine());
        System.out.println("Ingrese el Precio del libro: ");
        libroACrear.setPrecio(scanner.nextDouble());
        scanner.nextLine(); // Limpiar buffer
        System.out.println("Ingrese el Stock del libro: ");
        libroACrear.setStock(scanner.nextInt());
        scanner.nextLine(); // Limpiar buffer
        return libroACrear;
    }

    public static String CapturarISBN(){
        libroDAO.ListarLibros(); // Listar para que el usuario vea que informacion actualizar
        System.out.println("Ingrese isbn del libro a modificar / eliminar");
        String isbn = scanner.nextLine();
        return isbn;
    }

    public static void EstablecerConexion(String url, String usuario, String pwd){
        try{
            Connection conn = DriverManager.getConnection(url, usuario, pwd);
            System.out.println("Conexion OK: " + conn.getCatalog());
            libroDAO = new LibroDAO(conn);
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }  
}
