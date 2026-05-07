public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private double precio;
    private int stock;

    public Libro(){

    }

    public Libro(String _isbn, String _titulo, String _autor, double _precio, int stock){
        setIsbn(_isbn);
        setTitulo(_titulo);
        setAutor(_autor);
        setPrecio(_precio);
        setStock(stock);
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        // Validar que el precio sea mayor que 0
        if(precio <= 0){
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        this.precio = precio;
    }
    public int getStock() {
        // Validar
        if(stock <= 0){
            throw new IllegalArgumentException("El stock debe ser mayor 0");
        }
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    
}
