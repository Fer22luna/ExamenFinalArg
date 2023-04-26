package conexion.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ExamenFinal {
    Connection conexion = null;
    Statement sentencia = null; 
    
   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExamenFinal m = new ExamenFinal();

        m.conectar();    
        boolean salir = false;
        do {
            switch (menuPrin()) {
            	case 1:
            		m.consultarTablaPacientes();  
            		break;
            	case 2:
            		m.agregarTablaPacientes();    
            		break;
            	case 3:
            		m.eliminarRegistoPacientes();
            		break;	
            	case 4:
            		m.consultarTablaHistorial();  
            		break;
            	case 5:
            		m.consultarTablaMedicos();  
            		break;
            	case 6:
            		m.agregarTablaMedicos();    
            		break;
            	case 7:
            		m.consultarHistorialPaciente();  
            		break;
                case 0:
                    System.out.println("Gracias por usar nuestro sistema. Saludos !");
                    m.desconectar();               
                    salir = true;
                    break;
                default:
                    System.out.println("Opción incorrecta");
                    break;
            }
        } while (!salir);

    } 
//-------------------------------------------------------------------------------
    
    private static int menuPrin() {

        Scanner sc = new Scanner(System.in);

        System.out.println("--------------------------------");
        System.out.println("Conexión de base de datos MySQL");
        System.out.println("--------------------------------");
        System.out.println("-1. Mostrar el contenido del registro de un paciente"); 
        System.out.println("-2. Ingresar  un nuevo paciente "); 
        System.out.println("-3  Eliminar un Paciente de la base de datos");
        System.out.println("-4  Acceder a un  historial clinico por numero de legajo ");
        System.out.println("-5  Mostrar medicos en la lista de medicos");
        System.out.println("-6  Agregar medico a la base de medicos");       
        System.out.println("-7  Acceder a un historial medico por dni del paciente");
        System.out.println("0.SALIR");
        System.out.println("\n Por favor, escoja una opción.");
        System.out.println("--------------------------------");

        return sc.nextInt(); 

    }

//-----------------------------------------------------------------------------------------------
    

    public void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
             conexion = DriverManager.getConnection("jdbc:mysql://localhost/Clinica", "root", "password");
            System.out.println("**************************************");
            System.out.println(" * CONEXIÓN REALIZADA CORRECTAMENTE * ");
            System.out.println("**************************************");
        } catch (Exception e) {
            System.out.println("*****************************************");
            System.out.println(" * NO SE HA PODIDO REALIZAR LA CONEXIÓN * ");
            System.out.println("******************************************");
        }

    }
