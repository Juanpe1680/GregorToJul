import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainWindow extends JFrame {

    private int day, month, year;
    private HashMap<String, String> monthDict;

    public MainWindow()  {
        initComponents();
    }

    public static void main(String[] args) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    private boolean checkGregorianPatternDate(){
        if (!gregorianDateTF.getText().isEmpty()) {  // Si hay introducida una fecha en gregoriano....
            // Obtenemos el texto y eliminamos los posibles espacios
            String gregorianInput = gregorianDateTF.getText().trim();

            String patternGregorian = "^\\d{2}/\\d{2}/\\d{4}$"; // dd/MM/yyyy

            if (!gregorianInput.matches(patternGregorian)) {    // Si no coincide la fecha con el patrón de arriba...
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa el formato dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Array de Strings (dd, mm, yyyy) para separar los numeros por la '/'
            String[] parts = gregorianInput.split("/");
            day = Integer.parseInt(parts[0]); // Guardamos en 'day' el valor de 'dd' en la fecha introducida
            month = Integer.parseInt(parts[1]); // Guardamos en 'month' el valor de 'MM' en la fecha introducida
            year = Integer.parseInt(parts[2]); // Guardamos en 'year' el valor de 'yyyy' en la fecha introducida

            // Creamos un diccionario para asociar el mes introducido con su nombre para poder gestionar las incidencias
            monthDict = new HashMap<String, String>() {{
                put("1", "Enero");
                put("2", "Febrero");
                put("3", "Marzo");
                put("4", "Abril");
                put("5", "Mayo");
                put("6", "Junio");
                put("7", "Julio");
                put("8", "Agosto");
                put("9", "Septiembre");
                put("10", "Octubre");
                put("11", "Noviembre");
                put("12", "Diciembre");
            }};

            // Si no se ha introducido un mes valido...
            if (month < 1 || month > 12) {
                JOptionPane.showMessageDialog(this, "Introduce un mes válido (1-12)", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            switch (month) { // Switch para comprobar si los días se corresponden a los meses introducidos
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    // Si se ha introducido alguno de los meses que tienen 31 días...
                    if (day > 31) {
                        // Mostramos un mensaje "personalizado" en función del mes que hemos introducido (obtenemos el valor)
                        JOptionPane.showMessageDialog(this, monthDict.get(String.valueOf(month)) + " solo tiene 31 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30) {
                        JOptionPane.showMessageDialog(this, monthDict.get(String.valueOf(month)) + " solo tiene 30 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                    break;
                case 2:
                    if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) { // Si year es bisiesto...
                        if (day > 29) {
                            JOptionPane.showMessageDialog(this, "Febrero solo tiene 29 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return false;
                        }
                    } else { // Si year no es bisiesto...
                        if (day > 28) {
                            JOptionPane.showMessageDialog(this, "Febrero solo tiene 28 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return false;
                        }
                    }
                    break;
            } // Fin del Switch
            return true;
        }
        return false;
    }

    private void convertGregorianDate(){
        if(checkGregorianPatternDate()){
            int julianDate = 0; // Fecha final en juliano
            int julianMonth = 0; // Variable para almacenar los meses en juliano (marzo es comienzo de anio)

            if(!gregorianDateTF.getText().isEmpty()){
                ////🛠️🛠️🛠️ PRUEBAS
                // En el calendario GREGORIANO, en 1582 se pasó del 4 de octubre al 15 (no exiten del 5 al 14)
                if(year == 1582 && month == 10 && day > 4 && day < 15){
                    JOptionPane.showMessageDialog(this, "Esa fecha no existe en el calendario gregoriano", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // En el calendario juliano el anio empieza en marzo, (enero y febrero serían del año anterior)
                if(month > 2){
                    julianMonth = month + 1;
                } else {
                    // Como el mes es menor que 2 (enero y febrero) son del anio pasado (en calendario juliano) restamos 1 año
                    year--;
                    // Para convertir enero y febrero a juliano sumamos 13 al mes introducido (enero es el 13 y febrero el 14)
                    julianMonth = month + 13;
                }

                /* Calculamos el número de días correspondientes a los anios que han pasado hasta la fecha introducida
                * Primero hacemos una aproximacion de dias en anios tendiendo en cuenta los bisiestos (de ahi el 0.25)
                * Sumamos una aproximacion de dias en los meses (30.6001 es una constante para pasar meses a días)
                * Por último sumamos el dia introducido a los dias desde la fecha de inicio del calendario juliano
                * hasta su inicio en 1582 */
                int pastDays = (int) (Math.floor(365.25 * year) + Math.floor(30.6001 * julianMonth) + day + 1720996);

                // Si la fecha introducida es posterior a cuando se introdujo el calendario gregoriano...
                if (year > 1582 || (year == 1582 && (month > 10 || (month == 10 && day >= 15)))) { // El 15 de ocutbre de 1582 es el comienzo del calendario gregoriano
                    int numSiglos = year / 100; // Calculamos cuantos siglos han pasado desde el anio 0
                    // Corregimos los dias para tener en cuenta las diferencias entre los 2 calendarios
                    // Al numSiglos le restamos los anios bisiestos (En el calendario juliano cada 4 años es bisiesto si o si)
                    pastDays -= numSiglos - (numSiglos / 4);
                }

                // Como el calendario juliano cuenta los días que han pasado redondeamos los días hacia el entero más cercano
                julianDate = (int) Math.floor(pastDays);

                // Establecemos la fecha juliana en el TextField
                julianDateTF.setText(String.valueOf(julianDate));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Verifique la fecha.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void initComponents(){
        setSize(550, 400); // Window dimensions
        add(mainPanel);
        setTitle("Gregorian to Julian Converter"); // Title of the window
        setLocationRelativeTo(null); // To center de window on the screen
        setResizable(false); // To avoid to resize the window

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gregorianDateTF.getText().isEmpty() && julianDateTF.getText().isEmpty()){ // Si hay texto en los dos campos...
                    JOptionPane.showMessageDialog(mainPanel, "Introduce una fecha", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Variables para comprobar si las fechas introcidas están en un formato correcto
                boolean gregPatternOk;
                boolean julianPatternOk;

                if(!gregorianDateTF.getText().isEmpty() && julianDateTF.getText().isEmpty()){
                    gregPatternOk = checkGregorianPatternDate();

                    if (gregPatternOk){   // Si se ha introducido una fecha correctamente...
                        gregorianDateTF.setForeground(Color.BLACK);
                        convertGregorianDate();

                    } else { // Si el formato de fecha es incorrecto...
                        JOptionPane.showMessageDialog(mainPanel, "Introduzca una fecha correctamente", "Error", JOptionPane.ERROR_MESSAGE);
                        gregorianDateTF.setForeground(Color.RED);
                    }
                } else if(gregorianDateTF.getText().isEmpty() && !julianDateTF.getText().isEmpty()){
                    // COMPROBAMOS PATRON DE JULIANO Y LLAMAMOS A METODO PARA CONVERTIR
                    // gregPatternOk = checkJulianPatternDate(); CREAR METODO
                }


            }
        });


        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gregorianDateTF.setText("");
                julianDateTF.setText("");
            }
        });

    } // Final of initComponents

    private JPanel mainPanel;
    private JLabel Title;
    private JTextField julianDateTF;
    private JButton convertButton;
    private JLabel gregorianLabel;
    private JLabel julianLabel;
    private JLabel exGregLabel;
    private JButton clearButton;
    private JTextField gregorianDateTF;
}
