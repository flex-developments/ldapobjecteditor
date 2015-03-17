/*
 * Copyright (C) 2014
 * Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * 
 * Este programa es software libre; Usted puede usarlo bajo los terminos de la
 * licencia de software GPL version 2.0 de la Free Software Foundation.
 *
 * Este programa se distribuye con la esperanza de que sea util, pero SIN
 * NINGUNA GARANTIA; tampoco las implicitas garantias de MERCANTILIDAD o
 * ADECUACION A UN PROPOSITO PARTICULAR.
 * Consulte la licencia GPL para mas detalles. Usted debe recibir una copia
 * de la GPL junto con este programa; si no, escriba a la Free Software
 * Foundation Inc. 51 Franklin Street,5 Piso, Boston, MA 02110-1301, USA.
 */

package flex.LDAPObjectEditor;

import flex.LDAPObjectEditor.i18n.I18n;
import flex.helpers.CSVHelper;
import flex.helpers.LDAPHelper;
import flex.helpers.LoggerHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

/**
 * InitEdit
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * @version 1.0
 */
public class InitEdit {
    private static String LDAP_OBJECTS_PATH = null;
    private static String LDAP_OBJECTS_CN_FILE_LIST = null;
    private static String LDAP_OBJECTS_PROPERTY_TO_EDIT = null;
    private static String LDAP_OBJECTS_PROPERTY_TYPE_VALUE = "string";
    private static String LDAP_OBJECTS_PROPERTY_NEW_VALUE = null;
    private static String LDAP_URL = "ldap://localhost:389";
    private static String LDAP_USER = null;
    private static String LDAP_PASS = null;
    
    //Special default values
    private static String SPECIAL_USE = "";
        private static final String LDAP_USER_ENABLED = "8389120";
        private static final String LDAP_USER_DISABLED = "8389122";
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch(arg) {
                case "-path": i++; LDAP_OBJECTS_PATH = args[i]; break;
                case "-l": i++; LDAP_OBJECTS_CN_FILE_LIST = args[i]; break;
                case "-p": i++; LDAP_OBJECTS_PROPERTY_TO_EDIT = args[i]; break;
                case "-ptv": i++; LDAP_OBJECTS_PROPERTY_TYPE_VALUE = args[i]; break;
                case "-pnv": i++; LDAP_OBJECTS_PROPERTY_NEW_VALUE = args[i]; break;
                case "-url": i++; LDAP_URL = args[i]; break;
                case "-user": i++; LDAP_USER = args[i]; break;
                case "-pass": i++; LDAP_PASS = args[i]; break;
                case "-h": printHelp(); break;
                
                //Special uses
                case "-a": SPECIAL_USE = "ENABLE_ACCOUNT"; break;
                case "-d": SPECIAL_USE = "DISABLE_ACCOUNT"; break;
                
                default:
                    System.out.println("Argumento <" + arg + "> inválido");
                    printHelp();
                    break;
            }
        }
        
        if(LDAP_USER == null) {
            String aux = System.console().readLine("No ha indicado credenciales de conexión al LDAP, desea introducirlas ahora? (Y/N): ");
            if(aux.toUpperCase().compareTo("Y") == 0) {
                LDAP_USER = System.console().readLine("Indique el usuario de conexion: ");
                LDAP_PASS = new String (System.console().readPassword("Indique el password: "));
            } else {
                System.out.println("Procediendo sin credenciales de conexión...");
            }
        }
        
        DirContext con = null;
        try {
            //Conexion con el LDAP
            con = LDAPHelper.establishLDAPConnection(
                LDAP_URL, 
                LDAP_USER, 
                LDAP_PASS
            );
            
        } catch (NamingException ex) {
            Logger.getLogger(InitEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Evaluar usos especiales
        switch (SPECIAL_USE) {
            case "ENABLE_ACCOUNT": {
                LDAP_OBJECTS_PROPERTY_TO_EDIT = "userAccountControl";
                LDAP_OBJECTS_PROPERTY_TYPE_VALUE = "string";
                LDAP_OBJECTS_PROPERTY_NEW_VALUE = LDAP_USER_ENABLED;
                break;
            }
            case "DISABLE_ACCOUNT": {
                LDAP_OBJECTS_PROPERTY_TO_EDIT = "userAccountControl";
                LDAP_OBJECTS_PROPERTY_TYPE_VALUE = "string";
                LDAP_OBJECTS_PROPERTY_NEW_VALUE = LDAP_USER_DISABLED;
                break;
            }
        }
        
        //Construir de nuevo valor
        Object newValue = LDAP_OBJECTS_PROPERTY_NEW_VALUE;
        switch (LDAP_OBJECTS_PROPERTY_TYPE_VALUE) {
            case "string": break;
            case "boolean": newValue = Boolean.valueOf(LDAP_OBJECTS_PROPERTY_NEW_VALUE); break;
            case "int": newValue = new Integer(LDAP_OBJECTS_PROPERTY_NEW_VALUE); break;
            default:
                System.out.println("Tipo de valor <" + LDAP_OBJECTS_PROPERTY_TYPE_VALUE + "> inválido");
                break;
        }
        
        //Modificar la propiedad
        List<Object> results = LDAPHelper.editLDAPObjectsByCN(
                con,
                LDAP_OBJECTS_PATH,
                CSVHelper.getLines(new File(LDAP_OBJECTS_CN_FILE_LIST)),
                LDAP_OBJECTS_PROPERTY_TO_EDIT,
                newValue
        );
        
        //Generar Logs
        LoggerHelper log = new LoggerHelper(
                InitEdit.class.getName(), 
                LoggerHelper.LOG_TYPE_SINGLE,
                LDAP_OBJECTS_CN_FILE_LIST + ".log",
                LoggerHelper.LOG_FORMATER_HANDLER_PATTERN_SIMPLE
        );
        for(Object o: results) {
            if(o instanceof Exception) {
                log.writeErrorLog((Throwable) o);
                
            } else {
                String msj = I18n.get("L_LDAP_ATTRIBUTE_MODIFIED_SUCCESS", (String) o);
                log.writeInfoLog(msj);
            }
        }
        
        System.out.println("Fin...");
        System.exit(0);
    }
    
    private static void printHelp() {
        System.out.println(I18n.get("M_USAGE"));
        System.exit(0);
    }
}