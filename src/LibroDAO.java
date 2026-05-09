import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private Connection conexion;

    // Constructor
    public LibroDAO(Connection conn){
        this.conexion = conn;
    }

    // Insertar
    public boolean InsertarLibro(Libro libro){
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

    public void ListarLibros(){
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

    public boolean ActualizarLibro(String isbn, Libro libro){
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

    public boolean EliminarLibro(String isbn){
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

    public Libro buscarPorISBN(String isbn){
        Libro libroBuscar = null;
        System.out.println("\n Buscar Libro por ISBN: ");
        
        String query = "SELECT * FROM libros WHERE isbn = ?";

        try(PreparedStatement ps = conexion.prepareStatement(query)){
            ps.setString(1, isbn);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return libroBuscar = new Libro(
                        rs.getString("isbn"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                }
            }catch(SQLException ex){
                System.out.println("Error al momento de leer la informacion de libro de la BD." + ex.getMessage());    
            }

        }catch(SQLException ex){
            System.out.println("Error al momento de executar el query de libro." + ex.getMessage());                    
        }
        return libroBuscar;
    }

}
