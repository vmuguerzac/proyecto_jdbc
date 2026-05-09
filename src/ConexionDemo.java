import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConexionDemo {
    static Scanner scanner = new Scanner(System.in);
     // Insrtanciar Scanner
    static Connection conexion = null;
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
                    VerificarConexion(url, usuario, pwd);
                    break;
                case 2: // Create
                    libro = CapturarDatosUsuario();
                    InsertarLibro(libro);
                    break;
                case 3: // Read
                    ListarLibros();
                    break;
                case 4: // Update
                    isbn = CapturarISBN();
                    libro = CapturarDatosUsuario();
                    ActualizarLibro(isbn, libro);
                    break;
                case 5:
                    isbn = CapturarISBN();
                    EliminarLibro(isbn);
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
        System.out.println("1. Verificar Conexion");
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
        ListarLibros(); // Listar para que el usuario vea que informacion actualizar
        System.out.println("Ingrese isbn del libro a modificar");
        String isbn = scanner.nextLine();
        return isbn;
    }

    public static void VerificarConexion(String url, String usuario, String pwd){
        try{
            Connection conn = DriverManager.getConnection(url, usuario, pwd);
            System.out.println("Conexion OK: " + conn.getCatalog());
            conexion = conn;
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static boolean InsertarLibro(Libro libro){
        // Insertar el libro en la DB
        System.out.println("\n====== Agregar Libro: =======");
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
            System.out.println("(" + filas + ")" + " filas insertadas");
            if(filas > 0){
                return true;
            }
        }catch(SQLException ex){
            System.out.println("Hubo un error al agregar el libro: " + ex.getMessage());
        }
        return false;
    }

    public static void ListarLibros(){
        List<Libro> ListaLibros = new ArrayList<>(); // Almacenar los libros obtenidos de la BD
        String query =  "SELECT * FROM libros";

        try(PreparedStatement ps = conexion.prepareStatement(query)){
            // Ejecutar query
            ResultSet rs = ps.executeQuery();
            // Leer resultados
            while(rs.next()){
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setPrecio(rs.getDouble("precio"));
                libro.setStock(rs.getInt("stock"));
                ListaLibros.add(libro);
            }
            // Mostrar el output como tabla
            String formato = "| %-13s | %-40s | %-20s | %-6s | %-6s |%n";
            System.out.println("\nLibros Registrados en el Sistema");
            System.out.println("-".repeat(100));
            System.out.printf(formato, "ISBN", "TITULO", "AUTOR", "PRECIO", "STOCK");
            System.out.println("-".repeat(100));
            // Mostramos los resultado
            for(Libro l : ListaLibros){
                System.out.printf(formato, l.getIsbn(), l.getTitulo(), l.getAutor(), l.getPrecio(), l.getStock());
            }
            System.out.println("-".repeat(100));
        }catch(SQLException ex){
            System.out.println("Ha ocurrido un error al leer los libros. " + ex.getMessage());
        }
    }

    public static boolean ActualizarLibro(String isbn, Libro libro){
        System.out.println("\n Actualizar Informacion del Libro: ");
        int filas = -1;
        String query = 
            "UPDATE libros SET titulo = ?, autor=?, precio =?, stock=? " + 
            "WHERE isbn = ?";
        try(PreparedStatement ps = conexion.prepareStatement(query)){
            // Colocar los argumentos reemplazamos ? x valor
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setDouble(3, libro.getPrecio());
            ps.setInt(4, libro.getStock());
            ps.setString(5, isbn);
            filas = ps.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Ha ocurrido un error al actualizar el libro. " + ex.getMessage());
        }
        return filas > 0;
    }

    public static boolean EliminarLibro(String isbn){
        System.out.println("\n Eliminar Libro: ");
        int filas = -1;
        String query = "DELETE FROM libros WHERE isbn = ?";
        try(PreparedStatement ps = conexion.prepareStatement(query)){
            ps.setString(1, isbn);
            filas = ps.executeUpdate();
            if(filas == 0){
                System.out.println("ISBN no encontrado: " + isbn);
            }
        }catch(SQLException ex){
            System.out.println("Ha ocurrido un error al eliminar el libro. " + ex.getMessage());
        }
        return filas > 0;
    }
}
