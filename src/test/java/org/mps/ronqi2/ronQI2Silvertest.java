package org.mps.ronqi2;
/*
 * No son todos los casos que tenemos que probar.
 * Hay mas
 * 
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.dispositivo.Dispositivo;
import org.mps.dispositivo.DispositivoSilver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    

    RonQI2Silver rqi;
    DispositivoSilver dispMock;

    @BeforeEach
    public void setUp(){
        rqi  = new RonQI2Silver();
        dispMock = mock(DispositivoSilver.class);          
        rqi.anyadirDispositivo(dispMock); 
    }
    /*
     * Un inicializar debe configurar ambos sensores, comprueba que cuando se inicializa de forma correcta (el conectar es true), 
     * se llama una sola vez al configurar de cada sensor.
     
     * CASOS DE PRUEBA:
     * 
     *  - Se conectan y se configuran ambos sensores DONE
     *  - Se conecta y se configura el sensor de presión pero no se conecta el sensor del sonido DONE
     *  - Se conectan ambos sensores, pero se configura solamente el de presión DONE
     *  - No se conecta el sensor de presión DONE
     *  - Se conecta el sensor de presión pero no se configura  
    */
    @Test
    @DisplayName("Conseguimos conectar y configurar ambos sensores")
    public void inicializar_SeConectanYSeConfiguranSensores_DevuelveVerdadero(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorSonido()).thenReturn(true);     
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertTrue(result);
        // Comprobamos las llamadas a las funciones
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, times(1)).configurarSensorPresion();
        verify(rqi.disp, times(1)).conectarSensorSonido();
        verify(rqi.disp, times(1)).configurarSensorSonido();
    }
    @Test
    @DisplayName("Conseguimos conectar y configurar el sensor de presión, pero no se conecta el de sonido")
    public void inicializar_NoSeConectaSonido_DevuelveFalso(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(false);
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        // Comprobamos las llamadas
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, times(1)).configurarSensorPresion();
        verify(rqi.disp, times(1)).conectarSensorSonido();
        verify(rqi.disp, never()).configurarSensorSonido();
    }
    @Test
    @DisplayName("Se consigue conectar ambos sensores, pero solamente se configura el sensor de presoón")
    public void inicializar_NoSeConfiguraSonido_DevuelveFalso(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(true);
        //No se define when para configurarSensorSonido porque este devuelve false por defecto
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, times(1)).configurarSensorPresion();
        verify(rqi.disp, times(1)).conectarSensorSonido();
        verify(rqi.disp, times(1)).configurarSensorSonido();
    }

    @Test
    @DisplayName("Se conecta el sensor de presión, pero no se configura")
    public void inicializar_SeConectaPresionNoSeConfigura_DevuelveFalse(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(false);
        //No se define when para configurarSensorSonido porque este devuelve false por defecto
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, times(1)).configurarSensorPresion();
        verify(rqi.disp, times(1)).conectarSensorSonido();
        verify(rqi.disp, never()).configurarSensorSonido();
    }

    /*
    @Test
    @DisplayName("No se conecta el sensor de presión")
    public void inicializar_NoSeConectaPresion_DevuelveFalso(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(false);
        
        rqi.anyadirDispositivo(dispMock);      
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        // Comprobamos que solamente se llama el método para conectar el sensor de presión
        verify(dispMock, times(1)).conectarSensorPresion();
        verify(dispMock, never()).configurarSensorPresion();
        verify(dispMock, never()).conectarSensorSonido();
        verify(dispMock, never()).configurarSensorSonido();
    }


    @Test
    public void inicilizar_llamarSoloUnaVezConfiguracionPresion(){
        //Arrange
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        rqi.anyadirDispositivo(dispMock);
        //Act
        rqi.inicializar();
        //Assert
        verify(rqi.disp, times(1)).conectarSensorPresion();
    }

    @Test
    public void inicilizar_llamarSoloUnaVezConfiguracionSonido(){
        //Arrange
        RonQI2Silver ronQI2Silver = new RonQI2Silver();
        DispositivoSilver dispMock = mock(DispositivoSilver.class);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorSonido()).thenReturn(true);
        ronQI2Silver.anyadirDispositivo(dispMock);
        //Act
        ronQI2Silver.inicializar();
        //Assert
        verify(ronQI2Silver.disp, times(1)).conectarSensorSonido();
    }
    */
    
    //Comprobamos que cuando se inicializa llama solo una vez a configuracion sonido



    /*
     * Un reconectar, comprueba si el dispositivo desconectado, en ese caso, conecta ambos y devuelve true si ambos han sido conectados. 
     * Genera las pruebas que estimes oportunas para comprobar su correcto funcionamiento. 
     * Centrate en probar si todo va bien, o si no, y si se llama a los métodos que deben ser llamados.
     * 
     * CASOS DE PRUEBA:
     * 1- Si el dispositivo está conectado no se reconecta DONE
     * 2- Si el dispositivo no está conectado, intentamos conectar ambos sensores pero no conectamos ninguno DONE
     * 3- Si el dispositivo no está conectado, conseguimos conectar el sensor de presión, pero no conseguimos conectar el sensor de sonido DONE
     * 4- Si el dispositivo no está conectado, conectamos el sensor de sonido, pero no conectamos el sensor de presión DONE
     * 5- Si el dispositivo no está conectado, conectamos ambos sensores 
     */

     @Test
     @DisplayName("Si el dispositivo está conectado no se reconecta")
     public void reconectar_DispositivoConectado_DevuelveFalso(){
        // Arrange
        when(dispMock.estaConectado()).thenReturn(true);
        //Act
        boolean result = rqi.reconectar();
        //Assert
        assertFalse(result);
        verify(rqi.disp, times(1)).estaConectado();
        verify(rqi.disp, never()).conectarSensorPresion();
        verify(rqi.disp, never()).conectarSensorSonido();
     }
     @Test
     @DisplayName("Si el dispositivo no está conectado, intentamos conectar ambos sensores pero no conectamos ninguno")
     public void reconectar_NoSeConectanSensores_DevuelveFalso(){
        // Arrange
        when(dispMock.estaConectado()).thenReturn(false);
        when(dispMock.conectarSensorPresion()).thenReturn(false);
        when(dispMock.conectarSensorSonido()).thenReturn(false);
        //Act
        boolean result = rqi.reconectar();
        //Assert
        assertFalse(result);
        //Comprobamos si las llamadas se han realizado correctamente:
        verify(rqi.disp, times(1)).estaConectado();
        verify(rqi.disp, times(1)).conectarSensorPresion();
        // Si no conectamos el sensor de presión, tampoco conectamos el sensor de sonido
        verify(rqi.disp, never()).conectarSensorSonido(); 
     }
     @Test
     @DisplayName("Si el dispositivo no está conectado, conseguimos conectar el sensor de presión,"+
     " pero no conseguimos conectar el sensor de sonido")
     public void reconectar_NoSeConectaElSensorDeSonido_DevuelveFalso(){
        // Arrange
        when(dispMock.estaConectado()).thenReturn(false);
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(false);
        //Act
        boolean result = rqi.reconectar();
        //Assert
        assertFalse(result);
        //Comprobamos si las llamadas se han realizado correctamente:
        verify(rqi.disp, times(1)).estaConectado();
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, times(1)).conectarSensorSonido(); 
     }
     @Test
     @DisplayName("Si el dispositivo no está conectado, conseguimos conectar el sensor de presión,"+
     " pero no conseguimos conectar el sensor de sonido")
     public void reconectar_NoSeConectaElSensorDePresion_DevuelveFalso(){
        // Arrange
        when(dispMock.estaConectado()).thenReturn(false);
        when(dispMock.conectarSensorPresion()).thenReturn(false);
        when(dispMock.conectarSensorSonido()).thenReturn(true);
        //Act
        boolean result = rqi.reconectar();
        //Assert
        assertFalse(result);
        //Comprobamos si las llamadas se han realizado correctamente:
        verify(rqi.disp, times(1)).estaConectado();
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, never()).conectarSensorSonido(); 
     }
     @Test
     @DisplayName("Si el dispositivo no está conectado, conectamos ambos sensores ")
     public void reconectar_SeConectanAmbosSensores_DevuelveVerdadero(){
        // Arrange
        when(dispMock.estaConectado()).thenReturn(false);
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(true);
        //Act
        boolean result = rqi.reconectar();
        //Assert
        assertTrue(result);
        //Comprobamos si las llamadas se han realizado correctamente:
        verify(rqi.disp, times(1)).estaConectado();
        verify(rqi.disp, times(1)).conectarSensorPresion();
        verify(rqi.disp, times(1)).conectarSensorSonido(); 
     }

    
    /*
     * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con obtenerNuevaLectura(), 
     * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP = 20.0f y thresholdS = 30.0f;, 
     * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también debería realizar la media.
     * 
     * CASOS DE PRUEBA:
     * ----------------
     * 
     * 1- Si no obtenemos lecturas de ningún sensor, no hay apena.
     * 2- Si no se obtienen lecturas del sensor de presión, no hay apnea.
     * 3- Si no se obtienen lecturas del sensor de sonido, no hay apnea.
     * 4- Obtenemos menos de 5 lecturas tanto desde el sensor de la presión como desde el sensor del sonido, sin embargo
     *    en ambos casos obtenemos el promedio mayor que los límites.
     * 5- Obtenemos menos de 5 lecturas tanto desde el sensor de la presión como desde el sensor del sonido. En el caso del sonido, la 
     *    media no supera el límite.
     * 6- Obtenemos menos de 5 lecturas tanto desde el sensor de la presión como desde el sensor del sonido. En el caso de la presión, la 
     *    media no supera el límite.
     * 7- Obtenemos 5 lecturas tanto desde el sensor de la presión como desde el sensor del sonido. En el caso del sonido, la 
     *    media no supera el límite.
     * 8- Obtenemos 5 lecturas tanto desde el sensor de la presión como desde el sensor del sonido. En el caso de la presión, la 
     *    media no supera el límite.
     * 9- Obtenemos 5 lecturas desde el sensor de la presión pero desde el sensor del sonido obtenemos menos de 5. En ambos casos la media
     *    supera el límite permitido.  
     * /
     

    
     
     /* Realiza un primer test para ver que funciona bien independientemente del número de lecturas.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-parameterized-tests
     */
}
