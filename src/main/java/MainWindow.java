import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

        if (!gregorianDateTF.getText().isEmpty()) {  // Si hay introducida una fecha en gregoriano....
            String gregorianInput = gregorianDateTF.getText().trim(); // Obtenemos el texto y eliminamos los posibles espacios

            String patternGregorian = "^\\d{2}/\\d{2}/\\d{4}$"; // dd/MM/yyyy

            if (!gregorianInput.matches(patternGregorian)) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa el formato dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String[] parts = gregorianInput.split("/"); // Array de Strings (dd, mm, yyyy) para separar los numeros por la '/'
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
                        return false; // NUEVO
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30) {
                        JOptionPane.showMessageDialog(this, monthDict.get(String.valueOf(month)) + " solo tiene 30 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return false; // NUEVO
                    }
                    break;
                case 2:
                    if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) { // Si year es bisiesto...
                        if (day > 29) {
                            JOptionPane.showMessageDialog(this, "Febrero solo tiene 29 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return false; // NUEVO
                        }
                    } else { // Si year no es bisiesto...
                        if (day > 28) {
                            JOptionPane.showMessageDialog(this, "Febrero solo tiene 28 días.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return false; // NUEVO
                        }
                    }
                    break;
            } // Fin del Switch
            return true;
        }
        return false;
    }

    private void convertDate(){
        if(checkPatternDate()){
            daysUntil = 0; // Reiniciamos los días para que no se acumulen

            if(!gregorianDateTF.getText().isEmpty()){
                // Array de int con los días máx de cada mes (ponemos el 0 para igualar los meses a las posiciones del array)
                int [] monthDays = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

                // Si el año es bisiesto se le añade un día mas a febrero
                if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                    monthDays[2] = 29;
                }

                // Recorremos los meses hata el que se ha introducido en la interfaz (Empezamos con enero)
                for(int i = 1; i < month; i++){
                    daysUntil += monthDays[i]; // Sumamos los días de los meses que hemos pasado hasta el mes introducido
                }
                daysUntil += day; // A los días que han pasado les sumamos los días que hemos introducido del mes

                // Formula más precisa para convertir fechas (teniendo en cuenta los multiplos de 100 que no son bisiestos y los multiplos de 400)
                String conversionJul = String.valueOf(day + ((153 * month + 2) / 5) + (365 * year) + (year / 4) - (year / 100) + (year / 400) - 32045);
                julianDateTF.setText(conversionJul);

            }
            // Convertir de juliano a gregoriano. Falta por poner



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

                boolean patternOk;
                patternOk = checkPatternDate();

                 if (patternOk){   // Si se ha introducido una fecha correctamente...
                    gregorianDateTF.setForeground(Color.BLACK);
                    convertDate();

                } else { // Si el formato de fecha es incorrecto...
                    JOptionPane.showMessageDialog(mainPanel, "Introduzca una fecha correctamente", "Error", JOptionPane.ERROR_MESSAGE);
                    gregorianDateTF.setForeground(Color.RED);
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gregorianDateTF.setText("");
                julianDateTF.setText("");
            }
        });

    } // Final of initComponents

    private JPanel mainPanel;
    private JLabel Title;
    private JTextField gregorianDateTF;
    private JTextField julianDateTF;
    private JButton convertButton;
    private JLabel gregorianLabel;
    private JLabel julianLabel;
    private JLabel exGregLabel;
    private JButton deleteButton;
}
