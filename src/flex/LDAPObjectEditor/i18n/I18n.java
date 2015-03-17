/*
 * LDAPObjectEditor
 *
 * Copyright (C) 2010
 * Ing. Felix D. Lopez M. - flex.developments en gmail
 * 
 * Desarrollo apoyado por la Superintendencia de Servicios de Certificación 
 * Electrónica (SUSCERTE) durante 2010-2014 por:
 * Ing. Felix D. Lopez M. - flex.developments en gmail | flopez en suscerte gob ve
 * Ing. Yessica De Ascencao - yessicadeascencao en gmail | ydeascencao en suscerte gob ve
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

package flex.LDAPObjectEditor.i18n;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * I18n
 * Clase estatica para internacionalizacion de los mensajes.
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @version 1.0
 */
public class I18n {
    private static String LANG_PATH = "flex/LDAPObjectEditor/i18n/LANG";
    private static Locale LANGUAGE = Locale.forLanguageTag("es-VE");
    private static ResourceBundle bundle = ResourceBundle.getBundle(LANG_PATH, LANGUAGE);
    
    /**
     * Obtener String internacionalizado.
     * 
     * @param key Clave del string dentro del bundle.
     * 
     * @return valor de la clave dentro del bundle.
     */
    public static String get(String key) {
        return bundle.getBundle(LANG_PATH, LANGUAGE).getString(key);
    }
    
    /**
     * Obtener String internacionalizado con formato.
     * 
     * @param key Clave del string dentro del bundle.
     * @param arguments Argumentos para el formato.
     * 
     * @return valor de la clave dentro del bundle con formato procesado.
     */
    public static String get(String key, Object ... arguments) {
        MessageFormat temp = new MessageFormat(get(key));
        return temp.format(arguments);
    }
    
    /**
     * Obtener todas las keys del buundle.
     * 
     * @return Enumeration de las keys.
     */
    public static Enumeration<String> getKeys() {
        return bundle.getKeys();
    }
    
    /**
     * Obtener el lenguaje utilizado por la libreria para la internacionalizacion
     * de los mensajes.
     * 
     * @return Lenguaje para la internacionalizacion de los mensajes.
     */
    public static Locale getLanguage() {
        return LANGUAGE;
    }
    
    /**
     * Establecer el lenguaje utilizado por la libreria para la internacionalizacion
     * de los mensajes.
     * Ejemplos:
     *      I18n.setLanguage(es);
     *      I18n.setLanguage(en);
     *      I18n.setLanguage(es-VE);
     *      I18n.setLanguage(es-ES);
     * @param language 
     */
    public static void setLanguage(String language) {
        LANGUAGE = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle(LANG_PATH, LANGUAGE);
    }

    public static String getLangPath() {
        return LANG_PATH;
    }

    public static void setLangPath(String langPath) {
        LANG_PATH = langPath;
        bundle = ResourceBundle.getBundle(LANG_PATH, LANGUAGE);
    }
}
