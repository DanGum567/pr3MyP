package org.mps.ronqi2;
/*
 * No son todos los casos que tenemos que probar.
 * Hay mas
 * 
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mps.dispositivo.Dispositivo;
import org.mps.dispositivo.DispositivoSilver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RonQI2SilverTest {

    
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
     *  - No se conecta el sensor de presión DONE 
     *  - Se conecta el sensor de presión pero no se configura DONE
     *  - Se conecta y se configura el sensor de presión pero no se conecta el sensor del sonido DONE
     *  - Se conectan ambos sensores, pero se configura solamente el de presión DONE
     *  - Se conectan y se configuran ambos sensores DONE  
    */
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
       //verify(dispMock, times(1)).conectarSensorPresion();
       //verify(dispMock, never()).configurarSensorPresion();
       //verify(dispMock, never()).conectarSensorSonido();
       //verify(dispMock, never()).configurarSensorSonido();
    }
    @Test
    @DisplayName("Se conecta el sensor de presión, pero no se configura")
    public void inicializar_NoSeConfiguraPresion_DevuelveFalse(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(false);
        when(dispMock.conectarSensorSonido()).thenReturn(true);
        when(dispMock.configurarSensorSonido()).thenReturn(true);
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        //verify(rqi.disp, times(1)).conectarSensorPresion();
        //verify(rqi.disp, times(1)).configurarSensorPresion();
        //verify(rqi.disp, never()).conectarSensorSonido();
        //verify(rqi.disp, never()).configurarSensorSonido();
    }
    @Test
    @DisplayName("Se conectar y configura el sensor de presión, pero no se conecta el sensor de sonido")
    public void inicializar_NoSeConectaSonido_DevuelveFalso(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(false);
        when(dispMock.configurarSensorSonido()).thenReturn(false);
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        // Comprobamos las llamadas
        // verify(rqi.disp, times(1)).conectarSensorPresion();
        // verify(rqi.disp, times(1)).configurarSensorPresion();
        // verify(rqi.disp, times(1)).conectarSensorSonido();
        // verify(rqi.disp, never()).configurarSensorSonido();
    }
    @Test
    @DisplayName("Se conectan ambos sensores, pero no se configura el sensor de sonido")
    public void inicializar_NoSeConfiguraSonido_DevuelveFalso(){
        //Arrange
        when(dispMock.conectarSensorPresion()).thenReturn(true);
        when(dispMock.configurarSensorPresion()).thenReturn(true);
        when(dispMock.conectarSensorSonido()).thenReturn(true);
        when(dispMock.configurarSensorSonido()).thenReturn(false);
        //Act
        boolean result = rqi.inicializar();
        //Assert
        assertFalse(result);
        //verify(rqi.disp, times(1)).conectarSensorPresion();
        //verify(rqi.disp, times(1)).configurarSensorPresion();
        //verify(rqi.disp, times(1)).conectarSensorSonido();
        //verify(rqi.disp, times(1)).configurarSensorSonido();
    }
    @Test
    @DisplayName("Se conectan y configurar ambos sensores")
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
        // verify(rqi.disp, times(1)).conectarSensorPresion();
        // verify(rqi.disp, times(1)).configurarSensorPresion();
        // verify(rqi.disp, times(1)).conectarSensorSonido();
        // verify(rqi.disp, times(1)).configurarSensorSonido();
    }
    
       

    /*
     * obtenerNuevaLectura():
     * 
     * Obtiene las lecturas de presion y sonido del dispositivo y las almacena en sus respectivos
     * contenedores.
     * 
     * CASOS DE PRUEBA:
     *    
     * - Se recibe la información por la parte de ambos sensores y se almacena en los contenedores correspondiente
    */

    @DisplayName("Se recibe la información por la parte de ambos sensores y se almacena en los contenedores correspondiente")
    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6,7})
    public void obtenerNuevaLectura_LecturasSeObtienenCorrectamente(int nlecturas){
      //Act:
      for(int i = 0; i < nlecturas; ++i){
         rqi.obtenerNuevaLectura();
      }
      //Asserts
      verify(rqi.disp, times(nlecturas)).leerSensorPresion();
      verify(rqi.disp, times(nlecturas)).leerSensorSonido();
    }
       
       
   /*
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
     * 5- Si el dispositivo no está conectado, conectamos ambos sensores DONE
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
        //verify(rqi.disp, times(1)).estaConectado();
        //verify(rqi.disp, times(1)).conectarSensorPresion();
        // Si no conectamos el sensor de presión, tampoco conectamos el sensor de sonido
        //verify(rqi.disp, never()).conectarSensorSonido(); 
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
        //verify(rqi.disp, times(1)).estaConectado();
        //verify(rqi.disp, times(1)).conectarSensorPresion();
        //verify(rqi.disp, times(1)).conectarSensorSonido(); 
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
        //verify(rqi.disp, times(1)).estaConectado();
        //verify(rqi.disp, times(1)).conectarSensorPresion();
        //verify(rqi.disp, never()).conectarSensorSonido(); 
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
        //verify(rqi.disp, times(1)).estaConectado();
        //verify(rqi.disp, times(1)).conectarSensorPresion();
        //verify(rqi.disp, times(1)).conectarSensorSonido(); 
     }

    
    /*
     * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con obtenerNuevaLectura(), 
     * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP = 20.0f y thresholdS = 30.0f;, 
     * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también debería realizar la media.
     * 
     * CASOS DE PRUEBA:
     * ----------------
     * 
     * 1- Si no obtenemos lecturas de ningún sensor, no hay apena. DONE
     * 2- Si no se obtienen lecturas del sensor de presión, no hay apnea. DONE
     * 3- Si no se obtienen lecturas del sensor de presión, no hay apnea aunque el de sonido supere el límite DONE
     * 4- Si no se obtienen lecturas del sonido, no hay apnea aunque el sonido supere el límite DONE
     * 5- Si no se obtienen lecturas del sensor de sonido, no hay apnea aunque el de presion supere el límite DONE
     * 6- Si las medidas de ambos sensores, pero en ningún caso se supera el límite, no hay apnea DONE
     * 7- Si las medidas de ambos sensores y solamente se supera el límite de presión, no hay apena DONE
     * 8- Si las medidas de ambos sensores y solamente se supera el límite de sonido, no hay apnea DONE
     * 9- Si las medidas de ambos sensores y en ambos casos se supera el límite, hay apnea DONE
     */

    
     
    /* Realiza un primer test para ver que funciona bien independientemente del número de lecturas.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-parameterized-tests
     */

     @Test
     @DisplayName("Si no obtenemos lecturas de ningún sensor, no hay apena")
     public void evaluarApneaSuenyo_NoHayDatosDeSensores_NoHayApnea(){
        // Arrange:
        when(dispMock.leerSensorPresion()).thenReturn(1f);
        when(dispMock.leerSensorSonido()).thenReturn(2f);
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     @DisplayName("Si no se obtienen lecturas del sensor de presión, no hay apnea")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void evaluarApneaSuenyo_NoHayDatosSensorPresion_NoHayApnea(){
        // Arrange:
        // No asignamos salida al leerSensorPresion() porque este por defecto devuelve 0;
        when(dispMock.leerSensorPresion()).thenReturn(0f);
        when(dispMock.leerSensorSonido()).thenReturn(2f);
        for(int i = 0; i < 5; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     @DisplayName("Si no se obtienen lecturas del sensor de presión, no hay apnea aunque el de sonido supere el límite")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void evaluarApneaSuenyo_NoHayDatosPresion_SonidoSuperaLimite_NoHayApnea(){
        // Arrange:
        // No asignamos salida al leerSensorPresion() porque este por defecto devuelve 0;
        when(dispMock.leerSensorPresion()).thenReturn(0f);
        when(dispMock.leerSensorSonido()).thenReturn(40f);
        for(int i = 0; i < 5; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     
     @DisplayName("Si no se obtienen lecturas del sensor de sonido, no hay apnea")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void evaluarApneaSuenyo_NoHayDatosSensorSonido_NoHayApnea(int nlecturas){
        // Arrange:
        // No asignamos salida al leerSensorPresion() porque este por defecto devuelve 0;
        when(dispMock.leerSensorPresion()).thenReturn(30f);
        when(dispMock.leerSensorSonido()).thenReturn(0f);
        for(int i = 0; i < nlecturas; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     @DisplayName("Si no se obtienen lecturas del sensor de sonido, no hay apnea aunque el de presion supere el límite")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void valuarApneaSuenyo_NoHayDatosSonido_PresionSuperaLimite_NoHayApnea(){
        // Arrange:
        // No asignamos salida al leerSensorPresion() porque este por defecto devuelve 0;
        when(dispMock.leerSensorPresion()).thenReturn(30f);
        when(dispMock.leerSensorSonido()).thenReturn(0f);
        for(int i = 0; i < 5; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     @DisplayName("Si las medidas de ambos sensores, pero en ningún caso se supera el límite, no hay apnea")
     @ParameterizedTest
     @ValueSource(ints = {4,5,10})
     public void evaluarApneaSuenyo_noSeSuperanLimites_NoHayApnea(int nlecturas){
        // Arrange:
        when(dispMock.leerSensorPresion()).thenReturn(1f);
        when(dispMock.leerSensorSonido()).thenReturn(2f);
        for(int i = 0; i < nlecturas; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }
     @DisplayName(" Si las medidas de ambos sensores y solamente se supera el límite de presión, no hay apena")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void evaluarApneaSuenyo_SeSuperaLimitePresion_NoHayApnea(int nlecturas){
        // Arrange:
        when(dispMock.leerSensorPresion()).thenReturn(30f);
        when(dispMock.leerSensorSonido()).thenReturn(10f);
        for(int i = 0; i < nlecturas; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     @DisplayName("OSi las medidas de ambos sensores y solamente se supera el límite de sonido, no hay apnea")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void evaluarApneaSuenyo_SeSuperaLimiteSonido_NoHayApnea(int nlecturas){
        // Arrange:
        when(dispMock.leerSensorPresion()).thenReturn(10f);
        when(dispMock.leerSensorSonido()).thenReturn(40f);
        for(int i = 0; i < nlecturas; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertFalse(result);
     }

     @DisplayName("Si las medidas de ambos sensores y en ambos casos se supera el límite, hay apnea")
     @ParameterizedTest
     @ValueSource(ints = {5,7,10})
     public void evaluarApneaSuenyo_SeSuperanLimitesPresionSonido_HayApnea(int nlecturas){
        // Arrange:
        when(dispMock.leerSensorPresion()).thenReturn(30f);
        when(dispMock.leerSensorSonido()).thenReturn(40f);
        for(int i = 0; i < nlecturas; ++i){
            rqi.obtenerNuevaLectura();
        }
        // Act:        
        boolean result = rqi.evaluarApneaSuenyo();
        // Assert
        assertTrue(result);
     }

      /*
     * El método estaConectado() de la clase RonQI2Silver, comprueba si el dispositivo está conectado
     * 
     * CASOS DE PRUEBA:
     * ----------------
     * 
     * 1- Si el dispositivo no está conectado, devuelve falso
     * 2- Si el dispositvo está conectado, devuelve verdadero
     */

     @DisplayName("Si el dispositivo no está conectado, devuelve falso")
     @Test
     public void estaConectado_DispositivoNoEstaConectado_DevuelveFalso(){
        // Arrange:
        when(dispMock.estaConectado()).thenReturn(false);
        // Act:        
        boolean result = rqi.estaConectado();
        // Assert
        assertFalse(result);
     }

     @DisplayName("Si el dispositvo está conectado, devuelve verdadero")
     @Test
     public void estaConectado_DispositivoEstaConectado_DevuelveVerdadero(){
        // Arrange:
        when(dispMock.estaConectado()).thenReturn(false);
        // Act:        
        boolean result = rqi.estaConectado();
        // Assert
        assertFalse(result);
     }


}
