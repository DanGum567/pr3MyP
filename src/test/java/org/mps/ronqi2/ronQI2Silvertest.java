package org.mps.ronqi2;
/*
 * No son todos los casos que tenemos que probar.
 * Hay mas
 * 
*/

import org.junit.jupiter.api.Test;
import org.mps.dispositivo.Dispositivo;
import org.mps.dispositivo.DispositivoSilver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ronQI2Silvertest {

    
    /*
     * Analiza con los caminos base qué pruebas se han de realizar para comprobar que al inicializar funciona como debe ser. 
     * El funcionamiento correcto es que si es posible conectar ambos sensores y configurarlos, 
     * el método inicializar de ronQI2 o sus subclases, 
     * debería devolver true. En cualquier otro caso false. Se deja programado un ejemplo.
     */
    
    /*
     * Un inicializar debe configurar ambos sensores, comprueba que cuando se inicializa de forma correcta (el conectar es true), 
     * se llama una sola vez al configurar de cada sensor.
     */
    //Comprobamos que cuando se inicializa llama solo una vez a configuracion presion
    @Test
    public void inicilizar_llamarSoloUnaVezConfiguracionPresion(){
        //Arrange
        RonQI2Silver ronQI2Silver = new RonQI2Silver();
        DispositivoSilver dispMock = mock(DispositivoSilver.class);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        ronQI2Silver.anyadirDispositivo(dispMock);
        //Act
        ronQI2Silver.inicializar();
        //Assert
        verify(ronQI2Silver.disp, times(1)).conectarSensorPresion();
    }

    @Test
    public void inicilizar_llamarSoloUnaVezConfiguracionSonido(){
        //Arrange
        RonQI2Silver ronQI2Silver = new RonQI2Silver();
        DispositivoSilver dispMock = mock(DispositivoSilver.class);
        when(dispMock.configurarSensorSonido()).thenReturn(true);
        ronQI2Silver.anyadirDispositivo(dispMock);
        //Act
        ronQI2Silver.inicializar();
        //Assert
        verify(ronQI2Silver.disp, times(1)).conectarSensorSonido();
    }
    
    //Comprobamos que cuando se inicializa llama solo una vez a configuracion sonido



    /*
     * Un reconectar, comprueba si el dispositivo desconectado, en ese caso, conecta ambos y devuelve true si ambos han sido conectados. 
     * Genera las pruebas que estimes oportunas para comprobar su correcto funcionamiento. 
     * Centrate en probar si todo va bien, o si no, y si se llama a los métodos que deben ser llamados.
     */
    
    /*
     * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con obtenerNuevaLectura(), 
     * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP = 20.0f y thresholdS = 30.0f;, 
     * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también debería realizar la media.
     * /
     
     /* Realiza un primer test para ver que funciona bien independientemente del número de lecturas.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-parameterized-tests
     */
}
