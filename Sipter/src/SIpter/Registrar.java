package SIpter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Registrar {
    private List<String> usarios=new ArrayList<>();
    Scanner ni=new Scanner(System.in);

   

    public void registrarUsuario(){
        System.out.println("Ingrese le nombre de usuario que desea registrar:");
        String nombreUsuario=ni.nextLine();

        boolean existente=false;

        for(String u:usarios){
            if (u.equalsIgnoreCase(nombreUsuario)){
                existente=true;
                break;
            }
        }
        if (existente){
            System.out.println("El nombre de usuario " + nombreUsuario + " no se puede registrar.");
        } else {
            usarios.add(nombreUsuario);
            System.out.println("Usuarios registrado "+nombreUsuario+" exitosamente.");
        }

        }
    public static void main(String[] args) {
        Registrar sistema = new Registrar();
        sistema.registrarUsuario();
    }
}
