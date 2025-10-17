package Cliente;

public abstract class Usuario {
	protected String login;
	private String nombre; 
	private double saldo;
	protected String password;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	
	public Usuario(String login, String password, String nombre, double saldo) {
		super();
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.saldo = saldo;
		
	}
	
	
	public boolean autenticar(String Login, String Password) {
		
		return (Login.equals(login) && Password.equals(password));
		
		
	}
	
	public double consultarSaldo() {
		return saldo;
	}
	
	public void acreditarSaldo(double monto) {
		saldo+=monto;
		
	}
	
	public void usarSaldo(double monto){
		saldo-=monto;
		
	}
	
	
}
