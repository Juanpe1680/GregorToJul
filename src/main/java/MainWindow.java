import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainWindow extends JFrame {

    private int day, month, year;
    private HashMap<String, String> monthDict;
    private int daysUntil;   // Días desde el 1 de enero hasta el día que se ha introducido

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

    private boolean checkPatternDate(){
        if(!gregorianDateTF.getText().isEmpty() && !julianDateTF.getText().isEmpty()){ // Si hay texto en los dos campos...
            JOptionPane.showMessageDialog(this, "Rellene un solo campo, por favor", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (!gregorianDateTF.getText().isEmpty()) {  // Si hay introducida una fecha en gregoriano....
            String gregorianInput = gregorianDateTF.getText().trim(); // Obtenemos el texto y eliminamos los posibles espacios

            String patternGregorian = "^\\d{2}/\\d{2}/\\d{4}$"; // dd/MM/yyyy
            if(gregorianInput.matches(patternGregorian)){
                String [] parts = gregorianInput.split("/"); // Array de Strings (dd, mm, yyyy) para separar los numeros por la '/'
                day = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                year = Integer.parseInt(parts[2]);

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

                switch (month){ // Switch para comprobar si los días se corresponden a los meses introducidos
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        // Si se ha introducido alguno de los meses que tienen 31 días...
                        if(day > 31){
                            // Mostramos un mensaje "personalizado" en función del mes que hemos introducido (obtenemos el valor)
                            JOptionPane.showMessageDialog(this, monthDict.get(String.valueOf(month)) + " solo tiene 31 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        if(day > 30){
                            JOptionPane.showMessageDialog(this, monthDict.get(String.valueOf(month)) + " solo tiene 30 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 2:
                        if(year % 4 == 0){ // Si el anio es bisiesto...
                            if(day > 29){
                                JOptionPane.showMessageDialog(this, "Febrero solo tiene 29 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
                        } else { // Si el anio introducido no es bisiesto...
                            if(day > 28){
                                JOptionPane.showMessageDialog(this, "Febrero solo tiene 28 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Introduce un mes válido (1-12)", "Error", JOptionPane.WARNING_MESSAGE);
                        break;
                }

                return true;
            } else {
                return false;
            } // Fin de la comprobación del patrón de Gregoriano
        } else {
            return false;
        }
        return false;
    }

    private void convertDate(){
        if(!gregorianDateTF.getText().isEmpty()){
            // Array de int con los días máx de cada mes (ponemos el 0 para igualar los meses a las posiciones del array)
            int [] monthDays = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            // FORMULA --- > (year - 1) * 365 + ((year-1)/4) + daysUntil + day

            if(year%4 == 0){    // Si el año es bisiesto se le añade un día mas a febrero
                monthDays[2] = 29;
            }

            for(int i = 0; i < month; i++){
                daysUntil += monthDays[i]; // Sumamos los días de los meses que hemos pasado hasta el mes introducido
            }

            daysUntil = daysUntil + day; // A los días que han pasado les sumamos los días que hemos introducido del mes

            // -----------------------------------------------------------------
            // HAY QUE MODIFICAR (EL AÑO 1900 NO ES BISIESTO PERO ES MODULO DE 4)
            // -----------------------------------------------------------------

        }
        // Convertir de juliano a gregoriano. Falta por poner

    }



    private void initComponents(){
        setSize(550, 400); // Window dimensions
        add(mainPanel);
        setTitle("Gregorian to Julian Converter"); // Title of the window
        setLocationRelativeTo(null); // To center de window on the screen
        setResizable(false); // To avoid to resize the window

        convertBT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean patterOk;
                patterOk = checkPatternDate();

                if(patterOk){   // Si se ha introducido una fecha correctamente...
                    gregorianDateTF.setForeground(Color.BLACK);
                    convertDate();

                } else { // Si el formato de fecha es incorrecto...
                    JOptionPane.showMessageDialog(mainPanel, "Introduzca una fecha correctamente", "Error", JOptionPane.ERROR_MESSAGE);
                    gregorianDateTF.setForeground(Color.RED);
                }
            }
        });
    } // Final of initComponents

    private JPanel mainPanel;
    private JLabel Title;
    private JTextField gregorianDateTF;
    private JTextField julianDateTF;
    private JButton convertBT;
    private JLabel gregorianLabel;
    private JLabel julianLabel;
    private JLabel exGregLabel;

}
