package org.mps.ronqi2;

import java.util.ArrayList;
import java.util.List;

public class RonQI2Silver extends RonQI2{
    
    private int numLecturas;
    private List<Float> lecturasP;
    private List<Float> lecturasS;
    private float thresholdP;
    private float thresholdS;

    public RonQI2Silver() {
        lecturasP = new ArrayList<Float>();
        lecturasS = new ArrayList<Float>();
        thresholdP = 20.0f;
        thresholdS = 30.0f;
        numLecturas=5;
    }

    /* 
     * Obtiene las lecturas de presion y sonido del dispositivo y las almacena en sus respectivos
     * contenedores.
    */
    public void obtenerNuevaLectura(){
        lecturasP.add(disp.leerSensorPresion());
        if(lecturasP.size()>numLecturas){
            lecturasP.remove(0); 
        }
        // Corrección #1: se lee la presión cuando debería leerse el sonido
        lecturasS.add(disp.leerSensorSonido()); 
        if(lecturasS.size()>numLecturas){
            lecturasS.remove(0); 
        }
    }

    /* 
     * Evalua la apnea del sueno. 
     * - Devuelve true si el promedio de las lecturas de presion y sonido es mayor a los limites 
     *   establecidos.
     * - False en otro caso.
    */
    @Override
    public boolean evaluarApneaSuenyo() {
        //boolean resultado;
        Double avgP = lecturasP.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0);
        Double avgS = lecturasS.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0);
       /*
        Corrección #2:
        if-else es INCORRECTO dado que el método debe devolver verdadero cuando el promedio de 
        ambos sensores supera los límites correspondientes.

        Si no los supera devuelve falso.
       
        if (avgP>=thresholdP && avgS > thresholdS){
        
            resultado = false;
        }   
        else{
            resultado = true;
        }
        */
        return avgP>=thresholdP && avgS > thresholdS;
    }

   
    
}