//-----------------------------------------------------------------------------------------------


    private void desconectar() {
        try {
            conexion.close(); 
            System.out.println("\n************************************************************\n");
            System.out.println("La conexion a la base de datos se ha terminado");
            System.out.println("\n************************************************************");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
   
//----------------------------------------------------------------------------------------------   

        private void consultarTablaPacientes() {
        ResultSet r = buscar("select PacDni,PacNom,PacApe from Pacientes");  
        try {
            System.out.println("REGISTROS DE LA TABLA PACIENTES");

            while (r.next()) {
              
                System.out.println(r.getInt("PacDni") + " | " + r.getString("PacNom") + " | " + r.getString("PacApe"));// + " | " + r.getInt("EmpDep"));
            }
        } catch (SQLException ex) {
        }

    }
          
    ResultSet buscar(String sql) {
        try {
            sentencia = conexion.createStatement(); 
            return sentencia.executeQuery(sql);  
        } catch (SQLException ex) {
            Logger.getLogger(ExamenFinal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
     
    ////////////////////////////////////////////////////////////////////////////
    
        private void agregarTablaPacientes() {
        	String usuario="root";
            String password="password";
            Scanner sc = new Scanner(System.in);
            
            System.out.println("Escriba el DNI del Paciente: ");
            int PacDni  = sc.nextInt(); 
            
            System.out.println("Ingrese el nombre del Paciente:  ");
            String PacNom = sc.next(); 
            
            System.out.println("Ingrese el apellido del Paciente:  ");
            String PacApe = sc.next(); 
                    
            String sql = "insert into Pacientes (PacDni,PacNom,PacApe) values ('"+PacDni+"','"+PacNom+"','"+PacApe+"')";
            Connection con=null;
                try{
            
           con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Clinica", usuario, password);  
           Statement sentencia = con.createStatement();    
                                                  

           int m = sentencia.executeUpdate(sql); 
             if (m == 1)
                 System.out.println("Se realizo correctamente la insercion : "+sql);
             else
                 System.out.println("fallo la insercion");
          con.close();  
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
    }
   
 //-----------------------------------------------------------------------------------------------------
        
        
            private void eliminarRegistoPacientes() {
            	String usuario="root";
                String password="password";
                Scanner sc = new Scanner(System.in);

                System.out.println("Escriba el DNI del Paciente a eliminar:...");
                int PacDni  = sc.nextInt(); 
                
                String sql ="DELETE FROM Pacientes WHERE PacDni = '"+PacDni+"'";
                Connection con=null;
                
                try {
                	  
                	con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Clinica", usuario, password);  
                    Statement sentencia = conexion.createStatement();
                    sentencia.execute(sql);   
                    System.out.println("El registro se elimino!!");
                } catch (Exception e) {  
                  e.printStackTrace();
                  System.out.println("Error en el borrado del registro!!");
                }
              }
    


/////////////////////////////////////////////////////////////////////////////////////////

private void consultarTablaHistorial() {
    
    ResultSet r = buscarHC("select HcNum,HcFec,HcDiag,HcPacDni,HcMedMat from HistorialClinico");  
    try {
        System.out.println("REGISTROS DE LA TABLA DEL HISTORIAL CLINICO");

        while (r.next()) {
           
            System.out.println(r.getInt("HcNum") + " | " + r.getDate("HcFec") + " | "+ r.getString("HcDiag") + " | " + r.getInt("HcPacDni")+ " | " + r.getInt("HcMedMat") + " | ");
        }
    } catch (SQLException ex) {
    
    }

}
      

ResultSet buscarHC(String sql) {
    try {
        sentencia = conexion.createStatement(); 
        
        return sentencia.executeQuery(sql); 
    } catch (SQLException ex) {
        Logger.getLogger(ExamenFinal.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;

}

///////////////////////////////////////////////////////////////////////
private void consultarTablaMedicos() {
    ResultSet r = buscarM("select MedMat,MedNom,MedApe from Medicos ");      
    try {
        System.out.println("REGISTROS DE LA TABLA DE MEDICOS EN LA CLINICA");

        while (r.next()) {
            System.out.println(r.getInt("MedMat") + " | "+ r.getString("MedNom") + " " + r.getString("MedApe") + " | ");
        }
    } catch (SQLException ex) {
    }

}
      
ResultSet buscarM(String sql) {
    try {
        sentencia = conexion.createStatement(); 
        return sentencia.executeQuery(sql);  
    } catch (SQLException ex) {
        Logger.getLogger(ExamenFinal.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;

}
/////////////////////////////////////////////////////////////////////////////////7

private void agregarTablaMedicos() {
	
	String usuario="root";
    String password="password";
    Scanner sc = new Scanner(System.in);
    
    System.out.println("Escriba el numero de Matricula del Medico: ");
    int MedMat  = sc.nextInt(); 
    
    System.out.println("Ingrese el nombre del Medico:  ");
    String MedNom = sc.next(); 
    
    System.out.println("Ingrese el apellido del Medico:  ");
    String MedApe = sc.next(); 
            
    String sql = "insert into Medicos (MedMat,MedNom,MedApe) values ('"+MedMat+"','"+MedNom+"','"+MedApe+"')";
    Connection con=null;
        try{
   con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Clinica", usuario, password);  
   Statement sentencia = con.createStatement();    

   int m = sentencia.executeUpdate(sql); 
     if (m == 1)
         System.out.println("Se realizo correctamente la insercion : "+sql);
     else
         System.out.println("fallo la insercion");
  con.close();  
}
catch(Exception e)
{
   e.printStackTrace();
}
}
///////////////////////////////////////////////////////////////////////////////////////////////
private void consultarHistorialPaciente() {
    Scanner sc = new Scanner(System.in); 
    System.out.println("Ingrese el DNI del Paciente para buscar en el Historial Clinico:");
    int dni = sc.nextInt(); 

    String sql = "SELECT HcNum, HcFec, HcDiag, HcPacDni, HcMedMat FROM HistorialClinico WHERE HcPacDni = " + dni;
    ResultSet r = buscarHC(sql); 
    try {
        System.out.println("REGISTROS DE LA TABLA DEL HISTORIAL CLINICO PARA EL PACIENTE CON DNI " + dni);

        while (r.next()) {
 
            System.out.println(r.getInt("HcNum") + " | " + r.getDate("HcFec") + " | "+ r.getString("HcDiag") + " | " + r.getInt("HcPacDni")+ " | " + r.getInt("HcMedMat") + " | ");
        }
    } catch (SQLException ex) {
        Logger.getLogger(ExamenFinal.class.getName()).log(Level.SEVERE, null, ex);

    }
}


}